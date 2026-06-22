package com.marco.notifications.dto;

import com.marco.notifications.model.Notification;

import java.time.Instant;

/**
 * DTO de salida que se expone en las respuestas de la API.
 *
 * Separar el DTO de respuesta del modelo de dominio (Notification) es una
 * buena practica: la API publica controla exactamente que campos se exponen
 * y en que formato, sin acoplar el contrato externo a la clase interna.
 */
public class NotificationResponse {

    private final long id;
    private final String userId;
    private final String message;
    private final String channel;
    private final String provider;
    private final Instant sentAt;

    public NotificationResponse(long id, String userId, String message,
                                String channel, String provider, Instant sentAt) {
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.channel = channel;
        this.provider = provider;
        this.sentAt = sentAt;
    }

    /** Construye el DTO a partir del objeto de dominio. */
    public static NotificationResponse from(Notification n) {
        return new NotificationResponse(
                n.getId(),
                n.getUserId(),
                n.getMessage(),
                n.getChannel().name().toLowerCase(),
                n.getProvider(),
                n.getSentAt());
    }

    public long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

    public String getChannel() {
        return channel;
    }

    public String getProvider() {
        return provider;
    }

    public Instant getSentAt() {
        return sentAt;
    }
}
