package com.example.busticket.repository;

import com.example.busticket.entity.Bus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusRepository extends JpaRepository<Bus, Long> {

    boolean existsByLicensePlate(String licensePlate);

    boolean existsByLicensePlateAndIdNot(String licensePlate, Long id);

    Optional<Bus> findByLicensePlate(String licensePlate);
}
