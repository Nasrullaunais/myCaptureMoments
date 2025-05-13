package com.eventBooking.services;

import com.eventBooking.models.review.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReviewService {
    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);
    private static final String REVIEW_FILE = "data/reviews.txt";

    public List<Review> getAllReviews() {
        List<Review> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(REVIEW_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(Review.fromFileString(line));
            }
        } catch (IOException e) {
            logger.error("Error reading reviews.txt: {}", e.getMessage());
        }
        return list;
    }

    public List<Review> getReviewsByProvider(String providerName) {
        List<Review> providerReviews = new ArrayList<>();
        for (Review review : getAllReviews()) {
            if (review.getProviderName().equals(providerName)) {
                providerReviews.add(review);
            }
        }
        return providerReviews;
    }

    public List<Review> getReviewsByUser(String username) {
        List<Review> userReviews = new ArrayList<>();
        for (Review review : getAllReviews()) {
            if (review.getUsername().equals(username)) {
                userReviews.add(review);
            }
        }
        return userReviews;
    }

    public Review getReviewByBookingId(String bookingId) {
        for (Review review : getAllReviews()) {
            if (review.getBookingId().equals(bookingId)) {
                return review;
            }
        }
        return null;
    }

    public boolean hasUserReviewedBooking(String username, String bookingId) {
        return getReviewByBookingId(bookingId) != null;
    }

    public boolean createReview(Review review) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(REVIEW_FILE, true))) {
            writer.write(review.toFileString());
            writer.newLine();
            return true;
        } catch (IOException e) {
            logger.error("Error writing to reviews.txt: {}", e.getMessage());
            return false;
        }
    }

    public boolean updateReview(String reviewId, Review updatedReview) {
        List<Review> reviews = getAllReviews();
        List<Review> updatedList = new ArrayList<>();
        boolean updated = false;

        for (Review review : reviews) {
            if (review.getReviewId().equals(reviewId)) {
                updatedList.add(updatedReview);
                updated = true;
            } else {
                updatedList.add(review);
            }
        }

        if (updated) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(REVIEW_FILE))) {
                for (Review review : updatedList) {
                    writer.write(review.toFileString());
                    writer.newLine();
                }
                return true;
            } catch (IOException e) {
                logger.error("Error updating reviews.txt: {}", e.getMessage());
                return false;
            }
        }

        return false;
    }

    public boolean deleteReview(String reviewId) {
        List<Review> reviews = getAllReviews();
        List<Review> updatedList = new ArrayList<>();
        boolean deleted = false;

        for (Review review : reviews) {
            if (!review.getReviewId().equals(reviewId)) {
                updatedList.add(review);
            } else {
                deleted = true;
            }
        }

        if (deleted) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(REVIEW_FILE))) {
                for (Review review : updatedList) {
                    writer.write(review.toFileString());
                    writer.newLine();
                }
                return true;
            } catch (IOException e) {
                logger.error("Error updating reviews.txt: {}", e.getMessage());
                return false;
            }
        }

        return false;
    }

    public double getAverageRatingForProvider(String providerName) {
        List<Review> reviews = getReviewsByProvider(providerName);
        if (reviews.isEmpty()) {
            return 0.0;
        }

        int sum = 0;
        for (Review review : reviews) {
            sum += review.getRating();
        }

        return (double) sum / reviews.size();
    }

    public Map<String, Double> getAllProviderAverageRatings() {
        Map<String, Double> ratings = new HashMap<>();
        Map<String, Integer> counts = new HashMap<>();
        Map<String, Integer> sums = new HashMap<>();

        for (Review review : getAllReviews()) {
            String providerName = review.getProviderName();
            int rating = review.getRating();

            counts.put(providerName, counts.getOrDefault(providerName, 0) + 1);
            sums.put(providerName, sums.getOrDefault(providerName, 0) + rating);
        }

        for (String providerName : counts.keySet()) {
            ratings.put(providerName, (double) sums.get(providerName) / counts.get(providerName));
        }

        return ratings;
    }
}
