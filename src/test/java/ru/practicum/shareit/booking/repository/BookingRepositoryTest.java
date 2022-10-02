package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingRepositoryTest {
    private static final int PAGE = 0;
    private static final int SIZE = 20;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private BookingRepository bookingRepository;


    private static final User mockUserFirst = User.builder().id(1).name("User1")
            .email("User1@ya.com").build();
    private static final User mockUserSecond = User.builder().id(2).name("User2")
            .email("User2@ya.com").build();

    private static final Item mockItem1 = Item.builder().id(1).name("Item").description("Description")
            .available(true).owner(mockUserFirst).build();
    private static final Item mockItem2 = Item.builder().name("Item").description("Description")
            .available(true).owner(mockUserSecond).build();

    private static final Booking mockBooking1 = Booking.builder().id(1)
            .start(LocalDate.now().atStartOfDay().plusDays(1))
            .end(LocalDate.now().atStartOfDay().plusDays(2))
            .item(mockItem1).booker(mockUserSecond).status(BookingStatus.WAITING).build();

    private static final Booking mockBooking2 = Booking.builder().id(2)
            .start(LocalDate.now().atStartOfDay().plusMonths(1))
            .end(LocalDate.now().atStartOfDay().plusMonths(1).plusDays(3))
            .item(mockItem1).booker(mockUserSecond).status(BookingStatus.APPROVED).build();

    private static final Booking mockBooking3 = Booking.builder()
            .start(LocalDate.now().atStartOfDay().plusDays(4))
            .end(LocalDate.now().atStartOfDay().plusDays(5))
            .item(mockItem2).booker(mockUserFirst).status(BookingStatus.WAITING).build();

    private static final Booking mockBooking4 = Booking.builder()
            .start(LocalDate.now().atStartOfDay().plusDays(6)).end(LocalDate.now().atStartOfDay().plusDays(7))
            .item(mockItem2).booker(mockUserFirst).status(BookingStatus.REJECTED).build();

    @Test
    void testFindAllByBookerId() {
        userRepository.save(mockUserFirst);
        userRepository.save(mockUserSecond);
        itemRepository.save(mockItem1);
        itemRepository.save(mockItem2);
        bookingRepository.save(mockBooking1);
        bookingRepository.save(mockBooking2);
        bookingRepository.save(mockBooking3);
        bookingRepository.save(mockBooking4);

        Sort sortById = Sort.by(Sort.Direction.DESC, "start");
        Pageable page = PageRequest.of(PAGE, SIZE, sortById);

        Collection<Booking> bookings = bookingRepository.findAllByBookerId(2, page);

        assertThat(bookings).isNotEmpty();
        assertThat(bookings).hasSize(2).contains(mockBooking1, mockBooking2);
    }


    @Test
    void testFindAllByBookerIdAndEndIsBefore() {
        userRepository.save(mockUserFirst);
        userRepository.save(mockUserSecond);
        itemRepository.save(mockItem1);
        itemRepository.save(mockItem2);
        bookingRepository.save(mockBooking1);
        bookingRepository.save(mockBooking2);
        bookingRepository.save(mockBooking3);
        bookingRepository.save(mockBooking4);

        Sort sortById = Sort.by(Sort.Direction.DESC, "start");
        Pageable page = PageRequest.of(PAGE, SIZE, sortById);

        Collection<Booking> bookings = bookingRepository.findAllByBookerIdAndEndIsBefore(2,
                LocalDateTime.now().plusDays(15), page);

        assertThat(bookings).isNotEmpty();
        assertThat(bookings).hasSize(1).contains(mockBooking1);
    }

    @Test
    void testFindAllByBookerId_StartIsAfter() {
        userRepository.save(mockUserFirst);
        userRepository.save(mockUserSecond);
        itemRepository.save(mockItem1);
        itemRepository.save(mockItem2);
        bookingRepository.save(mockBooking1);
        bookingRepository.save(mockBooking2);
        bookingRepository.save(mockBooking3);
        bookingRepository.save(mockBooking4);

        Sort sortById = Sort.by(Sort.Direction.DESC, "start");
        Pageable page = PageRequest.of(PAGE, SIZE, sortById);

        Collection<Booking> bookings = bookingRepository.findAllByBookerAndFutureState(2, page);

        assertThat(bookings).isNotEmpty();
        assertThat(bookings).hasSize(2).contains(mockBooking1, mockBooking2);
    }

    @Test
    void testFindAllByBookerId_Status() {
        userRepository.save(mockUserFirst);
        userRepository.save(mockUserSecond);
        itemRepository.save(mockItem1);
        itemRepository.save(mockItem2);
        bookingRepository.save(mockBooking1);
        bookingRepository.save(mockBooking2);
        bookingRepository.save(mockBooking3);
        bookingRepository.save(mockBooking4);

        Sort sortById = Sort.by(Sort.Direction.DESC, "start");
        Pageable page = PageRequest.of(PAGE, SIZE, sortById);

        Collection<Booking> bookings = bookingRepository.findAllByBookerIdAndStatus(1,
                BookingStatus.REJECTED, page);

        assertThat(bookings).isNotEmpty();
        assertThat(bookings).hasSize(1).contains(mockBooking4);
    }

    @Test
    void testFindAllByItemOwnerId() {
        userRepository.save(mockUserFirst);
        userRepository.save(mockUserSecond);
        itemRepository.save(mockItem1);
        itemRepository.save(mockItem2);
        bookingRepository.save(mockBooking1);
        bookingRepository.save(mockBooking2);
        bookingRepository.save(mockBooking3);
        bookingRepository.save(mockBooking4);

        Sort sortById = Sort.by(Sort.Direction.DESC, "start");
        Pageable page = PageRequest.of(PAGE, SIZE, sortById);

        Collection<Booking> bookings = bookingRepository.findAllByItemOwnerId(1, page);

        assertThat(bookings).isNotEmpty();
        assertThat(bookings).hasSize(2).contains(mockBooking1, mockBooking2);
    }

    @Test
    void testFindAllByItemOwnerIdEndIsAfterAndStartIsBefore() {
        userRepository.save(mockUserFirst);
        userRepository.save(mockUserSecond);
        itemRepository.save(mockItem1);
        itemRepository.save(mockItem2);
        bookingRepository.save(mockBooking1);
        bookingRepository.save(mockBooking2);
        bookingRepository.save(mockBooking3);
        bookingRepository.save(mockBooking4);

        Sort sortById = Sort.by(Sort.Direction.DESC, "start");
        Pageable page = PageRequest.of(PAGE, SIZE, sortById);

        Collection<Booking> bookings = bookingRepository.findAllByItemOwnerIdAndEndIsAfterAndStartIsBefore(1,
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), page);

        assertThat(bookings).isNotEmpty();
        assertThat(bookings).hasSize(1).contains(mockBooking1);
    }

    @Test
    void testFindAllByItemOwnerIdEndIsBefore() {
        userRepository.save(mockUserFirst);
        userRepository.save(mockUserSecond);
        itemRepository.save(mockItem1);
        itemRepository.save(mockItem2);
        bookingRepository.save(mockBooking1);
        bookingRepository.save(mockBooking2);
        bookingRepository.save(mockBooking3);
        bookingRepository.save(mockBooking4);

        Sort sortById = Sort.by(Sort.Direction.DESC, "start");
        Pageable page = PageRequest.of(PAGE, SIZE, sortById);

        Collection<Booking> bookings = bookingRepository.findAllByItemOwnerIdAndEndIsBefore(1,
                LocalDateTime.now().plusMonths(1).plusDays(4), page);

        assertThat(bookings).hasSize(2).contains(mockBooking1, mockBooking2);
    }

    @Test
    void testFindAllByItemOwnerIdStartIsAfter() {
        userRepository.save(mockUserFirst);
        userRepository.save(mockUserSecond);
        itemRepository.save(mockItem1);
        itemRepository.save(mockItem2);
        bookingRepository.save(mockBooking1);
        bookingRepository.save(mockBooking2);
        bookingRepository.save(mockBooking3);
        bookingRepository.save(mockBooking4);

        Sort sortById = Sort.by(Sort.Direction.DESC, "start");
        Pageable page = PageRequest.of(PAGE, SIZE, sortById);

        Collection<Booking> bookings = bookingRepository.findAllByItemOwnerIdAndStartIsAfter(2,
                LocalDateTime.now().plusDays(2), page);

        assertThat(bookings).hasSize(2).contains(mockBooking3, mockBooking4);
    }

    @Test
    void testFindAllByItemOwnerIdStatus() {
        userRepository.save(mockUserFirst);
        userRepository.save(mockUserSecond);
        itemRepository.save(mockItem1);
        itemRepository.save(mockItem2);
        bookingRepository.save(mockBooking1);
        bookingRepository.save(mockBooking2);
        bookingRepository.save(mockBooking3);
        bookingRepository.save(mockBooking4);

        Sort sortById = Sort.by(Sort.Direction.DESC, "start");
        Pageable page = PageRequest.of(PAGE, SIZE, sortById);

        Collection<Booking> bookings = bookingRepository.findAllByItemOwnerIdAndStatus(1,
                BookingStatus.WAITING, page);

        assertThat(bookings).hasSize(1).contains(mockBooking1);
    }

    @Test
    void testFindFirstByItemOwnerIdStatusOrderByEnd() {
        userRepository.save(mockUserFirst);
        userRepository.save(mockUserSecond);
        itemRepository.save(mockItem1);
        itemRepository.save(mockItem2);
        bookingRepository.save(mockBooking1);
        bookingRepository.save(mockBooking2);
        bookingRepository.save(mockBooking3);
        bookingRepository.save(mockBooking4);

        Optional<Booking> bookings = bookingRepository.findFirstByItemIdAndStatusOrderByEnd(1,
                BookingStatus.APPROVED);

        assertThat(bookings).isPresent();
        assertThat(bookings.get()).isEqualTo(mockBooking2);
    }

    @Test
    void testFindFirstByItemOwnerIdStatusOrderByEndDesc() {
        userRepository.save(mockUserFirst);
        userRepository.save(mockUserSecond);
        itemRepository.save(mockItem1);
        itemRepository.save(mockItem2);
        bookingRepository.save(mockBooking1);
        bookingRepository.save(mockBooking2);
        bookingRepository.save(mockBooking3);
        bookingRepository.save(mockBooking4);

        Optional<Booking> bookings = bookingRepository.findFirstByItemIdAndStatusOrderByEndDesc(2,
                BookingStatus.REJECTED);

        assertThat(bookings).isPresent();
        assertThat(bookings.get()).isEqualTo(mockBooking4);
    }

    @Test
    void testFindFirstByBookerIdAndItemId_StatusAndStartAreBefore() {
        userRepository.save(mockUserFirst);
        userRepository.save(mockUserSecond);
        itemRepository.save(mockItem1);
        itemRepository.save(mockItem2);
        bookingRepository.save(mockBooking1);
        bookingRepository.save(mockBooking2);
        bookingRepository.save(mockBooking3);
        bookingRepository.save(mockBooking4);

        Optional<Booking> bookings = bookingRepository.findFirstByBookerIdAndItemIdAndStatusAndStartBefore(1,
                2, BookingStatus.WAITING, LocalDateTime.now().plusDays(7));

        assertThat(bookings).isPresent();
        assertThat(bookings.get()).isEqualTo(mockBooking3);
    }
}