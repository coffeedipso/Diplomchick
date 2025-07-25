package com.main.reflect_diary.service.service_impl;

import com.main.reflect_diary.model.Entry;
import com.main.reflect_diary.repository.EntryRepository;
import com.main.reflect_diary.service.EntryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class EntryServiceImpl implements EntryService {

    private static final Logger logger = LoggerFactory.getLogger(EntryServiceImpl.class);
    private final EntryRepository entryRepository;

    public EntryServiceImpl(EntryRepository entryRepository) {
        this.entryRepository = entryRepository;
    }

    @Override
    public Entry findById(UUID id) {
        logger.info("Поиск записи по ID: {}", id);
        return entryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entry with id " + id + " not found"));
    }

    @Override
    @Transactional
    public Entry save(Entry entry) {
        logger.info("Сохранение записи пользователя: {}", entry.getUser() != null ? entry.getUser().getUsername() : "неизвестно");
        entry.setCreatedAt(LocalDateTime.now());
        return entryRepository.save(entry);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        logger.info("Удаление записи с ID: {}", id);
        Entry entry = entryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entry with id " + id + " not found"));
        entryRepository.delete(entry);
    }

    @Override
    public List<Entry> findByUserId(UUID userId) {
        logger.info("Получение всех записей пользователя с ID: {}", userId);
        return entryRepository.findByUserId(userId);
    }

    @Override
    public java.util.Map<java.time.LocalDate, String> getMoodByDateForUser(UUID userId) {
        List<Entry> entries = entryRepository.findByUserId(userId);
        java.util.Map<java.time.LocalDate, String> moodByDate = new java.util.HashMap<>();
        for (Entry entry : entries) {
            moodByDate.put(entry.getDate(), entry.getMood().getEmoji());
        }
        return moodByDate;
    }
}
