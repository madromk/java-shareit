package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreatedBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {
    public static CreatedBookingDto toCreatedBookingDto(Booking booking) {
        return new CreatedBookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem().getId()
        );
    }

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                toItem(booking.getItem()),
                toUser(booking.getBooker()),
                booking.getStatus()
        );
    }

    public static Booking toBooking(CreatedBookingDto bookingDto) {
        return new Booking(
                bookingDto.getId(),
                new Item(bookingDto.getItemId(), null, null, null, null,
                        null, null, null, null),
                bookingDto.getStart(),
                bookingDto.getEnd(),
                null,
                null
        );
    }

    private static BookingDto.User toUser(User user) {
        return new BookingDto.User(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    private static BookingDto.Item toItem(Item item) {
        return new BookingDto.Item(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
    }
}
