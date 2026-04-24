package com.medishop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.medishop.repository.MedicineRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class ShopController {

    @Autowired
    MedicineRepository medicineRepository;

    @GetMapping("/")
    public String home(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "redirect:/shop";
        }
        return "splash";
    }

    @GetMapping("/shop")
    public String shop(@RequestParam(required = false) String search,
                       @RequestParam(required = false) String category,
                       Model model) {
        var medicines = medicineRepository.findAll();

        if (search != null && !search.isEmpty()) {
            medicines = medicineRepository.findByNameContainingIgnoreCase(search);
        } else if (category != null && !category.isEmpty()) {
            medicines = medicineRepository.findByCategory(category);
        }

        model.addAttribute("medicines", medicines);
        model.addAttribute("search", search);
        model.addAttribute("category", category);
        return "shop";
    }
}