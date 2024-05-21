-- CREATE TABLE STATION
DROP TABLE IF EXISTS "station";

CREATE TABLE IF NOT EXISTS "station" (
    id SERIAL PRIMARY KEY,
    location VARCHAR(100)
);

-- CREATE TABLE PRODUCT
DROP TABLE IF EXISTS "product";

CREATE TABLE IF NOT EXISTS "product" (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DOUBLE PRECISION
);

-- CREATE TABLE STOCKMOVE
DROP TABLE IF EXISTS "stockmove";

CREATE TABLE IF NOT EXISTS "stockmove" (
    id SERIAL PRIMARY KEY,
    type VARCHAR(5) CHECK (type = 'ENTRY' or type = 'OUT'),
    amount DOUBLE PRECISION,
    datetime TIMESTAMP DEFAULT current_timestamp,
    isMoney BOOLEAN NOT NULL DEFAULT false,
    productId BIGINT REFERENCES "product"(id),
    stationId BIGINT REFERENCES "station"(id)
);

-- -- RELATION STATION & PRODUCT
CREATE TABLE "stock" (
    id SERIAL PRIMARY KEY,
    productId BIGINT REFERENCES "product"(id),
    stationId BIGINT REFERENCES "station"(id),
    quantity DOUBLE PRECISION,
    datetime TIMESTAMP DEFAULT current_timestamp,
    evaporationRate DOUBLE PRECISION
);

-- -- ADD DEFAULT VALUE
INSERT INTO "station" (location) VALUES
    ('station1');

INSERT INTO "product" (name, price) VALUES
    ('essence', 5900),  -- id.1
    ('gasoil', 4900),   -- id.2
    ('petrol', 2130);   -- id.3
