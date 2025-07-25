package com.main.reflect_diary.controller;

import com.main.reflect_diary.model.Entry;
import com.main.reflect_diary.model.ReflectionQuestion;
import com.main.reflect_diary.model.User;
import com.main.reflect_diary.service.EntryService;
import com.main.reflect_diary.service.ReflectionQuestionService;
import com.main.reflect_diary.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@io.swagger.v3.oas.annotations.tags.Tag(name = "Записи", description = "Операции с дневниковыми записями")
@Controller
@RequestMapping("/entries")
public class EntryController {

    private static final Logger logger = LoggerFactory.getLogger(EntryController.class);
    private final EntryService entryService;
    private final ReflectionQuestionService reflectionQuestionService;
    private final UserService userService;

    public EntryController(EntryService entryService, ReflectionQuestionService reflectionQuestionService, UserService userService) {
        this.entryService = entryService;
        this.reflectionQuestionService = reflectionQuestionService;
        this.userService = userService;
    }

    @Operation(summary = "Получить все записи пользователя")
    @GetMapping
    public String getAllEntries(Model model, Principal principal) {
        UUID id = userService.findByUsername(principal.getName()).getId();
        List<Entry> entries = entryService
                .findByUserId(id)
                .stream()
                .sorted(Comparator.comparing(Entry::getDate).reversed())
                .toList();
        model.addAttribute("entries", entries);
        return "entries";
    }

    @Operation(summary = "Получить запись по ID")
    @GetMapping("/{id}")
    public String getEntryById(@Parameter(description = "ID записи") @PathVariable UUID id, Model model) {
        Entry entry = entryService.findById(id);
        ReflectionQuestion reflectionQuestion = reflectionQuestionService.findById(entry.getReflectionQuestionId());
        model.addAttribute("reflectionQuestion", reflectionQuestion);
        model.addAttribute("entry", entry);
        return "entry-details";
    }

    @Operation(summary = "Показать форму создания записи")
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        Entry entry = new Entry();

        ReflectionQuestion q = reflectionQuestionService.getRandomQuestion();
        if (q != null) {
            entry.setReflectionQuestionId(q.getId());
            model.addAttribute("question", q);
        } else {
            model.addAttribute("questionMissing", true);
        }
        model.addAttribute("entry", entry);
        model.addAttribute("selectedTags", new ArrayList<>());
        return "create-entry";
    }

    @Operation(summary = "Создать новую запись")
    @PostMapping("/create")
    public String createEntry(@Parameter(description = "Запись") @ModelAttribute Entry entry,
                              Principal principal,
                              @Parameter(description = "ID тегов") @RequestParam(value = "tags", required = false) List<UUID> tagIds) {
        logger.info("Создание записи для пользователя: {}", principal.getName());


        User user = userService.findByUsername(principal.getName());
        entry.setUser(user);
        entryService.save(entry);

        return "redirect:/entries";
    }


    @Operation(summary = "Удалить запись по ID")
    @PostMapping("/{id}/delete")
    public String deleteEntry(@Parameter(description = "ID записи") @PathVariable UUID id) {
        logger.info("Удаление записи с ID: {}", id);
        entryService.deleteById(id);
        return "redirect:/entries";
    }

    @Operation(summary = "Показать форму редактирования записи")
    @GetMapping("/{id}/edit")
    public String editEntry(@Parameter(description = "ID записи") @PathVariable UUID id, Model model, Principal principal) {
        Entry entry = entryService.findById(id);

        if (!entry.getUser().getUsername().equals(principal.getName())) {
            return "redirect:/entries";
        }
        model.addAttribute("question", reflectionQuestionService.findById(entry.getReflectionQuestionId()));
        model.addAttribute("entry", entry);
        model.addAttribute("formAction", "/entries/update");

        return "create-entry";
    }

    @Operation(summary = "Обновить запись")
    @PostMapping("/update")
    public String update(@Parameter(description = "ID записи") @RequestParam("id") UUID id,
                         @Parameter(description = "ID тегов") @RequestParam(value = "tags", required = false) List<UUID> tagIds,
                         @ModelAttribute Entry entryFromForm,
                         Principal principal) {
        logger.info("Обновление записи с ID: {} для пользователя {}", id, principal.getName());
        Entry originalEntry = entryService.findById(id);
        if (!originalEntry.getUser().getUsername().equals(principal.getName())) {
            logger.warn("Попытка обновить чужую запись: {}", id);
            return "redirect:/entries";
        }

        originalEntry.setScore(entryFromForm.getScore());
        originalEntry.setDate(entryFromForm.getDate());
        originalEntry.setReflectionQuestionId(entryFromForm.getReflectionQuestionId());
        originalEntry.setTomorrowPlan(entryFromForm.getTomorrowPlan());
        originalEntry.setDifficulties(entryFromForm.getDifficulties());
        originalEntry.setHighlights(entryFromForm.getHighlights());
        originalEntry.setMainText(entryFromForm.getMainText());
        originalEntry.setMood(entryFromForm.getMood());
        originalEntry.setQuestionAnswer(entryFromForm.getQuestionAnswer());

        entryService.save(originalEntry);
        logger.info("Запись с ID: {} успешно обновлена", id);
        return "redirect:/entries";
    }
}
