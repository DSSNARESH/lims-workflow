package com.example.lims.config;

import com.example.lims.dto.*;
import com.example.lims.model.SampleStatus;
import com.example.lims.model.Verdict;
import com.example.lims.service.WorkflowService;
import java.time.Instant;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeedDataConfig {

    @Bean
    CommandLineRunner seedSamples(WorkflowService workflowService) {
        return args -> {
            if (!workflowService.getSamples().isEmpty()) {
                return;
            }

            workflowService.createSample(new SampleDto(
                    "S-1001",
                    "Acme Pharma",
                    "Paracetamol Tablets",
                    "Assay & Dissolution",
                    501L,
                    Instant.parse("2026-03-23T08:30:00Z"),
                    SampleStatus.PENDING,
                    new RfaDetailsDto(501L, 9001L, 7001L),
                    List.of(),
                    List.of(),
                    null,
                    null
            ));

            workflowService.saveTestSpec(new SaveTestSpecRequest(
                    "S-1001",
                    "Annie Analyst",
                    List.of(
                            new TestSpecificationDto("Assay", "95.0%-105.0%", "HPLC", "USP", null, 24L),
                            new TestSpecificationDto("Dissolution", ">= 80% in 30 min", "UV", "USP", null, 24L)
                    )
            ));

            workflowService.saveAnalysisResult(new SaveAnalysisRequest(
                    "S-1001",
                    List.of(
                            new AnalysisResultDto("Assay", "99.2", "%", Verdict.PASS, "Within limit"),
                            new AnalysisResultDto("Dissolution", "82", "%", Verdict.PASS, "Meets release profile")
                    )
            ));
        };
    }
}
