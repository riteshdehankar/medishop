package com.medishop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.medishop.OnlineMedicineShop.model.Medicine;
import com.medishop.repository.MedicineRepository;

@RestController
public class SearchApiController {

    @Autowired
    MedicineRepository medicineRepository;

    @GetMapping("/api/search")
    public List<Medicine> search(@RequestParam String q) {
        return medicineRepository.findByNameContainingIgnoreCase(q);
    }
}