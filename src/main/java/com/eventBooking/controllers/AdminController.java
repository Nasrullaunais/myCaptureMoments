package com.eventBooking.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import com.eventBooking.models.Booking;
import com.eventBooking.services.BookingService;
import com.eventBooking.models.Photographer;
import com.eventBooking.services.PhotographerService;



@Controller
@RequestMapping("/admin")
public class AdminController {
    private final BookingService bookingService = new BookingService();
    private final PhotographerService photographerService = new PhotographerService();

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        if (!isAdmin(session)) return "login";

        List<Booking> bookings = bookingService.getAllBookings();
        List<Photographer> photographers = photographerService.getAllPhotographers();
        model.addAttribute("bookings", bookings);
        model.addAttribute("photographers", photographers);

        return "admin-dashboard";
    }

    @PostMapping("/confirm")
    public String confirm(@RequestParam String providerName,@RequestParam String username, HttpSession session) {
        if (!isAdmin(session)) return "login";
        bookingService.updateBookingStatus(username, providerName, "confirmed");
        return "admin-dashboard";
    }

    @PostMapping("/add-provider")
    public String addProvider(@RequestParam String providerName, @RequestParam String speciality, @RequestParam int rating, HttpSession session) {
        if (isAdmin(session)) return "ogin";

        Photographer photographer = new Photographer(providerName, speciality, rating);
        photographerService.addPhotographer(photographer);
        return "admin-dashboard";
    }

    @PostMapping("/remove-provider")
    public String removeProvider(@RequestParam String providerName, HttpSession session) {
        if (isAdmin(session)) return "login";

        photographerService.removePhotographer(providerName);
        return "admin-dashboard";
    }





    private boolean isAdmin(HttpSession session) {
        String role = (String) session.getAttribute("role");
        return role != null && role.equalsIgnoreCase("admin");
    }


}