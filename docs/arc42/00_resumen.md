# Arquitectura BiblioTech API - Resumen de arc42

## Introducción

Esta documentación describe la arquitectura del sistema **BiblioTech**, una API REST para la gestión de autenticación, usuarios y préstamos de biblioteca. Se utiliza el modelo **arc42** para estructurar las decisiones de diseño, la estructura y los requisitos del sistema, garantizando la claridad y la mantenibilidad del código.

## Propósito de Este Documento

A través de este documento, se busca:
- Documentar de forma estructurada la arquitectura en capas del sistema.
- Servir como guía de referencia para la implementación y el mantenimiento.
- Demostrar el cumplimiento de los criterios técnicos y de calidad exigidos en la prueba técnica.

## Contenidos

1. [**Introducción y Objetivos**](01_introduccion_y_objetivos.md) - Propósito, partes interesadas y objetivos principales.
2. [**Restricciones**](02_restricciones.md) - Restricciones externas e internas (Stack, BD relacional, tiempos).
3. [**Contexto y Alcance**](03_contexto_y_alcance.md) - Delimitación del alcance y diagramas C4 Nivel 1.
4. [**Estrategia de Solución**](04_estrategia_de_solucion.md) - Arquitectura en capas y patrones de diseño.
5. [**Vista de Componentes**](05_vista_de_componentes.md) - Diagramas C4 Nivel 2 y 3 (Controladores, Servicios, Repositorios).
6. [**Vista de Tiempo de Ejecución**](06_vista_de_tiempo_de_ejecucion.md) - Flujos clave (Login, Creación de Préstamo).
7. [**Vista de Despliegue**](07_vista_de_despliegue.md) - Contenerización con Docker y despliegue.
8. [**Conceptos Transversales**](08_conceptos_transversales.md) - Seguridad (JWT), Manejo de errores, Validación.
9. [**Decisiones Arquitectónicas**](09_decisiones_arquitectonicas.md) - ADRs (Base de datos, Bootstrap del admin, etc.).
10. [**Requisitos de Calidad**](10_requisitos_de_calidad.md) - Testabilidad, Seguridad y Rendimiento.
11. [**Riesgos y Deuda Técnica**](11_riesgos_y_deuda_tecnica.md) - Riesgos identificados y supuestos.
12. [**Glosario**](12_glosario.md) - Términos del dominio de la biblioteca.
