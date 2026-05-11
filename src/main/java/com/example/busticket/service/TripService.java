package com.example.busticket.service;

import com.example.busticket.dto.TripRequest;
import com.example.busticket.entity.Bus;
import com.example.busticket.entity.Location;
import com.example.busticket.entity.Route;
import com.example.busticket.entity.Seat;
import com.example.busticket.entity.Trip;
import com.example.busticket.repository.BusRepository;
import com.example.busticket.repository.LocationRepository;
import com.example.busticket.repository.RouteRepository;
import com.example.busticket.repository.SeatRepository;
import com.example.busticket.repository.TripRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TripService {

    private final TripRepository tripRepository;
    private final RouteRepository routeRepository;
    private final LocationRepository locationRepository;
    private final BusRepository busRepository;
    private final SeatRepository seatRepository;

    public TripService(
            TripRepository tripRepository,
            RouteRepository routeRepository,
            LocationRepository locationRepository,
            BusRepository busRepository,
            SeatRepository seatRepository
    ) {
        this.tripRepository = tripRepository;
        this.routeRepository = routeRepository;
        this.locationRepository = locationRepository;
        this.busRepository = busRepository;
        this.seatRepository = seatRepository;
    }

    @Transactional(readOnly = true)
    public List<Trip> findAll() {
        return tripRepository.findAllDetails();
    }

    @Transactional
    public void create(TripRequest request) {
        if (request.getDepartureTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Không thể tạo chuyến xe trong quá khứ");
        }

        if (request.getDepartureId().equals(request.getArrivalId())) {
            throw new IllegalArgumentException("Diem di va diem den khong duoc trung nhau");
        }

        Location departure = locationRepository.findById(request.getDepartureId())
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay diem di"));
        Location arrival = locationRepository.findById(request.getArrivalId())
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay diem den"));
        Bus bus = busRepository.findById(request.getBusId())
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay xe"));

        Route route = routeRepository.findByDepartureIdAndArrivalId(departure.getId(), arrival.getId())
                .orElseGet(() -> {
                    Route newRoute = new Route();
                    newRoute.setDeparture(departure);
                    newRoute.setArrival(arrival);
                    return routeRepository.save(newRoute);
                });

        Trip trip = new Trip();
        trip.setRoute(route);
        trip.setBus(bus);
        trip.setDepartureTime(request.getDepartureTime());
        trip.setPrice(request.getPrice());
        tripRepository.save(trip);

        createSeats(trip, bus.getTotalSeats());
    }

    private void createSeats(Trip trip, Integer totalSeats) {
        int columns = totalSeats > 20 ? 4 : 2;
        for (int index = 1; index <= totalSeats; index++) {
            Seat seat = new Seat();
            seat.setTrip(trip);
            seat.setSeatNumber("A" + index);
            seat.setSeatRow(((index - 1) / columns) + 1);
            seat.setSeatColumn(((index - 1) % columns) + 1);
            seatRepository.save(seat);
        }
    }
}
