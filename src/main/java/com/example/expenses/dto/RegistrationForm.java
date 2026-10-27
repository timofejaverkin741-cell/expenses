package com.example.expenses.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegistrationForm {
    @NotBlank(message = "Введите логин")
    @Size(min = 3, max = 50, message = "Логин от 3 до 50 символов")
    @Pattern(regexp = "[A-Za-z0-9_.-]*", message = "Только латинские буквы, цифры и символы _ . -")
    private String login;

    @NotBlank(message = "Введите пароль")
    @Size(min = 6, max = 100, message = "Пароль не короче 6 символов")
    private String password;

    @NotBlank(message = "Повторите пароль")
    private String confirmPassword;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
