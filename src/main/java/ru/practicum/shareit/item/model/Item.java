package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * // TODO .
 */
@Entity
@Data
@Table(name = "items")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    @NotNull
    private Boolean available;
    @ManyToOne()
    @JoinColumn(name = "owner_id")
    private User owner;
    @OneToOne()
    @JoinColumn(name = "request_id")
    private ItemRequest request;
    @Transient
    private Booking lastBooking;
    @Transient
    private Booking nextBooking;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id")
    private List<Comment> comments = new ArrayList<>();
}
