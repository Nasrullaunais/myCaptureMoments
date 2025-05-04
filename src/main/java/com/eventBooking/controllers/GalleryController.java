package com.eventBooking.controllers;

import com.eventBooking.models.Photographer;
import com.eventBooking.services.PhotographerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/gallery")
public class GalleryController {
    PhotographerService photographerService = new PhotographerService();
    /**
     * Returns the gallery page with all photographers.
     * @param model Spring MVC model to pass data to the view
     * @param session HTTP session to get the user's username
     * @return the gallery page
     */
    @GetMapping("/")
    public String gallery(Model model, HttpSession session) {
        // Check if the user is logged in
        if (session.getAttribute("username") == null) {
            return "login";
        }
        // Get all photographers from the database
        List<Photographer> providers = photographerService.getAllPhotographers();
        // Pass the list of providers to the view
        model.addAttribute("providers", providers);
        // Return the gallery page
        return "gallery";
    }
}
