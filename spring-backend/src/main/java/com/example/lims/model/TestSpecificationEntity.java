package com.example.lims.model;

import jakarta.persistence.*;

@Entity
@Table(name = "test_specifications")
public class TestSpecificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String parameter;
    private String acceptanceCriteria;
    private String method;
    private String referenceStandard;
    private String assignedAnalyst;
    private Long targetSla;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sample_id")
    private SampleEntity sample;

    public Long getId() { return id; }
    public String getParameter() { return parameter; }
    public void setParameter(String parameter) { this.parameter = parameter; }
    public String getAcceptanceCriteria() { return acceptanceCriteria; }
    public void setAcceptanceCriteria(String acceptanceCriteria) { this.acceptanceCriteria = acceptanceCriteria; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
    public String getReferenceStandard() { return referenceStandard; }
    public void setReferenceStandard(String referenceStandard) { this.referenceStandard = referenceStandard; }
    public String getAssignedAnalyst() { return assignedAnalyst; }
    public void setAssignedAnalyst(String assignedAnalyst) { this.assignedAnalyst = assignedAnalyst; }
    public Long getTargetSla() { return targetSla; }
    public void setTargetSla(Long targetSla) { this.targetSla = targetSla; }
    public SampleEntity getSample() { return sample; }
    public void setSample(SampleEntity sample) { this.sample = sample; }
}
