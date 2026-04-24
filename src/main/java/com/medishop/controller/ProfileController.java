package com.medishop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.medishop.OnlineMedicineShop.model.User;
import com.medishop.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class ProfileController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/profile")
    public String profilePage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam String name,
                                @RequestParam String phone,
                                HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        user.setName(name);
        user.setPhone(phone);
        userRepository.save(user);
        session.setAttribute("user", user);
        return "redirect:/profile?success=true";
    }

    @PostMapping("/profile/password")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 HttpSession session,
                                 Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        if (!user.getPassword().equals(currentPassword)) {
            model.addAttribute("user", user);
            model.addAttribute("passError", "Current password is incorrect!");
            return "profile";
        }
        user.setPassword(newPassword);
        userRepository.save(user);
        session.setAttribute("user", user);
        return "redirect:/profile?success=true";
    }
}