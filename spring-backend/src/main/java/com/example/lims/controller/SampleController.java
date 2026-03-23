package com.example.lims.controller;

import com.example.lims.dto.SampleDto;
import com.example.lims.model.SampleStatus;
import com.example.lims.service.WorkflowService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/samples")
@CrossOrigin(origins = "http://localhost:5173")
public class SampleController {
    private final WorkflowService workflowService;

    public SampleController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @GetMapping
    public List<SampleDto> getSamples() {
        return workflowService.getSamples();
    }

    @GetMapping("/{sampleId}")
    public SampleDto getSample(@PathVariable String sampleId) {
        return workflowService.getSample(sampleId);
    }

    @PostMapping
    public SampleDto createSample(@RequestBody SampleDto sampleDto) {
        return workflowService.createSample(sampleDto);
    }

    @PutMapping("/{sampleId}/stage")
    public SampleDto updateStage(@PathVariable String sampleId, @RequestParam SampleStatus stage) {
        return workflowService.updateStage(sampleId, stage);
    }
}
