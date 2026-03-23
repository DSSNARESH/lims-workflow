package com.example.lims.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class WorkflowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void returnsSeededSamples() throws Exception {
        mockMvc.perform(get("/api/samples"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sampleId").value("S-1001"));
    }

    @Test
    void savesAndApprovesWorkflowStages() throws Exception {
        mockMvc.perform(post("/api/workflow/sic-review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "sampleId":"S-1001",
                                  "review":{
                                    "reviewerName":"Sam SIC",
                                    "decision":true,
                                    "comments":"Looks good",
                                    "flaggedRows":[1]
                                  }
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewerName").value("Sam SIC"));

        mockMvc.perform(post("/api/workflow/sic-review/S-1001/approve"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sampleStatus").value("QA_REVIEW"));

        mockMvc.perform(post("/api/workflow/qa-review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "sampleId":"S-1001",
                                  "review":{
                                    "qaHeadName":"Quinn QA",
                                    "decision":true,
                                    "comments":"Approved for COA"
                                  }
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.qaHeadName").value("Quinn QA"));

        mockMvc.perform(post("/api/workflow/qa-review/S-1001/approve"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sampleStatus").value("COA"));
    }
}
