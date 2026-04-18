# User-server Debug Runbook

## 1. Basic Checks
1. Start the service: `./scripts/run.local.sh`
2. Health check: `curl -i http://localhost:8082/actuator/health`
3. Metrics check: `curl -i http://localhost:8082/actuator/prometheus`

## 2. Public Signup
```bash
curl -i -X POST http://localhost:8082/users/signup \
  -H 'Content-Type: application/json' \
  -d '{"email":"debug-user@example.com"}'
```
- Expected: `201 Created`

## 3. My Profile
```bash
curl -i http://localhost:8082/users/me
```
- Expected:
  - `401 Unauthorized` without a valid authenticated principal
  - `403 Forbidden` when the authenticated user status is not `A`

## 4. Internal Create
```bash
curl -i -X POST http://localhost:8082/internal/users \
  -H 'Authorization: Bearer <auth-service-internal-jwt>' \
  -H 'Content-Type: application/json' \
  -d '{"email":"internal-user@example.com","role":"USER","status":"ACTIVE"}'
```
- Expected: `201 Created`
- The JWT must include `iss`, `aud`, `sub`, and `internal` scope.

## 5. Social Link
```bash
curl -i -X POST http://localhost:8082/internal/users/find-or-create-and-link-social \
  -H 'Authorization: Bearer <auth-service-internal-jwt>' \
  -H 'Content-Type: application/json' \
  -d '{"email":"social-user@example.com","provider":"GGL","providerUserId":"provider-123"}'
```
- Expected:
  - first request: `200 OK` or `201 Created` depending on the path
  - repeated request with the same provider key: existing owner is returned

## 6. Status Update
```bash
curl -i -X PUT http://localhost:8082/internal/users/<user-id>/status \
  -H 'Authorization: Bearer <auth-service-internal-jwt>' \
  -H 'Content-Type: application/json' \
  -d '{"status":"SUSPENDED"}'
```
- Expected: `200 OK`
- After the update, `/users/me` should be denied for that user until the status becomes `A` again.

## 7. Common Failure Signals
- `401 Unauthorized`
  - missing JWT
  - wrong issuer or audience
  - missing `sub`
- `403 Forbidden`
  - internal scope missing
  - user status not active
- `400 Bad Request`
  - invalid email
  - invalid UUID
  - invalid social provider or request body

## 8. Logs to Check
- Access log: `method`, `path`, `status`, `durationMs`
- Security: issuer, audience, and scope failures
- Social-link log: `provider`, `providerUserId`, `email`, `userId`, `result`, `errorCode`
