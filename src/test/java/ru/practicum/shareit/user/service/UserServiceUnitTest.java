//package ru.practicum.shareit.user.service;
//
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoSession;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import ru.practicum.shareit.exception.InputDataException;
//import ru.practicum.shareit.user.UserService;
//import ru.practicum.shareit.user.dto.UserDto;
//import ru.practicum.shareit.user.model.User;
//import ru.practicum.shareit.user.model.UserMapper;
//import ru.practicum.shareit.user.repository.UserRepository;
//import ru.practicum.shareit.user.validate.ValidateUserData;
//
//import java.util.Collection;
//import java.util.List;
//import java.util.Optional;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.*;
//import static org.hamcrest.Matchers.equalTo;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.Mockito.times;
//
//@SpringBootTest
//@RequiredArgsConstructor(onConstructor_ = @Autowired)
//public class UserServiceUnitTest {
//    @Mock
//    private UserRepository userRepository;
//
//    private UserService userService;
//
//    private MockitoSession mockitoSession;
//
//    private ValidateUserData validateUserData;
//
//    @BeforeEach
//    void setUp() {
//        mockitoSession = Mockito.mockitoSession().initMocks(this).startMocking();
//        userService = new UserService(userRepository, validateUserData);
//    }
//
//    @AfterEach
//    void finish() {
//        mockitoSession.finishMocking();
//    }
//
//    private final User User1 = User.builder().id(1).name("User1").email("User1@ya.ru").build();
//    private final User User1Update = User.builder().id(2).name("User1Update").email("User1@ya.ru").build();
//    private final User User2 = User.builder().id(2).name("User2").email("User2@ya.ru").build();
//
//    @Test
//    void testAddUser() {
//        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(User1);
//        UserDto userDto = UserMapper.toUserDto(User1);
//        UserDto user = userService.addUser(userDto);
//
//        Mockito.verify(userRepository, times(1)).save(User1);
//
//        assertThat(user.getId(), notNullValue());
//        assertThat(user.getName(), equalTo(User1.getName()));
//        assertThat(user.getEmail(), equalTo(User1.getEmail()));
//    }
//
//    @Test
//    void testGetUserById() throws InputDataException {
//        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(User1));
//
//        UserDto user = userService.getUser(1);
//
//        Mockito.verify(userRepository, times(1)).findById(1);
//
//        assertThat(user.getName(), equalTo(User1.getName()));
//        assertThat(user.getEmail(), equalTo(User1.getEmail()));
//    }
//
//    @Test
//    void testGetUserByWrongId() {
//        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(InputDataException.class, () -> userService.getUser(1));
//
//        assertEquals("Пользователя с таким id нет в БД", exception.getMessage());
//    }
//
//    @Test
//    void testFindAll() {
//        Mockito.when(userRepository.findAll()).thenReturn(List.of(User1, User2));
//
//        Collection<UserDto> users = userService.getAllUsers();
//
//        Mockito.verify(userRepository, times(1)).findAll();
//
//        assertThat(users, hasSize(2));
//        assertThat(users, equalTo(List.of(User1, User2)));
//    }
//
//    @Test
//    void testUpdateUser() throws InputDataException {
//        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(User1Update);
//        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(User1));
//
//        User1.setName("User1Update");
//        UserDto userDto = UserMapper.toUserDto(User1);
//        UserDto user = userService.updateUser(userDto, User1.getId());
//
//        Mockito.verify(userRepository, times(1)).save(User1);
//
//        assertThat(user.getId(), equalTo(User1Update.getId()));
//        assertThat(user.getName(), equalTo(User1Update.getName()));
//    }
//
//    @Test
//    void testDeleteUser() throws InputDataException {
//        Mockito.when(userRepository.existsById(anyInt())).thenReturn(true);
//
//        userService.deleteUser(1);
//
//        Mockito.verify(userRepository, times(1)).deleteById(1);
//    }
//
//    @Test
//    void testCheckUserId() throws InputDataException {
//        Mockito.when(userRepository.existsById(anyInt())).thenReturn(true);
//
//        userService.isContainsUser(1);
//
//        Mockito.verify(userRepository, times(1)).existsById(1);
//    }
//
//    @Test
//    void testCheckUserIdNotExist() {
//        Mockito.when(userRepository.existsById(anyInt())).thenReturn(false);
//
//        Exception exception = assertThrows(InputDataException.class, () -> userService.isContainsUser(1));
//
//        assertEquals("Пользователя с таким id нет в БД", exception.getMessage());
//    }
//}
