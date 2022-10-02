package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
    @MockBean
    private UserService userService;
    @MockBean
    private UserMapper userMapper;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    private final UserDto mockUserDto = UserDto.builder().id(1).name("UserDto").email("UserDto@ya.ru").build();
    private final User mockUser = User.builder().id(1).name("User1").email("User1@ya.ru").build();


    @Test
    void testAddUser() throws Exception {
        when(userService.addUser(any())).thenReturn(mockUserDto);

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(mockUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(mockUserDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(mockUserDto.getName())))
                .andExpect(jsonPath("$.email", is(mockUserDto.getEmail())));
    }

    @Test
    void testFindUserById() throws Exception {
        when(userService.getUser(any(Integer.class))).thenReturn(mockUser);
        doReturn(mockUserDto).when(userMapper).toUserDto(any());

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(mockUserDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(mockUserDto.getName())))
                .andExpect(jsonPath("$.email", is(mockUserDto.getEmail())));
    }

    @Test
    void testFindAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(mockUserDto));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$.[0].id", is(mockUserDto.getId()), Integer.class))
                .andExpect(jsonPath("$.[0].name", is(mockUserDto.getName())))
                .andExpect(jsonPath("$.[0].email", is(mockUserDto.getEmail())));
    }

    @Test
    void testUpdateUser() throws Exception {
        when(userService.updateUser(any(), any(Integer.class))).thenReturn(mockUserDto);

        mockMvc.perform(patch("/users/1")
                        .content(objectMapper.writeValueAsString(mockUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(mockUserDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(mockUserDto.getName())))
                .andExpect(jsonPath("$.email", is(mockUserDto.getEmail())));
    }

    @Test
    void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/users/1")).andExpect(status().isOk());
    }
}