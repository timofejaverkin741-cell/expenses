package com.example.expenses.controller;

import com.example.expenses.config.AuthInterceptor;
import com.example.expenses.dto.LoginForm;
import com.example.expenses.dto.RegistrationForm;
import com.example.expenses.model.User;
import com.example.expenses.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("form", new RegistrationForm());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("form") RegistrationForm form,
                           BindingResult result, RedirectAttributes redirect) {
        if (!result.hasFieldErrors("login") && userService.loginTaken(form.getLogin())) {
            result.rejectValue("login", "taken", "Такой логин уже занят");
        }
        if (form.getPassword() != null && !form.getPassword().equals(form.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "mismatch", "Пароли не совпадают");
        }
        if (result.hasErrors()) {
            return "register";
        }
        userService.register(form.getLogin(), form.getPassword());
        redirect.addFlashAttribute("success", "Регистрация прошла успешно. Войдите в систему.");
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("form", new LoginForm());
        return "login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("form") LoginForm form, BindingResult result,
                        HttpServletRequest request, Model model) {
        if (result.hasErrors()) {
            return "login";
        }
        Optional<User> user = userService.authenticate(form.getLogin(), form.getPassword());
        if (user.isEmpty()) {
            model.addAttribute("error", "Неверный логин или пароль");
            return "login";
        }
        HttpSession old = request.getSession(false);
        if (old != null) {
            old.invalidate();
        }
        HttpSession session = request.getSession(true);
        session.setAttribute(AuthInterceptor.USER_ID, user.get().getId());
        session.setAttribute(AuthInterceptor.USER_LOGIN, user.get().getLogin());
        return "redirect:/expenses";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }
}
