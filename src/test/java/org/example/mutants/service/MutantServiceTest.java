package org.example.mutants.service;

import org.example.mutants.entity.DnaRecord;
import org.example.mutants.exception.DnaHashCalculationException;
import org.example.mutants.repository.DnaRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MutantServiceTest {

    @Mock
    private DnaRecordRepository repository;

    @Mock
    private MutantDetector mutantDetector;

    @InjectMocks
    private MutantService mutantService;

    @Test
    void returnsCachedResultWhenHashExists() {
        DnaRecord record = new DnaRecord();
        record.setDnaHash("hash");
        record.setMutant(true);
        record.setCreatedAt(LocalDateTime.now());

        when(repository.findByDnaHash(anyString())).thenReturn(Optional.of(record));

        boolean result = mutantService.analyzeDna(new String[]{"AAAA", "AAAA", "AAAA", "AAAA"});

        assertTrue(result);
        verify(repository, never()).save(any());
        verify(mutantDetector, never()).isMutant(any());
    }

    @Test
    void savesNewMutantRecordWhenNotCached() {
        String[] dna = {"ATGC", "AAGT", "ATAT", "AGAC"};
        when(repository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(dna)).thenReturn(true);

        mutantService.analyzeDna(dna);

        ArgumentCaptor<DnaRecord> captor = ArgumentCaptor.forClass(DnaRecord.class);
        verify(repository).save(captor.capture());
        assertTrue(captor.getValue().isMutant());
        assertNotNull(captor.getValue().getDnaHash());
        assertNotNull(captor.getValue().getCreatedAt());
    }

    @Test
    void savesNewHumanRecordWhenNotCached() {
        String[] dna = {"ATGC", "CAGT", "TTAT", "AGAC"};
        when(repository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(dna)).thenReturn(false);

        boolean result = mutantService.analyzeDna(dna);

        ArgumentCaptor<DnaRecord> captor = ArgumentCaptor.forClass(DnaRecord.class);
        verify(repository).save(captor.capture());
        assertFalse(result);
        assertFalse(captor.getValue().isMutant());
    }

    @Test
    void throwsCustomExceptionWhenHashFails() {
        MutantService spyService = Mockito.spy(mutantService);
        doThrow(new DnaHashCalculationException("fail", new RuntimeException("missing")))
                .when(spyService).calculateDnaHash(any());

        assertThrows(DnaHashCalculationException.class, () -> spyService.analyzeDna(new String[]{"A"}));
        verify(repository, never()).save(any());
    }
}
