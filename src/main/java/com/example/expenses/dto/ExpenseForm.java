package com.example.expenses.dto;

import com.example.expenses.model.Category;
import com.example.expenses.model.Expense;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseForm {
    @NotNull(message = "Укажите сумму")
    @DecimalMin(value = "0.01", message = "Сумма должна быть больше нуля")
    @DecimalMax(value = "99999999.99", message = "Слишком большая сумма")
    @Digits(integer = 8, fraction = 2, message = "Не более двух знаков после запятой")
    private BigDecimal amount;

    @NotNull(message = "Выберите категорию")
    private Category category;

    @NotNull(message = "Укажите дату")
    @PastOrPresent(message = "Дата не может быть в будущем")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date = LocalDate.now();

    public static ExpenseForm from(Expense expense) {
        ExpenseForm form = new ExpenseForm();
        form.setAmount(expense.getAmount());
        form.setCategory(expense.getCategory());
        form.setDate(expense.getDate());
        return form;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
