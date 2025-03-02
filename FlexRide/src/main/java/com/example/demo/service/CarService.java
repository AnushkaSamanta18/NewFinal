package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.example.demo.entity.CarEntity;
import com.example.demo.repository.BookingRepo;
import com.example.demo.repository.CarRepo;

@Service
public class CarService {
    private static final Logger logger = LoggerFactory.getLogger(CarService.class);
    
    @Autowired
    private CarRepo carRepo;
    
    @Autowired
    private BookingRepo bookingRepo;

    public void saveCar(CarEntity car) {
        logger.info("Saving car: {}", car);
        carRepo.save(car);
    }

    public List<CarEntity> getAllCars() {
        logger.info("Fetching all cars");
        return carRepo.findAll();
    }

    public void deleteCar(int id) {
        logger.info("Attempting to delete car with ID: {}", id);
        CarEntity car = carRepo.findById(id).orElse(null);
        if (car != null) {
            if (!bookingRepo.findByCar(car).isEmpty()) {
                logger.warn("Car is booked, cannot delete");
                throw new RuntimeException("Car cannot be deleted as it has active bookings");
            }
            carRepo.delete(car);
            logger.info("Car deleted successfully");
        } else {
            logger.warn("Car not found with ID: {}", id);
        }
    }

    public CarEntity getCarById(int id) {
        logger.info("Fetching car with ID: {}", id);
        return carRepo.findById(id).orElse(null);
    }
    
    
    public Boolean isBookedOrNot(int id) {
    	return this.carRepo.isBooked(id);
    	
    }
}


