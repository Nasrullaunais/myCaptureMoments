package com.eventBooking.controllers;

import com.eventBooking.models.booking.Booking;
import com.eventBooking.models.review.Review;
import com.eventBooking.services.BookingService;
import com.eventBooking.services.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService = new ReviewService();
    private final BookingService bookingService = new BookingService();

    @GetMapping("/create/{bookingId}")
    public String showReviewForm(@PathVariable String bookingId, Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        // Find the booking
        List<Booking> userBookings = bookingService.getBookingsByUser(username);
        Booking booking = userBookings.stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst()
                .orElse(null);

        if (booking == null) {
            return "redirect:/bookings/manage";
        }

        // Check if booking is completed
        if (!"completed".equals(booking.getStatus())) {
            return "redirect:/bookings/manage";
        }

        // Check if user has already reviewed this booking
        if (reviewService.hasUserReviewedBooking(username, bookingId)) {
            return "redirect:/bookings/manage";
        }

        model.addAttribute("booking", booking);
        return "review/review-form";
    }

    @PostMapping("/submit")
    public String submitReview(@RequestParam String bookingId,
                              @RequestParam String providerName,
                              @RequestParam int rating,
                              @RequestParam String comment,
                              HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        // Create and save the review
        Review review = new Review(bookingId, username, providerName, rating, comment);
        reviewService.createReview(review);

        return "redirect:/bookings/manage?reviewSubmitted=true";
    }

    @GetMapping("/provider/{providerName}")
    public String viewProviderReviews(@PathVariable String providerName, Model model, HttpSession session) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }

        List<Review> reviews = reviewService.getReviewsByProvider(providerName);
        double averageRating = reviewService.getAverageRatingForProvider(providerName);

        model.addAttribute("providerName", providerName);
        model.addAttribute("reviews", reviews);
        model.addAttribute("averageRating", averageRating);

        return "review/provider-reviews";
    }
}