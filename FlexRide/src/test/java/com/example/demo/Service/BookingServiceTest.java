

package com.example.demo.Service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.demo.entity.BookingEntity;
import com.example.demo.entity.CarEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.BookingRepo;
import com.example.demo.repository.CarRepo;
import com.example.demo.service.BookingService;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepo bookingRepo;

    @Mock
    private CarRepo carRepo;

    @InjectMocks
    private BookingService bookingService;

    private UserEntity user;
    private CarEntity car;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setId(1);

        car = new CarEntity();
        car.setId(101);
        car.setBooked(false);
    }

    @Test
    void testBookCar_Success() {
        when(carRepo.save(any(CarEntity.class))).thenReturn(car);
        when(bookingRepo.save(any(BookingEntity.class))).thenReturn(new BookingEntity(user, car, "2025-02-26 10:00", "2025-02-26 12:00"));
        
        bookingService.bookCar(user, car, "2025-02-26 10:00", "2025-02-26 12:00");
        
        assertTrue(car.isBooked());
        verify(carRepo, times(1)).save(car);
        verify(bookingRepo, times(1)).save(any(BookingEntity.class));
    }

    @Test
    void testBookCar_Failure_AlreadyBooked() {
        car.setBooked(true);
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
            bookingService.bookCar(user, car, "2025-02-26 10:00", "2025-02-26 12:00")
        );
        
        assertEquals("Car is already booked and unavailable.", exception.getMessage());
        verify(carRepo, never()).save(any(CarEntity.class));
        verify(bookingRepo, never()).save(any(BookingEntity.class));
    }

    @Test
    void testGetAllBookings() {
        List<BookingEntity> mockBookings = Arrays.asList(
            new BookingEntity(user, car, "2025-02-26 10:00", "2025-02-26 12:00"),
            new BookingEntity(user, car, "2025-02-27 10:00", "2025-02-27 12:00")
        );
        when(bookingRepo.findAll()).thenReturn(mockBookings);
        
        List<BookingEntity> bookings = bookingService.getAllBookings();
        
        assertEquals(2, bookings.size());
        verify(bookingRepo, times(1)).findAll();
    }
}
