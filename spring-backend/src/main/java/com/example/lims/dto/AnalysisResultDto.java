package com.example.lims.dto;

import com.example.lims.model.Verdict;

public record AnalysisResultDto(
        String parameter,
        String observedValue,
        String unit,
        Verdict verdict,
        String remark
) {
}
