package com.example.busticket.controller;

import com.example.busticket.service.TripService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminTripController {

    private final TripService tripService;

    public AdminTripController(TripService tripService) {
        this.tripService = tripService;
    }

    @GetMapping("/admin/trips")
    public String list(Model model) {
        model.addAttribute("trips", tripService.findAll());
        return "admin/trips/list";
    }
}
