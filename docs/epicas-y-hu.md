# BiblioTech API — Épicas e Historias de Usuario

## Índice

### Épicas
- [Épica 1: Autenticación y Gestión de Usuarios](#épica-1-autenticación-y-gestión-de-usuarios)
- [Épica 2: Gestión de Préstamos](#épica-2-gestión-de-préstamos)
- [Épica 3: Gestión de Catálogo de Libros](#épica-3-gestión-de-catálogo-de-libros)

### Historias de Usuario
- [HU-001: Inicio de sesión](#hu-001-inicio-de-sesión)
- [HU-002: Crear usuario](#hu-002-crear-usuario)
- [HU-003: Listar usuarios](#hu-003-listar-usuarios)
- [HU-004: Consultar usuario por ID](#hu-004-consultar-usuario-por-id)
- [HU-005: Actualizar usuario](#hu-005-actualizar-usuario)
- [HU-006: Eliminar usuario](#hu-006-eliminar-usuario)
- [HU-007: Crear préstamo](#hu-007-crear-préstamo)
- [HU-008: Calcular fecha máxima de devolución](#hu-008-calcular-fecha-máxima-de-devolución)
- [HU-009: Consultar préstamo por ID](#hu-009-consultar-préstamo-por-id)
- [HU-010: Listar préstamos](#hu-010-listar-préstamos)
- [HU-011: Crear libro](#hu-011-crear-libro)
- [HU-012: Listar libros](#hu-012-listar-libros)
- [HU-013: Consultar libro por ISBN](#hu-013-consultar-libro-por-isbn)
- [HU-014: Actualizar libro](#hu-014-actualizar-libro)
- [HU-015: Eliminar libro](#hu-015-eliminar-libro)

---

## Épicas

### Épica 1: Autenticación y Gestión de Usuarios

**Usuario(s):** Administrador (Empleado) y Cliente (Afiliado/Invitado)

**Descripción**
Como sistema de biblioteca, necesito gestionar el acceso seguro de los usuarios y administrar sus perfiles, para garantizar que solo personal autorizado pueda operar la plataforma y que los clientes gestionen su propia información.

**Objetivo**
Implementar la base de seguridad mediante autenticación por token y proveer la gestión de usuarios con control de acceso (RBAC). El Administrador gestiona a todos; los Clientes solo se gestionan a sí mismos.

**Alcance**
- Inicio de sesión seguro.
- Gestión de usuarios (CRUD) total para el Administrador.
- Auto-gestión (leer, actualizar, eliminar su propio perfil) para los Clientes.
- Validación de usuario e identificación duplicados.
- Inicialización segura con un primer usuario Administrador.

**Criterios de Épica (Para darla por completada)**
- Cualquier usuario registrado puede iniciar sesión y obtener su token.
- El Administrador puede listar, crear y gestionar a cualquier usuario.
- Un Cliente solo puede consultar, editar o borrar su propio perfil.
- El sistema rechaza la creación de usuarios con credenciales duplicadas.

**Historias de Usuario Relacionadas**
- [HU-001: Inicio de sesión](#hu-001-inicio-de-sesión)
- [HU-002: Crear usuario](#hu-002-crear-usuario)
- [HU-003: Listar usuarios](#hu-003-listar-usuarios)
- [HU-004: Consultar usuario por ID](#hu-004-consultar-usuario-por-id)
- [HU-005: Actualizar usuario](#hu-005-actualizar-usuario)
- [HU-006: Eliminar usuario](#hu-006-eliminar-usuario)

### Épica 2: Gestión de Préstamos

**Usuario(s):** Administrador (Empleado) y Cliente (Afiliado/Invitado)

**Descripción**
Como bibliotecario, quiero registrar y consultar los préstamos. Como cliente, quiero consultar mis préstamos activos. Todo para llevar un control exacto de los libros y las devoluciones.

**Objetivo**
Implementar la lógica de negocio de préstamos, validando reglas de negocio estrictas (días hábiles, límites de invitados) y permitiendo al cliente consultar su propio historial.

**Alcance**
- Registro de préstamos con validación de reglas (solo Administrador).
- Cálculo de fecha máxima de devolución (días hábiles).
- Consulta de préstamo individual.
- Listado de préstamos (Administrador ve todos, Cliente ve los suyos).

**Criterios de Épica (Para darla por completada)**
- El sistema calcula la fecha de devolución omitiendo sábados y domingos.
- Un invitado no puede tener más de un préstamo activo.
- El Administrador puede crear y ver todos los préstamos.
- El Cliente solo puede ver sus propios préstamos.

**Historias de Usuario Relacionadas**
- [HU-007: Crear préstamo](#hu-007-crear-préstamo)
- [HU-008: Calcular fecha máxima de devolución](#hu-008-calcular-fecha-máxima-de-devolución)
- [HU-009: Consultar préstamo por ID](#hu-009-consultar-préstamo-por-id)
- [HU-010: Listar préstamos](#hu-010-listar-préstamos)

### Épica 3: Gestión de Catálogo de Libros

**Usuario(s):** Administrador (Empleado)

**Descripción**
Como bibliotecario, quiero administrar el catálogo de libros, para mantener actualizado el inventario y asegurar que solo se presten libros existentes.

**Objetivo**
Proveer un CRUD completo para la entidad `Libro`, restringido exclusivamente al personal autorizado.

**Alcance**
- Registro, consulta, actualización y eliminación de libros.

**Criterios de Épica (Para darla por completada)**
- Solo un Administrador puede crear, actualizar o eliminar libros.
- El sistema valida que no se duplique el ISBN.
- Los Clientes reciben HTTP 403 si intentan usar estos endpoints.

**Historias de Usuario Relacionadas**
- [HU-011: Crear libro](#hu-011-crear-libro)
- [HU-012: Listar libros](#hu-012-listar-libros)
- [HU-013: Consultar libro por ISBN](#hu-013-consultar-libro-por-isbn)
- [HU-014: Actualizar libro](#hu-014-actualizar-libro)
- [HU-015: Eliminar libro](#hu-015-eliminar-libro)

---

## Historias de Usuario

### HU-001: Inicio de sesión

| Épica | Rol |
| ----- | --- |
| [Épica 1: Autenticación y Gestión de Usuarios](#épica-1-autenticación-y-gestión-de-usuarios) | Administrador (Empleado) / Cliente (Afiliado/Invitado) |

**Historia**
Como usuario registrado, quiero iniciar sesión con mi usuario y clave, para acceder de forma segura a las funcionalidades de la plataforma.

**¿Por qué existe?**
Para garantizar que solo el personal autorizado y los clientes registrados puedan acceder a la plataforma.

**Prioridad MoSCoW**
- [x] **Must** (Debe tener)
- [ ] Should (Debería tener)
- [ ] Could (Podría tener)
- [ ] Won't (No tendrá)

**Criterios de Aceptación**
- Dado que un usuario envía credenciales válidas, entonces el sistema responde con un estado HTTP 200, el token (JWT) y sus datos básicos.
- Dado que un usuario envía credenciales inválidas, entonces el sistema responde con un estado HTTP 401 (Unauthorized) y un mensaje de error claro.
- Dado que un usuario intenta acceder a un endpoint protegido sin token o con un token inválido, entonces el sistema responde con un estado HTTP 401.

### HU-002: Crear usuario

| Épica | Rol |
| ----- | --- |
| [Épica 1: Autenticación y Gestión de Usuarios](#épica-1-autenticación-y-gestión-de-usuarios) | Administrador (Empleado) |

**Historia**
Como administrador autenticado, quiero registrar un nuevo usuario, para que pueda operar en la plataforma.

**¿Por qué existe?**
Para permitir el crecimiento del personal y la incorporación de nuevos clientes (afiliados o invitados).

**Prioridad MoSCoW**
- [x] **Must**
- [ ] Should
- [ ] Could
- [ ] Won't

**Criterios de Aceptación**
- Dado que el administrador envía datos válidos, entonces el sistema responde con un estado HTTP 201 (Created).
- Dado que el usuario o identificación ya existen, entonces el sistema responde con un estado HTTP 409 (Conflict).
- Dado que un Cliente intenta usar este endpoint, entonces el sistema responde con HTTP 403 (Forbidden).
- Dado que no se envía un token válido, entonces el sistema responde con estado HTTP 401.

### HU-003: Listar usuarios

| Épica | Rol |
| ----- | --- |
| [Épica 1: Autenticación y Gestión de Usuarios](#épica-1-autenticación-y-gestión-de-usuarios) | Administrador (Empleado) |

**Historia**
Como administrador autenticado, quiero obtener el listado de todos los usuarios registrados, para tener visibilidad de quiénes tienen acceso.

**¿Por qué existe?**
Para facilitar la gestión y auditoría de accesos a la plataforma.

**Prioridad MoSCoW**
- [x] **Must**
- [ ] Should
- [ ] Could
- [ ] Won't

**Criterios de Aceptación**
- Dado que un administrador solicita el listado, entonces el sistema responde con HTTP 200 y el arreglo de usuarios.
- Dado que no hay usuarios registrados, entonces el sistema responde con HTTP 200 y un arreglo vacío.
- Dado que un Cliente intenta usar este endpoint, entonces el sistema responde con HTTP 403 (Forbidden).
- Dado que no se envía un token válido, entonces el sistema responde con estado HTTP 401.

### HU-004: Consultar usuario por ID

| Épica | Rol |
| ----- | --- |
| [Épica 1: Autenticación y Gestión de Usuarios](#épica-1-autenticación-y-gestión-de-usuarios) | Administrador (Empleado) / Cliente (Afiliado/Invitado) |

**Historia**
Como usuario autenticado, quiero consultar los datos de un usuario específico por su ID, para revisar su información.

**¿Por qué existe?**
Para auditar datos (Admin) o para que un cliente verifique su propio perfil.

**Prioridad MoSCoW**
- [x] **Must**
- [ ] Should
- [ ] Could
- [ ] Won't

**Criterios de Aceptación**
- Dado que el administrador solicita cualquier ID existente, entonces el sistema responde con HTTP 200.
- Dado que un Cliente solicita su propio ID, entonces el sistema responde con HTTP 200.
- Dado que un Cliente intenta consultar el ID de otro usuario, entonces el sistema responde con HTTP 403 (Forbidden).
- Dado que el ID no existe, entonces el sistema responde con HTTP 404.
- Dado que no se envía un token válido, entonces el sistema responde con estado HTTP 401.

### HU-005: Actualizar usuario

| Épica | Rol |
| ----- | --- |
| [Épica 1: Autenticación y Gestión de Usuarios](#épica-1-autenticación-y-gestión-de-usuarios) | Administrador (Empleado) / Cliente (Afiliado/Invitado) |

**Historia**
Como usuario autenticado, quiero actualizar los datos de un usuario, para corregir información.

**¿Por qué existe?**
Para mantener la información actualizada.

**Prioridad MoSCoW**
- [x] **Must**
- [ ] Should
- [ ] Could
- [ ] Won't

**Criterios de Aceptación**
- Dado que el administrador actualiza cualquier ID existente, entonces el sistema responde con HTTP 200.
- Dado que un Cliente actualiza su propio ID, entonces el sistema responde con HTTP 200.
- Dado que un Cliente intenta actualizar a otro usuario, entonces el sistema responde con HTTP 403 (Forbidden).
- Dado que el ID no existe, entonces el sistema responde con HTTP 404.
- Dado que no se envía un token válido, entonces el sistema responde con estado HTTP 401.

### HU-006: Eliminar usuario

| Épica | Rol |
| ----- | --- |
| [Épica 1: Autenticación y Gestión de Usuarios](#épica-1-autenticación-y-gestión-de-usuarios) | Administrador (Empleado) / Cliente (Afiliado/Invitado) |

**Historia**
Como usuario autenticado, quiero eliminar un usuario del sistema, para revocar su acceso.

**¿Por qué existe?**
Para dar de baja a personal o permitir que un cliente cierre su cuenta.

**Prioridad MoSCoW**
- [x] **Must**
- [ ] Should
- [ ] Could
- [ ] Won't

**Criterios de Aceptación**
- Dado que el administrador elimina cualquier ID existente, entonces el sistema responde con HTTP 204.
- Dado que un Cliente elimina su propio ID, entonces el sistema responde con HTTP 204.
- Dado que un Cliente intenta eliminar a otro usuario, entonces el sistema responde con HTTP 403 (Forbidden).
- Dado que el ID no existe, entonces el sistema responde con HTTP 404.
- Dado que no se envía un token válido, entonces el sistema responde con estado HTTP 401.

---

### HU-007: Crear préstamo

| Épica | Rol |
| ----- | --- |
| [Épica 2: Gestión de Préstamos](#épica-2-gestión-de-préstamos) | Administrador (Empleado) |

**Historia**
Como bibliotecario, quiero registrar el préstamo de un libro indicando el ISBN, la identificación del usuario y su tipo.

**¿Por qué existe?**
Es la funcionalidad central del negocio.

**Prioridad MoSCoW**
- [x] **Must**
- [ ] Should
- [ ] Could
- [ ] Won't

**Criterios de Aceptación**
- Dado que el bibliotecario envía datos válidos (tipoUsuario 1, 2 o 3, y en el caso del 3 sin préstamo activo), entonces el sistema crea el préstamo y responde con HTTP 201.
- Dado que el `tipoUsuario` no es 1, 2 o 3, entonces el sistema responde con HTTP 400 y el mensaje exacto: `"Tipo de usuario no permitido en la biblioteca"`.
- Dado que se intenta crear un préstamo para un Invitado (tipo 3) con préstamo activo, entonces el sistema responde con HTTP 400 y el mensaje exacto: `"El usuario con identificación xxxxxx ya tiene un libro prestado por lo cual no se le puede realizar otro préstamo"`.
- Dado que un Cliente intenta crear un préstamo, entonces el sistema responde con HTTP 403 (Forbidden).
- Dado que no se envía un token válido, entonces el sistema responde con estado HTTP 401.

### HU-008: Calcular fecha máxima de devolución

| Épica | Rol |
| ----- | --- |
| [Épica 2: Gestión de Préstamos](#épica-2-gestión-de-préstamos) | Sistema |

**Historia**
Como sistema, necesito calcular la fecha máxima de devolución al momento de crear un préstamo.

**¿Por qué existe?**
Para automatizar una regla de negocio crítica y omitir fines de semana.

**Prioridad MoSCoW**
- [x] **Must**
- [ ] Should
- [ ] Could
- [ ] Won't

**Criterios de Aceptación**
- Afiliado (tipo 1): suma 10 días hábiles a la fecha actual.
- Empleado (tipo 2): suma 8 días hábiles a la fecha actual.
- Invitado (tipo 3): suma 7 días hábiles a la fecha actual.
- La fecha resultante debe formatearse como `dd/MM/yyyy`.
- Si la fecha de inicio cae en viernes, sábado o domingo, se maneja omitiendo los fines de semana.

### HU-009: Consultar préstamo por ID

| Épica | Rol |
| ----- | --- |
| [Épica 2: Gestión de Préstamos](#épica-2-gestión-de-préstamos) | Administrador (Empleado) / Cliente (Afiliado/Invitado) |

**Historia**
Como usuario autenticado, quiero consultar un préstamo específico por su ID, para revisar sus detalles.

**¿Por qué existe?**
Para resolver dudas o auditorías puntuales.

**Prioridad MoSCoW**
- [x] **Must**
- [ ] Should
- [ ] Could
- [ ] Won't

**Criterios de Aceptación**
- Dado que el bibliotecario consulta cualquier ID existente, entonces el sistema responde con HTTP 200.
- Dado que un Cliente consulta un ID que le pertenece, entonces el sistema responde con HTTP 200.
- Dado que un Cliente intenta consultar un préstamo ajeno, entonces el sistema responde con HTTP 403 (Forbidden).
- Dado que el ID no existe, entonces el sistema responde con HTTP 404.
- Dado que no se envía un token válido, entonces el sistema responde con estado HTTP 401.

### HU-010: Listar préstamos

| Épica | Rol |
| ----- | --- |
| [Épica 2: Gestión de Préstamos](#épica-2-gestión-de-préstamos) | Administrador (Empleado) / Cliente (Afiliado/Invitado) |

**Historia**
Como usuario autenticado, quiero ver el listado de préstamos activos, con filtro opcional por identificación.

**¿Por qué existe?**
Para la operación diaria (Admin) o para que los clientes consulten sus propios préstamos.

**Prioridad MoSCoW**
- [x] **Must**
- [ ] Should
- [ ] Could
- [ ] Won't

**Criterios de Aceptación**
- Dado que el bibliotecario consulta el listado sin filtros, entonces el sistema responde con HTTP 200 y todos los préstamos activos.
- Dado que el bibliotecario usa el filtro `identificacionUsuario`, entonces el sistema filtra los resultados.
- Dado que un Cliente consulta el listado, el sistema debe **forzar** el filtro para mostrarle únicamente sus préstamos (o responder 403 si intenta ver todos).
- Dado que no hay préstamos activos, entonces el sistema responde con HTTP 200 y un arreglo vacío.
- Dado que no se envía un token válido, entonces el sistema responde con estado HTTP 401.

---

### HU-011: Crear libro

| Épica | Rol |
| ----- | --- |
| [Épica 3: Gestión de Catálogo de Libros](#épica-3-gestión-de-catálogo-de-libros) | Administrador (Empleado) |

**Historia**
Como administrador autenticado, quiero registrar un nuevo libro en el catálogo.

**¿Por qué existe?**
Para que la biblioteca tenga un inventario formal.

**Prioridad MoSCoW**
- [x] **Must**
- [ ] Should
- [ ] Could
- [ ] Won't

**Criterios de Aceptación**
- Dado que el administrador envía datos válidos, entonces el sistema registra el libro (con `disponible = true` por defecto) y responde con HTTP 201.
- Dado que el ISBN ya existe, entonces el sistema responde con HTTP 409 (Conflict).
- Dado que un Cliente intenta usar el endpoint, entonces el sistema responde con HTTP 403 (Forbidden).
- Dado que no se envía un token válido, entonces el sistema responde con estado HTTP 401.

### HU-012: Listar libros

| Épica | Rol |
| ----- | --- |
| [Épica 3: Gestión de Catálogo de Libros](#épica-3-gestión-de-catálogo-de-libros) | Administrador (Empleado) |

**Historia**
Como administrador autenticado, quiero obtener el listado de todos los libros registrados.

**¿Por qué existe?**
Para conocer el inventario actual.

**Prioridad MoSCoW**
- [x] **Must**
- [ ] Should
- [ ] Could
- [ ] Won't

**Criterios de Aceptación**
- Dado que el administrador solicita el listado, entonces el sistema responde con HTTP 200 y todos los libros.
- Dado que no hay libros registrados, entonces el sistema responde con HTTP 200 y un arreglo vacío.
- Dado que un Cliente intenta usar el endpoint, entonces el sistema responde con HTTP 403 (Forbidden).
- Dado que no se envía un token válido, entonces el sistema responde con estado HTTP 401.

### HU-013: Consultar libro por ISBN

| Épica | Rol |
| ----- | --- |
| [Épica 3: Gestión de Catálogo de Libros](#épica-3-gestión-de-catálogo-de-libros) | Administrador (Empleado) |

**Historia**
Como administrador autenticado, quiero consultar los detalles de un libro específico por su ISBN.

**¿Por qué existe?**
Para verificar información de un libro.

**Prioridad MoSCoW**
- [x] **Must**
- [ ] Should
- [ ] Could
- [ ] Won't

**Criterios de Aceptación**
- Dado que el administrador busca un ISBN existente, entonces el sistema responde con HTTP 200.
- Dado que el ISBN no existe, entonces el sistema responde con HTTP 404.
- Dado que un Cliente intenta usar el endpoint, entonces el sistema responde con HTTP 403 (Forbidden).
- Dado que no se envía un token válido, entonces el sistema responde con estado HTTP 401.

### HU-014: Actualizar libro

| Épica | Rol |
| ----- | --- |
| [Épica 3: Gestión de Catálogo de Libros](#épica-3-gestión-de-catálogo-de-libros) | Administrador (Empleado) |

**Historia**
Como administrador autenticado, quiero actualizar los datos de un libro existente.

**¿Por qué existe?**
Para mantener la integridad del catálogo.

**Prioridad MoSCoW**
- [x] **Must**
- [ ] Should
- [ ] Could
- [ ] Won't

**Criterios de Aceptación**
- Dado que el administrador envía datos válidos para un ISBN existente, entonces el sistema actualiza el registro y responde con HTTP 200.
- Dado que el ISBN no existe, entonces el sistema responde con HTTP 404.
- Dado que un Cliente intenta usar el endpoint, entonces el sistema responde con HTTP 403 (Forbidden).
- Dado que no se envía un token válido, entonces el sistema responde con estado HTTP 401.

### HU-015: Eliminar libro

| Épica | Rol |
| ----- | --- |
| [Épica 3: Gestión de Catálogo de Libros](#épica-3-gestión-de-catálogo-de-libros) | Administrador (Empleado) |

**Historia**
Como administrador autenticado, quiero eliminar un libro del sistema.

**¿Por qué existe?**
Para dar de baja material obsoleto o perdido.

**Prioridad MoSCoW**
- [x] **Must**
- [ ] Should
- [ ] Could
- [ ] Won't

**Criterios de Aceptación**
- Dado que el administrador solicita eliminar un ISBN existente, entonces el sistema elimina el registro y responde con HTTP 204.
- Dado que el ISBN no existe, entonces el sistema responde con HTTP 404.
- Dado que un Cliente intenta usar el endpoint, entonces el sistema responde con HTTP 403 (Forbidden).
- Dado que no se envía un token válido, entonces el sistema responde con estado HTTP 401.
