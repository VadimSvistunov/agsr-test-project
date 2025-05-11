
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

           ('Thermometer', 'temp-x1', 0, 100, 'Measures temperature',
            (SELECT id FROM sensor_type WHERE name = 'Temperature'),
            (SELECT id FROM sensor_unit_of_measure WHERE name = '°C'),
            (SELECT id FROM sensor_location WHERE name = 'roof'));

INSERT INTO _user (email, password, role) VALUES
          ('admin@example.com', '$2a$10$EYyKxYlawh.3PeagpW7.qei5rueDQ1Rk1les6ZtDcZwvhodEKaX0u', 'ADMINISTRATOR'),--password:1111
          ('viewer@example.com', '$2a$10$EYyKxYlawh.3PeagpW7.qei5rueDQ1Rk1les6ZtDcZwvhodEKaX0u', 'VIEWER'); --password:1111
