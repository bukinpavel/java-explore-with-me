CREATE TABLE IF NOT EXISTS endpoint_hit (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    app VARCHAR(200),
    uri VARCHAR(200),
    ip VARCHAR(100),
    time_stamp TIMESTAMP WITHOUT TIME ZONE);