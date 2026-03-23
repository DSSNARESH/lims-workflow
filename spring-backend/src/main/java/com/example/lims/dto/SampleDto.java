package com.example.lims.dto;

import com.example.lims.model.SampleStatus;
import java.time.Instant;
import java.util.List;

public record SampleDto(
        String sampleId,
        String clientName,
        String sampleName,
        String testName,
        Long registrationId,
        Instant dateReceived,
        SampleStatus sampleStatus,
        RfaDetailsDto rfa,
        List<TestSpecificationDto> testSpecs,
        List<AnalysisResultDto> analysisResults,
        SicReviewDto sicReview,
        QaReviewDto qaReview
) {
}
