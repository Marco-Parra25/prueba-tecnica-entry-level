# Parte 3.C – Análisis breve

## ¿Qué automatizarías primero en este sistema y por qué?

Primero automatizaría las **pruebas del endpoint `POST /notifications`**, y
dentro de él, en este orden:

1. **El *happy path* de cada canal (email y sms).** Es la funcionalidad
   central del sistema: si esto se rompe, la API deja de cumplir su propósito.
   Además valida que el patrón Strategy/Factory selecciona el proveedor
   correcto según el `channel`.
2. **Las validaciones de entrada** (campo obligatorio faltante y canal
   inválido). Son errores muy frecuentes en la práctica, baratos de automatizar
   y protegen contra regresiones en la capa de validación.
3. **La persistencia en el historial** (que un `POST` se refleje en el `GET`),
   porque conecta las dos operaciones y verifica el flujo completo de punta a
   punta.

**Por qué en ese orden:** se prioriza por *riesgo × frecuencia de uso*. El
camino feliz es lo que más se ejecuta en producción y su fallo es el más
costoso; las validaciones son las regresiones más comunes al evolucionar el
código. Son además pruebas rápidas y deterministas (sin dependencias externas),
ideales para correr en cada *commit* dentro de un pipeline de CI.

## ¿Qué riesgos identificas en esta API?

1. **Persistencia en memoria (no durable).** El historial se pierde al
   reiniciar la aplicación y no se comparte entre instancias. *Mitigación:*
   mover el `NotificationRepository` a una base de datos. La interfaz ya está
   preparada para ese cambio.

2. **Envío simulado, sin integración real ni manejo de fallos del proveedor.**
   Hoy `send()` solo registra en consola; un proveedor real puede fallar,
   tener *timeouts* o *rate limits*. *Mitigación:* reintentos, *timeouts*,
   *circuit breaker* y registrar el estado del envío (enviado/fallido).

3. **Sin autenticación ni autorización.** Cualquiera puede enviar
   notificaciones o leer el historial completo (que incluye `userId` y
   mensajes). *Mitigación:* autenticación (API key / token) y control de acceso.

4. **Sin límite de tasa (*rate limiting*).** La API es susceptible a abuso o
   *spam* de notificaciones. *Mitigación:* *throttling* por usuario/IP.

5. **`GET` sin paginación.** Devuelve TODO el historial; con muchos registros
   la respuesta se vuelve pesada y lenta. *Mitigación:* paginación y filtros
   (por `userId`, `channel`, rango de fechas).

6. **Validación de contenido limitada.** No se valida formato del `userId`,
   longitud máxima del `message`, ni que el destino corresponda al canal
   (email válido / número de teléfono válido). *Mitigación:* reglas de
   validación específicas por canal.

7. **Datos sensibles en logs.** Hoy se registra el mensaje completo en consola;
   en producción podría exponer información personal. *Mitigación:* enmascarar
   o no registrar el contenido.
