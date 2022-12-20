# Тестовое задание
**Стек технологий: Java 11, Spring Boot 2.7.6, Spring Data JPA, PostgreSQL, Kafka 2.13**

Существует некоторый бизнес-процесс – пополнение счета консультанта с помощью транзакций с
банковской карты в рублях.

**Требуется реализовать сервисы:**

**1. Микросервис Payment**

Должен иметь endpoint на получение {host}/pay
POST запроса с телом json в формате
```bash
{
accountId: 1, # ID аккаунта консультанта
amount: 100.2 # Сумма пополнения в рублях
}
```

Endpoint сервиса должен получать, обрабатывать этот json и отправлять в некоторую очередь
Queue таких транзакций.

**2. Микросервис Backend**

- Должен читать очередь Queue
- Сохранять транзакции из очереди Queue в базу данных Postgres «payments»
- Каждые 5 минут записывать в файл transaction.csv в локальной папке транзакции в
  формате

```bash
id;account_id;amount;datetime_transaction
1;1;100.2;2022-07-28 07:00:00
```
## Задание:
1. Спроектировать БД Postgres «payments»
2. Создать сервис “Payment” реализовать эндпоинт для приёма запросов
3. Реализовать очередь обмена сообщениями между сервисами
4. Наполнить БД Postgres «payments» транзакциями из запросов
5. Наполнить файл transaction.csv транзакциями из запросов

## Комментарий к решению:

**1. Создание базы данных PostgreSQL**

```bash
create database processing
```
- run `src/main/resources/sql/SQL_create_table.sql`

**2. Импорт проекта в IntellijIDEA**

`File -> New -> Project From Version Control-> From existing sources`

Вставить ссылку на репозиторий [https://github.com/priorgnewb/payments-queue.git](https://github.com/priorgnewb/payments-queue.git)

**3. Настройка подключения к PostgreSQL, Kafka**

+ в `src/main/resources/application.properties` указать для `spring.datasource.username`, `spring.datasource.password` `spring.datasource.url` значения, используемые вами в PostgreSQL
+ параметры Kafka брокера, топика и консюмера также доступны для изменения

Для работы приложения необходимо предварительно запустить Kafka,
инструкции см. https://kafka.apache.org/quickstart

Для запуска в Windows добавлен скрипт kafka-starter.bat

**4. Запуск проекта**

Запустить приложение `Run -> Run -> PaymentsQueueApplication`

**5. Тестирование приложения**

Отправим POST запрос с body вида
```bash
{
"accountId": 47,
"amount": 5304.84
}
```
на `http://localhost:8080/pay`

В консоли появится сообщение об обработке этого запроса,
транзакция валидируется и запишется в БД, а также запишется в топик Kafka.

Каждые 60 секунд в `output-files/transaction.csv` записываются новые транзакции из БД.

![img1](https://github.com/priorgnewb/payments-queue/blob/master/img1.png)
![img2](https://github.com/priorgnewb/payments-queue/blob/master/img2.png)
