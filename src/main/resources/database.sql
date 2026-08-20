-- Schema for the bank_api database (owner: heartsvoid)

CREATE TABLE IF NOT EXISTS accounts (
    id            UUID PRIMARY KEY,
    account_type  VARCHAR(20) NOT NULL CHECK (account_type IN ('STANDARD', 'PREMIUM', 'GOLD'))
);

CREATE TABLE IF NOT EXISTS transactions (
    id                UUID PRIMARY KEY,
    account_id        UUID NOT NULL REFERENCES accounts(id),
    created_at        TIMESTAMPTZ NOT NULL,
    transaction_type  VARCHAR(10) NOT NULL CHECK (transaction_type IN ('IN', 'OUT')),
    amount            NUMERIC(19, 2) NOT NULL,
    reason            VARCHAR(255)
);

CREATE INDEX IF NOT EXISTS idx_transactions_account_id ON transactions(account_id);
CREATE INDEX IF NOT EXISTS idx_transactions_type ON transactions(transaction_type);
