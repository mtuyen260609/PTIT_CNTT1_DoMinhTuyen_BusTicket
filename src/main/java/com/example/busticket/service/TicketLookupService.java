package com.example.busticket.service;

import com.example.busticket.dto.TicketLookupRequest;
import com.example.busticket.entity.Ticket;
import com.example.busticket.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketLookupService {

    private final TicketRepository ticketRepository;

    public TicketLookupService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Transactional(readOnly = true)
    public Ticket findTicket(TicketLookupRequest request) {
        return ticketRepository.findDetailByTicketCodeAndPassengerPhone(
                        request.getTicketCode().trim().toUpperCase(),
                        request.getPassengerPhone().trim()
                )
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay ve voi thong tin da nhap"));
    }
}
