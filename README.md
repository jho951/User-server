# User-server

사용자 마스터 데이터, 소셜 계정 연결, 사용자 상태, 내부 사용자 조회 API를 담당하는 `user-service` 구현 저장소입니다.

## 역할

- 사용자 기본 정보의 기준 저장소입니다.
- 소셜 제공자 계정과 내부 사용자 계정의 연결 정보를 소유합니다.
- gateway 뒤에서 `/users/**`, `/internal/users/**` API를 제공합니다.
- 사용자 생성, 상태 변경, 소셜 링크 생성 이력을 감사 로그로 기록합니다.

## 저장소와 런타임 이름

| 항목 | 값 |
| --- | --- |
| 구현 저장소 | `User-server` |
| 런타임 서비스명 | `user-service` |
| Gradle group | `io.github.jho951` |
| 서비스 포트 | `8082` |

문서와 Docker, gateway 설정에서는 런타임 이름인 `user-service`를 사용합니다. 저장소나 PR 문맥에서는 `User-server`를 사용합니다.

## 빠른 시작

GitHub Packages 의존성을 받으려면 `GH_TOKEN`이 필요합니다.

```bash
export GITHUB_ACTOR=jho951
export GH_TOKEN=<github-token-with-read-packages>
```

Docker 개발 스택 실행:

```bash
./scripts/run.docker.sh up dev
```

로컬 직접 실행:

```bash
./scripts/run.local.sh
```

빌드와 테스트:

```bash
./gradlew build
```

상태 확인:

```bash
curl -i http://localhost:8082/actuator/health
curl -i http://localhost:8082/actuator/prometheus
```

## 문서

- [문서 홈](docs/README.md)
- [구조](docs/architecture.md)
- [Auth API](docs/auth-api.md)
- [CI와 구현 기준](docs/ci-and-implementation.md)
- [DB](docs/database.md)
- [Docker](docs/docker.md)
- [Platform 사용 기준](docs/platform.md)
- [문제 해결](docs/troubleshooting.md)
- [OpenAPI](docs/openapi/user-service.yml)
