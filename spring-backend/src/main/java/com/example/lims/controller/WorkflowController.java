package com.example.lims.controller;

import com.example.lims.dto.*;
import com.example.lims.service.WorkflowService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workflow")
@CrossOrigin(origins = "http://localhost:5173")
public class WorkflowController {
    private final WorkflowService workflowService;

    public WorkflowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @PostMapping("/test-spec")
    public List<TestSpecificationDto> saveTestSpec(@RequestBody SaveTestSpecRequest request) {
        return workflowService.saveTestSpec(request);
    }

    @GetMapping("/test-spec/{sampleId}")
    public List<TestSpecificationDto> getTestSpec(@PathVariable String sampleId) {
        return workflowService.getTestSpec(sampleId);
    }

    @PostMapping("/analysis")
    public List<AnalysisResultDto> saveAnalysis(@RequestBody SaveAnalysisRequest request) {
        return workflowService.saveAnalysisResult(request);
    }

    @GetMapping("/analysis/{sampleId}")
    public List<AnalysisResultDto> getAnalysis(@PathVariable String sampleId) {
        return workflowService.getAnalysisResult(sampleId);
    }

    @PostMapping("/analysis/{sampleId}/submit")
    public SampleDto submitAnalysis(@PathVariable String sampleId) {
        return workflowService.submitAnalysis(sampleId);
    }

    @PostMapping("/sic-review")
    public SicReviewDto saveSicReview(@RequestBody SaveSicReviewRequest request) {
        return workflowService.saveSICReview(request);
    }

    @GetMapping("/sic-review/{sampleId}")
    public SicReviewDto getSicReview(@PathVariable String sampleId) {
        return workflowService.getSICReview(sampleId);
    }

    @PostMapping("/sic-review/{sampleId}/approve")
    public SampleDto approveSicReview(@PathVariable String sampleId) {
        return workflowService.approveSICReview(sampleId);
    }

    @PostMapping("/sic-review/{sampleId}/reject")
    public SampleDto rejectSicReview(@PathVariable String sampleId) {
        return workflowService.rejectSICReview(sampleId);
    }

    @PostMapping("/qa-review")
    public QaReviewDto saveQaReview(@RequestBody SaveQaReviewRequest request) {
        return workflowService.saveQAReview(request);
    }

    @GetMapping("/qa-review/{sampleId}")
    public QaReviewDto getQaReview(@PathVariable String sampleId) {
        return workflowService.getQAReview(sampleId);
    }

    @PostMapping("/qa-review/{sampleId}/approve")
    public SampleDto approveQaReview(@PathVariable String sampleId) {
        return workflowService.approveQAReview(sampleId);
    }

    @PostMapping("/qa-review/{sampleId}/reject")
    public SampleDto rejectQaReview(@PathVariable String sampleId) {
        return workflowService.rejectQAReview(sampleId);
    }
}
