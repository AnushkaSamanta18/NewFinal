

package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.entity.BookingEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.entity.CarEntity;
import com.example.demo.repository.BookingRepo;
import com.example.demo.repository.CarRepo;

@Service
public class BookingService {
    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    @Autowired
    private BookingRepo bookingRepo;

    @Autowired
    private CarRepo carRepo;

    public void bookCar(UserEntity user, CarEntity car,String startDateAndTime,String endDateAndTime) {
        logger.info("Attempting to book car: {} for user: {}", car.getId(), user.getId());

        if (car.isBooked()) {
            logger.warn("Booking failed: Car {} is already booked", car.getId());
            throw new RuntimeException("Car is already booked and unavailable.");
        }

        car.setBooked(true); //If the car is not already booked, this code sets the car’s status to booked and saves the updated car entity to the repository.

        carRepo.save(car);
        BookingEntity booking = new BookingEntity(user, car,startDateAndTime,endDateAndTime);
        bookingRepo.save(booking);

        logger.info("Booking successful: Car {} booked by user {}", car.getId(), user.getId());
    }
    
    
    

    public List<BookingEntity> getAllBookings() {
        logger.info("Fetching all bookings from the database");
        List<BookingEntity> bookings = bookingRepo.findAll();
        logger.info("Total bookings fetched: {}", bookings.size());
        return bookings;
    }
    
  
}







