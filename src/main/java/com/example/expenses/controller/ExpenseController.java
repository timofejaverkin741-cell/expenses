package com.example.expenses.controller;

import com.example.expenses.config.AuthInterceptor;
import com.example.expenses.dto.ExpenseForm;
import com.example.expenses.model.Category;
import com.example.expenses.model.Expense;
import com.example.expenses.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ExpenseController {
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @ModelAttribute("categories")
    public Category[] categories() {
        return Category.values();
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/expenses";
    }

    @GetMapping("/expenses")
    public String list(@SessionAttribute(AuthInterceptor.USER_ID) Long userId,
                       @RequestParam(required = false) Category category, Model model) {
        List<Expense> expenses = expenseService.findAll(userId, category);
        model.addAttribute("expenses", expenses);
        model.addAttribute("total", expenseService.total(expenses));
        model.addAttribute("selected", category);
        return "expenses";
    }

    @GetMapping("/expenses/new")
    public String addPage(Model model) {
        model.addAttribute("form", new ExpenseForm());
        return "expense-add";
    }

    @PostMapping("/expenses")
    public String add(@SessionAttribute(AuthInterceptor.USER_ID) Long userId,
                      @Valid @ModelAttribute("form") ExpenseForm form, BindingResult result,
                      RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "expense-add";
        }
        expenseService.create(userId, form);
        redirect.addFlashAttribute("message", "Расход добавлен");
        return "redirect:/expenses";
    }

    @GetMapping("/expenses/{id}/edit")
    public String editPage(@SessionAttribute(AuthInterceptor.USER_ID) Long userId,
                           @PathVariable Long id, Model model) {
        model.addAttribute("expenseId", id);
        model.addAttribute("form", ExpenseForm.from(expenseService.findOwn(userId, id)));
        return "expense-edit";
    }

    @PostMapping("/expenses/{id}")
    public String edit(@SessionAttribute(AuthInterceptor.USER_ID) Long userId,
                       @PathVariable Long id,
                       @Valid @ModelAttribute("form") ExpenseForm form, BindingResult result,
                       Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("expenseId", id);
            return "expense-edit";
        }
        expenseService.update(userId, id, form);
        redirect.addFlashAttribute("message", "Изменения сохранены");
        return "redirect:/expenses";
    }

    @PostMapping("/expenses/{id}/delete")
    public String delete(@SessionAttribute(AuthInterceptor.USER_ID) Long userId,
                         @PathVariable Long id, RedirectAttributes redirect) {
        expenseService.delete(userId, id);
        redirect.addFlashAttribute("message", "Расход удалён");
        return "redirect:/expenses";
    }
}
