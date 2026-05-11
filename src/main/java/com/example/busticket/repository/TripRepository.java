package com.example.busticket.repository;

import com.example.busticket.entity.Trip;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TripRepository extends JpaRepository<Trip, Long> {

    boolean existsByBusId(Long busId);

    boolean existsByBusIdAndDepartureTime(Long busId, LocalDateTime departureTime);

    @Query("""
            select t from Trip t
            join fetch t.route r
            join fetch r.departure
            join fetch r.arrival
            join fetch t.bus
            order by t.departureTime desc
            """)
    List<Trip> findAllDetails();

    @Query("""
            select t from Trip t
            join fetch t.route r
            join fetch r.departure
            join fetch r.arrival
            join fetch t.bus
            where r.departure.id = :departureId
              and r.arrival.id = :arrivalId
              and t.departureTime >= :startTime
              and t.departureTime < :endTime
            order by t.departureTime asc
            """)
    List<Trip> findTrips(
            @Param("departureId") Long departureId,
            @Param("arrivalId") Long arrivalId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    @Query("""
            select t from Trip t
            join fetch t.route r
            join fetch r.departure
            join fetch r.arrival
            join fetch t.bus
            where t.id = :id
            """)
    Optional<Trip> findDetailById(@Param("id") Long id);
}
