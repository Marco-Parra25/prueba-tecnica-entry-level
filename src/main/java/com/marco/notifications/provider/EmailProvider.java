package com.marco.notifications.provider;

import com.marco.notifications.model.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Estrategia concreta para el canal EMAIL.
 *
 * Es un @Component: Spring la detecta automaticamente y la inyecta en la
 * factory junto al resto de proveedores.
 */
@Component
public class EmailProvider implements NotificationProvider {

    private static final Logger log = LoggerFactory.getLogger(EmailProvider.class);

    @Override
    public void send(String userId, String message) {
        // Envio simulado. En un caso real aqui iria la integracion SMTP / API.
        log.info("[EMAIL] Enviando a userId={} | mensaje='{}'", userId, message);
    }

    @Override
    public Channel getChannel() {
        return Channel.EMAIL;
    }

    @Override
    public String getName() {
        return "EmailProvider";
    }
}
