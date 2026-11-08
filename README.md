# BiblioTech API

[![CI](https://github.com/jvilamarin31/bibliotech-backend/actions/workflows/ci.yml/badge.svg)](https://github.com/jvilamarin31/bibliotech-backend/actions/workflows/ci.yml)

API REST para la gestión de autenticación, usuarios, catálogo de libros y préstamos de la biblioteca. Desarrollada con Spring Boot 4 y MariaDB, con un enfoque en buenas prácticas, seguridad (JWT + RBAC) y arquitectura en capas.

## Features

- **EPI-001 — Autenticación y usuarios:** inicio de sesión (JWT), creación, consulta, actualización y eliminación de usuarios con control de acceso basado en roles (RBAC).
- **EPI-002 — Gestión de préstamos:** crear, consultar y listar préstamos. Incluye cálculo automatizado de días hábiles y validación estricta para usuarios invitados.
- **EPI-003 — Gestión de catálogo:** CRUD completo de libros, restringido exclusivamente a administradores.

## Stack

| Tecnología | Versión |
|---|---|
| Java | 21 |
| Spring Boot | 4.1.2 |
| Spring Security | 7.1.2 (JWT stateless) |
| Spring Data JPA | 4.1.2 |
| MariaDB | 10.11 (local) / Cloud |
| JWT (jjwt) | 0.12.6 |
| Maven | 3.10 (con wrapper) |
| Docker | multi-stage build |
| Lombok | reducción de boilerplate |
| Springdoc OpenAPI | Swagger UI |

## Estructura del proyecto

```text
bibliotech-backend/
├── .github/
│   └── workflows/
│       └── ci.yml         # Pipeline de integración continua (GitHub Actions)
├── backend/               # API REST (Spring Boot): código fuente, tests y contenedores
│   ├── src/               # Código fuente Java (controllers, services, dtos, entities)
│   ├── Dockerfile         # Imagen del backend
│   └── pom.xml            # Dependencias Maven
├── database/              # Script de inicialización de MariaDB (init.sql)
├── docs/                  # Documentación del proyecto
│   ├── arc42/             # Documentación de arquitectura (00 al 12)
│   ├── diagrams/          # Diagramas C4 (contexto, contenedores, componentes)
│   ├── epicas-y-hu.md     # Épicas e Historias de Usuario
│   └── postman/           # Colección de Postman exportada
├── docker-compose.yml     # Levanta App + BD
├── docker-compose.db-only.yml # Levanta solo la BD
├── .env.example           # Plantilla de variables de entorno (Docker)
├── LICENSE                # Licencia MIT
└── README.md
```

## Requisitos previos

- **Opción Docker:** solo [Docker](https://www.docker.com/).
- **Opción local:** JDK 21, MariaDB y Maven 3.10+ (o usar el wrapper incluido).

## Configuración

El proyecto utiliza variables de entorno para proteger las credenciales. Dependiendo de cómo lo ejecutes, necesitarás configurar los archivos `.env`:

1. **Para Docker (raíz del proyecto):** copia el `.env.example` de la raíz a `.env`. Allí se configuran las claves JWT.
2. **Para local (carpeta `backend/`):** copia el `backend/.env.example` a `backend/.env`. Allí se configuran las credenciales de MariaDB y JWT.

| Variable | Descripción | Usada en |
|---|---|---|
| `DB_URL` | Cadena de conexión a MariaDB (ej. `jdbc:mariadb://localhost:3306/bibliotech_db`) | Ejecución local |
| `DB_USERNAME` | Usuario de la base de datos | Ejecución local |
| `DB_PASSWORD` | Contraseña de la base de datos | Ejecución local |
| `JWT_KEY` | Clave secreta en base64 (mín. 32 bytes) para firmar los tokens | Docker y local |
| `JWT_EXPIRATION` | Expiración del token en milisegundos (ej. `86400000` = 24 horas) | Docker y local |

> **Nota:** el `docker-compose.yml` define credenciales de ejemplo para desarrollo (`root` / `rootpassword`). Cámbialas antes de desplegar en producción.

## Cómo ejecutar

### Opción 1: Docker (App + BD) — Recomendada

Levanta la base de datos MariaDB y el backend en contenedores.

**Ubicación:** raíz del proyecto (donde está el archivo `docker-compose.yml`).

```bash
docker compose up -d --build
```

La API arranca en `http://localhost:8080`. La BD se inicializa automáticamente con el script `database/init.sql`.

### Opción 2: Ejecución local (Maven) + Base de datos en Docker

Si prefieres ejecutar la aplicación localmente (en tu terminal o IDE de preferencia) pero mantener la base de datos en Docker:

**Ubicación:** raíz del proyecto.

```bash
docker compose -f docker-compose.db-only.yml up -d
```

**Ubicación:** carpeta `backend/` (donde está el archivo `mvnw`).

```bash
./mvnw spring-boot:run
```

> Al arrancar por primera vez, un `CommandLineRunner` creará automáticamente el usuario administrador: **Usuario:** `admin` / **Contraseña:** `admin123`.

## Tests

El proyecto cuenta con **15 pruebas unitarias y de integración** (JUnit 6 + Mockito) distribuidas en:

- `LoanServiceTest` (7)
- `UserServiceTest` (3)
- `AuthControllerTest` (2)
- `UserControllerTest` (3)

Además existe `BackendApplicationTests`, que valida el arranque completo del contexto de Spring. Ese test requiere MariaDB y las variables de `backend/.env`, por lo que el pipeline de CI lo excluye y ejecuta únicamente los 15 tests que no dependen de una base de datos externa.

**Ubicación:** carpeta `backend/`.

```bash
# 15 tests sin base de datos (lo que ejecuta el CI)
./mvnw test -Dtest='!BackendApplicationTests'

# Suite completa (requiere MariaDB y backend/.env)
./mvnw test
```

## Endpoints

> La documentación detallada de cada endpoint (parámetros, validaciones, respuestas y ejemplos) está disponible en la colección de Postman y en Swagger.

Los endpoints protegidos requieren el header `Authorization: Bearer <token>`.

### Auth (públicos)

| Método | Ruta | Descripción |
|---|---|---|
| `POST` | `/auth/login` | Inicio de sesión (devuelve token JWT) |

### Usuarios (autenticado)

| Método | Ruta | Descripción |
|---|---|---|
| `POST` | `/usuarios` | Crear usuario (solo Admin) |
| `GET` | `/usuarios` | Listar usuarios (solo Admin) |
| `GET` | `/usuarios/{id}` | Consultar usuario (Admin o el propio usuario) |
| `PUT` | `/usuarios/{id}` | Actualizar usuario (Admin o el propio usuario). Un cliente no puede modificar su `tipoUsuario` ni su `identificacion` |
| `DELETE` | `/usuarios/{id}` | Eliminar usuario (Admin o el propio usuario) |

### Libros (autenticado — solo Admin)

| Método | Ruta | Descripción |
|---|---|---|
| `POST` | `/libros` | Crear libro |
| `GET` | `/libros` | Listar libros |
| `GET` | `/libros/{isbn}` | Consultar libro por ISBN |
| `PATCH` | `/libros/{isbn}` | Actualizar libro |
| `DELETE` | `/libros/{isbn}` | Eliminar libro |

### Préstamos (autenticado)

| Método | Ruta | Descripción |
|---|---|---|
| `POST` | `/prestamos` | Crear préstamo (solo Admin) |
| `GET` | `/prestamos` | Listar préstamos activos (Admin ve todos, Cliente ve los suyos) |
| `GET` | `/prestamos/{id}` | Consultar préstamo por ID (Admin o el dueño del préstamo) |

## Documentación

El proyecto cuenta con una documentación exhaustiva para facilitar su mantenimiento y comprensión.

### Documentación de arquitectura (arc42)

La carpeta `docs/arc42/` contiene el documento de arquitectura estructurado con el modelo **arc42**. Incluye desde la introducción y restricciones hasta las decisiones arquitectónicas (ADRs), requisitos de calidad y riesgos.

- **Índice principal:** `docs/arc42/00_resumen.md`
- **Diagramas C4:** ubicados en `docs/diagrams/` (archivos Draw.io y SVGs).
- **Épicas e Historias de Usuario:** disponibles en `docs/epicas-y-hu.md`.

### Swagger UI (interactivo)

La API cuenta con documentación generada automáticamente.

- **Local:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **Producción (Render):** [https://bibliotech-backend-uasq.onrender.com/swagger-ui/index.html](https://bibliotech-backend-uasq.onrender.com/swagger-ui/index.html)

### Postman

La colección exportada se encuentra en `docs/postman/BIBLIOTECH-API.postman_collection.json` y está documentada endpoint por endpoint en la pestaña **Docs** de cada request.

> **Servidor en producción (Render):** puedes probar la API en vivo configurando la base URL con `https://bibliotech-backend-uasq.onrender.com`.

**Variables de colección configuradas:**

1. `base_url`: por defecto está vacía. Debes asignarle la URL del entorno (`http://localhost:8080` o `https://bibliotech-backend-uasq.onrender.com`) antes de ejecutar cualquier petición.
2. `jwt_token_admin`: se guarda automáticamente al hacer login con el admin.
3. `jwt_token_cliente`: se guarda automáticamente al hacer login con un cliente.

## Deseables completados

- [x] Pruebas unitarias/integración con JUnit 6 + Mockito.
- [x] Documentación de la API (Swagger UI y colección de Postman exportada).
- [x] Contenerización (Dockerfile multi-stage y archivos Docker Compose).
- [x] Despliegue en servidor gratuito (Render + MariaDB Cloud).
- [x] Historial de commits incremental (Conventional Commits).
- [x] Integración continua con GitHub Actions.

## Licencia

Este proyecto está bajo la licencia MIT. Consulta el archivo [LICENSE](LICENSE) para más detalles.
