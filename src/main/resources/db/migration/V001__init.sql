CREATE TABLE doctor (
    id          SERIAL PRIMARY KEY,
    name        VARCHAR NULL,
    version     INTEGER
);

CREATE TABLE appointment (
    id          SERIAL PRIMARY KEY,
    date        TIMESTAMP NOT NULL,
    name        VARCHAR NOT NULL,
    email       VARCHAR NOT NULL,
    private     BOOLEAN DEFAULT false,
    doctor_id   INTEGER NULL,
    version     INTEGER,

    CONSTRAINT doc FOREIGN KEY (doctor_id) REFERENCES doctor(id)
);
