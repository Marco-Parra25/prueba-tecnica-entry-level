# Prueba Técnica – Entry Level (Notification API)

Solución a la prueba técnica para el cargo de Software Engineer – Entry Level.
Implementada en **Java 21 + Spring Boot 3.4** con **Maven**.

Autor: Marco Parra

---

## Índice

- [Cómo ejecutar](#cómo-ejecutar)
- [Parte 1 – Lógica de programación](#parte-1--lógica-de-programación-30-pts)
- [Parte 2 – Backend + POO](#parte-2--backend--poo-40-pts)
- [Parte 3 – QA + Automatización](#parte-3--qa--automatización-30-pts)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Decisiones de diseño](#decisiones-de-diseño)

---

## Cómo ejecutar

Requisitos: **JDK 21** y **Maven 3.9+**.

```bash
# 1) Ejecutar todos los tests (Parte 1 + Parte 3)
mvn test

# 2) Levantar la API (queda escuchando en http://localhost:8080)
mvn spring-boot:run

# 3) Ejecutar la demo de la Parte 1 de forma aislada
mvn -q compile exec:java -Dexec.mainClass=com.marco.notifications.part1.UniqueSorter
# (o desde el IDE: ejecutar el main de UniqueSorter.java)
```

### Probar la API rápidamente (cURL)

```bash
# Enviar una notificación por email
curl -i -X POST http://localhost:8080/notifications \
  -H "Content-Type: application/json" \
  -d '{"userId":"123","message":"Hola","channel":"email"}'

# Enviar una por SMS
curl -i -X POST http://localhost:8080/notifications \
  -H "Content-Type: application/json" \
  -d '{"userId":"456","message":"Tu código es 9999","channel":"sms"}'

# Ver el historial
curl -i http://localhost:8080/notifications
```

---

## Parte 1 – Lógica de Programación (30 pts)

**Enunciado:** dada una lista de enteros, devolver una nueva lista **sin
duplicados** y **ordenada de menor a mayor**, sin usar `sort`/`sorted`,
`set`/`distinct`/`unique` ni librerías que automaticen la solución.

**Solución:** [`UniqueSorter.java`](src/main/java/com/marco/notifications/part1/UniqueSorter.java)

En una sola pasada por la entrada, para cada número:

1. **Deduplicación manual** – se descarta si ya existe en el resultado
   (recorriendo la lista resultado con un bucle propio, sin `Set`).
2. **Inserción ordenada** – si es nuevo, se inserta directamente en la posición
   que le corresponde para mantener el orden ascendente (variante de
   *insertion sort* hecha a mano, sin `Collections.sort`).

```
entrada = [4, 2, 7, 2, 4, 9, 1]
salida  = [1, 2, 4, 7, 9]
```

- Solo se usan ciclos, condicionales, `List` como contenedor y funciones propias.
- Complejidad: O(n²) en el peor caso — aceptable y explícita; el objetivo es
  demostrar el algoritmo, no usar utilidades del lenguaje.
- Maneja casos borde: lista vacía, `null`, todos iguales, negativos, cero.

---

## Parte 2 – Backend + POO (40 pts)

API REST para envío de notificaciones.

### Endpoints

| Método | Ruta             | Descripción                          | Respuesta |
|--------|------------------|--------------------------------------|-----------|
| `POST` | `/notifications` | Envía una notificación               | `201 Created` |
| `GET`  | `/notifications` | Devuelve el historial de envíos      | `200 OK`  |

**POST /notifications** – Request body (todos los campos obligatorios):

```json
{ "userId": "123", "message": "Hola", "channel": "email" }
```

`channel` debe ser `"email"` o `"sms"`. Respuesta `201`:

```json
{
  "id": 1,
  "userId": "123",
  "message": "Hola",
  "channel": "email",
  "provider": "EmailProvider",
  "sentAt": "2026-06-22T15:00:00Z"
}
```

**GET /notifications** – Respuesta `200`:

```json
{
  "data": [
    { "id": 1, "userId": "123", "message": "Hola", "channel": "email", "provider": "EmailProvider", "sentAt": "..." }
  ]
}
```

### Patrones de diseño aplicados

- **Strategy** – [`NotificationProvider`](src/main/java/com/marco/notifications/provider/NotificationProvider.java)
  define el contrato de envío; [`EmailProvider`](src/main/java/com/marco/notifications/provider/EmailProvider.java)
  y [`SmsProvider`](src/main/java/com/marco/notifications/provider/SmsProvider.java)
  son las estrategias concretas, una por canal.
- **Factory** – [`NotificationProviderFactory`](src/main/java/com/marco/notifications/provider/NotificationProviderFactory.java)
  selecciona la estrategia según el `channel`. Spring inyecta todos los
  proveedores y la factory los indexa por canal.

> **Por qué.** Agregar un nuevo canal (ej. `push`, `whatsapp`) solo requiere
> crear una nueva clase `@Component` que implemente `NotificationProvider`: se
> registra sola y **no hay que modificar** el servicio ni la factory
> (principio Abierto/Cerrado).

### Historial

Se guarda **en memoria** (el enunciado lo permite) detrás de la interfaz
[`NotificationRepository`](src/main/java/com/marco/notifications/repository/NotificationRepository.java),
de modo que cambiar a base de datos no afectaría al servicio.

### Validación y errores

- `@Valid` + `@NotBlank` garantizan que los campos sean obligatorios → `400`.
- Canal inválido → `400` con mensaje claro.
- [`GlobalExceptionHandler`](src/main/java/com/marco/notifications/exception/GlobalExceptionHandler.java)
  centraliza los errores en un cuerpo JSON uniforme.

---

## Parte 3 – QA + Automatización (30 pts)

### A. Casos de prueba diseñados

Ver el detalle completo en [`docs/CASOS_DE_PRUEBA.md`](docs/CASOS_DE_PRUEBA.md).

### B. Pruebas automatizadas

Automatizadas con **JUnit 5 + Spring MockMvc** en
[`NotificationControllerTest`](src/test/java/com/marco/notifications/controller/NotificationControllerTest.java)
(5 casos, supera el mínimo de 4) y
[`UniqueSorterTest`](src/test/java/com/marco/notifications/part1/UniqueSorterTest.java)
(8 casos de la Parte 1). **Total: 13 pruebas, todas en verde.**

```bash
mvn test
```

### C. Análisis breve

Las respuestas a *¿qué automatizarías primero?* y *¿qué riesgos identificas?*
están en [`docs/ANALISIS.md`](docs/ANALISIS.md).

---

## Estructura del proyecto

```
src/main/java/com/marco/notifications/
├── NotificationApiApplication.java      # Arranque Spring Boot
├── part1/UniqueSorter.java              # PARTE 1: algoritmo
├── controller/NotificationController.java
├── service/NotificationService.java     # Lógica de negocio
├── provider/                            # PATRÓN Strategy + Factory
│   ├── NotificationProvider.java        #   interfaz (Strategy)
│   ├── EmailProvider.java               #   estrategia email
│   ├── SmsProvider.java                 #   estrategia sms
│   └── NotificationProviderFactory.java #   selector (Factory)
├── repository/                          # Historial (interfaz + en memoria)
├── model/                               # Notification, Channel
├── dto/                                 # Request / Response
└── exception/GlobalExceptionHandler.java
```

## Decisiones de diseño

- **Arquitectura en capas** (controller → service → provider/repository) para
  separar responsabilidades y mantener el controlador "delgado".
- **DTOs separados del dominio**: la API controla qué expone, sin acoplar el
  contrato externo a las clases internas.
- **Enum `Channel`** en lugar de strings sueltos para centralizar los valores
  válidos y evitar *magic strings*.
- **Inmutabilidad** en `Notification`: una vez registrado, un envío no cambia.
