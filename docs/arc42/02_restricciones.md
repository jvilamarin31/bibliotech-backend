# 2. Restricciones

## Descripción General

Esta sección describe las restricciones técnicas y de negocio que afectan el diseño y desarrollo de la API de BiblioTech. Estas decisiones son definitivas y enmarcan la implementación del sistema.

## Restricciones Externas (Impuestas por la Prueba)

- **Stack Tecnológico**: 
  - **Lenguaje**: Java 21.
  - **Framework**: Spring Boot 4.1.2 con gestor de dependencias **Maven**.
  - **Persistencia**: Base de datos relacional **MariaDB** exclusivamente (Driver: `org.mariadb.jdbc.Driver`, Dialecto: `MariaDBDialect`). Queda prohibido el uso de bases de datos NoSQL o en memoria.
  - **Seguridad**: Autenticación stateless obligatoria mediante **JWT** (JSON Web Tokens) firmado con **HMAC-SHA256**, utilizando la librería **JJWT (0.12.6)**.
- **Reglas de Negocio Estrictas**: Los errores de validación (tipoUsuario inválido, invitado con préstamo activo) deben devolver códigos HTTP exactos (400) y los mensajes literales solicitados en el documento de la prueba.
- **Formato de Fechas**: La fecha de devolución (`fechaMaximaDevolucion`) debe formatearse estrictamente como `dd/MM/yyyy`.

## Restricciones Internas

- **Patrón Arquitectónico**: **MVC por Capas (Package by Layer)**. Dado el tamaño del dominio (3 entidades), se descarta la organización por dominios (Package by Feature) para evitar sobreingeniería. El código se organizará estrictamente en: `controller`, `service`, `repository`, `entity`, `dto`, `exception`, `security` y `config`.
- **Responsabilidad Única (SRP)**: Queda totalmente prohibida la lógica de negocio en los controladores. Los controladores solo reciben DTOs, delegan al servicio y retornan respuestas HTTP. La lógica (cálculo de días hábiles, validaciones) reside exclusivamente en la capa `service`.
- **Separación de DTOs**: No se expondrán las entidades de la base de datos directamente en las respuestas de la API; se usarán Data Transfer Objects (DTOs).
- **Fecha de Entrega**: El repositorio debe estar completo y documentado a más tardar el **07/11/2026** (5 días hábiles desde el 31/10/2026).
