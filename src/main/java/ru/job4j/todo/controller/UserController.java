package ru.job4j.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.todo.model.User;
import ru.job4j.todo.sevice.user.UserService;

import javax.servlet.http.HttpSession;
import java.util.TimeZone;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String getRegistrationPage(Model model) {
        model.addAttribute("timezones", TimeZone.getAvailableIDs());
        return "users/register";
    }

    @PostMapping("/register")
    public String register(Model model, @ModelAttribute("submittedUser") User user) {
        var savedUser = userService.save(user);
        if (savedUser.isEmpty()) {
            model.addAttribute("error",
                    "Пользователь с таким логином уже существует");
            model.addAttribute("timezones", TimeZone.getAvailableIDs());
            return "users/register";
        }
        return "redirect:/users/login";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "users/login";
    }

    @PostMapping("/login")
    public String loginUser(Model model, @ModelAttribute("submittedUser") User user,
                            HttpSession session) {
        var userOptional = userService.findByLoginAndPassword(
                user.getLogin(), user.getPassword());
        if (userOptional.isEmpty()) {
            model.addAttribute("error",
                    "Логин или пароль введены неверно");
            return "users/login";
        }
        session.setAttribute("user", userOptional.get());
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/users/login";
    }
}
