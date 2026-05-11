package com.example.busticket.repository;

import com.example.busticket.entity.Ticket;
import com.example.busticket.entity.TicketStatus;
import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findByTicketCodeAndPassengerPhone(String ticketCode, String passengerPhone);

    boolean existsByTicketCode(String ticketCode);

    @Query("""
            select t from Ticket t
            join fetch t.seat s
            join fetch s.trip tr
            join fetch tr.bus
            join fetch tr.route r
            join fetch r.departure
            join fetch r.arrival
            where t.status = :status
            order by tr.departureTime asc, t.bookingTime asc
            """)
    List<Ticket> findDetailsByStatus(@Param("status") TicketStatus status);

    @Query("""
            select t from Ticket t
            join fetch t.seat s
            join fetch s.trip tr
            join fetch tr.bus
            join fetch tr.route r
            join fetch r.departure
            join fetch r.arrival
            where t.status = :status
              and t.bookingTime <= :deadline
            order by t.bookingTime asc
            """)
    List<Ticket> findExpiredPendingTickets(
            @Param("status") TicketStatus status,
            @Param("deadline") LocalDateTime deadline
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select t from Ticket t
            join fetch t.seat s
            join fetch s.trip tr
            join fetch tr.bus
            join fetch tr.route r
            join fetch r.departure
            join fetch r.arrival
            where t.id = :id
            """)
    Optional<Ticket> findDetailByIdForUpdate(@Param("id") Long id);

    @Query("""
            select t from Ticket t
            join fetch t.seat s
            join fetch s.trip tr
            join fetch tr.bus
            join fetch tr.route r
            join fetch r.departure
            join fetch r.arrival
            where t.ticketCode = :ticketCode
              and t.passengerPhone = :passengerPhone
            """)
    Optional<Ticket> findDetailByTicketCodeAndPassengerPhone(
            @Param("ticketCode") String ticketCode,
            @Param("passengerPhone") String passengerPhone
    );

    @Query("""
            select t from Ticket t
            join fetch t.seat s
            join fetch s.trip tr
            join fetch tr.bus
            join fetch tr.route r
            join fetch r.departure
            join fetch r.arrival
            where t.user.id = :userId
            order by tr.departureTime desc, t.bookingTime desc
            """)
    List<Ticket> findDetailsByUserId(@Param("userId") Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select t from Ticket t
            join fetch t.user
            join fetch t.seat s
            join fetch s.trip tr
            join fetch tr.bus
            join fetch tr.route r
            join fetch r.departure
            join fetch r.arrival
            where t.id = :id
            """)
    Optional<Ticket> findDetailWithUserByIdForUpdate(@Param("id") Long id);
}
