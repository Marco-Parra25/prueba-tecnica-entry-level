package com.marco.notifications.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO del cuerpo de la peticion POST /notifications.
 *
 * Las anotaciones de validacion garantizan que TODOS los parametros sean
 * obligatorios (no nulos y no vacios), tal como pide el enunciado. La
 * validacion del valor concreto de 'channel' (email | sms) se hace en la
 * capa de servicio mediante Channel.fromString para devolver un mensaje claro.
 */
public class NotificationRequest {

    @NotBlank(message = "userId es obligatorio")
    private String userId;

    @NotBlank(message = "message es obligatorio")
    private String message;

    @NotBlank(message = "channel es obligatorio")
    private String channel;

    public NotificationRequest() {
        // Constructor por defecto requerido por Jackson para deserializar JSON.
    }

    public NotificationRequest(String userId, String message, String channel) {
        this.userId = userId;
        this.message = message;
        this.channel = channel;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
