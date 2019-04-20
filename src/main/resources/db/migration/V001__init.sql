CREATE TABLE doctor (
    id          SERIAL PRIMARY KEY,
    name        VARCHAR NULL,
    version     INTEGER
);

CREATE TABLE patient (
    id          SERIAL PRIMARY KEY,
    name        VARCHAR NULL,
    doctor_id   INTEGER NULL,
    version     INTEGER,

    CONSTRAINT doc FOREIGN KEY (doctor_id) REFERENCES doctor(id)
);

CREATE TABLE appointment (
    id          SERIAL PRIMARY KEY,
    date        TIMESTAMP NOT NULL,
    name        VARCHAR NULL,
    private     BOOLEAN DEFAULT false,
    patient_id  INTEGER NULL,
    doctor_id   INTEGER NULL,
    version     INTEGER,

    CONSTRAINT doc FOREIGN KEY (doctor_id) REFERENCES doctor(id),
    CONSTRAINT pat FOREIGN KEY (patient_id) REFERENCES patient(id)
);
