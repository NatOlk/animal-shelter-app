ALTER TABLE animals ALTER COLUMN version SET DEFAULT 0;
ALTER TABLE vaccinations ALTER COLUMN version SET DEFAULT 0;

INSERT INTO animals (admission_date, birth_date, breed, gender, implant_chip_id, name, pattern, primary_color, species, id)
VALUES ('2024-06-17', '2018-04-16', 'Persian', 'F', '11111111-21111111-1111', 'BoBo', '', 'Brown', 'Cat', 1);

INSERT INTO animals (admission_date, birth_date, breed, gender, implant_chip_id, name, pattern, primary_color, species, id)
VALUES ('2024-07-21', '2021-04-16', 'Persian', 'F', '11111111-11111111-1111', 'Kitty', '', 'Brown', 'Cat', 2);

INSERT INTO vaccinations (animal_id, batch, comments, email, vaccination_time, vaccine, id)
VALUES (2, 'RTL-0001', 'First vaccination for the new cat.', ':defaultEmail', '2024-10-18 10:00:00', 'Rabies', 1),
       (2, 'RTL-0002', 'Second vaccination for the new cat.', ':defaultEmail', '2024-12-18 10:00:00', 'Rabies', 2);

INSERT INTO animals (admission_date, birth_date, breed, gender, implant_chip_id, name, pattern, primary_color, species, id)
VALUES ('2024-06-30', '2020-05-10', 'Bulldog', 'M', '11111111-11111111-1113', 'Bobby', '', 'White', 'Dog', 3);

INSERT INTO vaccinations (animal_id, batch, comments, email, vaccination_time, vaccine, id)
VALUES (3, 'RTL-0003', 'First vaccination for the new dog.', ':defaultEmail', '2024-10-02 10:00:00', 'Rabies', 3);

INSERT INTO animals (admission_date, birth_date, breed, gender, implant_chip_id, name, pattern, primary_color, species, id)
VALUES ('2024-07-13', '2021-06-01', 'Syrian', 'F', '11111111-11111111-1114', 'Hammy', '', 'Gold', 'Hamster', 4);

INSERT INTO vaccinations (animal_id, batch, comments, email, vaccination_time, vaccine, id)
VALUES (4, 'RTL-0004', 'Hamster first vaccine.', ':defaultEmail', '2024-10-20 10:30:00', 'Distemper', 4);

INSERT INTO animals (admission_date, birth_date, breed, gender, implant_chip_id, name, pattern, primary_color, species, id)
VALUES ('2024-10-21', '2022-08-25', 'Himalayan', 'M', '11111111-11111111-1115', 'Thumper', '', 'White', 'Rabbit', 5);

INSERT INTO vaccinations (animal_id, batch, comments, email, vaccination_time, vaccine, id)
VALUES (5, 'RTL-0005', 'First vaccination for rabbit.', ':defaultEmail', '2024-10-22 12:00:00', 'Parvovirus', 5);

INSERT INTO animals (admission_date, birth_date, breed, gender, implant_chip_id, name, pattern, primary_color, species, id)
VALUES ('2024-06-17', '2021-03-11', 'English', 'F', '11111111-11111111-1116', 'Molly', '', 'Brown', 'Guinea Pig', 6);

INSERT INTO vaccinations (animal_id, batch, comments, email, vaccination_time, vaccine, id)
VALUES (6, 'RTL-0006', 'Guinea Pig vaccination.', ':defaultEmail', '2024-10-23 11:45:00', 'Adenovirus', 6);

INSERT INTO animals (admission_date, birth_date, breed, gender, implant_chip_id, name, pattern, primary_color, species, id)
VALUES ('2024-06-17', '2020-12-14', 'European', 'M', '11111111-11111111-1117', 'Fuzz', '', 'Gray', 'Ferret', 7);

INSERT INTO vaccinations (animal_id, batch, comments, email, vaccination_time, vaccine, id)
VALUES (7, 'RTL-0007', 'First vaccine for ferret.', ':defaultEmail', '2024-10-25 09:30:00', 'Leptospirosis', 7);

INSERT INTO animals (admission_date, birth_date, breed, gender, implant_chip_id, name, pattern, primary_color, species, id)
VALUES ('2024-06-17', '2021-02-07', 'Macaw', 'F', '11111111-11111111-11118', 'Rio', '', 'Blue', 'Parrot', 8);

INSERT INTO vaccinations (animal_id, batch, comments, email, vaccination_time, vaccine, id)
VALUES (8, 'RTL-0008', 'Macaw vaccination.', ':defaultEmail', '2024-10-27 08:15:00', 'Avian Influenza', 8);

INSERT INTO animals (admission_date, birth_date, breed, gender, implant_chip_id, name, pattern, primary_color, species, id)
VALUES ('2024-06-17', '2023-03-21', 'Siamese', 'F', '11111111-11111111-1119', 'Luna', '', 'Blue', 'Cat', 9);

INSERT INTO animals (admission_date, birth_date, breed, gender, implant_chip_id, name, pattern, primary_color, species, id)
VALUES ('2024-06-17', '2022-09-04', 'Labrador', 'M', '11111111-11111111-1120', 'Rocky', '', 'Yellow', 'Dog', 10);

INSERT INTO animals (admission_date, birth_date, breed, gender, implant_chip_id, name, pattern, primary_color, species, id)
VALUES ('2024-06-17', '2022-01-10', 'Maine Coon', 'M', '11111111-11111111-1121', 'Chester', '', 'Gray', 'Cat', 11);

INSERT INTO animals (admission_date, birth_date, breed, gender, implant_chip_id, name, pattern, primary_color, species, id)
VALUES ('2024-06-17', '2022-04-25', 'Rottweiler', 'F', '11111111-11111111-1122', 'Bella', '', 'Black', 'Dog', 12);

INSERT INTO animals (admission_date, birth_date, breed, gender, implant_chip_id, name, pattern, primary_color, species, id)
VALUES ('2024-06-17', '2021-11-12', 'Dutch', 'M', '11111111-11111111-1123', 'Oreo', '', 'Black', 'Rabbit', 13);

INSERT INTO animals (admission_date, birth_date, breed, gender, implant_chip_id, name, pattern, primary_color, species, id)
VALUES ('2024-06-17', '2023-05-15', 'Persian', 'F', '11111111-11111111-11124', 'Lily', '', 'Cream', 'Cat', 14);

INSERT INTO animals (admission_date, birth_date, breed, gender, implant_chip_id, name, pattern, primary_color, species, id)
VALUES ('2024-11-27', '2022-07-30', 'Poodle', 'M', '11111111-11111111-1125', 'Sammy', '', 'White', 'Dog', 15);

INSERT INTO animals (admission_date, birth_date, breed, gender, implant_chip_id, name, pattern, primary_color, species, id)
VALUES ('2024-06-17', '2021-10-20', 'Golden Retriever', 'F', '11111111-11111111-1126', 'Goldie', '', 'Gold', 'Dog', 16);

INSERT INTO animals (admission_date, birth_date, breed, gender, implant_chip_id, name, pattern, primary_color, species, id)
VALUES ('2024-06-17', '2023-02-12', 'Schnauzer', 'M', '11111111-11111111-1127', 'Charlie', '', 'Silver', 'Dog', 17);

INSERT INTO animals (admission_date, birth_date, breed, gender, implant_chip_id, name, pattern, primary_color, species, id)
VALUES ('2024-06-17', '2020-06-05', 'Tabby', 'M', '11111111-11111111-1128', 'Max', '', 'Orange', 'Cat', 18);

INSERT INTO animals (admission_date, birth_date, breed, gender, implant_chip_id, name, pattern, primary_color, species, id)
VALUES ('2024-06-17', '2021-12-22', 'Dachshund', 'M', '11111111-11111111-1129', 'Milo', '', 'Brown', 'Dog', 19);

INSERT INTO animals (admission_date, birth_date, breed, gender, implant_chip_id, name, pattern, primary_color, species, id)
VALUES ('2024-06-17', '2023-09-18', 'British Shorthair', 'F', '11111111-11111111-1130', 'Nina', '', 'Gray', 'Cat', 20);

INSERT INTO vaccinations (animal_id, batch, comments, email, vaccination_time, vaccine, id)
VALUES (9, 'RTL-0009', '', ':defaultEmail', '2024-10-28 10:00:00', 'Rabies', 9);

INSERT INTO vaccinations (animal_id, batch, comments, email, vaccination_time, vaccine, id)
VALUES (10, 'RTL-0010', '', ':defaultEmail', '2024-10-29 09:30:00', 'Parvovirus', 10);

INSERT INTO vaccinations (animal_id, batch, comments, email, vaccination_time, vaccine, id)
VALUES (11, 'RTL-0011', '', ':defaultEmail', '2024-10-30 08:15:00', 'Leptospirosis', 11);

INSERT INTO vaccinations (animal_id, batch, comments, email, vaccination_time, vaccine, id)
VALUES (12, 'RTL-0012', '', ':defaultEmail', '2024-11-01 11:00:00', 'Distemper', 12);

INSERT INTO vaccinations (animal_id, batch, comments, email, vaccination_time, vaccine, id)
VALUES (13, 'RTL-0013', '', ':defaultEmail', '2024-11-02 10:30:00', 'Rabies', 13);

INSERT INTO vaccinations (animal_id, batch, comments, email, vaccination_time, vaccine, id)
VALUES (14, 'RTL-0014', '', ':defaultEmail', '2024-11-03 10:00:00', 'Rabies', 14);

INSERT INTO vaccinations (animal_id, batch, comments, email, vaccination_time, vaccine, id)
VALUES (15, 'RTL-0015', '', ':defaultEmail', '2024-11-04 09:45:00', 'Parvovirus', 15);

INSERT INTO vaccinations (animal_id, batch, comments, email, vaccination_time, vaccine, id)
VALUES (16, 'RTL-0016', '', ':defaultEmail', '2024-11-05 09:00:00', 'Leptospirosis', 16);

INSERT INTO vaccinations (animal_id, batch, comments, email, vaccination_time, vaccine, id)
VALUES (17, 'RTL-0017', '', ':defaultEmail', '2024-11-06 11:30:00', 'Distemper', 17);

INSERT INTO vaccinations (animal_id, batch, comments, email, vaccination_time, vaccine, id)
VALUES (18, 'RTL-0018', '', ':defaultEmail', '2024-11-07 09:15:00', 'Rabies', 18);

INSERT INTO vaccinations (animal_id, batch, comments, email, vaccination_time, vaccine, id)
VALUES (19, 'RTL-0019', '', ':defaultEmail', '2024-11-08 10:30:00', 'Parvovirus', 19);

SELECT setval('animals_seq', (SELECT MAX(id) FROM animals), true);

SELECT setval('vaccinations_seq', (SELECT MAX(id) FROM vaccinations), true);