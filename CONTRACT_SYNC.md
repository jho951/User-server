# Contract Sync (User-server)

- Contract Source: https://github.com/jho951/contract
- Service SoT Branch: `main`
- Contract Role: User master and social-link ownership

## Required Links
- User OpenAPI: https://github.com/jho951/contract/blob/main/contracts/openapi/user-service.v1.yaml
- Routing: https://github.com/jho951/contract/blob/main/contracts/routing.md
- Headers: https://github.com/jho951/contract/blob/main/contracts/headers.md
- Security: https://github.com/jho951/contract/blob/main/contracts/security.md

## Sync Checklist
- [ ] `/users/signup`, `/users/me`, `/internal/users/**` paths aligned
- [ ] internal JWT verification (`iss/aud/sub/scope`) enforced
- [ ] trace headers logged and propagated
- [ ] social link ownership behavior aligned with contract
