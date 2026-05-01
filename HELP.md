# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.5.6/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.5.6/maven-plugin/build-image.html)
* [Spring Security](https://docs.spring.io/spring-boot/3.5.6/reference/web/spring-security.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/3.5.6/reference/data/sql.html#data.sql.jpa-and-spring-data)
* [Validation](https://docs.spring.io/spring-boot/3.5.6/reference/io/validation.html)

### Guides
The following guides illustrate how to use some features concretely:

* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Validation](https://spring.io/guides/gs/validating-form-input/)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

Spring Data JPA derived queries follow a fixed pattern:
<prefix>By<property><operator><connectors>...
Common prefixes:
•
findBy
•
getBy
•
readBy
•
existsBy
•
countBy
•
deleteBy
Common property parts:
•
findByEmail
•
findById
•
findByRoleAndStatus
•
findByProfileFirstName
You can traverse nested fields too. If User has profile.firstName, this works:
findByProfileFirstName(String firstName)
Common operators:
•
equality:
findByEmail(String email)
•
booleans:
findByDeletedFalse()
findByDeletedTrue()
•
null checks:
findByAvatarUrlIsNull()
findByAvatarUrlIsNotNull()
•
comparisons:
findByCreatedAtAfter(instant)
findByAgeGreaterThan(age)
findByAgeLessThanEqual(age)
•
string matching:
findByEmailContaining(String part)
findByEmailStartingWith(String prefix)
findByEmailEndingWith(String suffix)
findByEmailLike(String pattern)
•
collections:
findByIdIn(List<UUID> ids)
findByRoleIn(List<Role> roles)
Connectors:
•
And
•
Or
Example:
findByRoleAndDeletedFalse(Role role)
findByEmailOrUsername(String email, String username)
Sorting in the name:
findByDeletedFalseOrderByEmailAsc()
findByStatusOrderByCreatedAtDesc(UserStatus status)
Top/first limits:
findFirstByOrderByCreatedAtDesc()
findTop3ByDeletedFalseOrderByEmailAsc()
Examples from your project that are valid:

findFirstByOrderByCreatedAtDesc()
findTop3ByDeletedFalseOrderByEmailAsc()
Examples from your project that are valid:
Optional<User> findByIdAndDeletedFalse(UUID id);
Optional<User> findByEmailAndDeletedFalse(String email);
List<User> findAllByIdInAndDeletedFalse(List<UUID> ids);
Page<User> findAllByDeletedFalse(Pageable pageable);
boolean existsByEmail(String email);
Rules to keep in mind:
•
property names must match entity fields exactly
•
nested properties must exist on the entity graph
•
the method name must use supported keywords
•
return type can be Optional<T>, List<T>, Page<T>, Slice<T>, long, boolean, etc.
When to stop using derived names:
•
query name becomes too long
•
joins/conditions get complex
•
you need custom SQL/JPQL
•
performance needs explicit tuning
Then use:
•
@Query("...")
•
Specification
•
Criteria API
•
custom repository implementation
Example explicit version of your method:
@Query("select u from User u where u.id = :id and u.deleted = false")
Optional<User> findActiveById(@Param("id") UUID id);