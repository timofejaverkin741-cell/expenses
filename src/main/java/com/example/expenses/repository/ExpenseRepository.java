package com.example.expenses.repository;

import com.example.expenses.model.Category;
import com.example.expenses.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserIdOrderByDateDescIdDesc(Long userId);

    List<Expense> findByUserIdAndCategoryOrderByDateDescIdDesc(Long userId, Category category);

    Optional<Expense> findByIdAndUserId(Long id, Long userId);
}
