package com.example.busticket.controller;

import com.example.busticket.dto.BusRequest;
import com.example.busticket.entity.BusTypeOption;
import com.example.busticket.service.BusService;
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
public class AdminBusController {

    private final BusService busService;

    public AdminBusController(BusService busService) {
        this.busService = busService;
    }

    @GetMapping("/admin/buses")
    public String list(Model model) {
        model.addAttribute("buses", busService.findAll());
        return "admin/buses/list";
    }

    @GetMapping("/admin/buses/new")
    public String createForm(Model model) {
        model.addAttribute("busRequest", new BusRequest());
        addBusTypes(model);
        return "admin/buses/new";
    }

    @PostMapping("/admin/buses")
    public String create(
            @Valid @ModelAttribute BusRequest busRequest,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            addBusTypes(model);
            return "admin/buses/new";
        }

        try {
            busService.create(busRequest);
            redirectAttributes.addFlashAttribute("message", "Đã thêm xe");
            return "redirect:/admin/buses";
        } catch (IllegalArgumentException ex) {
            addBusTypes(model);
            model.addAttribute("formError", ex.getMessage());
            return "admin/buses/new";
        }
    }

    @GetMapping("/admin/buses/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("busRequest", busService.findRequestById(id));
        model.addAttribute("busId", id);
        addBusTypes(model);
        return "admin/buses/edit";
    }

    @PostMapping("/admin/buses/{id}/edit")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute BusRequest busRequest,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("busId", id);
            addBusTypes(model);
            return "admin/buses/edit";
        }

        try {
            busService.update(id, busRequest);
            redirectAttributes.addFlashAttribute("message", "Đã cập nhật xe");
            return "redirect:/admin/buses";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("busId", id);
            addBusTypes(model);
            model.addAttribute("formError", ex.getMessage());
            return "admin/buses/edit";
        }
    }

    @PostMapping("/admin/buses/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            busService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Đã xóa xe");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("formError", ex.getMessage());
        }
        return "redirect:/admin/buses";
    }

    private void addBusTypes(Model model) {
        model.addAttribute("busTypes", BusTypeOption.values());
    }
}
