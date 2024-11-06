INSERT INTO user_profiles (email, name, password, animal_notif_status)
VALUES ('nolkoeva@gmail.com', 'admin', '$2b$12$rAbcG66.Jawjm31LImq3GOjA.oGlYhlWiNaQPL2SEzBcg9SGBvw26', 'NONE');

INSERT INTO animals (admission_date, birth_date, breed, gender, implant_chip_id, name, pattern, primary_color, species, id)
VALUES ('2024-10-17', '2022-05-01', 'Bulldog', 'M', '11111111-1111-1111-1111-111111111111', 'Max', 'Solid', 'Brown', 'Dog', 99999);

INSERT INTO animals (admission_date, birth_date, breed, gender, implant_chip_id, name, pattern, primary_color, species, id)
VALUES ('2024-06-17', '2021-04-16', 'Persian', 'F', '11111111-1111-1111-1111-111111111112', 'Kitty', '', 'Brown', 'Cat', 99998);

INSERT INTO vaccinations (animal_id, batch, comments, email, vaccination_time, vaccine, id)
VALUES (99998, 'RTL-0001', 'First vaccination for the new cat.', 'nolkoeva@gmail.com', '2024-10-18 10:00:00', 'Rabies', 99999);

INSERT INTO vaccinations (animal_id, batch, comments, email, vaccination_time, vaccine, id)
VALUES (99998, 'RTL-0002', 'Second vaccination for the new cat.', 'nolkoeva@gmail.com', '2024-10-18 10:00:00', 'Rabies', 99998);

INSERT INTO vaccinations (animal_id, batch, comments, email, vaccination_time, vaccine, id)
VALUES (99999, 'RTL-0003', 'First vaccination for the new dog.', 'nolkoeva@gmail.com', '2024-10-18 10:00:00', 'Rabies', 99997);
