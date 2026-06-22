package com.marco.notifications.repository;

import com.marco.notifications.model.Notification;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Implementacion del historial EN MEMORIA.
 *
 * El enunciado permite guardar el historial "en memoria, archivo, base de
 * datos, etc.". Se elige memoria por simplicidad para la prueba.
 *
 * Se usa CopyOnWriteArrayList para que la lectura/escritura sea segura entre
 * hilos (varios requests concurrentes), evitando condiciones de carrera.
 */
@Repository
public class InMemoryNotificationRepository implements NotificationRepository {

    private final List<Notification> history = new CopyOnWriteArrayList<>();

    @Override
    public Notification save(Notification notification) {
        history.add(notification);
        return notification;
    }

    @Override
    public List<Notification> findAll() {
        // Copia inmutable para que quien consuma no pueda mutar el historial.
        return Collections.unmodifiableList(new ArrayList<>(history));
    }
}
