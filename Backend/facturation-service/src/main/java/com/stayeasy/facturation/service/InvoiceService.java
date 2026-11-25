package com.stayeasy.facturation.service;

import com.stayeasy.facturation.dto.InvoiceResponse;
import com.stayeasy.facturation.entity.Invoice;
import com.stayeasy.facturation.exception.ConflictException;
import com.stayeasy.facturation.exception.ResourceNotFoundException;
import com.stayeasy.facturation.mapper.InvoiceMapper;
import com.stayeasy.facturation.repository.InvoiceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class InvoiceService {

    private final InvoiceRepository repository;
    private final InvoiceMapper mapper;
    private final EventPublisher events;

    public InvoiceService(InvoiceRepository repository, InvoiceMapper mapper, EventPublisher events) {
        this.repository = repository;
        this.mapper = mapper;
        this.events = events;
    }

    public List<InvoiceResponse> findAll(String userId, String ownerId) {
        List<Invoice> list = userId != null ? repository.findByUserId(userId)
                : ownerId != null ? repository.findByOwnerId(ownerId)
                : repository.findAll();
        return list.stream().map(mapper::toResponse).toList();
    }

    public InvoiceResponse findById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    public InvoiceResponse findByReservation(Long reservationId) {
        return mapper.toResponse(repository.findByReservationId(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Facture introuvable pour la réservation " + reservationId)));
    }

    /** Simulation du paiement (Flouci) : la verification marque la facture comme payee. */
    public InvoiceResponse verify(Long id) {
        Invoice i = getEntity(id);
        if (!"PENDING".equals(i.getStatus())) {
            throw new ConflictException("Facture non payable");
        }
        if (i.getExpiresAt() != null && i.getExpiresAt().isBefore(LocalDateTime.now())) {
            i.setStatus("EXPIRED");
            repository.save(i);
            events.publish("invoice.failed", paymentEvent(i));
            return mapper.toResponse(i);
        }
        i.setStatus("PAID");
        i.setPaidAt(LocalDateTime.now());
        repository.save(i);
        events.publish("invoice.paid", paymentEvent(i));
        return mapper.toResponse(i);
    }

    public InvoiceResponse cancel(Long id) {
        Invoice i = getEntity(id);
        i.setStatus("CANCELLED");
        return mapper.toResponse(repository.save(i));
    }

    /** Événement reservation.requested : cree la facture (idempotent). */
    public void onReservationRequested(Long reservationId, Map<String, Object> message) {
        if (repository.findByReservationId(reservationId).isPresent()) return;
        repository.save(mapper.fromReservationEvent(reservationId, message));
    }

    /** Événement reservation.cancelled : annule la facture si elle est encore en attente. */
    public void onReservationCancelled(Long reservationId) {
        repository.findByReservationId(reservationId).ifPresent(i -> {
            if ("PENDING".equals(i.getStatus())) {
                i.setStatus("CANCELLED");
                repository.save(i);
            }
        });
    }

    private Map<String, Object> paymentEvent(Invoice i) {
        return Map.of("reservationId", i.getReservationId(), "invoiceId", i.getId(), "userId", i.getUserId());
    }

    private Invoice getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Facture introuvable : " + id));
    }
}
