package com.eventBooking.models.booking;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter; // Import the DateTimeFormatter class

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Booking {
    private static final Logger logger = LoggerFactory.getLogger(Booking.class);

    private final String bookingId;
    private final String username;
    private String providerName;
    private String eventDate;
    private String location;
    private String event;
    private String formattedDate;

    private String status; // pending, confirmed, completed

    public Booking(String username, String providerName, String eventDate, String location, String event, String status) {
        this.bookingId = UUID.randomUUID().toString();
        this.username = username;
        this.providerName = providerName;
        this.eventDate = eventDate;
        this.location = location;
        this.event = event;
        this.status = status;
        setFormattedDate();
    }

    public void setFormattedDate() {
        String dateString = eventDate;
        // Parse the string to LocalDate
        LocalDate date = LocalDate.parse(dateString);

        // Define the output format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy");

        // Format the date
        String formattedDate = date.format(formatter);

        logger.debug("Formatted date: {}", formattedDate);
        this.formattedDate = formattedDate;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public String getBookingId() {
        return bookingId;
    }
    public String getUsername() { return username; }
    public String getProviderName() { return providerName; }
    public String getEventDate() { return eventDate; }
    public String getLocation() { return location; }
    public String getType() { return event; }
    public String getStatus() { return status; }

    public void setStatus(String status) {
        this.status = status;
    }

    public String toFileString() {
        return bookingId + "," + username + "," + providerName + "," + eventDate + "," + location + "," + event + "," + status;
    }

    public static Booking fromFileString(String line) {
        String[] parts = line.split(",");
        return new Booking(parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]);
    }


}
