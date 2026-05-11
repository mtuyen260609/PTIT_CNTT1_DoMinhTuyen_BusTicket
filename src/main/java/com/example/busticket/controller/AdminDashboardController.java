package com.example.busticket.controller;

import com.example.busticket.repository.BusRepository;
import com.example.busticket.repository.TicketRepository;
import com.example.busticket.repository.TripRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminDashboardController {

    private final BusRepository busRepository;
    private final TripRepository tripRepository;
    private final TicketRepository ticketRepository;

    public AdminDashboardController(BusRepository busRepository, TripRepository tripRepository, TicketRepository ticketRepository) {
        this.busRepository = busRepository;
        this.tripRepository = tripRepository;
        this.ticketRepository = ticketRepository;
    }

    @GetMapping("/admin")
    public String dashboard(Model model) {
        model.addAttribute("busCount", busRepository.count());
        model.addAttribute("tripCount", tripRepository.count());
        model.addAttribute("ticketCount", ticketRepository.count());
        return "admin/dashboard";
    }
}
