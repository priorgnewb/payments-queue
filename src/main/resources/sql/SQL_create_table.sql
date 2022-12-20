DROP TABLE IF EXISTS payments_queue;

CREATE TABLE payments_queue
(
    id         BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    account_id INT           NOT NULL,
    amount     NUMERIC(8, 2) NOT NULL CHECK ( amount >= 0 ),
    datetime_transaction TIMESTAMP
);
