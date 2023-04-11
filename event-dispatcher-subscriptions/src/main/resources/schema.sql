CREATE TABLE IF NOT EXISTS subscription (
     id LONG PRIMARY KEY,
     created_at TIMESTAMP(6) NOT NULL,
     subscriber_uri VARCHAR(255) NOT NULL,
     filters JSON
);