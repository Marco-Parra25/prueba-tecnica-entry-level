package com.marco.notifications.model;

/**
 * Canales de notificacion soportados por el sistema.
 * El enunciado define que 'channel' puede ser "email" o "sms".
 *
 * Usar un enum (en vez de un String suelto) centraliza los valores validos
 * y evita "magic strings" repartidos por el codigo.
 */
public enum Channel {
    EMAIL,
    SMS;

    /**
     * Convierte un texto entrante (case-insensitive) al enum correspondiente.
     *
     * @param raw valor recibido en el request (ej: "email", "SMS").
     * @return el Channel correspondiente.
     * @throws IllegalArgumentException si el valor no es soportado.
     */
    public static Channel fromString(String raw) {
        if (raw == null) {
            throw new IllegalArgumentException("El canal no puede ser nulo");
        }
        return switch (raw.trim().toLowerCase()) {
            case "email" -> EMAIL;
            case "sms" -> SMS;
            default -> throw new IllegalArgumentException(
                    "Canal no soportado: '" + raw + "'. Valores validos: email, sms");
        };
    }
}
