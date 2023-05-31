package com.booking.restaurant;

import com.booking.restaurant.handler.BookingRequestHandler;
import com.booking.restaurant.model.Booking;
import com.booking.restaurant.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.muserver.ContentTypes;
import io.muserver.Method;
import io.muserver.MuRequest;
import io.muserver.MuResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
class RestaurantApplicationTests {

	private BookingRequestHandler bookingRequestHandler;
	private BookingService bookingService;
	private ObjectMapper objectMapper;

	@BeforeEach
	public void setup() {
		bookingService = mock(BookingService.class);
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		bookingRequestHandler = new BookingRequestHandler(bookingService);
	}

	@Test
	public void testHandleGet() throws Exception {
		LocalDateTime date = LocalDateTime.now();
		List<Booking> bookings = new ArrayList<>();
		Booking booking1 = new Booking("John Doe", 4, date);
		Booking booking2 = new Booking("Jane Smith", 2, date);
		bookings.add(booking1);
		bookings.add(booking2);

		when(bookingService.getBookingsByDate(date)).thenReturn(bookings);

		MuRequest request = mock(MuRequest.class);		MuResponse response = mock(MuResponse.class);

		when(request.method()).thenReturn(Method.GET);
//        when(request.query("date")).thenReturn(date.toString());

		assertTrue(bookingRequestHandler.handle(request, response));

		verify(response).contentType(ContentTypes.APPLICATION_JSON);
		verify(response).write(objectMapper.writeValueAsString(bookings));
	}

	@Test
	public void testHandlePost() throws Exception {
		Booking booking = new Booking("John Doe", 4, LocalDateTime.now());
		Booking createdBooking = new Booking("John Doe", 4, LocalDateTime.now());
		createdBooking.setId(1L);

		when(bookingService.createBooking(booking)).thenReturn(createdBooking);

		MuRequest request = mock(MuRequest.class);
		MuResponse response = mock(MuResponse.class);

		when(request.method()).thenReturn(Method.POST);
		when(request.readBodyAsString()).thenReturn(objectMapper.writeValueAsString(booking));

		assertTrue(bookingRequestHandler.handle(request, response));

		verify(response).contentType(ContentTypes.APPLICATION_JSON);
		verify(response).write(objectMapper.writeValueAsString(createdBooking));
	}

}
