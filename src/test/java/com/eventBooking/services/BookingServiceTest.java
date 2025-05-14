//package com.eventBooking.services;
//
//import com.eventBooking.models.booking.Booking;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.io.TempDir;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.util.Queue;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class BookingServiceTest {
//
//    private BookingService bookingService;
//    private static final String TEST_USERNAME = "testuser";
//    private static final String TEST_PROVIDER = "testprovider";
//    private static final String TEST_DATE = "2023-12-31";
//    private static final String TEST_LOCATION = "Test Location";
//    private static final String TEST_EVENT = "Wedding";
//    private static final String TEST_STATUS = "pending";
//
//    @BeforeEach
//    void setUp() {
//        // Create a new BookingService instance for each test
//        bookingService = new BookingService();
//    }
//
//    @Test
//    void testCreateBookingAddsToQueue() {
//        // Create a new booking
//        Booking booking = new Booking(TEST_USERNAME, TEST_PROVIDER, TEST_DATE,
//                                     TEST_LOCATION, TEST_EVENT, TEST_STATUS);
//
//        // Add the booking
//        bookingService.createBooking(booking);
//
//        // Verify it was added to the queue
//        Queue<Booking> queue = bookingService.getBookingQueue();
//        assertEquals(1, queue.size());
//        assertTrue(queue.contains(booking));
//
//        // Verify peek returns the booking
//        Booking peekedBooking = bookingService.peekNextBooking();
//        assertNotNull(peekedBooking);
//        assertEquals(booking.getBookingId(), peekedBooking.getBookingId());
//    }
//
//    @Test
//    void testUpdateBookingStatus() {
//        // Create and add a booking
//        Booking booking = new Booking(TEST_USERNAME, TEST_PROVIDER, TEST_DATE,
//                                     TEST_LOCATION, TEST_EVENT, TEST_STATUS);
//        bookingService.createBooking(booking);
//
//        // Update the status
//        String newStatus = "confirmed";
//        bookingService.updateBookingStatus(TEST_USERNAME, TEST_PROVIDER, newStatus);
//
//        // Verify the status was updated in the queue
//        Booking updatedBooking = bookingService.peekNextBooking();
//        assertNotNull(updatedBooking);
//        assertEquals(newStatus, updatedBooking.getStatus());
//    }
//
//    @Test
//    void testCompleteBooking() {
//        // Create and add a booking
//        Booking booking = new Booking(TEST_USERNAME, TEST_PROVIDER, TEST_DATE,
//                                     TEST_LOCATION, TEST_EVENT, TEST_STATUS);
//        bookingService.createBooking(booking);
//
//        // Complete the booking
//        bookingService.completeBooking(booking.getBookingId());
//
//        // Verify the status was updated to completed in the queue
//        Booking completedBooking = bookingService.peekNextBooking();
//        assertNotNull(completedBooking);
//        assertEquals("completed", completedBooking.getStatus());
//    }
//
//    @Test
//    void testDeleteBooking() {
//        // Create and add a booking
//        Booking booking = new Booking(TEST_USERNAME, TEST_PROVIDER, TEST_DATE,
//                                     TEST_LOCATION, TEST_EVENT, TEST_STATUS);
//        bookingService.createBooking(booking);
//
//        // Delete the booking
//        bookingService.deleteBooking(TEST_USERNAME, booking.getBookingId());
//
//        // Verify it was removed from the queue
//        Queue<Booking> queue = bookingService.getBookingQueue();
//        assertEquals(0, queue.size());
//        assertNull(bookingService.peekNextBooking());
//    }
//
//    @Test
//    void testPollNextBooking() {
//        // Create and add a booking
//        Booking booking = new Booking(TEST_USERNAME, TEST_PROVIDER, TEST_DATE,
//                                     TEST_LOCATION, TEST_EVENT, TEST_STATUS);
//        bookingService.createBooking(booking);
//
//        // Poll the booking
//        Booking polledBooking = bookingService.pollNextBooking();
//
//        // Verify the correct booking was returned and removed from the queue
//        assertNotNull(polledBooking);
//        assertEquals(booking.getBookingId(), polledBooking.getBookingId());
//        assertEquals(0, bookingService.getQueueSize());
//    }
//
//    @Test
//    void testGetQueueSize() {
//        // Initially the queue should be empty (or contain existing bookings)
//        int initialSize = bookingService.getQueueSize();
//
//        // Add a booking
//        Booking booking = new Booking(TEST_USERNAME, TEST_PROVIDER, TEST_DATE,
//                                     TEST_LOCATION, TEST_EVENT, TEST_STATUS);
//        bookingService.createBooking(booking);
//
//        // Verify the size increased by 1
//        assertEquals(initialSize + 1, bookingService.getQueueSize());
//    }
//}