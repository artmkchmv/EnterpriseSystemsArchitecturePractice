# Лабораторная работа №3. RESTful веб-сервис

За основу взято приложение из работы №2 (Spring, библиотека: авторы и книги).
К нему добавлен REST API в пакете `library.rest`.

## JAX-RS или Spring REST

| Критерий               | JAX-RS (Jersey, RESTEasy)                                   | Spring REST (Spring MVC)                                   |
|------------------------|-------------------------------------------------------------|------------------------------------------------------------|
| Что это                | Спецификация Jakarta EE, есть несколько реализаций          | Часть Spring MVC, одна реализация                          |
| Где работает           | На сервере приложений (GlassFish, WildFly) или с Jersey в контейнере сервлетов | В любом контейнере сервлетов, в Spring Boot — встроенный Tomcat |
| Аннотации              | `@Path`, `@GET`, `@Produces`, `@Consumes`, `@PathParam`     | `@RestController`, `@GetMapping`, `@PathVariable`, `@RequestBody` |
| JSON / XML             | JSON-B и JAXB через `MessageBodyReader/Writer`              | Jackson через `HttpMessageConverter`, XML — модуль `jackson-dataformat-xml` |
| Внедрение зависимостей | CDI                                                          | Spring IoC                                                 |
| Валидация, ошибки      | Bean Validation, `ExceptionMapper`                          | Bean Validation, `@ExceptionHandler`, `ResponseStatusException` |
| Переносимость          | Код не привязан к конкретной реализации                     | Код привязан к Spring                                      |

**Выбран Spring REST.** Причины:

- приложение из работы №2 уже построено на Spring: REST-контроллеры используют те же
  сервисы, транзакции и конфигурацию, ничего не нужно связывать заново;
- JAX-RS в Spring Boot потребовал бы отдельной интеграции (Jersey) и второго механизма
  сериализации рядом с уже имеющимся;
- поддержка JSON и XML делается одним и тем же механизмом: Jackson работает с обоими форматами
  по одним и тем же классам DTO, формат выбирается по заголовкам `Accept` и `Content-Type`;
- добавить инструкцию `xml-stylesheet` в XML-ответ можно, переопределив один метод стандартного
  XML-конвертера.

Переносимость между серверами приложений, главное преимущество JAX-RS, для этой работы не важна.

## REST API

| Метод  | URL                         | Описание                       |
|--------|-----------------------------|--------------------------------|
| GET    | `/api/authors`              | список авторов                 |
| GET    | `/api/authors/{id}`         | автор                          |
| GET    | `/api/authors/{id}/books`   | книги автора                   |
| POST   | `/api/authors`              | добавить автора (201 + Location) |
| PUT    | `/api/authors/{id}`         | изменить автора                |
| DELETE | `/api/authors/{id}`         | удалить автора вместе с книгами (204) |
| GET    | `/api/books`                | список книг                    |
| GET    | `/api/books/{id}`           | книга                          |
| POST   | `/api/books`                | добавить книгу (201 + Location) |
| PUT    | `/api/books/{id}`           | изменить книгу                 |
| DELETE | `/api/books/{id}`           | удалить книгу (204)            |

Если объекта нет — 404, при неверных данных — 400.

### JSON и XML

Формат ответа выбирается по заголовку `Accept` (`application/json` или `application/xml`),
формат тела запроса — по `Content-Type`. Без заголовка `Accept` возвращается JSON.

```
curl http://localhost:8080/api/authors
curl -H "Accept: application/xml" http://localhost:8080/api/authors/1

curl -X POST -H "Content-Type: application/json" \
     -d '{"fullName":"Антон Чехов","country":"Россия","birthYear":1860}' \
     http://localhost:8080/api/authors

curl -X POST -H "Content-Type: application/xml" -H "Accept: application/xml" \
     -d '<book><title>Вишнёвый сад</title><publishYear>1904</publishYear><pages>90</pages><authorId>5</authorId></book>' \
     http://localhost:8080/api/books

curl -X PUT -H "Content-Type: application/json" \
     -d '{"title":"1984","genre":"Антиутопия","publishYear":1949,"pages":328,"authorId":3}' \
     http://localhost:8080/api/books/5

curl -X DELETE http://localhost:8080/api/books/5
```

Пример XML-ответа:

```xml
<?xml version='1.0' encoding='UTF-8'?>
<?xml-stylesheet type="text/xsl" href="/xsl/library.xsl"?>
<author><id>1</id><fullName>Лев Толстой</fullName><country>Россия</country><birthYear>1828</birthYear></author>
```

### XSL-преобразование

Файл `src/main/resources/static/xsl/library.xsl` лежит в статической части веб-слоя и доступен
по адресу `/xsl/library.xsl`. Класс `XslXmlHttpMessageConverter` добавляет ссылку на него в начало
каждого XML-ответа. Браузер запрашивает `application/xml`, поэтому, открыв в нем
http://localhost:8080/api/authors, можно увидеть HTML-страницу со списком авторов и ссылками
на авторов, их книги и общий список книг.

## Запуск

База данных и настройки те же, что в работе №2:

```
psql -U postgres -d library -f db/library.sql
mvn spring-boot:run
```

Страницы Spring MVC из работы №2 остаются доступны по адресу http://localhost:8080/.
