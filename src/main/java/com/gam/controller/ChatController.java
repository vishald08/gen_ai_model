package com.gam.controller;

import com.gam.entity.User;
import com.gam.service.GeminiService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ChatController {

    private final GeminiService geminiService;

    public ChatController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    // Dashboard View (Protected by Session)
    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("LOGGED_IN_USER");

        if (loggedInUser == null) {
            return "redirect:/signin";
        }

        model.addAttribute("user", loggedInUser);
        return "dashboard";
    }

    // Handle User Search/Prompt (Temporary, Not Stored in DB)
    @PostMapping("/ask")
    public String askQuestion(@RequestParam("prompt") String prompt,
                              HttpSession session,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("LOGGED_IN_USER");

        if (loggedInUser == null) {
            return "redirect:/signin";
        }

        try {
            String aiAnswer = geminiService.askGemini(prompt);

            // Pass user question & AI answer temporarily to the view
            model.addAttribute("user", loggedInUser);
            model.addAttribute("userPrompt", prompt);
            model.addAttribute("aiResponse", aiAnswer);

            return "dashboard";

        } catch (Exception e) {
            model.addAttribute("user", loggedInUser);
            model.addAttribute("errorMessage", "AI response generate nahi ho paya: " + e.getMessage());
            return "dashboard";
        }
    }
}