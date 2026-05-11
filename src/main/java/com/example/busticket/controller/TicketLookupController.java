package com.example.busticket.controller;

import com.example.busticket.dto.TicketLookupRequest;
import com.example.busticket.service.TicketLookupService;
import com.example.busticket.service.TicketStatusService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TicketLookupController {

    private final TicketLookupService ticketLookupService;
    private final TicketStatusService ticketStatusService;

    public TicketLookupController(TicketLookupService ticketLookupService, TicketStatusService ticketStatusService) {
        this.ticketLookupService = ticketLookupService;
        this.ticketStatusService = ticketStatusService;
    }

    @GetMapping("/tickets/lookup")
    public String lookupForm(Model model, HttpServletRequest request) {
        String role = (String) request.getSession().getAttribute("role");
        if ("ADMIN".equals(role)) {
            return "redirect:/admin/tickets/lookup";
        }
        if ("STAFF".equals(role)) {
            return "redirect:/staff/tickets/lookup";
        }

        if (!model.containsAttribute("ticketLookupRequest")) {
            model.addAttribute("ticketLookupRequest", new TicketLookupRequest());
        }
        return "tickets/lookup";
    }

    @GetMapping("/admin/tickets/lookup")
    public String adminLookupForm(Model model) {
        prepareLookupForm(model);
        return "admin/tickets/lookup";
    }

    @GetMapping("/staff/tickets/lookup")
    public String staffLookupForm(Model model) {
        prepareLookupForm(model);
        return "staff/tickets/lookup";
    }

    @PostMapping("/tickets/lookup")
    public String lookup(
            @Valid @ModelAttribute TicketLookupRequest ticketLookupRequest,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "tickets/lookup";
        }

        try {
            model.addAttribute("ticket", ticketLookupService.findTicket(ticketLookupRequest));
        } catch (IllegalArgumentException ex) {
            model.addAttribute("formError", ex.getMessage());
        }
        return "tickets/lookup";
    }

    @PostMapping("/admin/tickets/lookup")
    public String adminLookup(
            @Valid @ModelAttribute TicketLookupRequest ticketLookupRequest,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "admin/tickets/lookup";
        }

        try {
            model.addAttribute("ticket", ticketLookupService.findTicket(ticketLookupRequest));
        } catch (IllegalArgumentException ex) {
            model.addAttribute("formError", ex.getMessage());
        }
        return "admin/tickets/lookup";
    }

    @PostMapping("/staff/tickets/lookup")
    public String staffLookup(
            @Valid @ModelAttribute TicketLookupRequest ticketLookupRequest,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "staff/tickets/lookup";
        }

        try {
            model.addAttribute("ticket", ticketLookupService.findTicket(ticketLookupRequest));
        } catch (IllegalArgumentException ex) {
            model.addAttribute("formError", ex.getMessage());
        }
        return "staff/tickets/lookup";
    }

    @PostMapping("/tickets/cancel")
    public String cancel(
            @Valid @ModelAttribute TicketLookupRequest ticketLookupRequest,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "tickets/lookup";
        }

        try {
            model.addAttribute("ticket", ticketStatusService.cancelByPassenger(ticketLookupRequest));
            model.addAttribute("message", "Đã hủy vé và giải phóng ghế");
        } catch (IllegalArgumentException ex) {
            model.addAttribute("formError", ex.getMessage());
        }
        return "tickets/lookup";
    }

    @PostMapping("/admin/tickets/cancel")
    public String adminCancel(
            @Valid @ModelAttribute TicketLookupRequest ticketLookupRequest,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "admin/tickets/lookup";
        }

        try {
            model.addAttribute("ticket", ticketStatusService.cancelByPassenger(ticketLookupRequest));
            model.addAttribute("message", "Đã hủy vé và giải phóng ghế");
        } catch (IllegalArgumentException ex) {
            model.addAttribute("formError", ex.getMessage());
        }
        return "admin/tickets/lookup";
    }

    @PostMapping("/staff/tickets/cancel")
    public String staffCancel(
            @Valid @ModelAttribute TicketLookupRequest ticketLookupRequest,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "staff/tickets/lookup";
        }

        try {
            model.addAttribute("ticket", ticketStatusService.cancelByPassenger(ticketLookupRequest));
            model.addAttribute("message", "Đã hủy vé và giải phóng ghế");
        } catch (IllegalArgumentException ex) {
            model.addAttribute("formError", ex.getMessage());
        }
        return "staff/tickets/lookup";
    }

    private void prepareLookupForm(Model model) {
        if (!model.containsAttribute("ticketLookupRequest")) {
            model.addAttribute("ticketLookupRequest", new TicketLookupRequest());
        }
    }
}
