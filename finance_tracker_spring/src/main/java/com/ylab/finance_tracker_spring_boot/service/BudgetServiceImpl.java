package com.ylab.finance_tracker_spring_boot.service;

import com.ylab.finance_tracker_spring_boot.domain.model.Budget;
import com.ylab.finance_tracker_spring_boot.domain.repository.BudgetRepository;
import com.ylab.finance_tracker_spring_boot.domain.service.BudgetService;
import com.ylab.finance_tracker_spring_boot.domain.service.NotificationService;
import com.ylab.finance_tracker_spring_boot.dto.BudgetDTO;
import com.ylab.finance_tracker_spring_boot.dto.TransactionDTO;
import com.ylab.finance_tracker_spring_boot.mapper.BudgetMapper;
import com.ylab.finance_tracker_spring_boot.security.AuthService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.YearMonth;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {
    private final BudgetRepository budgetRepository;
    private final NotificationService notificationService;
    private final BudgetMapper budgetMapper = Mappers.getMapper(BudgetMapper.class);
    private final AuthService authService;


    @Override
    public void createBudget(BudgetDTO budgetDTO) throws SQLException {
        Budget budget = budgetMapper.toBudget(budgetDTO);
        budget.setEmail(authService.getCurrentUser().getEmail());
        budget.setSpent(BigDecimal.ZERO);
        budgetRepository.save(budget);
    }

    @Override
    public Optional<BudgetDTO> getBudget(String email, YearMonth month) throws SQLException {
        return budgetRepository.findByUserAndMonth(email, month).map(budgetMapper::toBudgetDTO);
    }

    @Override
    public void addExpense(String email, YearMonth month, BigDecimal amount) throws SQLException {
        Optional<Budget> budgetOpt = budgetRepository.findByUserAndMonth(email, month);
        if (budgetOpt.isPresent()) {
            Budget budget = budgetOpt.get();
            BigDecimal newSpent = budget.getSpent().add(amount);
            Budget updatedBudget = Budget.builder()
                    .email(email)
                    .yearMonth(month)
                    .budget(budget.getBudget())
                    .spent(newSpent).build();
            budgetRepository.update(updatedBudget);
            checkAndNotify(email, month);
        } else {
            throw new IllegalStateException("Budget not found for " + email + " and " + month);
        }
    }

    @Override
    public boolean isBudgetExceeded(String email, YearMonth month) throws SQLException {
        return budgetRepository.findByUserAndMonth(email, month)
                .map(Budget::isExceeded)
                .orElse(false);
    }

    @Override
    public void checkAndNotify(String email, YearMonth month) throws SQLException {
        budgetRepository.findByUserAndMonth(email, month)
                .filter(Budget::isExceeded)
                .ifPresent(budget -> notificationService.sendEmail(email, "Внимание!", "Вы превысили бюджет на: " +
                        budget.getSpent().subtract(budget.getBudget())));
    }

    @Override
    public void processExpense(TransactionDTO transactionDTO) throws SQLException {
        YearMonth yearMonth = YearMonth.from(transactionDTO.getDate());
        addExpense(transactionDTO.getEmail(), yearMonth, transactionDTO.getAmount());
    }
}