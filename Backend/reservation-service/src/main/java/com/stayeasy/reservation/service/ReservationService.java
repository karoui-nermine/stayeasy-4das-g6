package com.stayeasy.reservation.service;

import feign.FeignException;
import com.stayeasy.reservation.client.AccommodationClient;
import com.stayeasy.reservation.dto.ReservationRequest;
import com.stayeasy.reservation.dto.ReservationResponse;
import com.stayeasy.reservation.dto.ReservationUpdateRequest;
import com.stayeasy.reservation.dto.RoomDto;
import com.stayeasy.reservation.entity.Reservation;
import com.stayeasy.reservation.exception.BadRequestException;
import com.stayeasy.reservation.exception.ConflictException;
import com.stayeasy.reservation.exception.ResourceNotFoundException;
import com.stayeasy.reservation.exception.ServiceUnavailableException;
import com.stayeasy.reservation.mapper.ReservationMapper;
import com.stayeasy.reservation.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository repository;
    private final ReservationMapper mapper;
    private final AccommodationClient accommodationClient;
    private final EventPublisher events;

    public ReservationService(ReservationRepository repository, ReservationMapper mapper,
                              AccommodationClient accommodationClient, EventPublisher events) {
        this.repository = repository;
        this.mapper = mapper;
        this.accommodationClient = accommodationClient;
        this.events = events;
    }

    public List<ReservationResponse> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    public ReservationResponse findById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    public List<ReservationResponse> findByUser(String userId) {
        return repository.findByGuestId(userId).stream().map(mapper::toResponse).toList();
    }

    public ReservationResponse create(ReservationRequest request) {
        if (!request.getCheckOut().isAfter(request.getCheckIn())) {
            throw new BadRequestException("Données invalides : la date de départ doit suivre la date d'arrivée");
        }
        RoomDto room;
        try {
            room = accommodationClient.getRoom(request.getRoomId());
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("Chambre introuvable");
        } catch (Exception e) {
            throw new ServiceUnavailableException("Service Accommodation indisponible");
        }
        if (!"DISPONIBLE".equals(room.getStatus())
                || repository.overlaps(request.getRoomId(), request.getCheckIn(), request.getCheckOut())) {
            throw new ConflictException("Chambre indisponible");
        }
        long nights = ChronoUnit.DAYS.between(request.getCheckIn(), request.getCheckOut());
        Reservation reservation = mapper.toEntity(request);
        reservation.setId(null);
        reservation.setStatus("PENDING");
        reservation.setTotalAmount(room.getPrice().multiply(BigDecimal.valueOf(nights)));
        reservation.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        Reservation saved = repository.save(reservation);
        events.publish("reservation.requested", mapper.toEvent(saved));
        return mapper.toResponse(saved);
    }

    public ReservationResponse update(Long id, ReservationUpdateRequest request) {
        Reservation r = getEntity(id);
        if (!"PENDING".equals(r.getStatus())) {
            throw new ConflictException("Réservation non modifiable");
        }
        if (request.getCheckIn() != null) r.setCheckIn(request.getCheckIn());
        if (request.getCheckOut() != null) r.setCheckOut(request.getCheckOut());
        return mapper.toResponse(repository.save(r));
    }

    public ReservationResponse confirm(Long id) {
        Reservation r = getEntity(id);
        r.setStatus("CONFIRMED");
        repository.save(r);
        events.publish("reservation.confirmed", mapper.toEvent(r));
        return mapper.toResponse(r);
    }

    public ReservationResponse cancel(Long id) {
        Reservation r = getEntity(id);
        if ("CANCELLED".equals(r.getStatus())) {
            throw new ConflictException("Déjà annulée");
        }
        r.setStatus("CANCELLED");
        r.setCancelReason("CLIENT_CANCELLED");
        repository.save(r);
        events.publish("reservation.cancelled", mapper.toEvent(r));
        return mapper.toResponse(r);
    }

    /** Appele par le listener RabbitMQ quand la facture est payee. */
    public void onPaymentSucceeded(Long reservationId, String invoiceId) {
        repository.findById(reservationId).ifPresent(r -> {
            r.setStatus("CONFIRMED");
            r.setPaymentId(invoiceId);
            repository.save(r);
            events.publish("reservation.confirmed", mapper.toEvent(r));
        });
    }

    /** Appele par le listener RabbitMQ quand le paiement echoue ou expire. */
    public void onPaymentFailed(Long reservationId) {
        repository.findById(reservationId).ifPresent(r -> {
            r.setStatus("CANCELLED");
            r.setCancelReason("PAYMENT_REFUSED");
            repository.save(r);
            events.publish("reservation.cancelled", mapper.toEvent(r));
        });
    }

    private Reservation getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Réservation introuvable : " + id));
    }
}
