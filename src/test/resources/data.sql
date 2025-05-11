-- =========================
-- TABLE CREATION
-- =========================
DROP TABLE IF EXISTS _user CASCADE;
DROP TABLE IF EXISTS sensor CASCADE;
DROP TABLE IF EXISTS sensor_location CASCADE;
DROP TABLE IF EXISTS sensor_type CASCADE;
DROP TABLE IF EXISTS sensor_unit_of_measure CASCADE;

CREATE TABLE IF NOT EXISTS _user (
   id SERIAL PRIMARY KEY,
   email VARCHAR(255)  NOT NULL,
   password VARCHAR(255)  NOT NULL,
   role VARCHAR(255) check (role in ('ADMINISTRATOR','VIEWER'))
);

CREATE TABLE IF NOT EXISTS sensor_type (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL
    );

CREATE TABLE IF NOT EXISTS sensor_unit_of_measure (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL
    );

CREATE TABLE IF NOT EXISTS sensor_location (
       id SERIAL PRIMARY KEY,
       name VARCHAR(40) NOT NULL
    );

CREATE TABLE IF NOT EXISTS sensor (
    id SERIAL PRIMARY KEY,
    name VARCHAR(30) NOT NULL,
    model VARCHAR(15) NOT NULL,
    range_from INTEGER NOT NULL,
    range_to INTEGER NOT NULL,
    description VARCHAR(200),
    type_id INTEGER NOT NULL REFERENCES sensor_type(id),
    unit_id INTEGER REFERENCES sensor_unit_of_measure(id),
    location_id INTEGER REFERENCES sensor_location(id)
    );

-- =========================
-- INSERT STATIC DATA
-- =========================

INSERT INTO sensor_type (name) VALUES
   ('Pressure'),
   ('Voltage'),
   ('Temperature'),
   ('Humidity');

INSERT INTO sensor_unit_of_measure (name) VALUES
  ('bar'),
  ('voltage'),
  ('°C'),
  ('%');

INSERT INTO sensor_location (name) VALUES
('kitchen'),
('street'),
('roof');

-- =========================
-- INSERT SENSOR DATA
-- =========================

INSERT INTO sensor (name, model, range_from, range_to, description, type_id, unit_id, location_id) VALUES
   ('Barometer', 'ac-23', 22, 45, 'Measures air pressure',
    (SELECT id FROM sensor_type WHERE name = 'Pressure'),
    (SELECT id FROM sensor_unit_of_measure WHERE name = 'bar'),
    (SELECT id FROM sensor_location WHERE name = 'kitchen')),
   ('Barometer', 'ac-23', 22, 45, 'Measures air pressure',
    (SELECT id FROM sensor_type WHERE name = 'Pressure'),
    (SELECT id FROM sensor_unit_of_measure WHERE name = 'bar'),
    (SELECT id FROM sensor_location WHERE name = 'kitchen')),
    ('Thermometer', 'temp-x2', 22, 45, 'Measures air temperature',
      (SELECT id FROM sensor_type WHERE name = 'Pressure'),
      (SELECT id FROM sensor_unit_of_measure WHERE name = 'bar'),
      (SELECT id FROM sensor_location WHERE name = 'kitchen')),
   ('Thermometer', 'temp-x1', 0, 100, 'Measures temperature',
    (SELECT id FROM sensor_type WHERE name = 'Temperature'),
    (SELECT id FROM sensor_unit_of_measure WHERE name = '°C'),
    (SELECT id FROM sensor_location WHERE name = 'roof')),
   ('Thermometer', 'temp-x13', 0, 100, 'Measures temperature',
    (SELECT id FROM sensor_type WHERE name = 'Temperature'),
    (SELECT id FROM sensor_unit_of_measure WHERE name = '°C'),
    (SELECT id FROM sensor_location WHERE name = 'roof'));

INSERT INTO _user (email, password, role) VALUES
      ('admin@example.com', '$2a$10$EYyKxYlawh.3PeagpW7.qei5rueDQ1Rk1les6ZtDcZwvhodEKaX0u', 'ADMINISTRATOR'),--password:1111
      ('viewer@example.com', '$2a$10$EYyKxYlawh.3PeagpW7.qei5rueDQ1Rk1les6ZtDcZwvhodEKaX0u', 'VIEWER'); --password:1111


