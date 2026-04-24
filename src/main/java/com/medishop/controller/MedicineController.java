package com.medishop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.medishop.repository.MedicineRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class MedicineController {

    @Autowired
    MedicineRepository medicineRepository;

    @GetMapping("/medicine/{id}")
    public String medicineDetail(@PathVariable Long id, Model model, HttpSession session) {
        if (session.getAttribute("user") == null) return "redirect:/login";
        medicineRepository.findById(id).ifPresent(m -> model.addAttribute("medicine", m));
        return "medicine-detail";
    }
}