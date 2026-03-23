package com.example.lims.model;

import jakarta.persistence.*;

@Entity
@Table(name = "qa_reviews")
public class QaReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String qaHeadName;
    private Boolean decision;

    @Column(length = 2000)
    private String comments;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sample_id", unique = true)
    private SampleEntity sample;

    public Long getId() { return id; }
    public String getQaHeadName() { return qaHeadName; }
    public void setQaHeadName(String qaHeadName) { this.qaHeadName = qaHeadName; }
    public Boolean getDecision() { return decision; }
    public void setDecision(Boolean decision) { this.decision = decision; }
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
    public SampleEntity getSample() { return sample; }
    public void setSample(SampleEntity sample) { this.sample = sample; }
}
