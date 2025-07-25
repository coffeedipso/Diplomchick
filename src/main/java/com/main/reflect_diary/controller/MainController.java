package com.main.reflect_diary.controller;

import com.main.reflect_diary.model.Entry;
import com.main.reflect_diary.model.Goal;
import com.main.reflect_diary.model.User;
import com.main.reflect_diary.service.EntryService;
import com.main.reflect_diary.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Главная", description = "Главная страница пользователя")
@Controller
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    UserService userService;
    EntryService entryService;

    public MainController(UserService userService, EntryService entryService) {
        this.userService = userService;
        this.entryService = entryService;
    }

    @Operation(summary = "Главная страница пользователя с превью целей и записей")
    @GetMapping("/")
    public String index(Model model, Principal principal) {
        logger.info("Пользователь {} зашёл на главную страницу", principal.getName());
        String username = principal.getName();
        User user = userService.findByUsername(username);

        List<Goal> goals = user.getGoals();
        List<Entry> entries = user.getEntries();

        List<Goal> goalsPreview = goals.stream()
                .sorted(Comparator.comparing(Goal::getDeadline))
                .limit(3)
                .toList();

        List<Entry> entriesPreview = entries.stream()
                .sorted(Comparator.comparing(Entry::getDate).reversed())
                .limit(3)
                .toList();

        // --- Календарь настроения ---
        YearMonth currentMonth = YearMonth.now();
        LocalDate firstOfMonth = currentMonth.atDay(1);
        LocalDate lastOfMonth = currentMonth.atEndOfMonth();
        LocalDate today = LocalDate.now();

        // Сопоставление дат и записей
        Map<LocalDate, Entry> entryByDate = new HashMap<>();
        for (Entry entry : entries) {
            entryByDate.put(entry.getDate(), entry);
        }

        // Список недель, каждая неделя — список дней
        ArrayList<ArrayList<Map<String, Object>>> calendarWeeks = new ArrayList<>();
        LocalDate calendarStart = firstOfMonth;
        while (calendarStart.getDayOfWeek().getValue() != 1) { // 1 = Monday
            calendarStart = calendarStart.minusDays(1);
        }
        LocalDate calendarEnd = lastOfMonth;
        while (calendarEnd.getDayOfWeek().getValue() != 7) { // 7 = Sunday
            calendarEnd = calendarEnd.plusDays(1);
        }
        LocalDate date = calendarStart;
        while (!date.isAfter(calendarEnd)) {
            ArrayList<Map<String, Object>> week = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                Map<String, Object> day = new HashMap<>();
                day.put("dayOfMonth", date.getDayOfMonth());
                day.put("today", date.equals(today));
                day.put("entry", entryByDate.get(date));
                week.add(day);
                date = date.plusDays(1);
            }
            calendarWeeks.add(week);
        }
        // --- конец календаря ---

        model.addAttribute("user", user);
        model.addAttribute("goals", goalsPreview);
        model.addAttribute("entries", entriesPreview);
        model.addAttribute("calendarWeeks", calendarWeeks);
        model.addAttribute("moodByDate", entryService.getMoodByDateForUser(user.getId()));
        return "index";
    }

}
