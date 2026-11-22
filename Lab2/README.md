# Лабораторная работа №2. Приложение на Spring

Предметная область - библиотека. Две сущности:

- Author (автор): имя, страна, год рождения;
- Book (книга): название, жанр, год издания, число страниц, автор.

Слои приложения:

| Слой          | Технология                                    | Пакет / файлы |
|---------------|-----------------------------------------------|---------------|
| Данные        | JPA-сущности (Java Beans), Hibernate, Spring Data JPA | `library.model`, `library.repository`, `application.properties` |
| Логика        | Spring Beans (`@Service`, `@Transactional`)   | `library.service` |
| Представление | Spring MVC + Thymeleaf                        | `library.web`, `templates` |

Используется: Java 17, Spring Boot 3.3, Hibernate 6, PostgreSQL 18.

## Запуск

1. Создать базу данных и заполнить ее скриптом:

   ```
   createdb -U postgres library
   psql -U postgres -d library -f db/library.sql
   ```

2. При необходимости поменять логин и пароль в `src/main/resources/application.properties`.
3. Собрать и запустить:

   ```
   mvn spring-boot:run
   ```

Приложение: http://localhost:8080/
