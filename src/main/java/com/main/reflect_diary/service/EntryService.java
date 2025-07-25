package com.main.reflect_diary.service;

import com.main.reflect_diary.model.Entry;

import java.util.List;
import java.util.UUID;
import java.util.Map;

public interface EntryService {
    Entry findById(UUID id);
    Entry save(Entry entry);
    void deleteById(UUID id);
    List<Entry> findByUserId(UUID userId);
    Map<java.time.LocalDate, String> getMoodByDateForUser(UUID userId);
}
