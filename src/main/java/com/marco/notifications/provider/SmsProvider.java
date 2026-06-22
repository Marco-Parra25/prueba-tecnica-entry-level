package com.marco.notifications.provider;

import com.marco.notifications.model.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Estrategia concreta para el canal SMS.
 */
@Component
public class SmsProvider implements NotificationProvider {

    private static final Logger log = LoggerFactory.getLogger(SmsProvider.class);

    @Override
    public void send(String userId, String message) {
        // Envio simulado. En un caso real aqui iria la integracion con la
        // pasarela de SMS (Twilio, etc.).
        log.info("[SMS] Enviando a userId={} | mensaje='{}'", userId, message);
    }

    @Override
    public Channel getChannel() {
        return Channel.SMS;
    }

    @Override
    public String getName() {
        return "SmsProvider";
    }
}
