DROP TABLE IF EXISTS role_endpoints;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS person;
DROP TABLE IF EXISTS oauth_access_token;
DROP TABLE IF EXISTS oauth_refresh_token;
DROP TABLE IF EXISTS oauth_client_details;

CREATE TABLE person(
  id NUMERIC IDENTITY PRIMARY KEY,
  name VARCHAR NOT NULL,
  age NUMERIC NOT NULL,
  salary NUMERIC NOT NULL
);

CREATE TABLE roles(
  id NUMERIC IDENTITY PRIMARY KEY,
  name VARCHAR NOT NULL,
  CONSTRAINT UQ_NAME UNIQUE (name)
);

CREATE TABLE users(
  id NUMERIC IDENTITY PRIMARY KEY,
  username VARCHAR NOT NULL,
  rol_id NUMERIC NOT NULL,
  password VARCHAR NOT NULL,
  FOREIGN KEY (rol_id) REFERENCES roles(id),
  CONSTRAINT UQ_USERNAME UNIQUE (username)
);

CREATE TABLE role_endpoints(
  id NUMERIC IDENTITY PRIMARY KEY,
  role_id NUMERIC NOT NULL,
  endpoint VARCHAR NOT NULL,
  accion VARCHAR NOT NULL,
  FOREIGN KEY (role_id) REFERENCES roles(id)
);



-- Tabla donde se guardarán los Access Tokens reales
CREATE TABLE oauth_access_token (
  token_id VARCHAR(256),
  token LONGVARBINARY,
  authentication_id VARCHAR(256) PRIMARY KEY,
  user_name VARCHAR(256),
  client_id VARCHAR(256),
  authentication LONGVARBINARY,
  refresh_token VARCHAR(256)
);

-- Tabla donde se guardarán los Tokens de Refresco
CREATE TABLE oauth_refresh_token (
  token_id VARCHAR(256),
  token LONGVARBINARY,
  authentication LONGVARBINARY
);

-- Opcional (por si en el futuro migras también los clientes a la BD)
CREATE TABLE oauth_client_details (
  client_id VARCHAR(256) PRIMARY KEY,
  resource_ids VARCHAR(256),
  client_secret VARCHAR(256),
  scope VARCHAR(256),
  authorized_grant_types VARCHAR(256),
  web_server_redirect_uri VARCHAR(256),
  authorities VARCHAR(256),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additional_information VARCHAR(4096),
  autoapprove VARCHAR(256)
);