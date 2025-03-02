package com.example.demo.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.entity.AdminEntity;
import com.example.demo.repository.AdminRepo;
import com.example.demo.service.AdminService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private AdminRepo adminRepo;

    @InjectMocks
    private AdminService adminService;

    private AdminEntity admin;

    @BeforeEach
    void setUp() {
        admin = new AdminEntity();
        admin.setId(1);
        admin.setEmail("admin@example.com");
        admin.setPassword("securePassword");
    }

    @Test
    void testSaveAdmin() {
        when(adminRepo.save(any(AdminEntity.class))).thenReturn(admin);
        
        AdminEntity savedAdmin = adminService.saveAdmin(admin);
        
        assertNotNull(savedAdmin);
        assertEquals(admin.getEmail(), savedAdmin.getEmail());
        verify(adminRepo, times(1)).save(admin);
    }

    @Test
    void testValidateAdmin_Success() {
        when(adminRepo.findByEmail("admin@example.com")).thenReturn(admin);

        AdminEntity result = adminService.validateAdmin("admin@example.com", "securePassword");

        assertNotNull(result);
        assertEquals(admin.getEmail(), result.getEmail());
        verify(adminRepo, times(1)).findByEmail("admin@example.com");
    }

    @Test
    void testValidateAdmin_Failure_WrongPassword() {
        when(adminRepo.findByEmail("admin@example.com")).thenReturn(admin);

        AdminEntity result = adminService.validateAdmin("admin@example.com", "wrongPassword");

        assertNull(result);
        verify(adminRepo, times(1)).findByEmail("admin@example.com");
    }

    @Test
    void testValidateAdmin_Failure_NoUserFound() {
        when(adminRepo.findByEmail("unknown@example.com")).thenReturn(null);

        AdminEntity result = adminService.validateAdmin("unknown@example.com", "securePassword");

        assertNull(result);
        verify(adminRepo, times(1)).findByEmail("unknown@example.com");
    }
}


