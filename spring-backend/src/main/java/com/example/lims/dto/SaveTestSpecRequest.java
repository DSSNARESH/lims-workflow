package com.example.lims.dto;

import java.util.List;

public record SaveTestSpecRequest(String sampleId, String assignedAnalyst, List<TestSpecificationDto> testSpecs) {
}
