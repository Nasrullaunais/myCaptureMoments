package com.eventBooking.services;

import com.eventBooking.models.booking.Booking;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookingService {
    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);
    private static final String BOOKING_FILE = "data/bookings.txt";

    public List<Booking> getAllBookings() {
        List<Booking> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(BOOKING_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(Booking.fromFileString(line));
            }
        } catch (IOException e) {
            logger.error("Error reading bookings.txt: {}", e.getMessage());
        }
        return list;
    }

    public List<Booking> getBookingsByUser(String username) {
        List<Booking> userBookings = new ArrayList<>();
        for (Booking b : getAllBookings()) {
            if (b.getUsername().equals(username)) {
                userBookings.add(b);
            }
        }
        return userBookings;
    }

    public boolean createBooking(Booking booking) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKING_FILE, true))) {
            writer.write(booking.toFileString());
            writer.newLine();
            return true;
        } catch (IOException e) {
            logger.error("Error writing to bookings.txt: {}", e.getMessage());
            return false;
        }
    }

    public void updateBookingStatus(String username, String providerName, String newStatus) {
        List<Booking> updatedList = new ArrayList<>();
        boolean updated = false;
        for (Booking b : getAllBookings()) {
            if (b.getUsername().equals(username) && b.getProviderName().equals(providerName)) {
                b.setStatus(newStatus);
                updated = true;
            }
            updatedList.add(b);
        }

        if (updated) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKING_FILE))) {
                for (Booking b : updatedList) {
                    writer.write(b.toFileString());
                    writer.newLine();
                }
            } catch (IOException e) {
                logger.error("Error updating bookings.txt: {}", e.getMessage());
            }
        }
    }

    public void completeBooking(String bookingId) {
        List<Booking> updatedList = new ArrayList<>();
        boolean updated = false;
        for (Booking b : getAllBookings()) {
            if (b.getBookingId().equals(bookingId)) {
                b.setStatus("completed");
                updated = true;
            }
            updatedList.add(b);
        }

        if (updated) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKING_FILE))) {
                for (Booking b : updatedList) {
                    writer.write(b.toFileString());
                    writer.newLine();
                }
            } catch (IOException e) {
                logger.error("Error updating bookings.txt: {}", e.getMessage());
            }
        }
    }

    public void deleteBooking(String username, String bookingId) {
        List<Booking> updatedList = new ArrayList<>();
        boolean removed = false;
        for (Booking b : getAllBookings()) {
            if (b.getUsername().equals(username) && b.getBookingId().equals(bookingId)) {
                removed = true;
                continue;
            }
            updatedList.add(b);
        }

        if (removed) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKING_FILE))) {
                for (Booking b : updatedList) {
                    writer.write(b.toFileString());
                    writer.newLine();
                }
            } catch (IOException e) {
                logger.error("Error deleting booking from bookings.txt: {}", e.getMessage());
            }
        }

    }
}
