package com.eventBooking.models.provider;

import com.eventBooking.services.ReviewService;

public class Provider {
    protected String name;
    protected String specialty;
    protected int rating; // Default rating if no reviews exist

    private static ReviewService reviewService;

    static {
        reviewService = new ReviewService();
    }

    public Provider(String name, String specialty, int rating) {
        this.name = name;
        this.specialty = specialty;
        this.rating = rating;
    }

    public String getName() { return name; }
    public String getSpecialty() { return specialty; }

    public int getRating() { 
        double avgRating = reviewService.getAverageRatingForProvider(name);
        return avgRating > 0 ? (int)Math.round(avgRating) : rating; 
    }

    // Get the admin-set default rating
    public int getDefaultRating() {
        return rating;
    }

    public String toFileString() {
        return name + "," + specialty + "," + rating;
    }

    public static Provider fromFileString(String line) {
        String[] parts = line.split(",");
        return new Provider(parts[0], parts[1], Integer.parseInt(parts[2]));
    }
}
