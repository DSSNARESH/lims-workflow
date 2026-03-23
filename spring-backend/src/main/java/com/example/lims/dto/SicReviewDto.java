package com.example.lims.dto;

import java.util.List;

public record SicReviewDto(
        String reviewerName,
        Boolean decision,
        String comments,
        List<Long> flaggedRows
) {
}
