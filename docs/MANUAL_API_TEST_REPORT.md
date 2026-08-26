# Manual API Test Report

Test date: 2026-08-25  
Environment: local Docker Compose (`taskflow-app` on port 8080; healthy `taskflow-db`)  
Method: manually constructed `curl` requests against the running API, with response status and selected response fields checked using `jq`

## Follow-up: create-task fields

After the original test run, the create-task contract was extended with required `shortDescription` and `priority` fields; `status` remains required. A follow-up live test verified:

- omitting `shortDescription` and `priority` returns `400` with errors for both fields;
- sending `shortDescription`, `status`, and `priority` returns `201` and echoes the values in the response; and
- MySQL persists the same `short_description`, `status`, and `priority` values.

The follow-up task was hard-deleted and its temporary user was soft-deleted. The original results table below records the API behavior before this contract extension.

## Scope and cleanup

The test covered authentication, public user operations, search, pagination, validation, CORS, protected task operations, assignment, logout/token revocation, and error cases.

Successful registration was intentionally not exercised because it sends an external welcome email. Avatar upload was not exercised because it creates a persistent file; its contract was reviewed from the implementation.

Two unique temporary users and two tasks were used. Both tasks were hard-deleted. User deletion is implemented as a soft delete, so two test rows remain with `deleted=true`; no active test users remain.

## Results

| Area | Check | Expected | Actual | Result |
| --- | --- | ---: | ---: | --- |
| Users | List users with pagination/sort | 200 | 200 | Pass |
| Users | Blank search behaves like no filter | 200 | 200 | Pass |
| CORS | Preflight from `http://localhost:5173` | 200 + matching allow-origin | 200 + matching allow-origin | Pass |
| Users | Empty create payload | 400 | 400 with five field errors | Pass |
| Users | Valid create | 201 | 201 | Pass |
| Users | Duplicate create email | Client error | 400 validation error | Pass, status differs from registration |
| Users | Get created user | 200 | 200 | Pass |
| Users | Combined full-name search | One match | One match | Pass |
| Users | Partial JSON update | 200 + changed fields | 200 + changed fields | Pass |
| Users | Invalid nested update values | 400 | 200; invalid values persisted | Fail |
| Users | Password update followed by login | Login succeeds with new password | 401 | Fail |
| Users | Soft delete | 204 | 204 | Pass |
| Users | Get deleted user | 404 | 500 with embedded `404 NOT_FOUND` message | Fail |
| Auth | Invalid password | 401 | 401 | Pass |
| Auth | Valid login | 200 + token | 200 + token | Pass |
| Auth | Duplicate registration | 409 | 409 | Pass |
| Auth | Logout without token | 401 | 401 | Pass |
| Auth | Valid logout | 204 | 204 | Pass |
| Auth | Reuse blacklisted token | 401 | 401 | Pass |
| Tasks | List without token | 401 | 401 | Pass |
| Tasks | Empty create payload | 400 | 400 with four field errors | Pass |
| Tasks | Valid create | 201 | 201 | Pass |
| Tasks | Paginated list | 200 | 200 | Pass |
| Tasks | Get existing task | 200 | 200 | Status only passed; resolver issue below |
| Tasks | Assign one user | 200 + one user | 200 + one user | Pass |
| Tasks | Reassign same user through list endpoint | No duplicate | 200 + two copies | Fail |
| Tasks | Delete task | 204 | 204 | Pass |
| Tasks | Get deleted task | 404 | 200 | Fail |
| Debug | Authenticated `/hello9` | No sensitive fields | 200 with `password` field | Fail |

## Findings

### Critical: sensitive authentication data is exposed or logged

- `POST /hello9` serializes a raw `User` entity and includes its `password` field. Any authenticated user can call it.
- `JwtAuthenticationFilter` prints the complete bearer token to application logs.
- A mail credential is stored directly in `src/main/resources/application.properties`. Its value is deliberately not reproduced here.

Recommended action: remove `/hello9` or return a safe DTO, remove token `System.out` logging, rotate the exposed mail credential, and load secrets from environment variables or a secret store.

### High: all user administration routes are unauthenticated

The security rule `requestMatchers("/auth/**", "/users/**").permitAll()` exposes user list, create, read, update, avatar update, task-assignment route under `/users`, and soft delete without a token.

Recommended action: permit only the exact public authentication routes. Require authentication and appropriate role checks for user administration.

### High: password updates break authentication

`PATCH /users/{id}` maps `password` directly onto the entity without BCrypt encoding. The endpoint returned `200`, but login with the new password returned `401` because the stored value was not a BCrypt hash.

Recommended action: handle password changes in the service with validation and `PasswordEncoder.encode`, or remove password from the general update DTO.

### High: task-by-ID does not resolve the requested task

`CurrentTaskResolver` exists but is not added to `WebConfig.addArgumentResolvers()`. A request for a deleted task ID returned `200`, confirming that the route is not performing the intended repository lookup.

Recommended action: register both `CurrentUserResolver` and `CurrentTaskResolver`, then retest existing, malformed, missing, and deleted IDs.

### Medium: update validation is bypassed

The JSON update controller parameter lacks `@Valid`, and `ProfileUpdateRequest` has no validation annotations. A future birth date, invalid phone number, and blank address fields were accepted and persisted.

Recommended action: apply `@Valid` and add update-specific constraints consistent with create requests.

### Medium: not-found exceptions become 500 errors

`GlobalExceptionHandler.handleRuntime` catches `ResponseStatusException` before Spring can preserve its status. Getting a soft-deleted user returned `500`, with `404 NOT_FOUND` only embedded in the message.

Recommended action: add a specific `ResponseStatusException` handler that returns its status, or stop catching all `RuntimeException` values as internal errors.

### Medium: task assignment allows duplicates

Assigning a user once through `PUT /users/{userId}/tasks/{taskId}` and again through `PUT /api/tasks/{taskId}/users` returned two copies of the same user.

Recommended action: model assignments as a `Set`, check existing relationships before adding, and add a unique database constraint on `(task_id, user_id)`.

### Medium: registration validation is incomplete

Code inspection found no `@Valid` on `AuthController.register` and no comparison of `password` with `confirmPassword`. This was not executed with a new address because successful registration sends external email.

Recommended action: add `@Valid`, enforce matching passwords server-side, and decouple email delivery from the request transaction.

### Low: duplicate-email status is inconsistent

`POST /users` returns `400 Validation Failed` for a duplicate email, while `POST /auth/register` returns `409 Conflict` for the same conflict.

Recommended action: standardize duplicate resource responses, preferably as `409 Conflict`.

## Overall assessment

Core create/list/search/login/task/logout happy paths work against the live MySQL-backed service. The API should not be exposed outside a trusted local environment until the critical and high-severity findings above are addressed.
