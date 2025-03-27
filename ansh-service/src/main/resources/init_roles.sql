INSERT INTO roles (id, name, level) VALUES
(1, 'USER', 1),
(2, 'VOLUNTEER', 1),
(3, 'EMPLOYEE', 2),
(4, 'DOCTOR', 2),
(5, 'ADMIN', 3);

SELECT setval('roles_seq', (SELECT MAX(id) FROM roles), true);