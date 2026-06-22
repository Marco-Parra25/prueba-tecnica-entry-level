package com.marco.notifications.model;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Representa una notificacion ya procesada y guardada en el historial.
 *
 * Es un objeto de dominio inmutable: una vez creado, sus datos no cambian.
 * Esto evita efectos colaterales y hace el historial mas predecible.
 */
public class Notification {

    /** Generador simple de IDs incrementales, seguro entre hilos. */
    private static final AtomicLong SEQUENCE = new AtomicLong(0);

    private final long id;
    private final String userId;
    private final String message;
    private final Channel channel;
    private final String provider;   // nombre del proveedor que realizo el envio
    private final Instant sentAt;     // marca de tiempo del envio

    public Notification(String userId, String message, Channel channel, String provider) {
        this.id = SEQUENCE.incrementAndGet();
        this.userId = userId;
        this.message = message;
        this.channel = channel;
        this.provider = provider;
        this.sentAt = Instant.now();
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

    public Channel getChannel() {
        return channel;
    }

    public String getProvider() {
        return provider;
    }

    public Instant getSentAt() {
        return sentAt;
    }
}
