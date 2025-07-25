package com.main.reflect_diary.service;

import com.main.reflect_diary.model.Entry;
import com.main.reflect_diary.repository.EntryRepository;
import com.main.reflect_diary.service.service_impl.EntryServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EntryServiceImplTest {

    @Mock
    private EntryRepository entryRepository;

    @InjectMocks
    private EntryServiceImpl entryService;

    private Entry entry;
    private final UUID entryId = UUID.randomUUID();
    private final UUID userId = UUID.randomUUID();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        entry = new Entry();
        entry.setId(entryId);
        entry.setMainText("Test entry");
    }

    @Test
    public void testFindByIdExists() {
        when(entryRepository.findById(entryId)).thenReturn(Optional.of(entry));
        Entry result = entryService.findById(entryId);
        assertEquals("Test entry", result.getMainText());
    }

    @Test
    public void testFindByIdNotExists() {
        when(entryRepository.findById(entryId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> entryService.findById(entryId));
    }

    @Test
    public void testSave() {
        when(entryRepository.save(any(Entry.class))).thenAnswer(i -> i.getArgument(0));
        Entry saved = entryService.save(entry);
        assertNotNull(saved.getCreatedAt());
        verify(entryRepository, times(1)).save(entry);
    }

    @Test
    public void testDeleteByIdExists() {
        when(entryRepository.findById(entryId)).thenReturn(Optional.of(entry));
        entryService.deleteById(entryId);
        verify(entryRepository, times(1)).delete(entry);
    }

    @Test
    public void testDeleteByIdNotExists() {
        when(entryRepository.findById(entryId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> entryService.deleteById(entryId));
    }

    @Test
    public void testFindByUserId() {
        when(entryRepository.findByUserId(userId)).thenReturn(List.of(entry));
        List<Entry> result = entryService.findByUserId(userId);
        assertEquals(1, result.size());
        verify(entryRepository, times(1)).findByUserId(userId);
    }
}