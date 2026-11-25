package com.stayeasy.reservation.repository;

import com.stayeasy.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByGuestId(String guestId);

    @Query("select count(r)>0 from Reservation r where r.roomId=:roomId and r.status in ('PENDING','CONFIRMED') and r.checkIn<:cout and r.checkOut>:cin")
    boolean overlaps(@Param("roomId") Long roomId, @Param("cin") LocalDate cin, @Param("cout") LocalDate cout);
}
