package com.medishop.controller;

// Yeh hona chahiye tumhari files mein:
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
public class AuthController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @GetMapping("/forgot-password")
public String forgotPasswordPage() {
    return "forgot-password";
}

@PostMapping("/forgot-password")
public String forgotPassword(@RequestParam String email, Model model) {
    var userOpt = userRepository.findByEmail(email);
    if (userOpt.isEmpty()) {
        model.addAttribute("error", "No account found with this email!");
        return "forgot-password";
    }
    // Simple reset — set password to "reset123"
    userOpt.get().setPassword("reset123");
    userRepository.save(userOpt.get());
    model.addAttribute("success", "Password reset to: reset123 — Please login and change it!");
    return "forgot-password";
}

    @PostMapping("/register")
    public String registerUser(@RequestParam String name,
                               @RequestParam String email,
                               @RequestParam String password,
                               @RequestParam String phone,
                               Model model) {
        if (userRepository.findByEmail(email).isPresent()) {
            model.addAttribute("error", "Email already registered!");
            return "register";
        }
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhone(phone);
        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String email,
                            @RequestParam String password,
                            HttpSession session,
                            Model model) {
        var userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            User freshUser = userRepository.findById(userOpt.get().getId()).get();
            session.setAttribute("user", freshUser);

            System.out.println("DEBUG: Login - Email = " + freshUser.getEmail());
            System.out.println("DEBUG: Login - Role = " + freshUser.getRole());

            if ("ADMIN".equals(freshUser.getRole())) {
                return "redirect:/admin/dashboard";
            }
            return "redirect:/shop";
        }
        model.addAttribute("error", "Invalid email or password!");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}