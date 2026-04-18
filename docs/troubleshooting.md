# User-server Troubleshooting

## 1. `/users/me` returns 401
Symptoms:
- The endpoint fails before controller logic.

Likely causes:
- Missing bearer token or gateway principal.
- JWT issuer or audience does not match the configured values.
- `sub` claim is missing.

Checks:
- Confirm `USER_SERVICE_INTERNAL_JWT_ISSUER`, `USER_SERVICE_INTERNAL_JWT_AUDIENCE`, and `USER_SERVICE_INTERNAL_JWT_SECRET`.
- Confirm the gateway forwards `Authorization` or `X-User-Id` and `X-User-Status`.

Fix:
- Reissue the JWT with the configured issuer, audience, and subject UUID.

## 2. `/users/me` returns 403
Symptoms:
- Authentication succeeds, but access is still denied.

Likely causes:
- `status` is not `A`.
- The gateway forwarded a stale `X-User-Status`.

Checks:
- Inspect the JWT `status` claim or gateway principal status.
- Verify the user row has status `A`.

Fix:
- Update the user status back to active if the account should be usable.

## 3. Internal API returns 403
Symptoms:
- `/internal/users/**` is rejected even with a bearer token.

Likely causes:
- Missing `internal` scope.
- Wrong issuer, audience, or secret.

Checks:
- Verify the token carries `scope=internal` or `scp=[internal]`.
- Verify `iss` and `aud` match the service configuration.

Fix:
- Issue a new internal JWT from `auth-service`.

## 4. Signup returns 400
Symptoms:
- `POST /users/signup` fails immediately.

Likely causes:
- Invalid email format.
- Empty email.
- Duplicate email.

Checks:
- Confirm the payload contains a valid email.
- Check whether the email already exists in `users`.

Fix:
- Use a new email or clean the duplicate record in the test environment.

## 5. Social link retries create conflicts
Symptoms:
- `find-or-create-and-link-social` is called repeatedly for the same provider key.

Likely causes:
- Another request already created the `(social_type, provider_id)` row.

Checks:
- Query `user_social_accounts` for the provider pair.
- Compare the returned `userId` with the expected owner.

Fix:
- Treat the request as idempotent and reuse the existing mapping.

## 6. Social link is attached to the wrong user
Symptoms:
- The same social provider key points to a different user than expected.

Likely causes:
- Provider key collision.
- Existing mapping was created earlier and reused.

Checks:
- Inspect `providerId`, `socialType`, and the unique constraint on `user_social_accounts`.

Fix:
- Resolve the ownership conflict at the source identity provider, then remap if needed.

## 7. Runtime and repo naming looks inconsistent
Symptoms:
- The docs mention both `User-server` and `user-service`.

Meaning:
- `User-server` is the implementation repository name.
- `user-service` is the runtime service name used by Docker, gateway routing, and logs.

Fix:
- Keep `User-server` for repository and contract ownership.
- Keep `user-service` for runtime DNS and container identity.
