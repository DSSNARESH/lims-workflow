# DKR LIMS - Test Spec / Analysis / SIC Review / QA Review Backend APIs

## Current State
- Backend has Sample type with nested testSpecs, analysisResults, sicReview, qaReview fields
- TestSpecification, AnalysisResult, SicReview, QaReview types already defined in Motoko
- Frontend pages (TestSpecification, Analysis, SICReview, QAReview) call only `updateSample(sampleId, stage)` to advance stage
- No dedicated save/load APIs for these 4 workflow stages
- All workflow data stored directly in Sample object in `samples` map

## Requested Changes (Diff)

### Add
- `saveTestSpec(sampleId, testSpecs, assignedAnalyst)` — persist test specs + analyst into sample, advance stage to Analysis
- `getTestSpec(sampleId)` — return testSpecs array for a sample
- `saveAnalysisResult(sampleId, results)` — save analysis results into sample
- `getAnalysisResult(sampleId)` — return analysisResults for a sample
- `submitAnalysis(sampleId)` — run auto-verdict (pass/fail/oos per result), advance stage to SICReview
- `saveSICReview(sampleId, review)` — save SIC review data into sample
- `getSICReview(sampleId)` — return sicReview for a sample
- `approveSICReview(sampleId)` — mark SIC approved, advance stage to QAReview
- `rejectSICReview(sampleId)` — mark SIC rejected, stage goes back to Analysis
- `saveQAReview(sampleId, review)` — save QA review data into sample
- `getQAReview(sampleId)` — return qaReview for a sample
- `approveQAReview(sampleId)` — mark QA approved, advance stage to COA/completed
- `rejectQAReview(sampleId)` — mark QA rejected, stage goes back to SICReview

### Modify
- Frontend TestSpecification.tsx: call saveTestSpec + getTestSpec
- Frontend Analysis.tsx: call saveAnalysisResult + getAnalysisResult + submitAnalysis
- Frontend SICReview.tsx: call saveSICReview + getSICReview + approveSICReview + rejectSICReview
- Frontend QAReview.tsx: call saveQAReview + getQAReview + approveQAReview + rejectQAReview
- ApiDocs.tsx: add documentation for all 13 new endpoints

### Remove
- Remove hardcoded `updateSample(sampleId, stage)` calls from these 4 pages (replace with dedicated APIs)

## Implementation Plan
1. Generate new Motoko backend adding 13 new public functions that update Sample entries in the samples map
2. Update TestSpecification.tsx to call saveTestSpec/getTestSpec on load/save
3. Update Analysis.tsx to call saveAnalysisResult/getAnalysisResult/submitAnalysis
4. Update SICReview.tsx to call saveSICReview/getSICReview/approveSICReview/rejectSICReview
5. Update QAReview.tsx to call saveQAReview/getQAReview/approveQAReview/rejectQAReview
6. Update ApiDocs.tsx Backend API tab with all 13 new endpoints
7. Validate and deploy
