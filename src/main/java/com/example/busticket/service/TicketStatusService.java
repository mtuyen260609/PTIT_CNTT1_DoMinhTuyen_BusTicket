package com.example.busticket.service;

import com.example.busticket.dto.TicketLookupRequest;
import com.example.busticket.entity.Seat;
import com.example.busticket.entity.SeatStatus;
import com.example.busticket.entity.Ticket;
import com.example.busticket.entity.TicketStatus;
import com.example.busticket.repository.TicketRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketStatusService {

    private static final int PASSENGER_CANCEL_HOURS = 12;
    private static final int PENDING_EXPIRE_MINUTES = 15;

    private final TicketRepository ticketRepository;

    public TicketStatusService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Transactional(readOnly = true)
    public List<Ticket> findPendingTickets() {
        return ticketRepository.findDetailsByStatus(TicketStatus.PENDING);
    }

    @Transactional(readOnly = true)
    public List<Ticket> findExpiredPendingTickets() {
        return ticketRepository.findExpiredPendingTickets(
                TicketStatus.PENDING,
                LocalDateTime.now().minusMinutes(PENDING_EXPIRE_MINUTES)
        );
    }

    @Transactional
    public void confirmPaid(Long ticketId) {
        Ticket ticket = findLockedTicket(ticketId);
        if (ticket.getStatus() != TicketStatus.PENDING) {
            throw new IllegalArgumentException("Chi co the xac nhan ve dang cho xu ly");
        }
        if (ticket.getSeat().getTrip().getDepartureTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Khong the xac nhan ve cua chuyen da qua gio");
        }

        ticket.setStatus(TicketStatus.PAID);
        ticket.getSeat().setStatus(SeatStatus.BOOKED);
    }

    @Transactional
    public int cancelExpiredPendingTickets() {
        List<Ticket> expiredTickets = findExpiredPendingTickets();
        int cancelled = 0;
        for (Ticket expiredTicket : expiredTickets) {
            Ticket ticket = findLockedTicket(expiredTicket.getId());
            if (ticket.getStatus() == TicketStatus.PENDING
                    && !ticket.getBookingTime().isAfter(LocalDateTime.now().minusMinutes(PENDING_EXPIRE_MINUTES))) {
                cancelAndReleaseSeat(ticket);
                cancelled++;
            }
        }
        return cancelled;
    }

    @Transactional
    public Ticket cancelByPassenger(TicketLookupRequest request) {
        Ticket ticket = ticketRepository.findDetailByTicketCodeAndPassengerPhone(
                        request.getTicketCode().trim().toUpperCase(),
                        request.getPassengerPhone().trim()
                )
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay ve voi thong tin da nhap"));

        ticket = findLockedTicket(ticket.getId());
        validatePassengerCancel(ticket);
        cancelAndReleaseSeat(ticket);
        return ticket;
    }

    @Transactional
    public void cancelByUser(Long ticketId, Long userId) {
        Ticket ticket = ticketRepository.findDetailWithUserByIdForUpdate(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay ve"));

        if (ticket.getUser() == null || !ticket.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Ban khong co quyen huy ve nay");
        }

        validatePassengerCancel(ticket);
        cancelAndReleaseSeat(ticket);
    }

    private Ticket findLockedTicket(Long ticketId) {
        return ticketRepository.findDetailByIdForUpdate(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay ve"));
    }

    private void cancelAndReleaseSeat(Ticket ticket) {
        ticket.setStatus(TicketStatus.CANCELLED);
        Seat seat = ticket.getSeat();
        seat.setStatus(SeatStatus.AVAILABLE);
    }

    private void validatePassengerCancel(Ticket ticket) {
        if (ticket.getStatus() == TicketStatus.CANCELLED) {
            throw new IllegalArgumentException("Ve da duoc huy truoc do");
        }
        if (ticket.getStatus() == TicketStatus.PAID) {
            throw new IllegalArgumentException("Ve da xac nhan, vui long lien he nhan vien de xu ly");
        }

        LocalDateTime latestCancelTime = ticket.getSeat().getTrip().getDepartureTime().minusHours(PASSENGER_CANCEL_HOURS);
        if (LocalDateTime.now().isAfter(latestCancelTime)) {
            throw new IllegalArgumentException("Chi duoc huy ve truoc gio khoi hanh it nhat 12 gio");
        }
    }
}
