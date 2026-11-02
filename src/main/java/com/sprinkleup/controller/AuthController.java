package com.sprinkleup.controller;

import com.sprinkleup.config.AuthInterceptor;
import com.sprinkleup.dto.RegisterForm;
import com.sprinkleup.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import com.sprinkleup.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Sign in, sign up and sign out. */
@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @ModelAttribute("activePage")
    public String activePage() {
        return "auth";
    }

    @GetMapping("/login")
    public String loginForm(@RequestParam(required = false) String next,
                            @RequestAttribute(name = AuthInterceptor.CURRENT_USER, required = false) User currentUser,
                            Model model) {
        if (currentUser != null) {
            return "redirect:" + currentUser.getHomePath();
        }
        model.addAttribute("next", safeNext(next));
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password,
                        @RequestParam(required = false) String next,
                        HttpServletRequest request, Model model, RedirectAttributes redirect) {
        try {
            User user = authService.signIn(email, password);
            startSession(request, user);
            redirect.addFlashAttribute("success", "Welcome back, " + user.getFirstName() + "!");
            String target = safeNext(next);
            return "redirect:" + (target != null ? target : user.getHomePath());
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("email", email);
            model.addAttribute("next", safeNext(next));
            return "auth/login";
        }
    }

    @GetMapping("/register")
    public String registerForm(@RequestParam(required = false) String next, Model model) {
        model.addAttribute("registerForm", new RegisterForm());
        model.addAttribute("next", safeNext(next));
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerForm") RegisterForm form, BindingResult result,
                           @RequestParam(required = false) String next,
                           HttpServletRequest request, Model model, RedirectAttributes redirect) {
        if (!result.hasFieldErrors("confirmPassword") && !form.passwordsMatch()) {
            result.rejectValue("confirmPassword", "mismatch", "The two passwords don't match");
        }
        model.addAttribute("next", safeNext(next));
        if (result.hasErrors()) {
            return "auth/register";
        }
        try {
            User user = authService.register(form);
            startSession(request, user);
            redirect.addFlashAttribute("success", "Welcome to SprinkleUp, " + user.getFirstName() + "!");
            String target = safeNext(next);
            return "redirect:" + (target != null ? target : user.getHomePath());
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/register";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, RedirectAttributes redirect) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        redirect.addFlashAttribute("success", "You're signed out. See you soon!");
        return "redirect:/";
    }

    private void startSession(HttpServletRequest request, User user) {
        request.getSession(true);
        request.changeSessionId(); // new session id after sign-in (stops session fixation)
        request.getSession().setAttribute(AuthInterceptor.SESSION_USER_ID, user.getId());
    }

    /** Only allow going back to a page on this site. */
    private static String safeNext(String next) {
        return next != null && next.startsWith("/") && !next.startsWith("//") && !next.contains("\\") ? next : null;
    }
}
