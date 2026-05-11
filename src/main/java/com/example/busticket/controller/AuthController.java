package com.example.busticket.controller;

import com.example.busticket.dto.LoginRequest;
import com.example.busticket.dto.RegisterRequest;
import com.example.busticket.entity.Role;
import com.example.busticket.entity.User;
import com.example.busticket.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/register")
    public String registerForm(Model model, HttpSession session) {
        if (session.getAttribute("userId") != null) {
            return "redirect:/";
        }

        model.addAttribute("registerRequest", new RegisterRequest());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute RegisterRequest registerRequest,
            BindingResult bindingResult,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        try {
            authService.registerPassenger(registerRequest);
            redirectAttributes.addFlashAttribute("message", "\u0110\u0103ng k\u00fd th\u00e0nh c\u00f4ng. Vui l\u00f2ng \u0111\u0103ng nh\u1eadp.");
            return "redirect:/login";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("formError", ex.getMessage());
            return "auth/register";
        }
    }

    @GetMapping("/login")
    public String loginForm(Model model, HttpSession session) {
        if (session.getAttribute("userId") != null) {
            return "redirect:/";
        }

        model.addAttribute("loginRequest", new LoginRequest());
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(
            @Valid @ModelAttribute LoginRequest loginRequest,
            BindingResult bindingResult,
            HttpSession session,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "auth/login";
        }

        return authService.authenticate(loginRequest.getUsername(), loginRequest.getPassword())
                .map(user -> {
                    saveLoginSession(session, user);
                    return redirectAfterLogin(user);
                })
                .orElseGet(() -> {
                    loginRequest.setPassword(null);
                    model.addAttribute("formError", "Tên đăng nhập hoặc mật khẩu không đúng");
                    return "auth/login";
                });
    }

    @PostMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("message", "Đã đăng xuất");
        return "redirect:/login";
    }

    private void saveLoginSession(HttpSession session, User user) {
        session.setAttribute("userId", user.getId());
        session.setAttribute("username", user.getUsername());
        session.setAttribute("role", user.getRole().name());
        session.setAttribute("fullName", user.getProfile().getFullName());
    }

    private String redirectAfterLogin(User user) {
        if (user.getRole() == Role.ADMIN) {
            return "redirect:/admin";
        }
        if (user.getRole() == Role.STAFF) {
            return "redirect:/staff";
        }
        return "redirect:/";
    }
}
