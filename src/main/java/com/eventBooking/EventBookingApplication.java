package com.eventBooking;

import com.eventBooking.models.Photographer;
import com.eventBooking.models.User;
import com.eventBooking.services.PhotographerService;
import com.eventBooking.services.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class EventBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventBookingApplication.class, args);
	}


}
