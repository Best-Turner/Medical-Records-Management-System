drop table IF EXISTS doctor_patient, appointments, doctor_schedule, patients, doctors;


CREATE TABLE  patients
(
    id            serial primary key not null,
    name          varchar(100)       not null,
    age           int                not null,
    policy_number varchar(50)        not null
);


CREATE TABLE  doctors
(
    id         serial primary key not null,
    name       varchar(50)        not null,
    speciality varchar(50)
);

CREATE TABLE doctor_schedule
(
    id        serial primary key not null,
    date      DATE               not null,
    time      TIME               not null,
    is_booked boolean            not null,
    doctor_id int                not null,
    foreign key (doctor_id) references doctors (id)
);

CREATE TABLE  appointments
(
    id         SERIAL PRIMARY KEY,
    date       DATE NOT NULL,
    time       TIME NOT NULL,
    doctor_id  INT  NOT NULL,
    patient_id INT  NOT NULL,
    FOREIGN KEY (doctor_id) REFERENCES doctors (id),
    FOREIGN KEY (patient_id) REFERENCES patients (id)
);

CREATE TABLE  doctor_patient
(
    id_doctor  INT,
    id_patient INT,
    PRIMARY KEY (id_doctor, id_patient),
    FOREIGN KEY (id_doctor) REFERENCES doctors (id),
    FOREIGN KEY (id_patient) REFERENCES patients (id)
);


INSERT INTO patients(name, age, policy_number)
VALUES ('Александр', 52, '123-456'),
       ('Сергей', 45, '654-321'),
       ('Юлия', 32, '111-222'),
       ('Алексей', 25, '222-333');

INSERT INTO doctors(name, speciality)
VALUES ('Василий', 'SURGEON'),
       ('Юрий', 'THERAPIST'),
       ('Светлана', 'PEDIATRICIAN');

INSERT INTO doctor_schedule(date, time, is_booked, doctor_id)
VALUES ('2024-01-10', '13:45:00', false, 1),
       ('2024-01-10', '14:00:00', false, 1),
       ('2024-01-10', '14:15:00', true, 1),
       ('2024-01-10', '10:30:00', true, 2),
       ('2024-01-10', '11:00:00', false, 2),
       ('2024-01-11', '12:10:00', false, 3),
       ('2024-01-11', '13:00:00', false, 3),
       ('2024-01-11', '13:30:00', true, 3);

INSERT INTO appointments(date, time, doctor_id, patient_id)
VALUES ('2024-01-10', '14:15:00', 1, 1),
       ('2024-01-10', '10:30:00', 2, 1),
       ('2024-01-11', '13:30:00', 3, 1);

INSERT INTO doctor_patient (id_doctor, id_patient)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (2, 1),
       (2, 2),
       (3, 1);