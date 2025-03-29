package com.ylab.finance_tracker_spring.controller;

import com.ylab.finance_tracker_spring.annotation.Loggable;
import com.ylab.finance_tracker_spring.domain.service.GoalService;
import com.ylab.finance_tracker_spring.dto.GoalDTO;
import com.ylab.finance_tracker_spring.dto.UserDTO;
import com.ylab.finance_tracker_spring.security.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/goals")
@Validated
@Loggable
public class GoalController {
    private final GoalService goalService;
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<List<GoalDTO>> getAllGoals() throws SQLException {
        var goals = goalService.getUserGoals(authService.getCurrentUser().getEmail());
        return goals != null ? ResponseEntity.ok(goals) : ResponseEntity.notFound().build();
    }

    @PostMapping(("/create"))
    public ResponseEntity<GoalDTO> createGoal(@Valid @RequestBody GoalDTO goalDTO) throws SQLException {
        goalService.createGoal(goalDTO);
        return ResponseEntity.ok(goalDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<GoalDTO> updateGoal(@RequestParam String name, @RequestParam BigDecimal amount) throws SQLException {
        UserDTO user = authService.getCurrentUser();
        GoalDTO existingGoal = goalService.getGoalByName(user.getEmail(), name);
        if (existingGoal != null) {
            BigDecimal newSavedAmount = existingGoal.getSavedAmount().add(amount);
            GoalDTO updatedGoal = GoalDTO.builder()
                    .email(user.getEmail())
                    .title(name)
                    .targetAmount(existingGoal.getTargetAmount())
                    .savedAmount(newSavedAmount)
                    .build();
            goalService.updateGoal(updatedGoal);
            return ResponseEntity.ok(updatedGoal);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<GoalDTO> deleteGoal(@RequestParam String name) throws SQLException {
        UserDTO user = authService.getCurrentUser();
        GoalDTO existingGoal = goalService.getGoalByName(user.getEmail(), name);
        if (existingGoal != null) {
            goalService.deleteGoal(user.getEmail(), name);
            return ResponseEntity.ok(existingGoal);
        }
        return ResponseEntity.notFound().build();
    }

}
