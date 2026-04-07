# TaskFlow
docker compose up --build

docker exec -it taskflow-db mysql -u root -p

SHOW DATABASES;
USE TaskFlow;



# 🔐 Security Execution Flow (VERY IMPORTANT)

This section describes how authentication, authorization, and exception
handling work in the application.

------------------------------------------------------------------------

## ✅ Case 1: No Token

-   Request does not contain an `Authorization` header
-   User is **not authenticated**

➡ **Handled by:** `CustomAuthenticationEntryPoint`\
➡ **Response:** `401 Unauthorized`

------------------------------------------------------------------------

## ✅ Case 2: Invalid Token

-   Token is expired, malformed, or invalid

➡ **Handled by:** `CustomAuthenticationEntryPoint`\
➡ **Response:** `401 Unauthorized`

------------------------------------------------------------------------

## ✅ Case 3: Valid Token, Wrong Role

-   Token is valid ✔\
-   User is authenticated ✔\
-   BUT user does not have required permissions ❌

➡ **Handled by:** `CustomAccessDeniedHandler`\
➡ **Response:** `403 Forbidden`

------------------------------------------------------------------------

## ✅ Case 4: Valid Token, Everything OK

-   Token is valid ✔\
-   User has correct role ✔

➡ **Result:** Request reaches Controller and is processed successfully

------------------------------------------------------------------------

## ✅ Case 5: Exception Inside Service

-   Any runtime or business exception occurs inside service layer

➡ **Handled by:** `@RestControllerAdvice`\
➡ **Response:** `500 Internal Server Error` or `400 / 404` depending on
exception type

------------------------------------------------------------------------

## 🔁 Summary Flow

Request → JwtFilter → SecurityContext → Controller\
↓\
(if error happens)\
401 → AuthenticationEntryPoint\
403 → AccessDeniedHandler\
App errors → @RestControllerAdvice

------------------------------------------------------------------------

## ⚠️ Notes

-   `@RestControllerAdvice` does **NOT** handle security errors (401,
    403)
-   Security errors are handled separately by:
    -   `AuthenticationEntryPoint` (401)
    -   `AccessDeniedHandler` (403)
-   Always return a **consistent error response structure** across all
    handlers


Logging Strategy

A structured and consistent logging strategy has been implemented to enhance observability, maintainability, and operational diagnostics across the application. Logging is primarily focused on critical layers, including global exception handling and core business services.

Global Exception Handling

The GlobalExceptionHandler is annotated with @Slf4j and is responsible for centralized exception logging. Log levels are assigned based on the nature and severity of the events:

WARN — Validation failures and request body parsing errors (e.g., malformed JSON)
WARN — Authentication and authorization-related failures
INFO — Business rule violations such as email duplication conflicts
ERROR — Unhandled runtime exceptions, including full stack trace logging for diagnostic purposes

This approach ensures clear differentiation between client-side issues, business constraints, and system-level failures.

User Service Logging

The UserService incorporates structured logging to track key business operations and state transitions:

INFO — Initiation and successful completion of user creation and update operations
WARN — Detection of data inconsistencies, such as duplicate address ZIP codes during update operations
INFO — Successful avatar upload operations
ERROR — Failures during avatar upload processes, with stack trace included for troubleshooting
Observability Outcomes

This logging strategy provides:

Consistent and predictable log structure across application layers
Appropriate log level classification aligned with industry best practices
Improved traceability of user actions and system behavior
Enhanced support for debugging, monitoring, and incident analysis in production environments


