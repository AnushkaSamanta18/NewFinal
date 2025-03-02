/*
 //correct
package com.cts.Flexride.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.cts.Flexride.Entity.UserEntity;
import com.cts.Flexride.Service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // Show Login Page
    @GetMapping("/login")
    public String showLoginPage(HttpServletResponse response, Model model) {
    	
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        return "login";
    }

    // Show Signup Page
    @GetMapping("/signup")
    public String showSignupPage(Model model) {
        model.addAttribute("user", new UserEntity());
        return "signup";
    }

    // Handle Signup Form Submission
    @PostMapping("/signup")
    public String signup(@ModelAttribute("user") UserEntity user, Model model) {
        userService.saveUser(user);
        model.addAttribute("message", "Signup successful! Please login.");
        return "login";
    }

    // Handle Login
    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session, Model model, HttpServletResponse response) {
    	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");

        if (userService.validateUser(email, password)) {
        	
       int userId = userService.getUserId(email, password);
       
            session.setAttribute("userEmail", email);
            session.setAttribute("userId", userId);
            return "redirect:/user/customerdashboard"; // Redirect to customer dashboard
        } else {
            model.addAttribute("error", "Invalid email or password. Please try again.");
            return "login";
        }
    }

    // Show Customer Dashboard
    @GetMapping("/customerdashboard")
    public String showDashboard(HttpSession session, Model model) {
        String email = (String) session.getAttribute("userEmail");
        if (email == null) {
            return "redirect:/user/login"; // Redirect to login if not authenticated
        }
        model.addAttribute("email", email);
        return "customerdashboard";
    }

    // Logout
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/user/login";
    }
}


*/

//add

package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.example.demo.entity.UserEntity;
import com.example.demo.service.CarService;
import com.example.demo.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private CarService carService;
	
	// Show Login Page
	@GetMapping("/login")
	public String showLoginPage(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		if(session!=null) {
		System.out.println("hi inside the showLoginPage"+session);
		session.removeAttribute("error");
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		}
		return "Login";
	}

	// Show Signup Page
	@GetMapping("/signup")
	public String showSignupPage(Model model) {
		model.addAttribute("user", new UserEntity());
		return "signup";
	}

	// Handle Signup Form Submission
	@PostMapping("/signup")
	public String signup(@ModelAttribute("user") UserEntity user, Model model) {
		userService.saveUser(user);
		model.addAttribute("message", "Signup successful! Please login.");
		return "Login";
	}

	// Handle Login
	@PostMapping("/login")
	public String login(@RequestParam String email, @RequestParam String password, HttpSession session, Model model,
			HttpServletResponse response) {

		if (userService.validateUser(email, password)) {
			int userId = userService.getUserId(email, password);
		
			session.setAttribute("userEmail", email);
			session.setAttribute("userId", userId);
			return "redirect:/user/customerdashboard"; // Redirect to customer dashboard
		} else {
			model.addAttribute("error", "Invalid email or password. Please try again.");
			return "Login";
		}
	}

	// Show Customer Dashboard with Session & Cookie Management
	@GetMapping("/customerdashboard")
	public String showDashboard(HttpSession session, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		String email = (String) session.getAttribute("userEmail");
		if (email == null) {
			return "redirect:/user/login"; // Redirect to login if not authenticated
		}
		if (!session.isNew()) {
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Expires", "0");
			String cookieName = "";
			Cookie cookies[] = request.getCookies();
			for (Cookie cookie : cookies) {
				if (cookieName.equals("JSESSIONID")) {
					cookieName = cookie.getName();
				}
			}

			
			model.addAttribute("email", email);
			return "customerdashboard";
		}
		return "Login"; // Ensures Dashboard.html is displayed with booking table
	}
	
	

	// Logout with Session & Cookie Removal
	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Object o=session.getAttribute("userId");
		System.out.println("o:"+o);
		if (session != null && o==null) {
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Expires", "0");
			session.invalidate();
			System.out.println("--session is invlidate--");
			return "redirect:/user/login";
	
		}
			return "redirect:/user/login";
		
		
		
	

	}

}
