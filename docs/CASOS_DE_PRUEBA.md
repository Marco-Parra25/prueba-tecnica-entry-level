# Parte 3.A – Diseño de casos de prueba

Casos de prueba para los endpoints `POST /notifications` y `GET /notifications`.
Los marcados con ✅ están **automatizados** en `NotificationControllerTest`.

## POST /notifications

| ID    | Caso | Entrada | Resultado esperado | Auto |
|-------|------|---------|--------------------|------|
| CP-01 | Envío email válido | `{userId:"123", message:"Hola", channel:"email"}` | `201`, `provider:"EmailProvider"`, `channel:"email"`, `id` numérico | ✅ |
| CP-02 | Envío sms válido | `{userId:"456", message:"...", channel:"sms"}` | `201`, `provider:"SmsProvider"`, `channel:"sms"` | ✅ |
| CP-03 | Falta `message` | `{userId:"123", channel:"email"}` | `400`, error indicando que `message` es obligatorio | ✅ |
| CP-04 | Canal inválido | `{userId:"123", message:"Hola", channel:"telegram"}` | `400`, mensaje "Canal no soportado" | ✅ |
| CP-05 | Falta `userId` | `{message:"Hola", channel:"email"}` | `400`, error en `userId` | |
| CP-06 | Falta `channel` | `{userId:"123", message:"Hola"}` | `400`, error en `channel` | |
| CP-07 | Campos vacíos (`""`) | `{userId:"", message:"", channel:""}` | `400` (los `@NotBlank` rechazan strings vacíos) | |
| CP-08 | Canal con mayúsculas | `{..., channel:"EMAIL"}` | `201` (se normaliza a minúsculas) | |
| CP-09 | JSON mal formado | `{` | `400` (cuerpo no parseable) | |
| CP-10 | Content-Type incorrecto | body válido sin `application/json` | `415 Unsupported Media Type` | |
| CP-11 | El envío queda persistido | tras un `POST` válido | aparece en el historial del `GET` | ✅ (vía CP-13) |

## GET /notifications

| ID    | Caso | Precondición | Resultado esperado | Auto |
|-------|------|--------------|--------------------|------|
| CP-12 | Historial vacío | sin envíos previos | `200`, `{ "data": [] }` | |
| CP-13 | Historial con datos | al menos 1 envío | `200`, `data` es array con `length >= 1` | ✅ |
| CP-14 | Orden de inserción | varios envíos | `data` respeta el orden cronológico de envío | |
| CP-15 | Estructura del item | al menos 1 envío | cada item trae `id, userId, message, channel, provider, sentAt` | |

## Cobertura automatizada

Se automatizaron **5 casos** de los endpoints (CP-01 a CP-04 y CP-13),
superando el mínimo de 4 requerido. Se priorizaron los caminos críticos:
el *happy path* de cada canal, las dos familias de validación (campo
faltante y valor inválido) y la persistencia en el historial.
