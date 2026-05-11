package com.example.busticket.repository;

import com.example.busticket.entity.Route;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, Long> {

    Optional<Route> findByDepartureIdAndArrivalId(Long departureId, Long arrivalId);
}
