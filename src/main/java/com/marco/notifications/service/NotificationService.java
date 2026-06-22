package com.marco.notifications.service;

import com.marco.notifications.dto.NotificationRequest;
import com.marco.notifications.model.Channel;
import com.marco.notifications.model.Notification;
import com.marco.notifications.provider.NotificationProvider;
import com.marco.notifications.provider.NotificationProviderFactory;
import com.marco.notifications.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Capa de servicio: contiene la logica de negocio del envio de notificaciones.
 *
 * Orquesta los tres colaboradores y mantiene al controlador "delgado":
 *   1) Resuelve el canal (valida que sea email|sms).
 *   2) Pide a la FACTORY el proveedor (STRATEGY) adecuado y envia.
 *   3) Persiste el envio en el historial (REPOSITORY).
 */
@Service
public class NotificationService {

    private final NotificationProviderFactory providerFactory;
    private final NotificationRepository repository;

    public NotificationService(NotificationProviderFactory providerFactory,
                               NotificationRepository repository) {
        this.providerFactory = providerFactory;
        this.repository = repository;
    }

    /**
     * Procesa una solicitud de notificacion: envia y registra en el historial.
     *
     * @param request datos validados del request.
     * @return la notificacion guardada.
     * @throws IllegalArgumentException si el canal no es valido (email|sms).
     */
    public Notification send(NotificationRequest request) {
        // (1) Validamos y convertimos el canal. Lanza IllegalArgumentException
        //     con mensaje claro si el valor no es soportado.
        Channel channel = Channel.fromString(request.getChannel());

        // (2) La factory nos entrega la estrategia correcta segun el canal.
        NotificationProvider provider = providerFactory.getProvider(channel);
        provider.send(request.getUserId(), request.getMessage());

        // (3) Guardamos el envio en el historial.
        Notification notification = new Notification(
                request.getUserId(),
                request.getMessage(),
                channel,
                provider.getName());
        return repository.save(notification);
    }

    /** Devuelve el historial completo de notificaciones enviadas. */
    public List<Notification> getHistory() {
        return repository.findAll();
    }
}
