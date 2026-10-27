package com.example.expenses.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginForm {
    @NotBlank(message = "Введите логин")
    private String login;

    @NotBlank(message = "Введите пароль")
    private String password;

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
}
