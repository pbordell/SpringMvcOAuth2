INSERT INTO person(id, name, age, salary) VALUES (1, 'Persona1', 32, 10000);
INSERT INTO person(id, name, age, salary) VALUES (2, 'Persona2', 33, 20000);
INSERT INTO person(id, name, age, salary) VALUES (3, 'Persona3', 34, 30000);
INSERT INTO person(id, name, age, salary) VALUES (4, 'Persona4', 45, 40000);
INSERT INTO person(id, name, age, salary) VALUES (5, 'bill', 45, 40000);
INSERT INTO person(id, name, age, salary) VALUES (6, 'bob', 45, 40000);

INSERT INTO roles(id, name) VALUES (1, 'admin');
INSERT INTO roles(id, name) VALUES (2, 'user');

INSERT INTO users(id, name, rol_id, password) VALUES (1, 'admin', 1, 'admin');
INSERT INTO users(id, name, rol_id, password) VALUES (2, 'user', 2, 'user');

-- El usuario común (user) solo puede LEER
INSERT INTO role_endpoints(id, role_id, endpoint, accion) VALUES (1, 2, '/person/', 'READ');

-- El administrador (admin) puede hacer TODO en esa ruta
INSERT INTO role_endpoints(id, role_id, endpoint, accion) VALUES (2, 1, '/person/', 'READ');
INSERT INTO role_endpoints(id, role_id, endpoint, accion) VALUES (3, 1, '/person/', 'WRITE');
INSERT INTO role_endpoints(id, role_id, endpoint, accion) VALUES (4, 1, '/person/', 'DELETE');