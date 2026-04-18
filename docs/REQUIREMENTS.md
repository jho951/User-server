# User-server Requirements (Active)

## 1. Service Identity
- Implementation repo: `https://github.com/jho951/User-server`
- Runtime service name: `user-service`
- Gateway and consumers should treat `User-server` as the source of truth for user master data, social-link ownership, and profile visibility/privacy policy.

## 2. Core API Surface
- Public API:
  - `POST /users/signup`
  - `GET /users/me`
- Internal API:
  - `POST /internal/users`
  - `POST /internal/users/social`
  - `POST /internal/users/ensure-social`
  - `POST /internal/users/find-or-create-and-link-social`
  - `PUT /internal/users/{userId}/status`
  - `GET /internal/users/{userId}`
  - `GET /internal/users/by-email`
  - `GET /internal/users/by-social`

## 3. Security Rules
- `GET /users/me` requires an authenticated principal with active status `A`.
- Gateway-provided context headers are accepted for user context, but the endpoint still enforces active-status checks.
- Internal endpoints require `auth-service` issued JWTs with:
  - matching `iss`
  - matching `aud`
  - non-empty `sub`
  - `scope` or `scp` containing `internal`
- `USER_SERVICE_INTERNAL_JWT_*` is the preferred configuration set.
- `AUTH_JWT_*` is kept only for backward compatibility.

## 4. Identity and Status
- Standard user identifier: UUID string.
- JWT `sub` must equal `userId`.
- User status codes:
  - `A` = active
  - `P` = pending
  - `S` = suspended
  - `D` = deleted
- `GET /users/me` only returns data when status is `A`.

## 5. Social Link Ownership
- `User-server` owns the canonical social-link records in `user_social_accounts`.
- The canonical entry point for idempotent linkage is `POST /internal/users/find-or-create-and-link-social`.
- Duplicate `provider + providerUserId` combinations must resolve idempotently rather than create duplicate owners.

## 6. Data Model
- Primary tables:
  - `users`
  - `user_social_accounts`
- Important invariants:
  - `users.email` is unique
  - `(social_type, provider_id)` is unique
  - profile visibility is driven by service policy, not by gateway-only logic

## 7. Observability
- Access logs must include `method`, `path`, `status`, and `durationMs`.
- Social-link requests emit structured metrics:
  - `social_link_requests_total`
  - `social_link_conflicts_total`
  - `social_link_create_total`
  - `social_link_existing_total`
  - `social_link_latency_ms`
- Audit logs are written through the shared `audit-log` contract.

## 8. Operational Dependencies
- Database: MySQL in production
- Shared contract dependencies:
  - Gateway
  - Auth-server
  - Authz-server
  - Block-server
  - audit-log

## 9. Completion Criteria
- Public signup works without gateway authentication.
- `/users/me` is denied unless the user is active.
- Internal user APIs reject JWTs without `internal` scope.
- Social-link requests stay idempotent under retries and uniqueness conflicts.
