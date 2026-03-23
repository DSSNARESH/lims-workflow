package com.example.lims.dto;

public record TestSpecificationDto(
        String parameter,
        String acceptanceCriteria,
        String method,
        String referenceStandard,
        String assignedAnalyst,
        Long targetSla
) {
}
