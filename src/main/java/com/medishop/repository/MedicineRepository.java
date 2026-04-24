package com.medishop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medishop.OnlineMedicineShop.model.Medicine;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    List<Medicine> findByNameContainingIgnoreCase(String name);
    List<Medicine> findByCategory(String category);
}