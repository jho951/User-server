# user-service Terraform

이 스택은 MSA Terraform 계약을 따르며, ECS/Fargate Blue/Green 배포 기준 인프라를 구성합니다.

생성 리소스:

- `create_vpc = true`일 때 생성되는 전용 VPC와 public/private subnet
- shared VPC 모드에서 internal 로 동작하는 운영 listener와 CodeDeploy test listener
- Blue/Green ALB target group
- `CODE_DEPLOY` 배포 컨트롤러를 사용하는 ECS cluster, task definition, ECS service
- CodeDeploy ECS application과 deployment group
- lifecycle policy가 적용된 ECR repository
- CloudWatch log group
- 민감 환경변수 저장용 Secrets Manager secret
- `enable_mysql = true`일 때 private RDS MySQL

## 권장 토폴로지

운영 기본값은 shared VPC + internal ALB + Route53 private DNS 입니다.

- 외부 클라이언트는 gateway public ALB로만 진입합니다.
- `user-service`는 `user.internal.platform.local`로만 노출합니다.
- ingress는 CIDR 공개 대신 gateway/auth caller security group으로 제한합니다.

권장 변수 형태:

```hcl
create_vpc = false

existing_vpc_id             = "vpc-..."
existing_public_subnet_ids  = ["subnet-public-a", "subnet-public-c"]
existing_private_subnet_ids = ["subnet-app-a", "subnet-app-c"]
existing_vpc_cidr           = "10.0.0.0/16"

alb_internal                          = true
alb_ingress_source_security_group_ids = ["sg-gateway-ecs-tasks", "sg-auth-ecs-tasks"]
private_dns_zone_id                   = "Z123456789PRIVATE"
private_dns_name                      = "user.internal.platform.local"
```

`USER_SERVICE_BASE_URL` 같은 자기참조/상호호출 주소도 public URL이 아니라 private DNS 기준으로 둡니다.

## 인프라 적용

```bash
cp infra/terraform/terraform.tfvars.example infra/terraform/terraform.tfvars
cd infra/terraform
terraform init
terraform plan
terraform apply
```

`terraform apply`는 AWS 인프라를 변경합니다. 운영 환경에서는 plan 결과, state backend, 권한, 비용 영향을 확인한 뒤 적용합니다.

## 주요 변수

| 변수                     | 기본값              | 설명                                           |
|------------------------|------------------|----------------------------------------------|
| `aws_region`           | `ap-northeast-2` | 배포 AWS region                                |
| `environment`          | `prod`           | 리소스 이름과 tag에 들어가는 환경 이름                      |
| `service_name`         | `user-service`   | contract 기준 논리 서비스 이름                        |
| `service_runtime_name` | `user-service`   | ECS task 안의 container 이름                     |
| `app_port`             | `8082`           | container와 target group이 사용하는 포트             |
| `desired_count`        | `2`              | ECS service task 수                           |
| `image_tag`            | `latest`         | Terraform이 task definition에 넣는 ECR image tag |
| `container_image`      | 빈 값              | 외부 image URI를 직접 지정할 때 사용                    |
| `enable_mysql`         | `false`          | private RDS MySQL 생성 여부                      |
| `app_env`              | `{}`             | 평문 환경변수 map                                  |
| `app_secret_env`       | `{}`             | Secrets Manager에 저장할 민감 환경변수 map             |

전체 변수 예시는 [terraform.tfvars.example](./terraform.tfvars.example)을 기준으로 합니다.

## 이미지 빌드와 Push

```bash
AWS_REGION=ap-northeast-2
ECR_REPO="$(terraform output -raw ecr_repository_url)"

aws ecr get-login-password --region "$AWS_REGION" \
  | docker login --username AWS --password-stdin "$(echo "$ECR_REPO" | cut -d/ -f1)"

docker build \
  -f ../../docker/Dockerfile \
  -t "$ECR_REPO:2026.04.17-001" \
  ../..

docker push "$ECR_REPO:2026.04.17-001"
```

운영 배포에는 불변 release tag를 사용합니다. rollback이 필요한 배포에서 `latest`에 의존하지 않습니다.

## 배포와 Rollback

Terraform은 Blue/Green 배포에 필요한 인프라를 만듭니다. 런타임 release는 새 ECS task definition revision과 AppSpec을 사용해 CodeDeploy로 수행합니다.

최소 AppSpec 형태:

```yaml
version: 0.0
Resources:
  - TargetService:
      Type: AWS::ECS::Service
      Properties:
        TaskDefinition: "<new-task-definition-arn>"
        LoadBalancerInfo:
          ContainerName: "user-service"
          ContainerPort: 8082
```

CodeDeploy는 다음 리소스를 사용해 blue target group에서 green target group으로 traffic을 전환합니다.

| 항목 | 확인 명령 |
| --- | --- |
| 운영 listener URL | `terraform output service_url` |
| test listener URL | `terraform output test_url` |
| CodeDeploy application | `terraform output codedeploy_app_name` |
| CodeDeploy deployment group | `terraform output codedeploy_deployment_group_name` |

배포 실패 또는 alarm 중단 시 rollback은 CodeDeploy auto rollback으로 처리합니다. 수동 rollback은 이전 task definition ARN을 사용한 새 CodeDeploy 배포로 수행합니다.

## State와 Secret 기준

- 운영 전에는 암호화된 remote backend를 사용합니다.
- `terraform.tfvars`는 commit하지 않습니다.
- `app_secret_env`, `mysql_password` 같은 민감 값은 Terraform state에도 남습니다.
- state 접근 권한은 운영 secret 접근 권한과 같은 수준으로 제한합니다.
- database 소유 서비스에서는 RDS deletion protection을 기본으로 켭니다.

## 운영 메모

- `terraform apply`는 인프라 변경 수단입니다. release 절차는 CodeDeploy가 담당합니다.
- `enable_mysql = true`이면 ECS task는 private subnet의 RDS에 접근하고, JDBC URL과 계정 정보가 container 환경변수로 주입됩니다.
- ALB production listener 기본 포트는 `80`, CodeDeploy test listener 기본 포트는 `9000`입니다.
- shared VPC에서는 `app_ingress_cidrs`보다 `alb_ingress_source_security_group_ids`를 우선 사용합니다.
- Route53 private DNS를 연결하면 `terraform output service_url`이 private FQDN 기준으로 반환됩니다.
