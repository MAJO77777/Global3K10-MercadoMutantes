package org.example.mutants.service;

import lombok.RequiredArgsConstructor;
import org.example.mutants.dto.StatsResponse;
import org.example.mutants.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final DnaRecordRepository repository;

    public StatsResponse getStats() {
        long mutantCount = repository.countByIsMutant(true);
        long humanCount = repository.countByIsMutant(false);

        double ratio = humanCount == 0 ? (mutantCount == 0 ? 0.0 : mutantCount) : (double) mutantCount / humanCount;

        return new StatsResponse(mutantCount, humanCount, ratio);
    }
}
