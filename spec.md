# DKR LIMS

## Current State
- EligibilityCheck page has multi-assignee approval logic but may not strictly enforce ALL must approve
- SampleIntake and EligibilityCheck use basic tab styling
- SampleRegistration uses basic tab styling for 3-tab form
- SICReview, QAReview, COA Analytical Test Results table may use static/hardcoded rows

## Requested Changes (Diff)

### Add
- Animated pill-style tab component (reusable) with underline sliding indicator
- Modern tab styling applied to SampleIntake (Entry Form / History tabs)
- Modern tab styling applied to EligibilityCheck (Entry Form / History tabs)
- Modern tab styling applied to SampleRegistration (Client Info / Billing / Sample Receipt tabs)

### Modify
- EligibilityCheck approval logic: harden so ALL assigned SICs must approve; any Hold or Reject blocks progression; show clear status per assignee
- SICReview Analytical Test Results: pull rows from completed Analysis test parameters for that sample
- QAReview Analytical Test Results: pull rows from completed Analysis test parameters for that sample
- COA Analytical Test Results: pull rows from completed Analysis test parameters for that sample

### Remove
- Static/hardcoded rows in Analytical Test Results tables in SICReview, QAReview, COA

## Implementation Plan
1. Create a reusable AnimatedTabs component with pill style and sliding underline indicator
2. Apply AnimatedTabs to SampleIntake, EligibilityCheck, SampleRegistration
3. Harden EligibilityCheck approval logic: compute canProceed = all assigned SICs have approved; any hold/reject shows block message
4. In SICReview, QAReview, COA: fetch analysis results for the current sample and use those test parameter rows for the Analytical Test Results table
