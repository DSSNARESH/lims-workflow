package com.example.lims.service;

import com.example.lims.dto.*;
import com.example.lims.model.*;
import com.example.lims.repository.SampleRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WorkflowService {
    private final SampleRepository sampleRepository;
    private final SampleMapper mapper;

    public WorkflowService(SampleRepository sampleRepository, SampleMapper mapper) {
        this.sampleRepository = sampleRepository;
        this.mapper = mapper;
    }

    public List<SampleDto> getSamples() {
        return sampleRepository.findAll().stream().map(mapper::toDto).toList();
    }

    public SampleDto getSample(String sampleId) {
        return mapper.toDto(getSampleEntity(sampleId));
    }

    public SampleDto createSample(SampleDto dto) {
        SampleEntity sample = new SampleEntity();
        sample.setSampleId(dto.sampleId());
        sample.setClientName(dto.clientName());
        sample.setSampleName(dto.sampleName());
        sample.setTestName(dto.testName());
        sample.setRegistrationId(dto.registrationId() == null ? 0L : dto.registrationId());
        sample.setDateReceived(dto.dateReceived() == null ? Instant.now() : dto.dateReceived());
        sample.setSampleStatus(dto.sampleStatus() == null ? SampleStatus.PENDING : dto.sampleStatus());
        RfaDetails rfa = new RfaDetails();
        if (dto.rfa() != null) {
            rfa.setRegistration(defaultLong(dto.rfa().registration()));
            rfa.setBilling(defaultLong(dto.rfa().billing()));
            rfa.setSampleDetails(defaultLong(dto.rfa().sampleDetails()));
        }
        sample.setRfa(rfa);
        return mapper.toDto(sampleRepository.save(sample));
    }

    public SampleDto updateStage(String sampleId, SampleStatus stage) {
        SampleEntity sample = getSampleEntity(sampleId);
        sample.setSampleStatus(stage);
        return mapper.toDto(sample);
    }

    public List<TestSpecificationDto> saveTestSpec(SaveTestSpecRequest request) {
        SampleEntity sample = getSampleEntity(request.sampleId());
        sample.getTestSpecs().clear();
        List<TestSpecificationDto> specs = request.testSpecs() == null ? List.of() : request.testSpecs();
        specs.stream()
                .map(spec -> mapper.toEntity(spec, request.assignedAnalyst(), sample))
                .forEach(sample.getTestSpecs()::add);
        sample.setSampleStatus(SampleStatus.ANALYSIS);
        return sample.getTestSpecs().stream().map(mapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<TestSpecificationDto> getTestSpec(String sampleId) {
        return getSampleEntity(sampleId).getTestSpecs().stream().map(mapper::toDto).toList();
    }

    public List<AnalysisResultDto> saveAnalysisResult(SaveAnalysisRequest request) {
        SampleEntity sample = getSampleEntity(request.sampleId());
        sample.getAnalysisResults().clear();
        List<AnalysisResultDto> results = request.results() == null ? List.of() : request.results();
        results.stream().map(result -> mapper.toEntity(result, sample)).forEach(sample.getAnalysisResults()::add);
        return sample.getAnalysisResults().stream().map(mapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<AnalysisResultDto> getAnalysisResult(String sampleId) {
        return getSampleEntity(sampleId).getAnalysisResults().stream().map(mapper::toDto).toList();
    }

    public SampleDto submitAnalysis(String sampleId) {
        SampleEntity sample = getSampleEntity(sampleId);
        List<AnalysisResultEntity> normalized = new ArrayList<>();
        for (AnalysisResultEntity result : sample.getAnalysisResults()) {
            if (result.getVerdict() == null) {
                result.setVerdict(Verdict.PASS);
            }
            if (result.getVerdict() == Verdict.OOS && (result.getRemark() == null || result.getRemark().isBlank())) {
                result.setRemark("Out of specification result flagged during submission.");
            }
            normalized.add(result);
        }
        sample.getAnalysisResults().clear();
        sample.getAnalysisResults().addAll(normalized);
        sample.setSampleStatus(SampleStatus.SIC_REVIEW);
        return mapper.toDto(sample);
    }

    public SicReviewDto saveSICReview(SaveSicReviewRequest request) {
        SampleEntity sample = getSampleEntity(request.sampleId());
        sample.setSicReview(mapper.toEntity(request.review(), sample));
        return mapper.toDto(sample.getSicReview());
    }

    @Transactional(readOnly = true)
    public SicReviewDto getSICReview(String sampleId) {
        SampleEntity sample = getSampleEntity(sampleId);
        return sample.getSicReview() == null ? null : mapper.toDto(sample.getSicReview());
    }

    public SampleDto approveSICReview(String sampleId) {
        SampleEntity sample = getSampleEntity(sampleId);
        if (sample.getSicReview() != null) {
            sample.getSicReview().setDecision(true);
        }
        sample.setSampleStatus(SampleStatus.QA_REVIEW);
        return mapper.toDto(sample);
    }

    public SampleDto rejectSICReview(String sampleId) {
        SampleEntity sample = getSampleEntity(sampleId);
        if (sample.getSicReview() != null) {
            sample.getSicReview().setDecision(false);
        }
        sample.setSampleStatus(SampleStatus.ANALYSIS);
        return mapper.toDto(sample);
    }

    public QaReviewDto saveQAReview(SaveQaReviewRequest request) {
        SampleEntity sample = getSampleEntity(request.sampleId());
        sample.setQaReview(mapper.toEntity(request.review(), sample));
        return mapper.toDto(sample.getQaReview());
    }

    @Transactional(readOnly = true)
    public QaReviewDto getQAReview(String sampleId) {
        SampleEntity sample = getSampleEntity(sampleId);
        return sample.getQaReview() == null ? null : mapper.toDto(sample.getQaReview());
    }

    public SampleDto approveQAReview(String sampleId) {
        SampleEntity sample = getSampleEntity(sampleId);
        if (sample.getQaReview() != null) {
            sample.getQaReview().setDecision(true);
        }
        sample.setSampleStatus(SampleStatus.COA);
        return mapper.toDto(sample);
    }

    public SampleDto rejectQAReview(String sampleId) {
        SampleEntity sample = getSampleEntity(sampleId);
        if (sample.getQaReview() != null) {
            sample.getQaReview().setDecision(false);
        }
        sample.setSampleStatus(SampleStatus.SIC_REVIEW);
        return mapper.toDto(sample);
    }

    private SampleEntity getSampleEntity(String sampleId) {
        return sampleRepository.findById(sampleId)
                .orElseThrow(() -> new EntityNotFoundException("Sample not found: " + sampleId));
    }

    private long defaultLong(Long value) {
        return value == null ? 0L : value;
    }
}
