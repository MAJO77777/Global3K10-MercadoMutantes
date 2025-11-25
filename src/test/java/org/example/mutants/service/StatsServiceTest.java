package org.example.mutants.service;

import org.example.mutants.dto.StatsResponse;
import org.example.mutants.repository.DnaRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {

    @Mock
    private DnaRecordRepository repository;

    @InjectMocks
    private StatsService statsService;

    @Test
    void returnsZeroRatioWhenNoHumans() {
        when(repository.countByIsMutant(true)).thenReturn(5L);
        when(repository.countByIsMutant(false)).thenReturn(0L);

        StatsResponse response = statsService.getStats();

        assertEquals(5L, response.getCount_mutant_dna());
        assertEquals(0L, response.getCount_human_dna());
        assertEquals(5.0, response.getRatio());
    }

    @Test
    void calculatesRatioWhenHumansExist() {
        when(repository.countByIsMutant(true)).thenReturn(4L);
        when(repository.countByIsMutant(false)).thenReturn(2L);

        StatsResponse response = statsService.getStats();

        assertEquals(2.0, response.getRatio());
    }

    @Test
    void returnsZeroWhenNoRecords() {
        when(repository.countByIsMutant(anyBoolean())).thenReturn(0L);

        StatsResponse response = statsService.getStats();

        assertEquals(0.0, response.getRatio());
        assertEquals(0L, response.getCount_human_dna());
        assertEquals(0L, response.getCount_mutant_dna());
    }
}
