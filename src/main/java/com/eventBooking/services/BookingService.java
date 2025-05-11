package com.eventBooking.services;

import com.eventBooking.models.booking.Booking;

import java.io.*;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookingService {
    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);
    private static final String BOOKING_FILE = "data/bookings.txt";
    private final Queue<Booking> bookingQueue = new LinkedList<>();

    public BookingService() {
        loadBookingsToQueue();
    }

    private void loadBookingsToQueue() {
        try (BufferedReader reader = new BufferedReader(new FileReader(BOOKING_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Booking booking = Booking.fromFileString(line);
                bookingQueue.add(booking);
            }
        } catch (IOException e) {
            logger.error("Error loading bookings into queue: {}", e.getMessage());
        }
    }

    private void saveQueueToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKING_FILE))) {
            for (Booking b : bookingQueue) {
                writer.write(b.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            logger.error("Error saving queue to bookings.txt: {}", e.getMessage());
        }
    }

    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookingQueue);
    }

    public List<Booking> getBookingsByUser(String username) {
        List<Booking> userBookings = new ArrayList<>();
        for (Booking b : bookingQueue) {
            if (b.getUsername().equals(username)) {
                userBookings.add(b);
            }
        }
        return userBookings;
    }

    public Booking getBookingById(String bookingId, String username){
        for(Booking b : getBookingsByUser(username)) {
            if (b.getBookingId().equals(bookingId)) {
                return b;
            }
        }
        return null;
    }

    public boolean createBooking(Booking booking) {
        bookingQueue.add(booking); // Enqueue the new booking
        saveQueueToFile();
        return true;
    }

    public void updateBookingStatus(String username, String providerName, String newStatus) {
        for (Booking b : bookingQueue) {
            if (b.getUsername().equals(username) && b.getProviderName().equals(providerName)) {
                b.setStatus(newStatus);
            }
        }
        saveQueueToFile();
    }

    public void completeBooking(String bookingId) {
        for (Booking b : bookingQueue) {
            if (b.getBookingId().equals(bookingId)) {
                b.setStatus("completed");
            }
        }
        saveQueueToFile();
    }

    public void deleteBooking(String username, String bookingId) {
        Iterator<Booking> iterator = bookingQueue.iterator();
        boolean removed = false;

        while (iterator.hasNext()) {
            Booking b = iterator.next();
            if (b.getUsername().equals(username) && b.getBookingId().equals(bookingId)) {
                iterator.remove(); // Dequeue the matched booking
                removed = true;
                break;
            }
        }

        if (removed) {
            saveQueueToFile();
        }
    }


}
