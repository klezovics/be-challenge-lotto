CREATE TABLE tenant
(
    id   UUID PRIMARY KEY,
    name VARCHAR NOT NULL
);

CREATE TABLE customer
(
    id        UUID   PRIMARY KEY,
    number    BIGINT NOT NULL,
    tenant_id UUID NOT NULL REFERENCES tenant (id)
);

CREATE TABLE balance
(
    id            UUID PRIMARY KEY,
    balance_cents BIGINT NOT NULL,
    customer_id   UUID   NOT NULL REFERENCES customer (id)
);


CREATE TABLE transaction
(
    id           UUID PRIMARY KEY,
    status       VARCHAR NOT NULL,
    amount_cents BIGINT  NOT NULL,
    balance_id   UUID    NOT NULL REFERENCES balance (id),
    created_at   TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);