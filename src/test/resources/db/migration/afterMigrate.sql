INSERT INTO doctor (name, version) VALUES ('John Smith', 0);
INSERT INTO doctor (name, version) VALUES ('Silvia Newton', 0);
INSERT INTO doctor (name, version) VALUES ('Natalie Walsh', 0);
INSERT INTO doctor (name, version) VALUES ('Robert Vincent', 0);

INSERT INTO patient (name, doctor_id, version) VALUES ('Patient 1', 1, 0);
INSERT INTO patient (name, doctor_id, version) VALUES ('Patient 2', 1, 0);
INSERT INTO patient (name, doctor_id, version) VALUES ('Patient 3', 2, 0);
INSERT INTO patient (name, doctor_id, version) VALUES ('Patient 4', 3, 0);
INSERT INTO patient (name, doctor_id, version) VALUES ('Patient 5', 3, 0);
INSERT INTO patient (name, doctor_id, version) VALUES ('Patient 6', 4, 0);

INSERT INTO appointment (date, name, private, patient_id, doctor_id, version) VALUES (now(), null, true, 1, 1, 0);
INSERT INTO appointment (date, name, private, patient_id, doctor_id, version) VALUES (now(), 'Another patient', true, null, 1, 0);
INSERT INTO appointment (date, name, private, patient_id, doctor_id, version) VALUES (now(), null, true, 2, 1, 0);
INSERT INTO appointment (date, name, private, patient_id, doctor_id, version) VALUES (now(), null, true, 3, 1, 0);
INSERT INTO appointment (date, name, private, patient_id, doctor_id, version) VALUES (now(), 'Another patient 2', false, null, 2, 0);
INSERT INTO appointment (date, name, private, patient_id, doctor_id, version) VALUES (now(), null, true, 4, 3, 0);
INSERT INTO appointment (date, name, private, patient_id, doctor_id, version) VALUES (now(), null, true, 5, 3, 0);
INSERT INTO appointment (date, name, private, patient_id, doctor_id, version) VALUES (now(), 'Another patient 3', false, null, 2, 0);
INSERT INTO appointment (date, name, private, patient_id, doctor_id, version) VALUES (now(), null, true, 6, 4, 0);
INSERT INTO appointment (date, name, private, patient_id, doctor_id, version) VALUES (now(), null, true, 1, 4, 0);
