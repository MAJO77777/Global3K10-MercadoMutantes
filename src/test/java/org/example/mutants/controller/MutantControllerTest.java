package org.example.mutants.controller;

import org.example.mutants.repository.DnaRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MutantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DnaRecordRepository repository;

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }

    @Test
    void testMutantEndpoint_ReturnOk() throws Exception {
        String dnaJson = """
                {"dna": ["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]}
                """;

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dnaJson))
                .andExpect(status().isOk());
    }

    @Test
    void testHumanEndpoint_ReturnForbidden() throws Exception {
        String dnaJson = """
                {"dna": ["ATGCGA","CAGTGC","TTATTT","AGACGG","GCGTCA","TCACTG"]}
                """;

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dnaJson))
                .andExpect(status().isForbidden());
    }

    @Test
    void testInvalidDna_ReturnBadRequest() throws Exception {
        String dnaJson = """
                {"dna": ["ATGX","CAGT"]}
                """;

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dnaJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testStatsEndpoint_ReturnOk() throws Exception {
        mockMvc.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count_mutant_dna", notNullValue()))
                .andExpect(jsonPath("$.count_human_dna", notNullValue()))
                .andExpect(jsonPath("$.ratio", notNullValue()));
    }
}
