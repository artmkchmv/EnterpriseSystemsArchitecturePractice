# Лабораторная работа №4. Java Message Service

За основу взято приложение из работы №1 (JakartaEE, GlassFish). В него добавлены
журналирование изменений и оповещение по электронной почте.

## Как это работает

1. Сервисы `AuthorService` и `BookService` при добавлении, изменении и удалении
   сущности вызывают `ChangeSender`, который отправляет сообщение в топик `jms/LibraryChanges`.
   Сообщение отправляется в той же транзакции, поэтому уходит только после ее успешной фиксации.
2. Сообщения получают два MDB:
   - `ChangeLogBean` - проверяет сообщение и пишет строку в таблицу `change_log`;
   - `MailNotifierBean` - проверяет сообщение и условие оповещения, при выполнении условия отправляет письмо.

**Тип объекта назначения - Topic.** Каждое изменение должны получить оба MDB
(журнал и оповещение). В очереди сообщение достается только одному получателю,
а топик доставляет копию каждому подписчику.

**Тип сообщения - MapMessage.** Данные об изменении - это набор именованных полей,
MapMessage передает их без сериализации Java-объектов (в отличие от ObjectMessage)
и без ручного разбора строки (в отличие от TextMessage). Поля сообщения:

| Поле          | Тип    | Описание                                        |
|---------------|--------|-------------------------------------------------|
| `changeType`  | String | `INSERT`, `UPDATE` или `DELETE`                 |
| `entityClass` | String | класс сущности, например `library.model.Book`   |
| `entityId`    | int    | идентификатор сущности                          |
| `name`        | String | имя автора или название книги                   |
| `details`     | String | новые значения полей (для удаления - последние) |

**Условие оповещения:** письмо отправляется, если имя автора или название книги содержит
заданную строку (без учета регистра). Строка и адреса получателей задаются в
`src/main/webapp/WEB-INF/web.xml` (`notify/keyword`, `notify/recipients`).

## Подготовка базы данных

```
psql -U postgres -d library -f db/library.sql
psql -U postgres -d library -f db/change_log.sql
```

## Настройка GlassFish

Нужна полная версия GlassFish 7 (в Web Profile нет JMS и MDB).
JDBC-ресурс `jdbc/library` создается так же, как в работе №1.

Административные объекты JMS:

```
asadmin create-jms-resource --restype jakarta.jms.ConnectionFactory jms/LibraryConnectionFactory
asadmin create-jms-resource --restype jakarta.jms.Topic --property Name=LibraryChanges jms/LibraryChanges
asadmin list-jms-resources
```

Почтовая сессия (пример для SMTP с авторизацией):

```
asadmin create-mail-resource --mailhost smtp.example.com --mailuser user@example.com --fromaddress user@example.com --property mail-smtp-port=587:mail-smtp-auth=true:mail-smtp-starttls-enable=true:mail-smtp-password=secret mail/library
```

Для проверки без настоящего почтового сервера можно запустить локальный SMTP-сервер
(например, `python -m aiosmtpd -n -l localhost:2525`) и создать ресурс так:

```
asadmin create-mail-resource --mailhost localhost --mailuser library --fromaddress library@localhost --property mail-smtp-port=2525 mail/library
```

## Сборка и развертывание

```
mvn package
asadmin deploy target/library.war
```

Приложение: http://localhost:8080/library/

Проверка журнала после изменений в приложении:

```
psql -U postgres -d library -c "SELECT * FROM change_log ORDER BY id"
```
