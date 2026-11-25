package com.stayeasy.facturation.repository;

import com.stayeasy.facturation.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findByReservationId(Long reservationId);

    List<Invoice> findByUserId(String userId);

    List<Invoice> findByOwnerId(String ownerId);
}
