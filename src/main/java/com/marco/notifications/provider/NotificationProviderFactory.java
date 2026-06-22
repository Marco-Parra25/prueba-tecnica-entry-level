package com.marco.notifications.provider;

import com.marco.notifications.model.Channel;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * PATRON FACTORY.
 *
 * Centraliza la decision de "que proveedor usar segun el canal". El servicio
 * pide a la factory el provider para un Channel y recibe la estrategia
 * correcta, sin conocer las clases concretas.
 *
 * Detalle clave: Spring inyecta automaticamente en el constructor la lista de
 * TODOS los beans que implementan NotificationProvider. La factory los indexa
 * por su Channel. Asi, cuando se agregue un nuevo proveedor, basta crear la
 * clase con @Component: se registra solo, sin tocar esta factory (Abierto/Cerrado).
 */
@Component
public class NotificationProviderFactory {

    private final Map<Channel, NotificationProvider> providersByChannel =
            new EnumMap<>(Channel.class);

    public NotificationProviderFactory(List<NotificationProvider> providers) {
        for (NotificationProvider provider : providers) {
            providersByChannel.put(provider.getChannel(), provider);
        }
    }

    /**
     * Devuelve la estrategia de envio asociada al canal indicado.
     *
     * @param channel canal de la notificacion.
     * @return el provider correspondiente.
     * @throws IllegalStateException si no hay proveedor registrado para el canal.
     */
    public NotificationProvider getProvider(Channel channel) {
        NotificationProvider provider = providersByChannel.get(channel);
        if (provider == null) {
            throw new IllegalStateException(
                    "No hay proveedor registrado para el canal: " + channel);
        }
        return provider;
    }
}
