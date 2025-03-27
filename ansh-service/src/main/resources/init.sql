INSERT INTO user_profiles (id, email, name, password, animal_notif_status, roles)
VALUES (
  1,
  ':defaultEmail',
  'admin',
  '$2b$12$rAbcG66.Jawjm31LImq3GOjA.oGlYhlWiNaQPL2SEzBcg9SGBvw26',
  'NONE',
  'ADMIN,DOCTOR'
);

SELECT setval('user_profiles_seq', (SELECT MAX(id) FROM user_profiles), true);