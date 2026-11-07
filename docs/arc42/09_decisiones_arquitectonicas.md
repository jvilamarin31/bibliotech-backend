# 9. Decisiones Arquitectónicas

## Descripción General

Esta sección documenta las decisiones arquitectónicas críticas que dan forma a BiblioTech API. Cada decisión incluye su contexto, las opciones consideradas, la elección final y la justificación.

## Principales Decisiones Arquitectónicas

1. **Seguridad stateless con JWT y RBAC** 
   - **Contexto**: la API es consumida por clientes desacoplados y requiere autenticación sin guardar estado en el servidor. Además, se necesita restringir el acceso a ciertos endpoints (como crear usuarios o libros).
   - **Opciones**: sesiones de servidor con cookies vs. tokens JWT. Control de acceso por URL vs. control por roles (RBAC).
   - **Decisión**: usar **JWT** firmado con HMAC-SHA256 (JJWT) y `SessionCreationPolicy.STATELESS`. Implementar RBAC mapeando el `tipoUsuario == 2` a `ROLE_ADMIN` y el resto a `ROLE_USER` en el `JwtAuthenticationFilter`, aplicando `@PreAuthorize` en los controladores.
   - **Razonamiento**: no requiere almacenar sesiones, escala horizontalmente bien y encaja perfecto con la arquitectura REST. El RBAC asegura que un cliente no pueda ejecutar acciones administrativas.

2. **Hash de contraseñas con BCrypt** 
   - **Contexto**: las contraseñas de los usuarios no deben almacenarse en texto plano ni poder revertirse.
   - **Opciones**: hashing con BCrypt vs. MD5/SHA (inseguros) vs. Argon2.
   - **Decisión**: usar `BCryptPasswordEncoder` expuesto como bean en `ApplicationConfig`.
   - **Razonamiento**: BCrypt es un estándar robusto de la industria, incluye un "salt" incorporado para proteger contra ataques de tablas arcoíris y es fácil de implementar con Spring Security.

3. **Generación de Esquema con `ddl-auto=validate` y Script Manual** 
   - **Contexto**: el proyecto requiere persistencia relacional estricta y control sobre el esquema de la base de datos.
   - **Opciones**: generación automática con `ddl-auto=update` vs. migraciones con Flyway vs. script SQL manual + validación.
   - **Decisión**: crear la base de datos con un script SQL explícito (`database/schema.sql`) y configurar Hibernate con `spring.jpa.hibernate.ddl-auto=validate`.
   - **Razonamiento**: usar `update` es peligroso en producción porque puede alterar columnas sin avisar. El script manual demuestra control del modelo relacional, y `validate` asegura que las entidades JPA siempre coincidan con la base de datos, fallando rápido si hay discrepancias.

4. **Arquitectura MVC por Capas (Package by Layer)** 
   - **Contexto**: el sistema maneja un dominio pequeño y acotado (Usuarios, Libros y Préstamos).
   - **Opciones**: capas técnicas globales (`controller`, `service`, `repository`) vs. paquetes por feature/dominio (`user`, `book`, `loan`).
   - **Decisión**: empaquetar estrictamente por capas técnicas (`com.bibliotech.backend.controller`, `service`, etc.).
   - **Razonamiento**: para un dominio de este tamaño, la organización por features introduce sobreingeniería. La separación por capas facilita la navegación del código, aplica el principio de responsabilidad única (SRP) y es el patrón más esperado y evaluado en este tipo de pruebas.

5. **DTOs como Records de Java + Mapeo JSON a Español** 
   - **Contexto**: se necesita un contrato de API estable y validación de entrada, pero el código interno debe mantenerse en inglés.
   - **Opciones**: exponer entidades JPA directamente vs. usar DTOs.
   - **Decisión**: usar `record` de Java 21 para peticiones/respuestas, validar con Jakarta Validation y usar `@JsonProperty` para mapear los nombres en inglés a las claves en español que exige la prueba (ej. `identificacionUsuario`).
   - **Razonamiento**: evita acoplar la base de datos con la interfaz HTTP. Los records son inmutables y concisos, y el mapeo JSON permite cumplir estrictamente con el contrato en español sin ensuciar las clases Java.

6. **Manejo centralizado de excepciones** 
   - **Contexto**: se requiere un formato de error uniforme que cumpla con los mensajes literales exigidos por la prueba.
   - **Opciones**: manejar errores por controlador vs. un `@RestControllerAdvice` global.
   - **Decisión**: una clase base `BiblioTechException` + un `GlobalExceptionHandler` que produce un `ErrorResponse` (con la clave `"mensaje"` para errores de negocio).
   - **Razonamiento**: homogeneiza las respuestas de error y evita duplicar lógica. Al usar la clave `"mensaje"`, garantizamos que los tests automatizados de la prueba (que buscan ese campo exacto) pasen sin problemas.

7. **Uso de `@Transactional` en la creación de préstamos** 
   - **Contexto**: crear un préstamo implica dos operaciones de escritura: guardar el préstamo y actualizar la disponibilidad del libro.
   - **Opciones**: ejecutar las operaciones secuencialmente vs. usar transacciones.
   - **Decisión**: anotar el método `createLoan` con `@Transactional` en `LoanService`.
   - **Razonamiento**: garantiza la integridad de los datos. Si por alguna razón la actualización del libro falla (ej. error de conexión a mitad de proceso), la transacción hace rollback y evita que se cree un préstamo "fantasma" sin haber bloqueado el libro.

8. **Cálculo de Días Hábiles con Algoritmo Propio** 
   - **Contexto**: la prueba exige sumar días hábiles (omitir sábados y domingos) según el tipo de usuario.
   - **Opciones**: usar una librería externa de días hábiles vs. escribir un método propio.
   - **Decisión**: implementar el método `calculateBusinessDays` con un bucle `while` y `java.time.DayOfWeek`.
   - **Razonamiento**: evita agregar dependencias innecesarias. El algoritmo es fácil de testear unitariamente y se adapta perfectamente a las reglas de negocio, sin contar el día actual y omitiendo fines de semana.

9. **Bootstrap del Administrador con `CommandLineRunner`** 
   - **Contexto**: se necesita un usuario administrador inicial para poder operar la API (ya que los endpoints están protegidos).
   - **Opciones**: insertar el admin con un script SQL directo en la base de datos vs. usar un `CommandLineRunner`.
   - **Decisión**: usar un componente `DataInitializer` que verifica si hay usuarios y, si no, crea uno usando `PasswordEncoder`.
   - **Razonamiento**: es mucho más seguro. Al usar el `CommandLineRunner`, Spring se encarga de encriptar la contraseña con BCrypt automáticamente. Además, es idempotente: si el usuario ya existe, no hace nada (útil al reiniciar el servidor).

## Tabla Resumen de Decisiones

| ID | Título de la Decisión | Nivel de Impacto | Sistemas Afectados |
| -- | --------------------- | ---------------- | ------------------ |
| 1  | Seguridad stateless con JWT y RBAC | Crítico | Auth, Seguridad, Controladores |
| 2  | Hash de contraseñas con BCrypt | Alto | Auth, UserService |
| 3  | Generación de Esquema con `validate` | Alto | Persistencia (MariaDB) |
| 4  | Arquitectura MVC por Capas | Medio | Todo el backend |
| 5  | DTOs como Records + Mapeo a Español | Medio | Toda la API |
| 6  | Manejo centralizado de excepciones | Medio | Todo el backend |
| 7  | Transacciones en creación de préstamos | Alto | LoanService |
| 8  | Algoritmo propio para días hábiles | Medio | LoanService |
| 9  | Bootstrap de Admin con CommandLineRunner | Bajo | DataInitializer |

## Motivación

Documentar estas decisiones proporciona una comprensión clara de las elecciones que guían la estructura y funcionalidad de BiblioTech API, y sirve de referencia para las implementaciones futuras o mantenimientos del sistema.

## NOTA SOBRE ADR

Las decisiones documentadas aquí siguen el formato **Architectural Decision Record (ADR)**, una práctica formalizada para registrar decisiones de arquitectura.

Para más información sobre ADR:

- [Michael Nygard – Documenting Architecture Decisions](https://cognitect.com/blog/2011/11/15/documenting-architecture-decisions.html)
- [Repositorio de Plantillas ADR en GitHub](https://github.com/joelparkerhenderson/architecture-decision-record)
