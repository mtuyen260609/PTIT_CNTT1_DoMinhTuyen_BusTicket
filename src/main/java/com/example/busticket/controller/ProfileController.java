package com.example.busticket.controller;

import com.example.busticket.dto.ProfileUpdateRequest;
import com.example.busticket.entity.User;
import com.example.busticket.service.ProfileService;
import com.example.busticket.service.TicketStatusService;
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
public class ProfileController {

    private final ProfileService profileService;
    private final TicketStatusService ticketStatusService;

    public ProfileController(ProfileService profileService, TicketStatusService ticketStatusService) {
        this.profileService = profileService;
        this.ticketStatusService = ticketStatusService;
    }

    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        model.addAttribute("profile", profileService.getProfile(userId));
        model.addAttribute("tickets", profileService.getUserTickets(userId));
        return "profile/index";
    }

    @GetMapping("/profile/edit")
    public String editProfile(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        model.addAttribute("profileUpdateRequest", profileService.getUpdateRequest(userId));
        return "profile/edit";
    }

    @PostMapping("/profile/edit")
    public String updateProfile(
            @Valid @ModelAttribute ProfileUpdateRequest profileUpdateRequest,
            BindingResult bindingResult,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        Long userId = (Long) session.getAttribute("userId");

        if (bindingResult.hasErrors()) {
            return "profile/edit";
        }

        User user = profileService.updateProfile(userId, profileUpdateRequest);
        session.setAttribute("fullName", user.getProfile().getFullName());

        redirectAttributes.addFlashAttribute("message", "\u0110\u00e3 c\u1eadp nh\u1eadt h\u1ed3 s\u01a1");
        return "redirect:/profile";
    }

    @PostMapping("/profile/tickets/{ticketId}/cancel")
    public String cancelTicket(
            @PathVariable Long ticketId,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        Long userId = (Long) session.getAttribute("userId");

        try {
            ticketStatusService.cancelByUser(ticketId, userId);
            redirectAttributes.addFlashAttribute("message", "Da huy ve thanh cong");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("formError", ex.getMessage());
        }
        return "redirect:/profile";
    }
}
