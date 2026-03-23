package com.example.lims.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sic_reviews")
public class SicReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reviewerName;
    private Boolean decision;

    @Column(length = 2000)
    private String comments;

    @ElementCollection
    @CollectionTable(name = "sic_review_flagged_rows", joinColumns = @JoinColumn(name = "sic_review_id"))
    @Column(name = "flagged_row")
    private List<Long> flaggedRows = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sample_id", unique = true)
    private SampleEntity sample;

    public Long getId() { return id; }
    public String getReviewerName() { return reviewerName; }
    public void setReviewerName(String reviewerName) { this.reviewerName = reviewerName; }
    public Boolean getDecision() { return decision; }
    public void setDecision(Boolean decision) { this.decision = decision; }
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
    public List<Long> getFlaggedRows() { return flaggedRows; }
    public void setFlaggedRows(List<Long> flaggedRows) { this.flaggedRows = flaggedRows; }
    public SampleEntity getSample() { return sample; }
    public void setSample(SampleEntity sample) { this.sample = sample; }
}
