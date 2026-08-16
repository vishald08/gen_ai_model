package com.gam.controller;

import com.gam.entity.User;
import com.gam.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/signin";
    }

    @GetMapping("/signup")
    public String showSignupPage() {
        return "signup";
    }

    @PostMapping("/send-otp")
    public String sendOtp(@RequestParam(value = "name", required = false) String name,
                          @RequestParam(value = "email", required = false) String email,
                          @RequestParam(value = "mobileNumber", required = false) String mobileNumber,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        try {
            if (name == null || name.trim().isEmpty() || email == null || email.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Name & Email both required.");
                return "redirect:/signup";
            }

            String otp = userService.generateAndSendOtp(email.trim());

            // Save in session
            session.setAttribute("TEMP_NAME", name.trim());
            session.setAttribute("TEMP_EMAIL", email.trim().toLowerCase());
            session.setAttribute("TEMP_MOBILE", mobileNumber != null ? mobileNumber.trim() : "");
            session.setAttribute("TEMP_OTP", otp);

            redirectAttributes.addFlashAttribute("successMessage", "4-digit OTP aapke email par bhej diya gaya hai!");
            redirectAttributes.addFlashAttribute("otpSent", true);
            redirectAttributes.addFlashAttribute("enteredName", name.trim());
            redirectAttributes.addFlashAttribute("enteredEmail", email.trim());

        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/signup";
    }

    @PostMapping("/verify-and-register")
    public String verifyAndRegister(@RequestParam(value = "otp", required = false) String enteredOtp,
                                   @RequestParam(value = "password", required = false) String password,
                                   @RequestParam(value = "confirmPassword", required = false) String confirmPassword,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        try {
            String sessionOtp = (String) session.getAttribute("TEMP_OTP");
            String sessionName = (String) session.getAttribute("TEMP_NAME");
            String sessionEmail = (String) session.getAttribute("TEMP_EMAIL");
            String sessionMobile = (String) session.getAttribute("TEMP_MOBILE");

            if (sessionName == null || sessionEmail == null || sessionOtp == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Session expire ho gaya. Dobara try karein.");
                return "redirect:/signup";
            }

            if (!userService.verifyOtp(enteredOtp, sessionOtp)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Wrong OTP! Email check karke sahi OTP daalein.");
                redirectAttributes.addFlashAttribute("otpSent", true);
                return "redirect:/signup";
            }

            if (password == null || password.trim().length() < 4) {
                redirectAttributes.addFlashAttribute("errorMessage", "Password must be atleast 4 characters.");
                redirectAttributes.addFlashAttribute("otpSent", true);
                return "redirect:/signup";
            }

            if (!password.trim().equals(confirmPassword != null ? confirmPassword.trim() : "")) {
                redirectAttributes.addFlashAttribute("errorMessage", "Passwords match nahi ho rahe.");
                redirectAttributes.addFlashAttribute("otpSent", true);
                return "redirect:/signup";
            }

            // Register
            userService.registerUser(sessionName, sessionEmail, sessionMobile, password.trim());

            session.removeAttribute("TEMP_OTP");
            session.removeAttribute("TEMP_NAME");
            session.removeAttribute("TEMP_EMAIL");
            session.removeAttribute("TEMP_MOBILE");

            redirectAttributes.addFlashAttribute("successMessage", "Account successfully Created! Ab Sign In karein.");
            return "redirect:/signin";

        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            redirectAttributes.addFlashAttribute("otpSent", true);
            return "redirect:/signup";
        }
    }

    @GetMapping("/signin")
    public String showSigninPage() {
        return "signin";
    }

    @PostMapping("/signin")
    public String login(@RequestParam(value = "email", required = false) String email,
                        @RequestParam(value = "password", required = false) String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        try {
            User user = userService.authenticateUser(email, password);
            session.setAttribute("LOGGED_IN_USER", user);
            return "redirect:/dashboard";
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/signin";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("successMessage", "LogOut Successfully.");
        return "redirect:/signin";
    }
}