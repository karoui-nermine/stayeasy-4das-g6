package com.stayeasy.notification.service;

import com.stayeasy.notification.dto.NotificationResponse;
import com.stayeasy.notification.entity.Notification;
import com.stayeasy.notification.exception.ConflictException;
import com.stayeasy.notification.exception.ResourceNotFoundException;
import com.stayeasy.notification.mapper.NotificationMapper;
import com.stayeasy.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class NotificationService {

    private final NotificationRepository repository;
    private final NotificationMapper mapper;

    public NotificationService(NotificationRepository repository, NotificationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<NotificationResponse> findAll(String userId) {
        List<Notification> list = userId != null
                ? repository.findByUserIdOrderByCreatedAtDesc(userId)
                : repository.findAll();
        return list.stream().map(mapper::toResponse).toList();
    }

    public NotificationResponse findById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    public NotificationResponse markAsRead(Long id) {
        Notification n = getEntity(id);
        n.setReadAt(LocalDateTime.now());
        return mapper.toResponse(repository.save(n));
    }

    public NotificationResponse retry(Long id) {
        Notification n = getEntity(id);
        if ("SENT".equals(n.getStatus())) {
            throw new ConflictException("Déjà envoyée");
        }
        n.setStatus("SENT");
        return mapper.toResponse(repository.save(n));
    }

    /** Transforme un evenement RabbitMQ en notification in-app pour l'utilisateur concerne. */
    public void createFromEvent(String routingKey, Map<String, Object> m) {
        if (m.get("userId") == null) return;
        String[] t = switch (routingKey) {
            case "reservation.confirmed" -> new String[]{"RESERVATION", "Réservation confirmée",
                    "Votre réservation #" + m.get("reservationId") + " est confirmée."};
            case "reservation.cancelled" -> new String[]{"RESERVATION", "Réservation annulée",
                    "Votre réservation #" + m.get("reservationId") + " a été annulée."};
            case "invoice.paid" -> new String[]{"PAYMENT", "Paiement reçu",
                    "Le paiement de la réservation #" + m.get("reservationId") + " a été accepté."};
            case "maintenance.completed" -> new String[]{"MAINTENANCE", "Intervention terminée",
                    "L'intervention sur la chambre " + m.get("roomId") + " est terminée."};
            case "colocation.joined" -> new String[]{"COLOCATION", "Colocation rejointe",
                    "Vous avez rejoint le séjour « " + m.get("title") + " »."};
            case "user.registered" -> new String[]{"SYSTEM", "Bienvenue sur StayEasy",
                    "Votre compte est prêt, bon séjour !"};
            default -> new String[]{"SYSTEM", "Notification", "Événement " + routingKey};
        };
        Notification n = new Notification();
        n.setUserId(String.valueOf(m.get("userId")));
        n.setType(t[0]);
        n.setTitle(t[1]);
        n.setMessage(t[2]);
        n.setCreatedAt(LocalDateTime.now());
        repository.save(n);
    }

    private Notification getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification introuvable : " + id));
    }
}
