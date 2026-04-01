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


