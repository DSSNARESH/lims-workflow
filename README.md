## LIMS Workflow

This repository currently contains the existing React-based LIMS user interface plus a new `spring-backend/` module that provides a conventional Spring Boot + H2 REST backend for the same workflow.

### What was added
- A Spring Boot 3 backend under `spring-backend/`
- H2 in-memory database with console enabled at `/h2-console`
- Workflow REST APIs for:
  - samples
  - test specification save/load
  - analysis save/load/submit
  - SIC review save/load/approve/reject
  - QA review save/load/approve/reject
- Seed data so the existing React page design can be exercised against a local database-backed service

### Run the Spring Boot backend
```bash
cd spring-backend
mvn spring-boot:run
```

The backend runs on `http://localhost:8080` and exposes the H2 console at `http://localhost:8080/h2-console`.

### Main API paths
- `GET /api/samples`
- `GET /api/samples/{sampleId}`
- `POST /api/samples`
- `PUT /api/samples/{sampleId}/stage?stage=ANALYSIS`
- `POST /api/workflow/test-spec`
- `GET /api/workflow/test-spec/{sampleId}`
- `POST /api/workflow/analysis`
- `GET /api/workflow/analysis/{sampleId}`
- `POST /api/workflow/analysis/{sampleId}/submit`
- `POST /api/workflow/sic-review`
- `GET /api/workflow/sic-review/{sampleId}`
- `POST /api/workflow/sic-review/{sampleId}/approve`
- `POST /api/workflow/sic-review/{sampleId}/reject`
- `POST /api/workflow/qa-review`
- `GET /api/workflow/qa-review/{sampleId}`
- `POST /api/workflow/qa-review/{sampleId}/approve`
- `POST /api/workflow/qa-review/{sampleId}/reject`

### Existing React app
The React app in `src/frontend/` is unchanged visually so the existing page layout and design can be reused while integrating this REST backend.
