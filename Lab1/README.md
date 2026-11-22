# Лабораторная работа №1. Приложение JakartaEE

Предметная область - библиотека. Две сущности:

- Author (автор): имя, страна, год рождения;
- Book (книга): название, жанр, год издания, число страниц, автор.

Слои приложения:

| Слой          | Технология                         | Пакет / файлы                  |
|---------------|------------------------------------|--------------------------------|
| Данные        | JPA-сущности (Java Beans)          | `library.model`, `persistence.xml` |
| Логика        | Сессионные EJB (`@Stateless`)      | `library.ejb`                  |
| Представление | Сервлеты + JSP/JSTL                | `library.web`, `src/main/webapp` |

Используется: Java 17, Jakarta EE 10, GlassFish 7, PostgreSQL 18.

## Подготовка

1. Установить GlassFish 7 и PostgreSQL 18.
2. Запустить сервер: `asadmin start-domain` - http://localhost:4848.
3. Создать базу и заполнить ее скриптом:

   ```
   createdb -U postgres library
   psql -U postgres -d library -f db/library.sql
   ```

## Настройка сервера (без IDE)

Драйвер PostgreSQL (`postgresql-42.x.jar`) добавляется в сервер:

```
asadmin add-library postgresql-42.7.4.jar
asadmin restart-domain
```

Пул соединений и JDBC-ресурс:

```
asadmin create-jdbc-connection-pool --datasourceclassname org.postgresql.ds.PGSimpleDataSource --restype javax.sql.DataSource --property user=postgres:password=postgres:serverName=localhost:portNumber=5432:databaseName=library LibraryPool
asadmin ping-connection-pool LibraryPool
asadmin create-jdbc-resource --connectionpoolid LibraryPool jdbc/library
```

## Сборка и развертывание

```
mvn package 
asadmin deploy target/library.war
```

Либо в консоли администратора: *Applications → Deploy*.

Приложение: http://localhost:8080/library/

Повторное развертывание после изменений: `asadmin redeploy --name library target/library.war`.
