package com.marco.notifications.provider;

import com.marco.notifications.model.Channel;

/**
 * PATRON STRATEGY.
 *
 * Define el contrato comun para todos los proveedores de envio. Cada canal
 * (email, sms) tendra su propia implementacion concreta. El servicio depende
 * de esta abstraccion y no de las clases concretas, de modo que agregar un
 * nuevo canal (ej: push, whatsapp) NO obliga a modificar el codigo existente
 * (principio Abierto/Cerrado).
 */
public interface NotificationProvider {

    /**
     * Realiza el envio de la notificacion por el canal correspondiente.
     * En esta prueba el envio es simulado (se registra en consola), pero la
     * firma es la que tendria una integracion real con un proveedor externo.
     *
     * @param userId  destinatario logico.
     * @param message contenido del mensaje.
     */
    void send(String userId, String message);

    /** Canal que atiende esta estrategia. Usado por la factory para registrarla. */
    Channel getChannel();

    /** Nombre legible del proveedor; queda guardado en el historial. */
    String getName();
}
