package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

/**
 * // TODO .
 */
@AllArgsConstructor
@Data
@Builder
public class BookingDto {
    private int id;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingDto.Item item;
    private BookingDto.User booker;
    private BookingStatus status;

    @Data
    @AllArgsConstructor
    @Builder
    public static class Item {
        private int id;
        private String name;
        private String description;
        private boolean available;
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class User {
        private int id;
        private String name;
        private String email;
    }
}
