package org.example.mutants.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MutantDetectorTest {

    private MutantDetector mutantDetector;

    @BeforeEach
    void setUp() {
        mutantDetector = new MutantDetector();
    }

    @Test
    void detectsMutantWithHorizontalAndVertical() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };

        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    void detectsMutantWithDiagonalSequence() {
        String[] dna = {
                "ATGCGA",
                "CAGTAC",
                "TTATGT",
                "AGAAGG",
                "CTCCTA",
                "TCACTG"
        };

        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    void detectsMutantWithAntiDiagonalSequence() {
        String[] dna = {
                "ATGCGA",
                "CAGTAC",
                "TTATAT",
                "AGTAGG",
                "CTCCTA",
                "TCACTG"
        };

        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    void returnsFalseWhenHumanHasNoSequence() {
        String[] dna = {
                "ATGC",
                "CAGT",
                "TTAT",
                "AGAC"
        };

        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    void returnsFalseWithSingleSequenceOnly() {
        String[] dna = {
                "AAAA",
                "CAGT",
                "TTAT",
                "AGAC"
        };

        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    void returnsFalseForNullDna() {
        assertFalse(mutantDetector.isMutant(null));
    }

    @Test
    void returnsFalseForEmptyArray() {
        assertFalse(mutantDetector.isMutant(new String[]{}));
    }

    @Test
    void returnsFalseForNonSquareMatrix() {
        String[] dna = {"ATG", "CAG", "TTAA"};

        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    void returnsFalseForInvalidCharacters() {
        String[] dna = {"ATGX", "CAGT", "TTAT", "AGAC"};

        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    void detectsMutantWithVerticalAndDiagonalCombo() {
        String[] dna = {
                "ATGCGA",
                "AAGTGC",
                "ATATGT",
                "AGAAGG",
                "ACCCTA",
                "TCACTG"
        };

        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    void detectsMutantNearMatrixBorders() {
        String[] dna = {
                "AAAAGG",
                "CAGTGC",
                "TTAAGT",
                "AGGAGG",
                "CCCCTA",
                "TCACTG"
        };

        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    void handlesSmallValidMatrix() {
        String[] dna = {
                "ATGC",
                "AAGT",
                "ATAT",
                "AGAC"
        };

        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    void detectsMutantWithTwoHorizontalSequences() {
        String[] dna = {
                "AAAAGG",
                "CAGTGC",
                "TTTTGT",
                "AGAAGG",
                "CTCCTA",
                "TCACTG"
        };

        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    void humanSequenceWithSingleDiagonalDoesNotCountAsMutant() {
        String[] dna = {
                "ATGCGA",
                "CAGTAC",
                "TTCTGT",
                "AGATGG",
                "CTCCTA",
                "TCACTG"
        };

        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    void invalidWhenRowIsNull() {
        String[] dna = {"ATGC", null, "TTAT", "AGAC"};
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    void invalidWhenRowLengthDiffers() {
        String[] dna = {"ATGC", "CAGT", "TTAT", "AGACG"};
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    void detectsMutantWithDiagonalAndVertical() {
        String[] dna = {
                "ATGCAA",
                "CAGTGA",
                "TTATGT",
                "AGAAAG",
                "CTCCTA",
                "TCACTG"
        };

        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    void detectsMutantInLargeMatrixQuickly() {
        String[] dna = {
                "ATGCGATTCC",
                "CAGTGCTTCA",
                "TTATGTTTCA",
                "AGAAGGATTC",
                "CCCCTATTCG",
                "TCACTGATTC",
                "GATCGATTCG",
                "ATGCGATTCG",
                "CAGTGCTTCG",
                "TTATGTTTCG"
        };

        assertTrue(mutantDetector.isMutant(dna));
    }
}
