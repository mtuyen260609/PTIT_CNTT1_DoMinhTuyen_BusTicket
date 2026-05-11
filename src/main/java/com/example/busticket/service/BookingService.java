package com.example.busticket.service;

import com.example.busticket.dto.BookingRequest;
import com.example.busticket.dto.SeatView;
import com.example.busticket.dto.TripSearchRequest;
import com.example.busticket.entity.Location;
import com.example.busticket.entity.Seat;
import com.example.busticket.entity.SeatStatus;
import com.example.busticket.entity.Ticket;
import com.example.busticket.entity.TicketStatus;
import com.example.busticket.entity.Trip;
import com.example.busticket.entity.User;
import com.example.busticket.repository.LocationRepository;
import com.example.busticket.repository.SeatRepository;
import com.example.busticket.repository.TicketRepository;
import com.example.busticket.repository.TripRepository;
import com.example.busticket.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {

    private final LocationRepository locationRepository;
    private final TripRepository tripRepository;
    private final SeatRepository seatRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public BookingService(
            LocationRepository locationRepository,
            TripRepository tripRepository,
            SeatRepository seatRepository,
            TicketRepository ticketRepository,
            UserRepository userRepository
    ) {
        this.locationRepository = locationRepository;
        this.tripRepository = tripRepository;
        this.seatRepository = seatRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<Location> getLocations() {
        return locationRepository.findAllByOrderByNameAsc();
    }

    @Transactional(readOnly = true)
    public List<Trip> searchTrips(TripSearchRequest request) {
        LocalDateTime start = request.getDepartureDate().atStartOfDay();
        return tripRepository.findTrips(
                request.getDepartureId(),
                request.getArrivalId(),
                start,
                start.plusDays(1)
        );
    }

    @Transactional(readOnly = true)
    public Trip getTripDetail(Long tripId) {
        return tripRepository.findDetailById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay chuyen xe"));
    }

    @Transactional(readOnly = true)
    public List<SeatView> getSeatMap(Long tripId) {
        return seatRepository.findByTripIdOrderBySeatRowAscSeatColumnAsc(tripId).stream()
                .map(seat -> new SeatView(
                        seat.getId(),
                        seat.getSeatNumber(),
                        seat.getSeatRow(),
                        seat.getSeatColumn(),
                        seat.getStatus()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public BookingRequest createRequestFromUser(Long userId) {
        BookingRequest request = new BookingRequest();
        if (userId == null) {
            return request;
        }

        userRepository.findWithProfileById(userId).ifPresent(user -> {
            request.setPassengerName(user.getProfile().getFullName());
            request.setPassengerPhone(user.getProfile().getPhone());
            request.setPassengerEmail(user.getProfile().getEmail());
        });
        return request;
    }

    @Transactional
    public Ticket bookTicket(Long tripId, Long userId, BookingRequest request) {
        Seat seat = seatRepository.findByIdForUpdate(request.getSeatId())
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay ghe"));

        if (!seat.getTrip().getId().equals(tripId)) {
            throw new IllegalArgumentException("Ghe khong thuoc chuyen xe nay");
        }
        if (seat.getStatus() != SeatStatus.AVAILABLE) {
            throw new IllegalArgumentException("Ghe da co nguoi dat, vui long chon ghe khac");
        }

        seat.setStatus(SeatStatus.PENDING);

        Ticket ticket = new Ticket();
        ticket.setTicketCode(generateTicketCode());
        ticket.setSeat(seat);
        ticket.setPassengerName(request.getPassengerName().trim());
        ticket.setPassengerPhone(request.getPassengerPhone().trim());
        ticket.setPassengerEmail(normalize(request.getPassengerEmail()));
        ticket.setTotalPrice(seat.getTrip().getPrice());
        ticket.setStatus(TicketStatus.PENDING);

        if (userId != null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Khong tim thay tai khoan"));
            ticket.setUser(user);
        }

        return ticketRepository.save(ticket);
    }

    private String generateTicketCode() {
        long sequence = ticketRepository.count() + 1;
        String code = formatTicketCode(sequence);

        while (ticketRepository.existsByTicketCode(code)) {
            sequence++;
            code = formatTicketCode(sequence);
        }
        return code;
    }

    private String formatTicketCode(long sequence) {
        return String.format("BX%03d", sequence);
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
