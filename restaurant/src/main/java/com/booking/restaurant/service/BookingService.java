package com.booking.restaurant.service;

import com.booking.restaurant.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    Booking createBooking(Booking booking);
    List<Booking> getBookingsByDate(LocalDateTime date);
}
