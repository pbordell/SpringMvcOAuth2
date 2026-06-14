## SpringMvcOAuth2

Este es un proyecto base que implementa la seguridad y autenticación en una aplicación Web Java utilizando **Spring MVC** y el protocolo **OAuth2**. Permite gestionar el acceso seguro a los recursos (API REST) mediante el flujo de credenciales de usuario (*Password Grant Type*).

## 🚀 Características

*   **Spring MVC**: Estructura robusta para la creación de APIs REST y controladores.
*   **OAuth2**: Servidor de autorización y recursos integrado para proteger los endpoints.
*   **Java**: Desarrollado al 100% utilizando el ecosistema de Java.
*   **Maven**: Gestión de dependencias y ciclo de vida del proyecto (`pom.xml`).

## 🛠️ Requisitos Previos

Antes de ejecutar la aplicación, asegúrate de tener instalado:

*   Java JDK 8 o superior.
*   Apache Maven.
*   Un servidor de aplicaciones local (por ejemplo, Apache Tomcat) configurado para desplegar en el puerto `8080`.

## 💻 Instalación y Despliegue

1. Clona este repositorio en tu máquina local:
   ```bash
   git clone https://github.com
   ```
2. Accede al directorio del proyecto:
   ```bash
   cd SpringMvcOAuth2
   ```
3. Construye el proyecto utilizando Maven:
   ```bash
   mvn clean install
   ```
4. Despliega el archivo `.war` generado en tu servidor Tomcat local.

---

## 🧪 Pruebas de la Aplicación (Endpoints)

Puedes probar el flujo de autenticación y consumo de recursos utilizando herramientas como **Postman**, **Insomnia** o **cURL**.

### 1. Obtener el Token de Acceso (Access Token)

Para autenticarte y obtener un token válido, debes realizar una petición HTTP utilizando el flujo de contraseña (*Resource Owner Password Credentials*).

*   **URL**: `http://localhost:8080/SpringMvcOAuth2/oauth/token?grant_type=password`
*   **Método HTTP**: `POST`
*   **Cabeceras (Headers)**: Incluye las credenciales de cliente (`username` y `password`) requeridas por el servidor OAuth2 para autorizar la petición.

**Ejemplo de respuesta exitosa:**
```json
{
  "access_token": "723698aa-6a78-4f84-9910-7b8c3fc92f76",
  "token_type": "bearer",
  "expires_in": 43199,
  "scope": "read write"
}
```

### 2. Acceder a un Recurso Protegido

Una vez que obtengas el `access_token`, puedes realizar peticiones a los controladores protegidos (por ejemplo, el recurso `person`).

*   **URL**: `http://localhost:8080/SpringMvcOAuth2/person/?access_token=TU_ACCESS_TOKEN`
*   **Métodos HTTP soportados**: `GET`, `POST`, `PUT`, `DELETE` (Añadir los parámetros o el cuerpo JSON que se desee en función del verbo HTTP).

**Ejemplo con cURL:**
```bash
curl -X GET "http://localhost:8080/SpringMvcOAuth2/person/?access_token=723698aa-6a78-4f84-9910-7b8c3fc92f76"
```

---

## 📁 Estructura del Repositorio

*   `/src`: Contiene el código fuente de la aplicación (Controladores, configuración de seguridad, etc.).
*   `pom.xml`: Archivo de configuración de Maven con las dependencias del proyecto.
*   `.gitignore`: Archivo para omitir reglas de rastreo en Git.
