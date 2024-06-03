INSERT INTO roles_db (role) VALUES ('ADMIN');
INSERT INTO roles_db (role) VALUES ('USER');

-- Insertar usuario
INSERT INTO users_db (firstname, lastname, email, username, password, mobile, fix, address_id) VALUES ('mohammed', 'el yousfi', 'moha_mar@hotmail.es', 'moha', 'mohammed', '651740832',null,null);

-- Obtener el ID del usuario insertado (suponiendo que es el primero en ser insertado)
SET @userId = (SELECT id FROM users_db WHERE email = 'moha_mar@hotmail.es');

-- Obtener los IDs de los roles
SET @adminRoleId = (SELECT id FROM roles_db WHERE role = 'ADMIN');
SET @userRoleId = (SELECT id FROM roles_db WHERE role = 'USER');

-- Vincular usuario con roles
INSERT INTO users_roles (user_id, role_id) VALUES (@userId, @adminRoleId);
INSERT INTO users_roles (user_id, role_id) VALUES (@userId, @userRoleId);