# TableQ ![example event parameter](https://github.com/ojh94/TableQ/actions/workflows/main.yml/badge.svg?event=push)



# Publishing Domain : http://www.tableq.click/

# Getting Started

## Environment
#### java 17, spring boot, gradle, jpa, TDD junit 5,

## Dependencies

### Spring Boot
- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Boot Starter Thymeleaf
- Spring Boot Starter Security

### Thymeleaf
- Thymeleaf Layout Dialect
- Thymeleaf Extras Spring Security

### Database
- PostgreSQL Driver
- HikariCP Connection Pool
- H2 Database (Development/Testing)

### Validation
- Validation API
- Hibernate Validator

### OpenAPI Documentation
- SpringDoc OpenAPI WebMVC UI

### Development Tools
- Spring Boot DevTools

### Testing
- Spring Boot Starter Test
- Spring Security Test

### Utility
- Lombok




## Deploy
 - AWS EC2
 - git
 - gradle
 - S3
 - AWS CodeDeploy
 - Github Action
 - nginx


# Lesson & Learned

## 정성적 교훈

- Lombok @Data 사용 시 foreign key 설정 된 부분에서 스택 오버플로우 발생(서로 호출 무한 루프)


## 정량적 교훈

메인이 되는 기능 외에 나머지는 전주 써드파티 라이브러리에 의존하는 걸 추천
- 소규모 개발은 무엇보다 시간이 부족하기 때문에 최대한 외부 서비스를 이용할 것




