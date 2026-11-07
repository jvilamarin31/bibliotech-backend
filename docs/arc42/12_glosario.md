# 12. Glosario

## Descripción General

Este glosario incluye todos los términos esenciales utilizados en la documentación arquitectónica y en el código de BiblioTech API, ordenados alfabéticamente. Cada término incluye un enlace a la sección relevante para más detalles.

---

- **ADR (Architectural Decision Record)**: Formato para registrar decisiones de arquitectura con su contexto y justificación.  
  [Ver más](./09_decisiones_arquitectonicas.md)

- **Afiliado (Tipo 1)**: Tipo de usuario de la biblioteca que tiene permitido llevarse libros por un máximo de 10 días hábiles.
  [Ver más](../epicas-y-hu.md)

- **arc42**: Plantilla estandarizada para documentar y comunicar arquitecturas de software.  
  [Ver más](./01_introduccion_y_objetivos.md)

- **BCrypt**: Algoritmo de hashing de contraseñas con "salt" incorporado, utilizado por `PasswordEncoder` para proteger las credenciales en la base de datos.  
  [Ver más](./08_conceptos_transversales.md)

- **Bean Validation / Jakarta Validation**: Conjunto de anotaciones (`@NotBlank`, `@NotNull`, `@Size`, `@Min`, `@Max`) utilizadas en los DTOs para validar la entrada de datos antes de que lleguen a la lógica de negocio.  
  [Ver más](./04_estrategia_de_solucion.md)

- **BiblioTechException**: Clase base abstracta para las excepciones de negocio, que transporta un `HttpStatus` para estandarizar las respuestas de error.
  [Ver más](./08_conceptos_transversales.md)

- **C4**: Modelo de diagramas de arquitectura en niveles (contexto, contenedores, componentes).  
  [Ver más](./05_vista_de_componentes.md)

- **DTO (Data Transfer Object)**: Objeto de transporte de datos entre capas; en BiblioTech se implementa con `record` de Java 21 y mapeo a JSON (`@JsonProperty`).  
  [Ver más](./04_estrategia_de_solucion.md)

- **Días Hábiles**: Días laborables de lunes a viernes (excluyendo sábados y domingos). Utilizados para calcular la fecha máxima de devolución de un préstamo.
  [Ver más](./06_vista_de_tiempo_de_ejecucion.md)

- **Empleado (Tipo 2)**: Tipo de usuario que actúa como Administrador/Bibliotecario del sistema. Tiene permitido llevarse libros por 8 días hábiles y es el único con acceso a los endpoints de gestión.
  [Ver más](../epicas-y-hu.md)

- **Épica**: Agrupación de historias de usuario con un objetivo común de negocio.  
  [Ver más](../epicas-y-hu.md)

- **GlobalExceptionHandler**: Componente `@RestControllerAdvice` que centraliza el manejo de errores y transforma las excepciones en respuestas JSON estandarizadas (HTTP 400, 401, 403, 404).  
  [Ver más](./08_conceptos_transversales.md)

- **HU (Historia de Usuario)**: Unidad de requisito funcional desde la perspectiva del usuario (HU-001..HU-015).  
  [Ver más](../epicas-y-hu.md)

- **IDOR (Insecure Direct Object Reference)**: Vulnerabilidad de seguridad que ocurre cuando un usuario puede acceder a recursos de otros simplemente cambiando un ID en la URL. Mitigado en BiblioTech mediante validaciones en el servicio.  
  [Ver más](./08_conceptos_transversales.md)

- **Invitado (Tipo 3)**: Tipo de usuario de la biblioteca que tiene permitido llevarse libros por 7 días hábiles, con la restricción de que solo puede tener un préstamo activo a la vez.
  [Ver más](../epicas-y-hu.md)

- **ISBN (International Standard Book Number)**: Identificador único para libros. Utilizado en BiblioTech como llave de negocio y restricción `UNIQUE` en la base de datos.  
  [Ver más](./05_vista_de_componentes.md)

- **JPA / Hibernate**: Estándar y framework de persistencia utilizados para mapear las entidades Java (`User`, `Book`, `Loan`) a las tablas de MariaDB.  
  [Ver más](./04_estrategia_de_solucion.md)

- **JWT (JSON Web Token)**: Token firmado (HMAC-SHA256) utilizado para la autenticación stateless de la API. Contiene los claims del usuario y su rol.  
  [Ver más](./08_conceptos_transversales.md)

- **MariaDB**: Motor de base de datos relacional único soportado por BiblioTech, encargado de la persistencia de datos.  
  [Ver más](./02_restricciones.md)

- **MoSCoW**: Técnica de priorización de requisitos (Must, Should, Could, Won't) utilizada en las Historias de Usuario.  
  [Ver más](../epicas-y-hu.md)

- **MVC por Capas (Package by Layer)**: Patrón arquitectónico utilizado, donde el código se organiza estrictamente en capas técnicas (`controller`, `service`, `repository`).  
  [Ver más](./05_vista_de_componentes.md)

- **RBAC (Role-Based Access Control)**: Control de acceso basado en roles. En BiblioTech, el `tipoUsuario == 2` obtiene el rol `ROLE_ADMIN` y los demás obtienen `ROLE_USER`.  
  [Ver más](./09_decisiones_arquitectonicas.md)

- **REST (Representational State Transfer)**: Estilo arquitectónico de la API, utilizando métodos HTTP estándar (GET, POST, PUT, PATCH, DELETE).  
  [Ver más](./03_contexto_y_alcance.md)

- **Transaccionalidad (`@Transactional`)**: Principio que garantiza que un conjunto de operaciones de base de datos se ejecuten como una sola unidad (todo o nada). Utilizado críticamente al crear préstamos.  
  [Ver más](./08_conceptos_transversales.md)

- **`ddl-auto=validate`**: Configuración de Hibernate que verifica que las entidades JPA coincidan exactamente con el esquema creado manualmente en la base de datos, sin modificarlo.  
  [Ver más](./02_restricciones.md)

---

## Motivación

El glosario respalda una comprensión común de los términos técnicos y de negocio entre todas las partes interesadas (desarrolladores, evaluadores y futuros mantenedores), reduciendo interpretaciones erróneas y mejorando la comunicación en el proyecto BiblioTech.
