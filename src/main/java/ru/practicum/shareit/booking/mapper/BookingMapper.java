package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

public class BookingMapper {

    public static BookingDto toBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setItem(ItemMapper.toItemDateDto(booking.getItem()));
        bookingDto.setBooker(UserMapper.toUserDto(booking.getBooker()));
        bookingDto.setStatus(booking.getStatus());
        return bookingDto;
    }

    public static BookingDateDto toBookingDateDto(Booking booking) {
        BookingDateDto bookingDateDto = new BookingDateDto();
        bookingDateDto.setId(booking.getId());
        bookingDateDto.setStart(booking.getStart());
        bookingDateDto.setEnd(booking.getEnd());
        bookingDateDto.setItemId(booking.getItem().getId());
        bookingDateDto.setBookerId(booking.getBooker().getId());
        return bookingDateDto;
    }

    public static Booking toBooking(BookingDto bookingDto) {
        Booking booking = new Booking();
        booking.setId(bookingDto.getId());
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        bookingDto.setItem(ItemMapper.toItemDateDto(booking.getItem()));
        bookingDto.setBooker(UserMapper.toUserDto(booking.getBooker()));
        booking.setStatus(bookingDto.getStatus());
        return booking;
    }

    public static Booking toBooking(BookingDateDto bookingDateDto) {
        Booking booking = new Booking();
        booking.setId(bookingDateDto.getId());
        booking.setStart(bookingDateDto.getStart());
        booking.setEnd(bookingDateDto.getEnd());
        return booking;
    }
}
