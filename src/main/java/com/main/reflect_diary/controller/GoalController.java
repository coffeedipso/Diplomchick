package com.main.reflect_diary.controller;

import com.main.reflect_diary.model.Goal;
import com.main.reflect_diary.model.User;
import com.main.reflect_diary.service.GoalService;
import com.main.reflect_diary.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Tag(name = "Цели", description = "Операции с целями пользователя")
@Controller
@RequestMapping("/goals")
public class GoalController {

    private static final Logger logger = LoggerFactory.getLogger(GoalController.class);
    private final GoalService goalService;
    private final UserService userService;

    public GoalController(GoalService goalService, UserService userService) {
        this.goalService = goalService;
        this.userService = userService;
    }

    @Operation(summary = "Получить все цели пользователя")
    @GetMapping
    public String getAllGoals(Model model, Principal principal) {
        UUID id = userService.findByUsername(principal.getName()).getId();
        List<Goal> goals = goalService.findAllGoalsByUserId(id);
        model.addAttribute("goals", goals);
        return "goals";
    }

    @Operation(summary = "Показать форму создания цели")
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("goal", new Goal());
        return "create-goal";
    }

    @Operation(summary = "Создать новую цель")
    @PostMapping("/create")
    public String createGoal(@Parameter(description = "Цель") @ModelAttribute Goal goal, Principal principal) {
        logger.info("Создание цели для пользователя: {}", principal.getName());
        User user = userService.findByUsername(principal.getName());
        goal.setUser(user);
        goalService.save(goal);
        return "redirect:/goals";
    }

    @Operation(summary = "Переключить статус цели по ID")
    @PostMapping("/{id}/toggle")
    public String toggleGoalStatus(@Parameter(description = "ID цели") @PathVariable UUID id,
                                   Principal principal,
                                   @RequestHeader(value = "Referer", required = false) String referer) {
        logger.info("Переключение статуса цели с ID: {} пользователем {}", id, principal.getName());
        Goal goal = goalService.findGoalById(id);
        if (!goal.getUser().getUsername().equals(principal.getName())) {
            logger.warn("Попытка переключить чужую цель: {}", id);
            return "redirect:/access-denied";
        }
        goal.setCompleted(!goal.isCompleted());
        goalService.save(goal);
        return "redirect:" + (referer != null ? referer : "/goals");
    }



    @Operation(summary = "Показать форму редактирования цели")
    @GetMapping("/{id}/edit")
    public String editGoal(@Parameter(description = "ID цели") @PathVariable UUID id, Model model) {
        Goal goal = goalService.findGoalById(id);



        model.addAttribute("goal", goal);
        model.addAttribute("formAction", "/goals/update");
        return "create-goal";
    }

    @Operation(summary = "Обновить цель")
    @PostMapping("/update")
    public String updateGoal(@ModelAttribute Goal goal, Principal principal) {
        logger.info("Обновление цели с ID: {} пользователем {}", goal.getId(), principal.getName());
        Goal original = goalService.findGoalById(goal.getId());
        if (!original.getUser().getUsername().equals(principal.getName())) {
            logger.warn("Попытка обновить чужую цель: {}", goal.getId());
            return "redirect:/goals";
        }

        original.setDescription(goal.getDescription());
        original.setDeadline(goal.getDeadline());
        original.setCompleted(goal.isCompleted());

        goalService.save(original);
        logger.info("Цель с ID: {} успешно обновлена", goal.getId());
        return "redirect:/goals";
    }

    @Operation(summary = "Удалить цель по ID")
    @PostMapping("/{id}/delete")
    public String deleteGoal(@Parameter(description = "ID цели") @PathVariable UUID id) {
        logger.info("Удаление цели с ID: {}", id);
        goalService.deleteGoal(id);
        return "redirect:/goals";
    }
}
