package com.medishop.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.medishop.OnlineMedicineShop.model.Cart;
import com.medishop.OnlineMedicineShop.model.Order;
import com.medishop.OnlineMedicineShop.model.OrderItem;
import com.medishop.OnlineMedicineShop.model.User;
import com.medishop.repository.CartRepository;
import com.medishop.repository.OrderRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class CheckoutController {

    @Autowired CartRepository cartRepository;
    @Autowired OrderRepository orderRepository;

    @GetMapping("/checkout")
    public String checkoutPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        List<Cart> cartItems = cartRepository.findByUserId(user.getId());
        double total = cartItems.stream()
                .mapToDouble(c -> c.getMedicine().getPrice() * c.getQuantity())
                .sum();

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        return "checkout";
    }

    @PostMapping("/checkout/place")
    public String placeOrder(@RequestParam String address,
                             HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        List<Cart> cartItems = cartRepository.findByUserId(user.getId());
        if (cartItems.isEmpty()) return "redirect:/cart";

        double total = cartItems.stream()
                .mapToDouble(c -> c.getMedicine().getPrice() * c.getQuantity())
                .sum();

        Order order = new Order();
        order.setUserId(user.getId());
        order.setTotalAmount(total);
        order.setAddress(address);
        order.setStatus("PENDING");

        List<OrderItem> items = new ArrayList<>();
        for (Cart cart : cartItems) {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setMedicineId(cart.getMedicineId());
            item.setQuantity(cart.getQuantity());
            item.setPrice(cart.getMedicine().getPrice());
            items.add(item);
        }
        order.setItems(items);
        orderRepository.save(order);

        cartRepository.deleteByUserId(user.getId());

        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String myOrders(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        model.addAttribute("orders", orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId()));
        return "orders";
    }
}