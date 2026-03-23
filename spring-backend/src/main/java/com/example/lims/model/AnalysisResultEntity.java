package com.example.lims.model;

import jakarta.persistence.*;

@Entity
@Table(name = "analysis_results")
public class AnalysisResultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String parameter;
    private String observedValue;
    private String unit;

    @Enumerated(EnumType.STRING)
    private Verdict verdict;

    private String remark;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sample_id")
    private SampleEntity sample;

    public Long getId() { return id; }
    public String getParameter() { return parameter; }
    public void setParameter(String parameter) { this.parameter = parameter; }
    public String getObservedValue() { return observedValue; }
    public void setObservedValue(String observedValue) { this.observedValue = observedValue; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public Verdict getVerdict() { return verdict; }
    public void setVerdict(Verdict verdict) { this.verdict = verdict; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public SampleEntity getSample() { return sample; }
    public void setSample(SampleEntity sample) { this.sample = sample; }
}
