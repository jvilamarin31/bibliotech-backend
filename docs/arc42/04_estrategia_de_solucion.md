# 4. Estrategia de Solución

## Descripción General

Esta sección describe las estrategias y decisiones fundamentales que dan forma a la arquitectura de BiblioTech API. El detalle de cada decisión (contexto, alternativas consideradas y justificación) se documenta en la [Sección 9: Decisiones Arquitectónicas](./09_decisiones_arquitectonicas.md).

## Estrategias Principales

1. **Arquitectura MVC por Capas (Package by Layer)** 
   El backend se organiza estrictamente en capas técnicas globales (`controller`, `service`, `repository`, `entity`, `dto`). Al ser un dominio pequeño (Usuarios y Préstamos), esta estructura evita la sobreingeniería, facilita la navegación del código para los evaluadores y garantiza el principio de responsabilidad única.

2. **API REST stateless con JWT** 
   Autenticación mediante tokens JWT firmados con HMAC-SHA256 (librería JJWT). La API no guarda estado de sesión en el servidor (`SessionCreationPolicy.STATELESS`), lo que la hace escalable y segura.

3. **Persistencia exclusiva con Spring Data JPA sobre MariaDB** 
   Entidades JPA mapeadas a un esquema relacional. Se prohíbe el uso de bases de datos en memoria o NoSQL, cumpliendo estrictamente con el requisito de la prueba técnica.

4. **Contrato de API desacoplado con DTOs** 
   Uso de `record` de Java 17 para las peticiones y respuestas, junto con Jakarta Bean Validation. Las entidades JPA nunca se exponen directamente en los controladores, evitando acoplar la base de datos con la interfaz HTTP.

5. **Manejo centralizado de errores** 
   Las excepciones de negocio (como tipo de usuario inválido o préstamo duplicado) se manejan de forma global mediante un `@RestControllerAdvice`. Esto garantiza que las respuestas de error sigan el formato JSON estándar y devuelvan los códigos HTTP exactos (400, 401, 404).

6. **Generación de esquema con Hibernate y Bootstrap con `CommandLineRunner`** 
   Para agilizar el desarrollo, se utilizará la generación automática de tablas de JPA/Hibernate (`ddl-auto=update`). La creación del primer usuario administrador se gestionará mediante un `CommandLineRunner` que verificará al arrancar la aplicación si el sistema está vacío y, de ser así, insertará el usuario por defecto.

7. **Aislamiento de la Lógica de Negocio** 
   Los controladores solo reciben y delegan. Toda la lógica de negocio (validación de invitados, cálculo de días hábiles omitiendo fines de semana, formato de fechas) reside exclusivamente en la capa de servicio (`@Service`).

8. **Validación de Libros sin CRUD** 
   Para respetar el modelo de dominio sin sobredimensionar el alcance, la entidad `Libro` se persistirá en la base de datos y se validará su existencia al crear un préstamo. Sin embargo, no se expondrán endpoints de creación o edición de libros; estos se cargarán inicialmente vía `CommandLineRunner` para fines de prueba.

## Motivación

Estas estrategias fijan los lineamientos de alto nivel sobre tecnología, patrones de diseño y seguridad. El razonamiento detrás de cada elección, las alternativas consideradas y los *tradeoffs* se documentan en la [Sección 9](./09_decisiones_arquitectonicas.md). La [Sección 5](./05_vista_de_componentes.md) descompone estas estrategias en contenedores y componentes concretos.
