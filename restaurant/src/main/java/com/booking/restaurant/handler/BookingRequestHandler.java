package com.booking.restaurant.handler;

import com.booking.restaurant.model.Booking;
import com.booking.restaurant.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.muserver.*;
import java.time.LocalDateTime;
import java.util.List;

public class BookingRequestHandler implements MuHandler {

    private final BookingService bookingService;
    private final ObjectMapper objectMapper;

    public BookingRequestHandler(BookingService bookingService) {
        this.bookingService = bookingService;
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public boolean handle(MuRequest request, MuResponse response) throws Exception {
        String method = request.method().toString();

        if (method.equals("GET")) {
            return handleGet(request, response);
        } else if (method.equals("POST")) {
            return handlePost(request, response);
        }

        return false;
    }

    private boolean handleGet(MuRequest request, MuResponse response) throws Exception {
        String dateParam = request.query().get("date");
        LocalDateTime date = LocalDateTime.parse(dateParam);
        List<Booking> bookings = bookingService.getBookingsByDate(date);

        String jsonResponse = objectMapper.writeValueAsString(bookings);

        response.contentType(ContentTypes.APPLICATION_JSON);
        response.write(jsonResponse);

        return true;
    }

    private boolean handlePost(MuRequest request, MuResponse response) throws Exception {
        String requestBodyString = request.readBodyAsString();
        Booking booking = objectMapper.readValue(requestBodyString, Booking.class);
        Booking createdBooking = bookingService.createBooking(booking);

        String jsonResponse = objectMapper.writeValueAsString(createdBooking);

        response.contentType(ContentTypes.APPLICATION_JSON);
        response.write(jsonResponse);

        return true;
    }

}
