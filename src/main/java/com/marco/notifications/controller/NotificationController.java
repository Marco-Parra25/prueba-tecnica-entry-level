package com.marco.notifications.controller;

import com.marco.notifications.dto.NotificationRequest;
import com.marco.notifications.dto.NotificationResponse;
import com.marco.notifications.model.Notification;
import com.marco.notifications.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST que expone los endpoints del enunciado:
 *   - POST /notifications  -> envia una notificacion.
 *   - GET  /notifications  -> devuelve el historial.
 *
 * El controlador es "delgado": solo traduce HTTP <-> dominio y delega toda la
 * logica en el servicio.
 */
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    /**
     * POST /notifications
     * Valida el body (@Valid -> 400 si falta algun campo) y envia la notificacion.
     * Responde 201 Created con la notificacion registrada.
     */
    @PostMapping
    public ResponseEntity<NotificationResponse> send(@Valid @RequestBody NotificationRequest request) {
        Notification notification = service.send(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(NotificationResponse.from(notification));
    }

    /**
     * GET /notifications
     * Devuelve el historial completo envuelto en { "data": [...] },
     * respetando el formato de respuesta indicado en el enunciado.
     */
    @GetMapping
    public Map<String, List<NotificationResponse>> getHistory() {
        List<NotificationResponse> data = service.getHistory().stream()
                .map(NotificationResponse::from)
                .toList();
        return Map.of("data", data);
    }
}
