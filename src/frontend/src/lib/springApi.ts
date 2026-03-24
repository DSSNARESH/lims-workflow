import type { TaskRecord, WorkflowStage } from "./mockData";

export type BackendSampleStatus =
  | "PENDING"
  | "ELIGIBLE"
  | "HOLD"
  | "REGISTRATION"
  | "TEST_SPEC"
  | "ANALYSIS"
  | "REVIEW"
  | "SIC_REVIEW"
  | "QA_REVIEW"
  | "COMPLETED"
  | "COA";

export type Verdict = "PASS" | "FAIL" | "OOS";

export interface TestSpecificationDto {
  parameter: string;
  acceptanceCriteria: string;
  method: string;
  referenceStandard: string;
  assignedAnalyst: string | null;
  targetSla: number | null;
}

export interface AnalysisResultDto {
  parameter: string;
  observedValue: string;
  unit: string;
  verdict: Verdict | null;
  remark: string;
}

export interface SicReviewDto {
  reviewerName: string;
  decision: boolean | null;
  comments: string;
  flaggedRows: number[];
}

export interface QaReviewDto {
  qaHeadName: string;
  decision: boolean | null;
  comments: string;
}

export interface SampleDto {
  sampleId: string;
  clientName: string;
  sampleName: string;
  testName: string;
  registrationId: number | null;
  dateReceived: string;
  sampleStatus: BackendSampleStatus;
  rfa: {
    registration: number | null;
    billing: number | null;
    sampleDetails: number | null;
  } | null;
  testSpecs: TestSpecificationDto[];
  analysisResults: AnalysisResultDto[];
  sicReview: SicReviewDto | null;
  qaReview: QaReviewDto | null;
}

const API_BASE = (import.meta.env.VITE_SPRING_API_BASE as string | undefined) || "/api";

async function apiFetch<T>(path: string, init?: RequestInit): Promise<T> {
  const response = await fetch(`${API_BASE}${path}`, {
    headers: {
      "Content-Type": "application/json",
      ...(init?.headers ?? {}),
    },
    ...init,
  });

  if (!response.ok) {
    const message = await response.text();
    throw new Error(message || `Request failed with ${response.status}`);
  }

  if (response.status === 204) {
    return undefined as T;
  }

  return (await response.json()) as T;
}

export function getSamples() {
  return apiFetch<SampleDto[]>("/samples");
}

export function createSample(sample: Partial<SampleDto> & Pick<SampleDto, "sampleId" | "clientName" | "sampleName" | "testName">) {
  return apiFetch<SampleDto>("/samples", {
    method: "POST",
    body: JSON.stringify(sample),
  });
}

export function updateSampleStage(sampleId: string, stage: BackendSampleStatus) {
  return apiFetch<SampleDto>(`/samples/${sampleId}/stage?stage=${stage}`, {
    method: "PUT",
  });
}

export function saveTestSpec(sampleId: string, assignedAnalyst: string, testSpecs: TestSpecificationDto[]) {
  return apiFetch<TestSpecificationDto[]>("/workflow/test-spec", {
    method: "POST",
    body: JSON.stringify({ sampleId, assignedAnalyst, testSpecs }),
  });
}

export function saveAnalysis(sampleId: string, results: AnalysisResultDto[]) {
  return apiFetch<AnalysisResultDto[]>("/workflow/analysis", {
    method: "POST",
    body: JSON.stringify({ sampleId, results }),
  });
}

export function submitAnalysis(sampleId: string) {
  return apiFetch<SampleDto>(`/workflow/analysis/${sampleId}/submit`, {
    method: "POST",
  });
}

export function saveSicReview(sampleId: string, review: SicReviewDto) {
  return apiFetch<SicReviewDto>("/workflow/sic-review", {
    method: "POST",
    body: JSON.stringify({ sampleId, review }),
  });
}

export function approveSicReview(sampleId: string) {
  return apiFetch<SampleDto>(`/workflow/sic-review/${sampleId}/approve`, {
    method: "POST",
  });
}

export function rejectSicReview(sampleId: string) {
  return apiFetch<SampleDto>(`/workflow/sic-review/${sampleId}/reject`, {
    method: "POST",
  });
}

export function saveQaReview(sampleId: string, review: QaReviewDto) {
  return apiFetch<QaReviewDto>("/workflow/qa-review", {
    method: "POST",
    body: JSON.stringify({ sampleId, review }),
  });
}

export function approveQaReview(sampleId: string) {
  return apiFetch<SampleDto>(`/workflow/qa-review/${sampleId}/approve`, {
    method: "POST",
  });
}

export function rejectQaReview(sampleId: string) {
  return apiFetch<SampleDto>(`/workflow/qa-review/${sampleId}/reject`, {
    method: "POST",
  });
}

export function toWorkflowStage(status: BackendSampleStatus): WorkflowStage {
  switch (status) {
    case "PENDING":
      return "Intake";
    case "ELIGIBLE":
    case "REGISTRATION":
      return "Registration";
    case "TEST_SPEC":
      return "TestSpec";
    case "ANALYSIS":
      return "Analysis";
    case "REVIEW":
    case "SIC_REVIEW":
      return "SICReview";
    case "QA_REVIEW":
      return "QAReview";
    case "COA":
    case "COMPLETED":
      return "COA";
    case "HOLD":
      return "OnHold";
    default:
      return "Intake";
  }
}

export function buildTasksFromSamples(samples: SampleDto[]): TaskRecord[] {
  return samples
    .map((sample, index) => {
      const stage = toWorkflowStage(sample.sampleStatus);
      const assignedAnalyst = sample.testSpecs[0]?.assignedAnalyst || "user-003";
      const deadline = new Date(sample.dateReceived || Date.now());
      deadline.setDate(deadline.getDate() + 3);

      const config: Record<
        WorkflowStage,
        { taskType: string; assignedRole: TaskRecord["assignedRole"]; assignedUserId: string; description: string } | null
      > = {
        Intake: {
          taskType: "eligibilityCheck",
          assignedRole: "sectionInCharge",
          assignedUserId: "user-002",
          description: `Review eligibility for ${sample.sampleName}`,
        },
        EligibilityCheck: {
          taskType: "eligibilityCheck",
          assignedRole: "sectionInCharge",
          assignedUserId: "user-002",
          description: `Review eligibility for ${sample.sampleName}`,
        },
        Registration: {
          taskType: "registration",
          assignedRole: "admin",
          assignedUserId: "user-001",
          description: `Complete registration for ${sample.sampleName}`,
        },
        TestSpec: {
          taskType: "testSpec",
          assignedRole: "qa",
          assignedUserId: "user-001",
          description: `Author test specification for ${sample.sampleName}`,
        },
        Analysis: {
          taskType: "analysis",
          assignedRole: "analyst",
          assignedUserId: assignedAnalyst === "Elena Rodriguez" ? "user-003" : "user-003",
          description: `Run analysis for ${sample.sampleName}`,
        },
        SICReview: {
          taskType: "review",
          assignedRole: "sectionInCharge",
          assignedUserId: "user-002",
          description: `Review analysis package for ${sample.sampleName}`,
        },
        QAReview: {
          taskType: "qaReview",
          assignedRole: "qa",
          assignedUserId: "user-001",
          description: `Approve QA review for ${sample.sampleName}`,
        },
        COA: {
          taskType: "coa",
          assignedRole: "qa",
          assignedUserId: "user-001",
          description: `Issue COA for ${sample.sampleName}`,
        },
        OnHold: null,
        Rejected: null,
        PendingApproval: {
          taskType: "eligibilityCheck",
          assignedRole: "sectionInCharge",
          assignedUserId: "user-002",
          description: `Complete remaining approvals for ${sample.sampleName}`,
        },
      };

      const taskConfig = config[stage];
      if (!taskConfig) return null;

      return {
        id: `task-${sample.sampleId}`,
        sampleId: sample.sampleId,
        taskType: taskConfig.taskType,
        assignedRole: taskConfig.assignedRole,
        assignedUserId: taskConfig.assignedUserId,
        deadline: deadline.toISOString(),
        priority: index % 3 === 0 ? "high" : index % 3 === 1 ? "medium" : "low",
        description: taskConfig.description,
      } satisfies TaskRecord;
    })
    .filter((task): task is TaskRecord => task !== null);
}
