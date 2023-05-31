package com.booking.restaurant;

import com.booking.restaurant.handler.*;
import com.booking.restaurant.service.BookingService;
import io.muserver.MuServer;
import io.muserver.MuServerBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RestaurantApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {

		ApplicationContext ctx = SpringApplication.run(RestaurantApplication.class, args);
		MuServer muServer = ctx.getBean(MuServer.class);
		muServer.activeConnections();
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(RestaurantApplication.class);
	}

	@Bean
	public MuServer muServer(BookingService bookingService) {
		return MuServerBuilder.httpsServer()
				.withHttpPort(8082) // Specify the desired port
				.withHttpsPort(8443) // Specify the desired HTTPS port
				.addHandler(new BookingRequestHandler(bookingService)) // Replace with your custom request handler
				.start();
	}

}
