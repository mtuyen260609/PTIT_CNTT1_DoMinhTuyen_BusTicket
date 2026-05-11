package com.example.busticket.repository;

import com.example.busticket.entity.Location;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findAllByOrderByNameAsc();

    Optional<Location> findByName(String name);
}
