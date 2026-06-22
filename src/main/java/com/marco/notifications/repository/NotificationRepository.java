package com.marco.notifications.repository;

import com.marco.notifications.model.Notification;

import java.util.List;

/**
 * Abstraccion del almacenamiento del historial de notificaciones.
 *
 * Definir una interfaz (y no atarse a la implementacion en memoria) permite
 * sustituir el almacenamiento por una base de datos real sin cambiar el
 * servicio que la consume (principio de Inversion de Dependencias).
 */
public interface NotificationRepository {

    /** Guarda una notificacion en el historial. */
    Notification save(Notification notification);

    /** Devuelve el historial completo, en orden de insercion. */
    List<Notification> findAll();
}
