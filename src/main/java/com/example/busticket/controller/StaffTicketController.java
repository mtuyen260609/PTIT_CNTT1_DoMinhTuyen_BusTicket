package com.example.busticket.controller;

import com.example.busticket.service.TicketStatusService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class StaffTicketController {

    private final TicketStatusService ticketStatusService;

    public StaffTicketController(TicketStatusService ticketStatusService) {
        this.ticketStatusService = ticketStatusService;
    }

    @GetMapping("/staff")
    public String dashboard(Model model) {
        model.addAttribute("pendingTickets", ticketStatusService.findPendingTickets());
        model.addAttribute("expiredTickets", ticketStatusService.findExpiredPendingTickets());
        return "staff/index";
    }

    @PostMapping("/staff/tickets/{id}/confirm")
    public String confirmPaid(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            ticketStatusService.confirmPaid(id);
            redirectAttributes.addFlashAttribute("message", "Đã xác nhận vé");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("formError", ex.getMessage());
        }
        return "redirect:/staff";
    }

    @PostMapping("/staff/tickets/cancel-expired")
    public String cancelExpired(RedirectAttributes redirectAttributes) {
        int count = ticketStatusService.cancelExpiredPendingTickets();
        redirectAttributes.addFlashAttribute("message", "Đã hủy " + count + " vé quá hạn");
        return "redirect:/staff";
    }
}
