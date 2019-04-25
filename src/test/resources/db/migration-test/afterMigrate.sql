INSERT INTO doctor (name, version) VALUES ('John Smith', 0);
INSERT INTO doctor (name, version) VALUES ('Silvia Newton', 0);
INSERT INTO doctor (name, version) VALUES ('Natalie Walsh', 0);
INSERT INTO doctor (name, version) VALUES ('Robert Vincent', 0);

INSERT INTO appointment (date, name, email, private, doctor_id, version) VALUES ('2019-01-01 09:15:25', 'Robert Smith', 'some@some.com', true, 1, 0);
INSERT INTO appointment (date, name, email, private, doctor_id, version) VALUES ('2019-01-01 09:15:00', 'Another patient', 'some@some.com', true, 1, 0);
INSERT INTO appointment (date, name, email, private, doctor_id, version) VALUES ('2019-01-01 01:05:25', 'David Bowie', 'some@some.com', true, 1, 0);
INSERT INTO appointment (date, name, email, private, doctor_id, version) VALUES (now(), 'Lou Reed', 'some@some.com', true, 1, 0);
INSERT INTO appointment (date, name, email, private, doctor_id, version) VALUES (now(), 'Another patient 2', 'some@some.com', false, 2, 0);
INSERT INTO appointment (date, name, email, private, doctor_id, version) VALUES ('2019-02-11 09:15:25', 'Miles Davis', 'some@some.com', true, 3, 0);
INSERT INTO appointment (date, name, email, private, doctor_id, version) VALUES ('2019-01-11 09:15:25', 'Pati Smith', 'some@some.com', true, 3, 0);
INSERT INTO appointment (date, name, email, private, doctor_id, version) VALUES ('2019-11-01 09:15:25', 'Another patient 3', 'some@some.com', false, 2, 0);
INSERT INTO appointment (date, name, email, private, doctor_id, version) VALUES (now(), 'David Gilmour', 'some@some.com', true, 4, 0);
INSERT INTO appointment (date, name, email, private, doctor_id, version) VALUES (now(), 'Reger Waters', 'some@some.com', true, 4, 0);
