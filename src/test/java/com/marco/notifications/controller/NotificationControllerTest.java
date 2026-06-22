package com.marco.notifications.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marco.notifications.dto.NotificationRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas de integracion de la API (PARTE 3 - automatizacion).
 *
 * Usan MockMvc para ejercitar los endpoints reales pasando por el controlador,
 * la validacion, el servicio, la factory/strategy y el repositorio en memoria,
 * sin necesidad de levantar un servidor HTTP completo.
 *
 * Cubre los 4+ casos automatizados que pide el enunciado:
 *   1. POST email -> 201 y se envia por EmailProvider.
 *   2. POST sms   -> 201 y se envia por SmsProvider.
 *   3. POST con campo faltante -> 400 (validacion).
 *   4. POST con canal invalido -> 400.
 *   5. GET historial -> 200 y crece tras un POST.
 */
@SpringBootTest
@AutoConfigureMockMvc
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String json(Object o) throws Exception {
        return objectMapper.writeValueAsString(o);
    }

    @Test
    @DisplayName("CP1: POST email valido -> 201 y proveedor EmailProvider")
    void postEmailValido() throws Exception {
        NotificationRequest req = new NotificationRequest("123", "Hola", "email");

        mockMvc.perform(post("/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value("123"))
                .andExpect(jsonPath("$.channel").value("email"))
                .andExpect(jsonPath("$.provider").value("EmailProvider"))
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    @DisplayName("CP2: POST sms valido -> 201 y proveedor SmsProvider")
    void postSmsValido() throws Exception {
        NotificationRequest req = new NotificationRequest("456", "Tu codigo es 9999", "sms");

        mockMvc.perform(post("/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.channel").value("sms"))
                .andExpect(jsonPath("$.provider").value("SmsProvider"));
    }

    @Test
    @DisplayName("CP3: POST sin message -> 400 (campo obligatorio)")
    void postCampoFaltante() throws Exception {
        String body = "{\"userId\":\"123\",\"channel\":\"email\"}";

        mockMvc.perform(post("/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.message").exists());
    }

    @Test
    @DisplayName("CP4: POST con canal invalido -> 400")
    void postCanalInvalido() throws Exception {
        NotificationRequest req = new NotificationRequest("123", "Hola", "telegram");

        mockMvc.perform(post("/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("CP5: GET /notifications -> 200 y el historial contiene envios")
    void getHistorial() throws Exception {
        // Garantizamos al menos un envio previo.
        mockMvc.perform(post("/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(new NotificationRequest("789", "Para historial", "email"))))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(greaterThanOrEqualTo(1)));
    }
}
