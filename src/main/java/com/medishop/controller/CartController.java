package com.medishop.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.medishop.OnlineMedicineShop.model.Cart;
import com.medishop.OnlineMedicineShop.model.User;
import com.medishop.repository.CartRepository;
import com.medishop.repository.OrderRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    // ✅ Cancel Order
    @PostMapping("/orders/cancel/{id}")
    public String cancelOrder(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        orderRepository.findById(id).ifPresent(order -> {
            if (order.getUserId().equals(user.getId()) 
                && "PENDING".equals(order.getStatus())) {

                order.setStatus("CANCELLED");
                orderRepository.save(order);
            }
        });

        return "redirect:/orders";
    }

    // ✅ Add to Cart
    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long medicineId,
                           @RequestParam int quantity,
                           HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        if (quantity <= 0) return "redirect:/shop";

        Optional<Cart> existing = cartRepository
                .findByUserIdAndMedicineId(user.getId(), medicineId);

        if (existing.isPresent()) {
            Cart cart = existing.get();
            cart.setQuantity(cart.getQuantity() + quantity);
            cartRepository.save(cart);
        } else {
            Cart cart = new Cart();
            cart.setUserId(user.getId());
            cart.setMedicineId(medicineId);
            cart.setQuantity(quantity);
            cartRepository.save(cart);
        }

        return "redirect:/shop";
    }

    // ✅ View Cart
    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        List<Cart> cartItems = cartRepository.findByUserId(user.getId());

        double total = cartItems.stream()
                .mapToDouble(c -> {
                    if (c.getMedicine() != null) {
                        return c.getMedicine().getPrice() * c.getQuantity();
                    }
                    return 0;
                })
                .sum();

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);

        return "cart";
    }

    // ✅ Remove from Cart (Secure)
    @GetMapping("/cart/remove/{id}")
    public String removeFromCart(@PathVariable Long id, HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        cartRepository.findById(id).ifPresent(cart -> {
            if (cart.getUserId().equals(user.getId())) {
                cartRepository.deleteById(id);
            }
        });

        return "redirect:/cart";
    }
}