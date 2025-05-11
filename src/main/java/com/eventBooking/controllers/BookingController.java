package com.eventBooking.controllers;


import com.eventBooking.models.booking.Booking;
import com.eventBooking.models.provider.Provider;
import com.eventBooking.services.ProviderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.eventBooking.services.BookingService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService = new BookingService();
    private final ProviderService providerService = new ProviderService();

    @GetMapping("/new")
    public String showBookingForm(Model model, HttpSession session) {
        if (session.getAttribute("username") == null) {
            return "login";
        }

        List<Provider> providers = providerService.getAllProviders();
        model.addAttribute("providers", providers);
        return "booking";
    }

    @PostMapping("/create")
    public String createBooking(@RequestParam String providerName,
                                @RequestParam String eventDate,
                                @RequestParam String location,
                                @RequestParam String type,
                                Model model,
                                HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "login";
        }

        Booking booking = new Booking(username, providerName, eventDate, location, type, "pending");
        boolean success = bookingService.createBooking(booking);
        return "dashboard";
    }

    @GetMapping("/manage")
    public String viewBookings(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "login";
        }

        List<Booking> bookings = bookingService.getBookingsByUser(username);
        model.addAttribute("bookings", bookings);
        return "manage";
    }


    @GetMapping ("/{bookingId}")
    public String showBooking(@PathVariable String bookingId, Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "login";
        }

        Booking booking = bookingService.getBookingById(bookingId, username);
        if (booking == null) {
            // Optionally add an error message or redirect
            return "errorPage"; // or show a 404/unauthorized page
        }
        model.addAttribute("booking", booking);
        return "bookingDetails";
    }

    @GetMapping("/cancel/{bookingId}")
    public String cancelBooking(@PathVariable String bookingId, HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "login";
        }
        bookingService.deleteBooking(username, bookingId);
        model.addAttribute("message", "Booking cancelled successfully");
        return "manage";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

}
