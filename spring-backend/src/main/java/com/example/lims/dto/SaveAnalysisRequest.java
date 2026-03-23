package com.example.lims.dto;

import java.util.List;

public record SaveAnalysisRequest(String sampleId, List<AnalysisResultDto> results) {
}
