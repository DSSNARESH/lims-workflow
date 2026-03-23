package com.example.lims.dto;

public record QaReviewDto(
        String qaHeadName,
        Boolean decision,
        String comments
) {
}
