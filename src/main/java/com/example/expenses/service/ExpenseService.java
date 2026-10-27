package com.example.expenses.service;

import com.example.expenses.dto.ExpenseForm;
import com.example.expenses.model.Category;
import com.example.expenses.model.Expense;
import com.example.expenses.model.User;
import com.example.expenses.repository.ExpenseRepository;
import com.example.expenses.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<Expense> findAll(Long userId, Category category) {
        if (category == null) {
            return expenseRepository.findByUserIdOrderByDateDescIdDesc(userId);
        }
        return expenseRepository.findByUserIdAndCategoryOrderByDateDescIdDesc(userId, category);
    }

    public BigDecimal total(List<Expense> expenses) {
        return expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional(readOnly = true)
    public Expense findOwn(Long userId, Long expenseId) {
        return expenseRepository.findByIdAndUserId(expenseId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Расход не найден"));
    }

    @Transactional
    public Expense create(Long userId, ExpenseForm form) {
        User owner = userRepository.getReferenceById(userId);
        return expenseRepository.save(
                new Expense(owner, form.getAmount(), form.getCategory(), form.getDate()));
    }

    @Transactional
    public Expense update(Long userId, Long expenseId, ExpenseForm form) {
        Expense expense = findOwn(userId, expenseId);
        expense.update(form.getAmount(), form.getCategory(), form.getDate());
        return expense;
    }

    @Transactional
    public void delete(Long userId, Long expenseId) {
        expenseRepository.delete(findOwn(userId, expenseId));
    }
}
