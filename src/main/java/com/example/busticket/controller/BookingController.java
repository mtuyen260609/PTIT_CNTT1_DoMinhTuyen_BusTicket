package com.example.busticket.controller;

import com.example.busticket.dto.BookingRequest;
import com.example.busticket.dto.TripSearchRequest;
import com.example.busticket.entity.Ticket;
import com.example.busticket.service.BookingService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/booking")
    public String searchForm(Model model) {
        addSearchModel(model);
        if (!model.containsAttribute("tripSearchRequest")) {
            model.addAttribute("tripSearchRequest", new TripSearchRequest());
        }
        return "booking/search";
    }

    @PostMapping("/booking/search")
    public String searchTrips(
            @Valid @ModelAttribute TripSearchRequest tripSearchRequest,
            BindingResult bindingResult,
            Model model
    ) {
        addSearchModel(model);
        if (bindingResult.hasErrors()) {
            return "booking/search";
        }

        model.addAttribute("trips", bookingService.searchTrips(tripSearchRequest));
        return "booking/search";
    }

    @GetMapping("/booking/trips/{tripId}")
    public String seatMap(@PathVariable Long tripId, Model model, HttpSession session) {
        addSeatModel(tripId, model);
        if (!model.containsAttribute("bookingRequest")) {
            Long userId = (Long) session.getAttribute("userId");
            model.addAttribute("bookingRequest", bookingService.createRequestFromUser(userId));
        }
        return "booking/seats";
    }

    @PostMapping("/booking/trips/{tripId}")
    public String bookTicket(
            @PathVariable Long tripId,
            @Valid @ModelAttribute BookingRequest bookingRequest,
            BindingResult bindingResult,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            addSeatModel(tripId, model);
            return "booking/seats";
        }

        try {
            Long userId = (Long) session.getAttribute("userId");
            Ticket ticket = bookingService.bookTicket(tripId, userId, bookingRequest);
            redirectAttributes.addFlashAttribute("message", "Dat ve thanh cong. Ma ve: " + ticket.getTicketCode());
            return "redirect:/tickets/lookup";
        } catch (IllegalArgumentException ex) {
            addSeatModel(tripId, model);
            model.addAttribute("formError", ex.getMessage());
            return "booking/seats";
        }
    }

    private void addSearchModel(Model model) {
        model.addAttribute("locations", bookingService.getLocations());
    }

    private void addSeatModel(Long tripId, Model model) {
        model.addAttribute("trip", bookingService.getTripDetail(tripId));
        model.addAttribute("seats", bookingService.getSeatMap(tripId));
    }
}
