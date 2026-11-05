# 1. Introducción y Objetivos

## Propósito

Esta sección proporciona una visión general de los principales objetivos del proyecto BiblioTech. El propósito es alinear las expectativas técnicas y funcionales, asegurando que el diseño de la API cumpla con los estándares de calidad requeridos.

## Principales Partes Interesadas

| Parte Interesada | Rol | Expectativas |
|------------------|-----|--------------|
| Evaluador Técnico | Auditor de Calidad | Código limpio, arquitectura en capas, pruebas unitarias, cumplimiento estricto de reglas de negocio y documentación clara. |
| Administrador (Empleado) | Usuario Final (Gestor) | API segura, rápida, con manejo claro de errores para gestionar usuarios, libros y préstamos. |
| Cliente (Afiliado/Invitado) | Usuario Final (Consulta) | Poder iniciar sesión y consultar su propio perfil y sus préstamos de forma segura. |
| Desarrollador (Juan Esteban) | Arquitecto / Dev | Aplicar el principio de responsabilidad única (SRP), mantener la simplicidad con una arquitectura MVC en capas y garantizar la mantenibilidad del código. |

## Principales Objetivos de Calidad

1. **Corrección Funcional**: Garantizar que las reglas de negocio (como el cálculo de días hábiles y las restricciones de préstamos) se ejecuten sin errores.
2. **Seguridad**: Proteger los endpoints mediante autenticación por token JWT y aplicar Control de Acceso Basado en Roles (RBAC) para restringir las acciones administrativas.
3. **Mantenibilidad**: Aplicar una arquitectura estrictamente en capas (Controller → Service → Repository) que separe las responsabilidades.
4. **Testabilidad**: Asegurar que la lógica de negocio crítica (cálculo de fechas, validaciones de préstamo) sea fácil de probar de forma aislada con JUnit y Mockito.

## Supuestos y Aclaraciones de Alcance

Durante la lectura del documento de la prueba técnica, se identificaron los siguientes puntos ambiguos que fueron resueltos bajo criterio propio:

1. **Roles de Sistema y Control de Acceso (RBAC)**:
   Aunque la prueba exige que los endpoints estén protegidos por token, no detalla un modelo de permisos. Para evitar fallas graves de seguridad (como que un Invitado cree préstamos), se implementará seguridad basada en roles.
   *Decisión*: El `tipoUsuario = 2` (Empleado) actuará como **Administrador/Bibliotecario** y tendrá acceso total a los endpoints. Los usuarios `1` (Afiliado) y `3` (Invitado) actuarán como clientes, y solo podrán acceder a sus propios datos, recibiendo un HTTP 403 (Forbidden) si intentan acceder a endpoints administrativos.
2. **Usuario Administrador Inicial**:
   Para el arranque del sistema, el `CommandLineRunner` creará un usuario por defecto con `tipoUsuario = 2` (Empleado), asumiendo que es el perfil lógico para un bibliotecario administrador.
3. **Criterio de Evaluación "Responsive y accesibilidad"**:
   Se identifica que este término es propio de la evaluación de Frontend. Para este Backend, se interpreta como la "accesibilidad de la API", lo cual se cubrirá garantizando una documentación interactiva clara (Swagger/OpenAPI), una colección de Postman organizada y un manejo estandarizado de errores HTTP.
4. **Gestión de Libros**:
   Aunque la prueba no exige explícitamente endpoints para libros, el dominio los requiere. Se implementará el CRUD completo para darle sentido funcional al sistema.
