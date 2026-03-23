package com.example.lims.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "samples")
public class SampleEntity {
    @Id
    private String sampleId;

    private String clientName;
    private String sampleName;
    private String testName;
    private Long registrationId;
    private Instant dateReceived;

    @Enumerated(EnumType.STRING)
    private SampleStatus sampleStatus;

    @Embedded
    private RfaDetails rfa = new RfaDetails();

    @OneToMany(mappedBy = "sample", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestSpecificationEntity> testSpecs = new ArrayList<>();

    @OneToMany(mappedBy = "sample", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnalysisResultEntity> analysisResults = new ArrayList<>();

    @OneToOne(mappedBy = "sample", cascade = CascadeType.ALL, orphanRemoval = true)
    private SicReviewEntity sicReview;

    @OneToOne(mappedBy = "sample", cascade = CascadeType.ALL, orphanRemoval = true)
    private QaReviewEntity qaReview;

    public String getSampleId() { return sampleId; }
    public void setSampleId(String sampleId) { this.sampleId = sampleId; }
    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }
    public String getSampleName() { return sampleName; }
    public void setSampleName(String sampleName) { this.sampleName = sampleName; }
    public String getTestName() { return testName; }
    public void setTestName(String testName) { this.testName = testName; }
    public Long getRegistrationId() { return registrationId; }
    public void setRegistrationId(Long registrationId) { this.registrationId = registrationId; }
    public Instant getDateReceived() { return dateReceived; }
    public void setDateReceived(Instant dateReceived) { this.dateReceived = dateReceived; }
    public SampleStatus getSampleStatus() { return sampleStatus; }
    public void setSampleStatus(SampleStatus sampleStatus) { this.sampleStatus = sampleStatus; }
    public RfaDetails getRfa() { return rfa; }
    public void setRfa(RfaDetails rfa) { this.rfa = rfa; }
    public List<TestSpecificationEntity> getTestSpecs() { return testSpecs; }
    public List<AnalysisResultEntity> getAnalysisResults() { return analysisResults; }
    public SicReviewEntity getSicReview() { return sicReview; }
    public void setSicReview(SicReviewEntity sicReview) { this.sicReview = sicReview; }
    public QaReviewEntity getQaReview() { return qaReview; }
    public void setQaReview(QaReviewEntity qaReview) { this.qaReview = qaReview; }
}
