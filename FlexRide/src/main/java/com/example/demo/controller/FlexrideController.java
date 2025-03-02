package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.AdminEntity;
import com.example.demo.entity.BookingEntity;
import com.example.demo.service.AdminService;
import com.example.demo.service.BookingService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class FlexrideController {

	@Autowired
	private AdminService adminservice;

	@Autowired
	private BookingService bookingService;

	@GetMapping("/")
	public String home() {
		return "front";
	}
	
/*
	@GetMapping("/front")
	public String front() {
		return "front";
	}
	*/

	@GetMapping("/contact")
	public String Contact() {
		return "contact";
	}

	

	// Admin part
	//Display the login form
	@GetMapping("/AdminLogin")
	public String showAdminLoginPage(HttpSession httpSession, Model model, HttpServletResponse response) {
		httpSession.removeAttribute("error"); //This line removes the attribute named "error" from the current session.This is typically done to clear any previous error messages.


		return "AdminLogin";
	}
    
	//Process the login req and redirects based on success or failure
	@PostMapping("/AdminLogin")
	public String login(@RequestParam String email, @RequestParam String password, HttpSession session, Model model,
			HttpServletRequest request) {
		AdminEntity admin = adminservice.validateAdmin(email, password);

		if (admin != null & session != null) {
			session.setAttribute("name", admin.getName()); // If the credentials are valid, this line stores the admin’s name in the session with the attribute name "name".
			//1. model.addAttribute("name", admin.getName()); //This line adds the admin’s name to the model, which can be used in the view to display the admin’s name.
			return "redirect:/dashboard"; // Redirect to update welcome message
		} else {
			model.addAttribute("error", "Wrong email or password");
			return "AdminLogin";
		}

	}
	

	// Adding
	@GetMapping("/dashboard")
	public String showDashboard(Model model, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {
		List<BookingEntity> bookings = bookingService.getAllBookings(); //This line calls the getAllBookings method of the bookingService to retrieve a list of all bookings.
	
		session.setAttribute("bookings", bookings); // store the list of bookings in the session 
		model.addAttribute("bookings", bookings); //add it to the model. This makes the bookings available in the view.
		if (!session.isNew()) {
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); //set HTTP response headers to prevent the browser from caching the page.
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Expires", "0");
			
			//This block of code iterates through the cookies in the request to find the JSESSIONID cookie.
			String cookieName = "";
			Cookie cookies[] = request.getCookies();
			for (Cookie cookie : cookies) {
				if (cookieName.equals("JSESSIONID")) {
					cookieName = cookie.getName();
				}
			}
			return "dashboard"; //If the session is not new, this line returns the view name "dashboard"
		}
		return "AdminLogin"; // Ensures Dashboard.html is displayed with booking table. If the session is new, this line returns the view name "AdminLogin", which will display the admin login page.
	}

	// Logout Function
	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

		if (session != null) {
			//sets response headers to prevent caching.
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Expires", "0");
			session.invalidate();//It logs the session removal process and invalidates the session.
			return "redirect:/";
			
		}
		return "AdminLogin";

	}

}
