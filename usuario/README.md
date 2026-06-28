# Microservicio Usuario

Usuario maneja las cuentas del sistema, sus roles, permisos y sesiones. Es uno de los servicios base del proyecto, porque varios microservicios necesitan saber que usuario existe o que rol tiene antes de registrar acciones.

## Que gestiona

- Usuarios.
- Roles.
- Permisos.
- Sesiones y autenticacion simple.
- Activacion y desactivacion de usuarios.

## Configuracion local

El proyecto Spring esta dentro de la carpeta `usuario/usuario`.

```properties
spring.application.name=usuario
server.port=8081
spring.datasource.url=jdbc:mysql://localhost:3307/usuario_bd?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
```

Base de datos:

```sql
CREATE DATABASE usuario_bd;
```

## Endpoints principales

Usuarios:

- `GET /api/v1/usuarios`
- `POST /api/v1/usuarios`
- `GET /api/v1/usuarios/{id}`
- `PUT /api/v1/usuarios/{id}`
- `DELETE /api/v1/usuarios/{id}`
- `PUT /api/v1/usuarios/{id}/activar`
- `PUT /api/v1/usuarios/{id}/desactivar`

Roles y permisos:

- `GET /api/v1/roles`
- `POST /api/v1/roles`
- `GET /api/v1/roles/{id}`
- `PUT /api/v1/roles/{id}`
- `DELETE /api/v1/roles/{id}`
- `GET /api/v1/permisos`
- `POST /api/v1/permisos`
- `GET /api/v1/permisos/{id}`
- `PUT /api/v1/permisos/{id}`
- `DELETE /api/v1/permisos/{id}`

Autenticacion:

- `POST /api/v1/auth/login`
- `POST /api/v1/auth/logout`
- `POST /api/v1/auth/recuperar-contrasena`
- `POST /api/v1/auth/validar-token`

## Uso dentro del proyecto

Pago y Soporte consultan Usuario para validar referencias. Tambien sirve como base para mantener roles y permisos separados de otros microservicios.

## Ejecutar

```powershell
cd usuario
mvn spring-boot:run
```

## Probar

```powershell
cd usuario
mvn test
```
