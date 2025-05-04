package com.eventBooking.services;

import com.eventBooking.models.Booking;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BookingService {
    private static final String BOOKING_FILE = "data/bookings.txt";

    public List<Booking> getAllBookings() {
        List<Booking> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(BOOKING_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(Booking.fromFileString(line));
            }
        } catch (IOException e) {
            System.out.println("Error reading bookings.txt: " + e.getMessage());
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
            System.out.println("Error writing to bookings.txt: " + e.getMessage());
            return false;
        }
    }

    public boolean updateBookingStatus(String username, String providerName, String newStatus) {
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
                return true;
            } catch (IOException e) {
                System.out.println("Error updating bookings.txt: " + e.getMessage());
            }
        }
        return false;
    }

    public boolean deleteBooking(String username, String bookingId) {
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
                return true;
            } catch (IOException e) {
                System.out.println("Error deleting booking from bookings.txt: " + e.getMessage());
            }
        }

        return false;
    }
}
