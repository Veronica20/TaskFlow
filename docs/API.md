# TaskFlow API Reference

Last checked against the local Docker Compose service on 2026-08-25.

## Base URL and authentication

The local base URL is `http://localhost:8080`.

Protected endpoints require a JWT in this header:

```http
Authorization: Bearer <token>
```

The current security configuration is:

| Routes | Access |
| --- | --- |
| `/auth/**` | Public |
| `/users/**` | Public |
| `GET /uploads/**` | Public |
| `/api/tasks/**`, `/hello9` | Bearer token required |

This table documents the implementation as tested. In particular, all user create, update, and delete operations are currently public.

## Common conventions

### JSON and dates

Send JSON requests with `Content-Type: application/json`. Dates use ISO format: `YYYY-MM-DD`.

Enum values are case-sensitive:

- `role`: `USER`, `ADMIN`
- `status` for a user: `ACTIVE`, `INACTIVE`
- `status` for a task: `TODO`, `IN_PROGRESS`, `DONE`
- `priority`: `LOW`, `MEDIUM`, `HIGH`
- `language`: `EN`, `RU`, `HY`

### Pagination

`GET /users` and `GET /api/tasks` use Spring pagination parameters:

| Parameter | Meaning | Example |
| --- | --- | --- |
| `page` | Zero-based page number | `page=0` |
| `size` | Items per page | `size=10` |
| `sort` | Property and direction | `sort=email,asc` |

The response is a Spring `Page` object containing `content`, `totalElements`, `totalPages`, `number`, `size`, `first`, and `last`, plus pageable metadata.

### Errors

Validation errors normally use this shape:

```json
{
  "timestamp": "2026-08-25T13:00:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "One or more fields failed validation",
  "path": "/users",
  "errors": [
    "email: must not be blank"
  ]
}
```

Security failures use the same general fields, although `timestamp` and `errors` may be omitted. Known inconsistencies are recorded in the [manual test report](MANUAL_API_TEST_REPORT.md).

## Authentication

### Log in

`POST /auth/login` is public.

```bash
curl -X POST http://localhost:8080/auth/login \
  -H 'Content-Type: application/json' \
  -d '{
    "email": "user@example.com",
    "password": "password123"
  }'
```

Success: `200 OK`

```json
{
  "token": "<jwt>"
}
```

Invalid credentials return `401`. Blank or malformed fields return `400`.

### Register

`POST /auth/register` is public.

```bash
curl -X POST http://localhost:8080/auth/register \
  -H 'Content-Type: application/json' \
  -d '{
    "email": "user@example.com",
    "password": "password123",
    "confirmPassword": "password123"
  }'
```

Success returns `200 OK` with a token and sends a welcome email. A duplicate email returns `409 Conflict`.

Current implementation caveat: the controller does not apply bean validation to this request and the service does not compare `password` with `confirmPassword`.

### Log out

`POST /auth/logout` blacklists the presented token.

```bash
curl -X POST http://localhost:8080/auth/logout \
  -H "Authorization: Bearer $TOKEN"
```

Success: `204 No Content`. A missing, empty, or invalid bearer token returns `401`. A blacklisted token can no longer access protected endpoints.

## Users

### Create a user

`POST /users` is public and returns `201 Created`.

```bash
curl -X POST http://localhost:8080/users \
  -H 'Content-Type: application/json' \
  -d '{
    "email": "ada@example.com",
    "password": "password123",
    "role": "USER",
    "status": "ACTIVE",
    "profile": {
      "firstName": "Ada",
      "lastName": "Lovelace",
      "birthDate": "1990-12-10",
      "phoneNumber": "+37499123456"
    },
    "addresses": [
      {
        "country": "Armenia",
        "city": "Yerevan",
        "street": "Example Street 1",
        "zipCode": "0010",
        "primary": true
      }
    ],
    "preferences": {
      "emailNotifications": true,
      "smsNotifications": false,
      "language": "EN"
    }
  }'
```

Required fields and rules:

- `email`: nonblank, valid email, unique
- `password`: 8–30 characters
- `role`: required
- `profile`: required; first and last names are nonblank, birth date must be in the past, phone is an optional 10–15 digit value with an optional leading `+`
- `addresses`: at least one valid address
- `preferences`: optional; when present, `language` is required
- `status`: optional; defaults to `ACTIVE`

A duplicate email on this endpoint returns validation status `400`.

### List and search users

`GET /users` is public and returns non-deleted users.

```bash
curl --get http://localhost:8080/users \
  --data-urlencode 'search=Ada Lovelace' \
  --data 'page=0' \
  --data 'size=10' \
  --data 'sort=email,asc'
```

The optional `search` value is trimmed and matched case-insensitively against email, first name, last name, and the combined `firstName + space + lastName`. An omitted or blank value returns all non-deleted users.

### Get one user

`GET /users/{userId}` is public.

```bash
curl http://localhost:8080/users/11111111-1111-1111-1111-111111111111
```

Success: `200 OK`. The intended missing-user response is `404`, but the current global exception handler converts it to `500`; see the test report.

### Update a user with JSON

`PATCH /users/{userId}` is public and applies a partial update.

```bash
curl -X PATCH http://localhost:8080/users/11111111-1111-1111-1111-111111111111 \
  -H 'Content-Type: application/json' \
  -d '{
    "status": "INACTIVE",
    "profile": {
      "firstName": "Augusta"
    },
    "preferences": {
      "emailNotifications": false
    }
  }'
```

Accepted top-level fields are `email`, `password`, `status`, `profile`, `preferences`, and `addresses`. Omitted fields are preserved. Supplying `addresses` replaces the address set by matching entries on `zipCode`.

Do not use the `password` field until the current password-update bug is fixed: it is stored without BCrypt encoding, after which normal login fails. JSON updates also currently skip request validation.

### Update a user and avatar

`PATCH /users/{userId}` also accepts `multipart/form-data`. The `user` part is optional JSON and the `avatar` part is an optional image.

```bash
curl -X PATCH http://localhost:8080/users/11111111-1111-1111-1111-111111111111 \
  -F 'user={"profile":{"firstName":"Ada"}};type=application/json' \
  -F 'avatar=@avatar.png;type=image/png'
```

Allowed avatar media types are JPEG, PNG, GIF, and WebP. The configured maximum file size is 2 MB and maximum multipart request size is 5 MB. A stored avatar is exposed under `/uploads/avatars/<generated-name>`.

### Delete a user

`DELETE /users/{userId}` is public and performs a soft delete.

```bash
curl -X DELETE http://localhost:8080/users/11111111-1111-1111-1111-111111111111
```

Success: `204 No Content`. The row remains in the database with `deleted=true` and `status=INACTIVE`, and is excluded from normal user lookup and search.

## Tasks

All task endpoints require a valid bearer token.

### Create a task

`POST /api/tasks` returns `201 Created`.

```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{
    "title": "Prepare release",
    "shortDescription": "Complete the release checklist",
    "description": "Run the release checklist",
    "deadline": "2026-09-01",
    "status": "TODO",
    "priority": "HIGH"
  }'
```

All six fields are required. `title`, `shortDescription`, and `description` must be nonblank.

### List tasks

`GET /api/tasks` returns a paginated task list.

```bash
curl 'http://localhost:8080/api/tasks?page=0&size=10&sort=deadline,asc' \
  -H "Authorization: Bearer $TOKEN"
```

### Get one task

`GET /api/tasks/{taskId}` is intended to return one task.

```bash
curl http://localhost:8080/api/tasks/22222222-2222-2222-2222-222222222222 \
  -H "Authorization: Bearer $TOKEN"
```

Current implementation caveat: `CurrentTaskResolver` is not registered with Spring MVC. Manual testing showed this route returning `200` even after the task had been deleted, so clients must not rely on it until the resolver is registered.

### Assign one user

`PUT /users/{userId}/tasks/{taskId}` assigns one active user to a task.

```bash
curl -X PUT \
  http://localhost:8080/users/11111111-1111-1111-1111-111111111111/tasks/22222222-2222-2222-2222-222222222222 \
  -H "Authorization: Bearer $TOKEN"
```

Success: `200 OK` with the updated task.

### Assign several users

`PUT /api/tasks/{taskId}/users` adds the supplied active users to a task.

```bash
curl -X PUT http://localhost:8080/api/tasks/22222222-2222-2222-2222-222222222222/users \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{
    "users": [
      {"userId": "11111111-1111-1111-1111-111111111111"}
    ]
  }'
```

The request de-duplicates IDs inside one payload, but it does not prevent re-assigning a user already attached to the task. Current testing observed duplicate users in the response.

### Delete a task

`DELETE /api/tasks/{taskId}` hard-deletes a task.

```bash
curl -X DELETE http://localhost:8080/api/tasks/22222222-2222-2222-2222-222222222222 \
  -H "Authorization: Bearer $TOKEN"
```

Success: `204 No Content`.

No task-update endpoint is currently implemented.

## Debug endpoint

`POST /hello9` requires authentication and returns the first raw `User` entity. It is not suitable for client use because its response currently includes the password field. Use the DTO-based `/users` endpoints instead.
