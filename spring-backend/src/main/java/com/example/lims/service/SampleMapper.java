package com.example.lims.service;

import com.example.lims.dto.*;
import com.example.lims.model.*;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SampleMapper {

    public SampleDto toDto(SampleEntity sample) {
        return new SampleDto(
                sample.getSampleId(),
                sample.getClientName(),
                sample.getSampleName(),
                sample.getTestName(),
                sample.getRegistrationId(),
                sample.getDateReceived(),
                sample.getSampleStatus(),
                new RfaDetailsDto(sample.getRfa().getRegistration(), sample.getRfa().getBilling(), sample.getRfa().getSampleDetails()),
                sample.getTestSpecs().stream().map(this::toDto).toList(),
                sample.getAnalysisResults().stream().map(this::toDto).toList(),
                sample.getSicReview() == null ? null : toDto(sample.getSicReview()),
                sample.getQaReview() == null ? null : toDto(sample.getQaReview())
        );
    }

    public TestSpecificationDto toDto(TestSpecificationEntity entity) {
        return new TestSpecificationDto(entity.getParameter(), entity.getAcceptanceCriteria(), entity.getMethod(), entity.getReferenceStandard(), entity.getAssignedAnalyst(), entity.getTargetSla());
    }

    public AnalysisResultDto toDto(AnalysisResultEntity entity) {
        return new AnalysisResultDto(entity.getParameter(), entity.getObservedValue(), entity.getUnit(), entity.getVerdict(), entity.getRemark());
    }

    public SicReviewDto toDto(SicReviewEntity entity) {
        return new SicReviewDto(entity.getReviewerName(), entity.getDecision(), entity.getComments(), entity.getFlaggedRows());
    }

    public QaReviewDto toDto(QaReviewEntity entity) {
        return new QaReviewDto(entity.getQaHeadName(), entity.getDecision(), entity.getComments());
    }

    public TestSpecificationEntity toEntity(TestSpecificationDto dto, String assignedAnalyst, SampleEntity sample) {
        TestSpecificationEntity entity = new TestSpecificationEntity();
        entity.setParameter(dto.parameter());
        entity.setAcceptanceCriteria(dto.acceptanceCriteria());
        entity.setMethod(dto.method());
        entity.setReferenceStandard(dto.referenceStandard());
        entity.setAssignedAnalyst(assignedAnalyst != null && !assignedAnalyst.isBlank() ? assignedAnalyst : dto.assignedAnalyst());
        entity.setTargetSla(dto.targetSla());
        entity.setSample(sample);
        return entity;
    }

    public AnalysisResultEntity toEntity(AnalysisResultDto dto, SampleEntity sample) {
        AnalysisResultEntity entity = new AnalysisResultEntity();
        entity.setParameter(dto.parameter());
        entity.setObservedValue(dto.observedValue());
        entity.setUnit(dto.unit());
        entity.setVerdict(dto.verdict());
        entity.setRemark(dto.remark());
        entity.setSample(sample);
        return entity;
    }

    public SicReviewEntity toEntity(SicReviewDto dto, SampleEntity sample) {
        SicReviewEntity entity = new SicReviewEntity();
        entity.setReviewerName(dto.reviewerName());
        entity.setDecision(dto.decision());
        entity.setComments(dto.comments());
        entity.setFlaggedRows(dto.flaggedRows() == null ? List.of() : dto.flaggedRows());
        entity.setSample(sample);
        return entity;
    }

    public QaReviewEntity toEntity(QaReviewDto dto, SampleEntity sample) {
        QaReviewEntity entity = new QaReviewEntity();
        entity.setQaHeadName(dto.qaHeadName());
        entity.setDecision(dto.decision());
        entity.setComments(dto.comments());
        entity.setSample(sample);
        return entity;
    }
}
