package com.example.busticket.repository;

import com.example.busticket.entity.Seat;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByTripIdOrderBySeatRowAscSeatColumnAsc(Long tripId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Seat s join fetch s.trip t join fetch t.bus join fetch t.route where s.id = :id")
    Optional<Seat> findByIdForUpdate(@Param("id") Long id);
}
