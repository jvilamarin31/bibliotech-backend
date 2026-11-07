# 10. Requisitos de Calidad

## Descripción General

Esta sección detalla los objetivos de calidad de BiblioTech API. Están expresados de forma cualitativa y funcional. Dado que el proyecto se encuentra en su fase de entrega y las pruebas unitarias/integración se implementarán a continuación, las métricas cuantitativas de rendimiento o disponibilidad se marcan como `[Métrica pendiente de definir]`, mientras que los escenarios funcionales se basan en los criterios de aceptación de las Historias de Usuario.

## Objetivos de Calidad

1. **Seguridad**: Impedir el acceso no autorizado a las operaciones de gestión (CRUDs y préstamos) y proteger los datos de los usuarios.
   - *Métrica*: Cobertura del 100% de los endpoints protegidos mediante validación de token JWT y roles (RBAC).
2. **Integridad de Datos**: Garantizar que las operaciones que afectan múltiples tablas (como registrar un préstamo y actualizar el libro) se ejecuten de forma atómica, evitando datos corruptos.
   - *Métrica*: 0% de registros de préstamos huérfanos o libros marcados como no disponibles sin un préstamo asociado.
3. **Mantenibilidad**: Mantener una arquitectura en capas limpia (MVC), donde la lógica de negocio esté aislada de los controladores y accesos a datos.
   - *Métrica*: Cumplimiento estricto del principio de responsabilidad única (SRP). 0 líneas de lógica de negocio en la capa `controller`.
4. **Testabilidad**: Facilitar la creación de pruebas automatizadas para la lógica de negocio crítica.
   - *Métrica*: Lógica del cálculo de días hábiles y validaciones de préstamos aislada en métodos puros o servicios fáciles de mockear.

## Escenarios de Calidad

### Escenario 1: Acceso a Recursos Protegidos (Seguridad)
- **Desencadenante**: Un Cliente (Afiliado/Invitado) intenta acceder a un endpoint administrativo (ej. crear usuario, crear libro o crear préstamo).

- **Resultado Esperado**: El sistema deniega la petición devolviendo un HTTP 403 (Forbidden) con un JSON estandarizado. Si el usuario no envía token, el sistema devuelve un HTTP 401 (Unauthorized).

- **Métrica**: 100% de bloqueo en rutas administrativas para roles `ROLE_USER`.


### Escenario 2: Integridad Transaccional en Creación de Préstamos (Integridad)
- **Desencadenante**: Un Administrador registra un préstamo de un libro disponible. El sistema debe guardar el préstamo y actualizar la disponibilidad del libro a `false`.

- **Resultado Esperado**: Ambas operaciones se ejecutan exitosamente dentro de una transacción (`@Transactional`). Si la actualización del libro falla, el préstamo no se guarda.

- **Métrica**: Consistencia total entre la tabla `loans` y `books`. 


### Escenario 3: Manejo de Errores de Negocio (Usabilidad de la API)
- **Desencadenante**: Un Administrador intenta crear un préstamo para un usuario Invitado (tipo 3) que ya tiene un libro prestado, o intenta prestar un libro que ya está ocupado.

- **Resultado Esperado**: El sistema rechaza la operación con un HTTP 400 (Bad Request) devolviendo el mensaje exacto exigido por el negocio (ej. *"El usuario con identificación xxxxxx ya tiene un libro prestado..."*).

- **Métrica**: 100% de los errores de regla de negocio devuelven el código HTTP correcto y el mensaje JSON estructurado sin arrojar errores HTTP 500.


## Diagrama de Árbol de Calidad

```
graph TD
    Calidad["Requisitos de Calidad<br>(BiblioTech API)"]

    Seguridad["Seguridad"]
    Integridad["Integridad de Datos"]
    Mantenibilidad["Mantenibilidad"]
    Testabilidad["Testabilidad"]

    Escenario1["Acceso a Recursos Protegidos"]
    Escenario2["Integridad Transaccional en Préstamos"]
    Escenario3["Manejo de Errores de Negocio"]

    Calidad --> Seguridad
    Calidad --> Integridad
    Calidad --> Mantenibilidad
    Calidad --> Testabilidad

    Seguridad --> Escenario1
    Integridad --> Escenario2
    Testabilidad --> Escenario3
```

## Diagrama de Impacto de Escenarios en Sistemas

```
graph LR
    Escenario1["Acceso a Recursos Protegidos<br><i>Relacionado: Seguridad</i>"]
    Escenario2["Integridad de Préstamos<br><i>Relacionado: Integridad de Datos</i>"]
    Escenario3["Errores de Negocio<br><i>Relacionado: Manejo de Errores</i>"]

    SecurityConfig["SecurityConfig / JwtFilter"]
    Controllers["Controladores (User, Book, Loan)"]
    LoanService["LoanService (@Transactional)"]
    ExceptionHandler["GlobalExceptionHandler"]

    Escenario1 --> SecurityConfig
    Escenario1 --> Controllers
    Escenario2 --> LoanService
    Escenario3 --> ExceptionHandler
```

## Motivación

Al definir estos requisitos de calidad, BiblioTech API se orienta hacia operaciones seguras, íntegras y predecibles. Al documentar estos escenarios, se establece un estándar claro para las pruebas que se implementarán y se asegura que la arquitectura cumpla con los rigurosos criterios de evaluación exigidos en la prueba técnica.