INSERT INTO roles_db (role) VALUES ('ADMIN');
INSERT INTO roles_db (role) VALUES ('USER');

--
--INSERT INTO address_db (country, community, province, city, zip_code, street, number, floor, door) VALUES ('España', 'Comunidad Autónoma', 'Provincia', 'Ciudad', '12345', 'Principal', '123', '1', 'A');
--
---- Obtener el ID de la dirección insertada
--SET @addressId = LAST_INSERT_ID();
---- Insertar usuario
--INSERT INTO users_db (firstname, lastname, email, username, password, mobile, fix, address_id) VALUES ('mohammed', 'el yousfi', 'moha_mar@hotmail.es', 'moha', '$2a$10$Y5dINhCeV12xrKodBTm.Gu0F.2RtUyz4WA1eCHFglYjHCDOICOr76', '651740832',null,@addressId);
--
---- Obtener el ID del usuario insertado (suponiendo que es el primero en ser insertado)
--SET @userId = (SELECT id FROM users_db WHERE email = 'moha_mar@hotmail.es');
--
---- Obtener los IDs de los roles
--SET @adminRoleId = (SELECT id FROM roles_db WHERE role = 'ADMIN');
--SET @userRoleId = (SELECT id FROM roles_db WHERE role = 'USER');
--
---- Vincular usuario con roles
--INSERT INTO users_roles (user_id, role_id) VALUES (@userId, @adminRoleId);
--INSERT INTO users_roles (user_id, role_id) VALUES (@userId, @userRoleId);


        -- Insertar tallas de zapatos
INSERT INTO sizes (shoe_size) VALUES ('SIZE_35');
INSERT INTO sizes (shoe_size) VALUES ('SIZE_36');
INSERT INTO sizes (shoe_size) VALUES ('SIZE_37');
INSERT INTO sizes (shoe_size) VALUES ('SIZE_38');
INSERT INTO sizes (shoe_size) VALUES ('SIZE_39');
INSERT INTO sizes (shoe_size) VALUES ('SIZE_40');
INSERT INTO sizes (shoe_size) VALUES ('SIZE_41');
INSERT INTO sizes (shoe_size) VALUES ('SIZE_42');
INSERT INTO sizes (shoe_size) VALUES ('SIZE_43');
INSERT INTO sizes (shoe_size) VALUES ('SIZE_44');
INSERT INTO sizes (shoe_size) VALUES ('SIZE_45');
INSERT INTO sizes (shoe_size) VALUES ('SIZE_46');