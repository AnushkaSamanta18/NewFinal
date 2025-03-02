
package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.BookingEntity;
import com.example.demo.entity.CarEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.service.BookingService;
import com.example.demo.service.CarService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/car")
public class CarController {

	@Autowired
	private CarService carService;

	@Autowired
	private BookingService bookingService;

	@Autowired
	private UserService userService;
	

	// Show all cars in CarDetails page
	@GetMapping("/list")
	public String showCarDetailsPage(Model model, HttpSession session) {
	
		model.addAttribute("cars", carService.getAllCars());
		model.addAttribute("car", new CarEntity());
		return "Cardetails";// This line returns the view name "Cardetails", which will render the car
							// details page.
	}
	

	// Add or Update Car
	@PostMapping("/save")
	public String saveCar(@ModelAttribute("car") CarEntity car) { // It binds form data to a CarEntity object using the
																	// @ModelAttribute annotation.
		carService.saveCar(car);
		return "redirect:/car/list";
	}

	// ADDING

	@GetMapping("/delete/{id}")//and a RedirectAttributes object as parameters
	public String deleteCar(@PathVariable int id, RedirectAttributes redirectAttributes) {
		try {
			carService.deleteCar(id);//his block attempts to delete the car with the given id
		} catch (RuntimeException e) // If a RuntimeException is thrown (e.g., if the car is booked and cannot be
										// deleted), this block catches the exception.
		{
		
			redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete this car as it is booked"); //This message will be available after the redirect.
			return "redirect:/car/list";
		}
		return "redirect:/car/list";
	}

	
	// Edit Car (Load existing data into form)
	@GetMapping("/edit/{id}")//t declares a method named editCar that takes an integer id (extracted from the URL path) 
	public String editCar(@PathVariable int id, Model model) {
		CarEntity car = carService.getCarById(id);
		if (car != null) {
			model.addAttribute("car", car); // add the car to the model with the attribute name car
		} else {
			model.addAttribute("car", new CarEntity()); // If the car is not found, it adds a new CarEntity to the model
														// to Ensures form does not break
		}
		model.addAttribute("cars", carService.getAllCars());
		return "Cardetails"; // Loads the page with populated form data
	}
	
	

	@GetMapping("/booking")
	public String showAvailableCars(Model model, HttpSession session) {
		List<CarEntity> cars = carService.getAllCars();

		model.addAttribute("carsList",cars);
		

		// Assuming user is stored in the session after login
		//UserEntity user = (UserEntity) session.getAttribute("userId"); // This block retrieves the logged-in user
																				// from the session using the attribute
		return "book"; // Renders book.html
	}
	
	
	 @PostMapping("/book/{carId}")
	    public String bookCar(@PathVariable int carId, HttpSession session, RedirectAttributes redirectAttributes,HttpServletRequest request) {
	        Integer userId = (Integer) session.getAttribute("userId");
	        CarEntity car = carService.getCarById(carId);
	        UserEntity user = userService.getUserById(userId);
	        
	        String crdt=request.getParameter("crdt");// used to retrieve a parameter from an HTTP request
	        int index=crdt.indexOf("T");
	        String time=crdt.substring(index+1, crdt.length());//this method call extracts a substring from the crdt string, 
	        
	        String startDateAndTime=crdt.substring(0, index)+" "+time;
	        System.out.println(startDateAndTime+"<---");
	        
	        String rrdt=request.getParameter("rrdt");
	        int index1=rrdt.indexOf("T");
	        String time1=rrdt.substring(index1+1,rrdt.length());
	        String endDateAndTime=rrdt.substring(0,index1)+" "+time1;
	        
	        System.out.println(endDateAndTime+"<---");
	        
	        System.out.println(time);
	        
	        if (car != null && user != null) {
	            try {
	                bookingService.bookCar(user, car,startDateAndTime,endDateAndTime);
	                redirectAttributes.addFlashAttribute("bookingSuccess", "Your car has been booked successfully!");
	            } catch (RuntimeException e) {
	                redirectAttributes.addFlashAttribute("bookingError", e.getMessage());
	            }
	        } else {
	            redirectAttributes.addFlashAttribute("bookingError", "Booking failed. Please try again.");
	        }
	        return "redirect:/car/booking";
	    }


	// Show the admin dashboard with bookings (Only for admin)
	@GetMapping("/dashboard")
	public String showDashboard(HttpSession session, Model model) {
		// Get logged-in user from session
		UserEntity user = (UserEntity) session.getAttribute("userId");

		// Only allow admin to see dashboard
		if (user == null || !"anushka@gmail.com".equalsIgnoreCase(user.getEmail())) {
			return "redirect:/car/booking"; // Redirect normal users to the booking page
		}

		List<BookingEntity> bookings = bookingService.getAllBookings();
		model.addAttribute("bookings", bookings);
		return "dashboard"; // Only admin can see this
	}
}
