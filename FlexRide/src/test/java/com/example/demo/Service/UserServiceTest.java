package com.example.demo.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepo;
import com.example.demo.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserService userService;

    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setId(1);
        user.setFullName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");
        user.setAddress("123 Street, NY");
    }

    @Test
    void testSaveUser() {
        when(userRepo.save(any(UserEntity.class))).thenReturn(user);

        UserEntity savedUser = userService.saveUser(user);

        assertNotNull(savedUser);
        assertEquals("John Doe", savedUser.getFullName());
        verify(userRepo, times(1)).save(user);
    }

    @Test
    void testValidateUser_ValidCredentials() {
        when(userRepo.findByEmail("john.doe@example.com")).thenReturn(user);

        boolean isValid = userService.validateUser("john.doe@example.com", "password123");

        assertTrue(isValid);
        verify(userRepo, times(1)).findByEmail("john.doe@example.com");
    }

    @Test
    void testValidateUser_InvalidPassword() {
        when(userRepo.findByEmail("john.doe@example.com")).thenReturn(user);

        boolean isValid = userService.validateUser("john.doe@example.com", "wrongpassword");

        assertFalse(isValid);
        verify(userRepo, times(1)).findByEmail("john.doe@example.com");
    }

    @Test
    void testValidateUser_InvalidEmail() {
        when(userRepo.findByEmail("unknown@example.com")).thenReturn(null);

        boolean isValid = userService.validateUser("unknown@example.com", "password123");

        assertFalse(isValid);
        verify(userRepo, times(1)).findByEmail("unknown@example.com");
    }

    @Test
    void testGetUserId_ValidUser() {
        when(userRepo.findByEmail("john.doe@example.com")).thenReturn(user);

        Integer userId = userService.getUserId("john.doe@example.com", "password123");

        assertNotNull(userId);
        assertEquals(1, userId);
        verify(userRepo, times(1)).findByEmail("john.doe@example.com");
    }

    @Test
    void testGetUserId_InvalidUser() {
        when(userRepo.findByEmail("unknown@example.com")).thenReturn(null);

        Integer userId = userService.getUserId("unknown@example.com", "password123");

        assertNull(userId);
        verify(userRepo, times(1)).findByEmail("unknown@example.com");
    }

    @Test
    void testGetUserById_ExistingUser() {
        when(userRepo.findById(1)).thenReturn(Optional.of(user));

        UserEntity foundUser = userService.getUserById(1);

        assertNotNull(foundUser);
        assertEquals("John Doe", foundUser.getFullName());
        verify(userRepo, times(1)).findById(1);
    }

    @Test
    void testGetUserById_NonExistingUser() {
        when(userRepo.findById(99)).thenReturn(Optional.empty());

        UserEntity foundUser = userService.getUserById(99);

        assertNull(foundUser);
        verify(userRepo, times(1)).findById(99);
    }
}
