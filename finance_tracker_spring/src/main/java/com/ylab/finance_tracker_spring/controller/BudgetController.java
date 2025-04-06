package com.ylab.finance_tracker_spring.controller;

import com.ylab.finance_tracker_spring.annotation.Loggable;
import com.ylab.finance_tracker_spring.domain.service.BudgetService;
import com.ylab.finance_tracker_spring.dto.BudgetDTO;
import com.ylab.finance_tracker_spring.security.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.YearMonth;

@RestController
@RequestMapping("/budgets")
@RequiredArgsConstructor
@Validated
@Loggable
public class BudgetController {
    private final BudgetService budgetService;
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<BudgetDTO> getBudgets(@RequestParam YearMonth yearMonth) throws SQLException {
        var user = authService.getCurrentUser();
        var budget = budgetService.getBudget(user.getEmail(), yearMonth);
        return budget.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("create")
    public ResponseEntity<BudgetDTO> createBudget(@Valid @RequestBody BudgetDTO budgetDTO) throws SQLException {
        var user = authService.getCurrentUser();
        budgetDTO.setEmail(user.getEmail());
        budgetDTO.setSpent(BigDecimal.ZERO);
        budgetService.createBudget(budgetDTO);
        return ResponseEntity.ok(budgetDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<BudgetDTO> updateBudget(@RequestParam YearMonth yearMonth, @RequestParam BigDecimal expense) throws SQLException {
        if (expense.compareTo(BigDecimal.ZERO) < 0) return ResponseEntity.badRequest().build();

        var user = authService.getCurrentUser();
        budgetService.addExpense(user.getEmail(), yearMonth, expense);
        return ResponseEntity.ok().build();
    }
}
