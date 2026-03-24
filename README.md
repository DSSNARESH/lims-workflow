## LIMS Workflow

This repository includes:

- React frontend (`src/frontend`)
- Spring Boot API (`spring-backend`)
- H2 in-memory database (configured in Spring Boot)

### Local integration setup

1. Start the Spring Boot API:

   ```bash
   cd spring-backend
   ./mvnw spring-boot:run
   ```

2. Start the React app (in a separate terminal):

   ```bash
   cd src/frontend
   VITE_USE_SPRING_API=true pnpm vite
   ```

The Vite dev server proxies `/api/*` requests to `http://127.0.0.1:8080` by default, so the UI can call the Spring Boot API without CORS issues.

### Optional environment variables

- `VITE_USE_SPRING_API=true` — enables Spring API-backed task/history data in the UI
- `VITE_API_PROXY_TARGET=http://127.0.0.1:8080` — sets the Vite proxy target for `/api`
- `VITE_SPRING_API_BASE=/api` — overrides the API base path used by frontend Spring API client
