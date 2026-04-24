package com.medishop.controller;

// Yeh hona chahiye tumhari files mein:
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.medishop.OnlineMedicineShop.model.Medicine;
import com.medishop.OnlineMedicineShop.model.User;
import com.medishop.repository.MedicineRepository;
import com.medishop.repository.OrderRepository;
import com.medishop.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired MedicineRepository medicineRepository;
    @Autowired OrderRepository orderRepository;
    @Autowired UserRepository userRepository;

    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            System.out.println("DEBUG: Session mein user nahi hai");
            return false;
        }
        System.out.println("DEBUG: Email = " + user.getEmail());
        System.out.println("DEBUG: Role = " + user.getRole());
        return "ADMIN".equals(user.getRole());
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";

        model.addAttribute("totalMedicines", medicineRepository.count());
        model.addAttribute("totalOrders", orderRepository.count());
        model.addAttribute("totalUsers", userRepository.count());
        model.addAttribute("orders", orderRepository.findAll());

        List<Medicine> lowStock = medicineRepository.findAll()
                .stream()
                .filter(m -> m.getStock() < 10)
                .collect(Collectors.toList());
        model.addAttribute("lowStock", lowStock);

        return "admin/dashboard";
    }

    @GetMapping("/medicines")
    public String medicines(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        model.addAttribute("medicines", medicineRepository.findAll());
        return "admin/medicines";
    }

    @PostMapping("/medicines/add")
    public String addMedicine(@RequestParam String name,
                              @RequestParam String brand,
                              @RequestParam String category,
                              @RequestParam double price,
                              @RequestParam int stock,
                              @RequestParam(defaultValue = "false") boolean requiresPrescription,
                              @RequestParam String description,
                              HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";

        Medicine m = new Medicine();
        m.setName(name);
        m.setBrand(brand);
        m.setCategory(category);
        m.setPrice(price);
        m.setStock(stock);
        m.setRequiresPrescription(requiresPrescription);
        m.setDescription(description);
        medicineRepository.save(m);
        return "redirect:/admin/medicines";
    }

    @GetMapping("/medicines/delete/{id}")
    public String deleteMedicine(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        medicineRepository.deleteById(id);
        return "redirect:/admin/medicines";
    }

    @PostMapping("/orders/status")
    public String updateStatus(@RequestParam Long orderId,
                               @RequestParam String status,
                               HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        orderRepository.findById(orderId).ifPresent(order -> {
            order.setStatus(status);
            orderRepository.save(order);
        });
        return "redirect:/admin/dashboard";
    }
}