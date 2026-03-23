package com.example.lims.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class RfaDetails {
    private Long registration = 0L;
    private Long billing = 0L;
    private Long sampleDetails = 0L;

    public Long getRegistration() { return registration; }
    public void setRegistration(Long registration) { this.registration = registration; }
    public Long getBilling() { return billing; }
    public void setBilling(Long billing) { this.billing = billing; }
    public Long getSampleDetails() { return sampleDetails; }
    public void setSampleDetails(Long sampleDetails) { this.sampleDetails = sampleDetails; }
}
