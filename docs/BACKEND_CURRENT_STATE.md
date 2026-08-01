# WanderSync Backend: Current-State Audit

**Audit scope:** `backend/` source and configuration as present in the repository. This report is source-verified; it does not treat planning documents as evidence that a feature exists. No application source was modified.

## 1. Executive Summary

WanderSync currently contains one Spring Boot modular-monolith backend with REST controllers for authentication, trips/seat locks, bookings, payments, vendor operations, a War Room, administration, knowledge ingestion, and a mock AI itinerary endpoint. Its intended structure is substantially ahead of its verified runtime state: the backend currently fails compilation, so no REST flow can start from this checkout.

The immediate blocker is Lombok annotation processing. Nearly every model, DTO, and service relies on Lombok-generated methods/loggers/builders, but the Maven build executed with JDK 25 emits none of them and reports missing getters, setters, builders, and `log` fields. The POM only declares Lombok as an optional dependency and has no explicit compiler annotation-processor configuration. The project declares Java 21 but the actual Maven runtime is JDK 25.0.1. Consequently, the suspected registration-to-login issue cannot be a runtime password/JWT defect in this checkout: registration and login cannot compile, package, or start.

At source level, registration uses `BCryptPasswordEncoder.encode` and login uses `PasswordEncoder.matches` against the same `passwordHash` field. There is no code-path mismatch between the two. Once the compilation blocker is resolved, the exact reported login symptom must be reproduced with a real request and database record before asserting an application-level cause.

## 2. Actual Tech Stack

| Area | Verified implementation |
|---|---|
| Language/build | Java release 21 configured; Maven; Spring Boot 3.3.5 |
| Runtime used for audit build | Maven ran on JDK 25.0.1 (shell `java` reported JDK 24.0.2) |
| Web/API | Spring MVC, Bean Validation, springdoc OpenAPI |
| Data | Spring Data MongoDB; local `mongodb://localhost:27017/wandersync` in dev |
| Security | Spring Security, stateless JWT (JJWT HS256), BCrypt, Bucket4j login rate limit |
| Realtime | Spring WebSocket/STOMP with SockJS and in-memory simple broker |
| AI/RAG | Spring AI 1.1.0-M1, Google GenAI dependency, MongoDB Atlas vector-store dependency; local fallback embedding/vector store |
| Payments | Razorpay Java SDK behind `PaymentGatewayClient`; disabled stub by default |
| Other declared dependencies | Actuator, Mail, OpenPDF, Testcontainers |

## 3. Current Project Structure

```
backend/
  pom.xml
  src/main/java/com/wandersync/backend/
    config/ controller/ dto/ exception/ model/ repository/ security/ service/
  src/main/resources/application.yml
  src/main/resources/application.yml.example
```

There are no files under `backend/src/test`, no Maven/Gradle wrapper, Dockerfile, Compose file, or environment file in the repository.

## 4. Existing Modules

Authentication/RBAC, scheduled trips and seat locking, booking/quote/cancellation, Razorpay order/webhook handling, vendor trip management and reporting, admin role replacement, War Room lobbies/voting/member payment, STOMP messaging, knowledge ingestion/vector storage scaffolding, and mock itinerary generation are present in source.

## 5. Existing REST APIs

| Method | Path | Source authorization |
|---|---|---|
| POST | `/api/v1/auth/register` | Public |
| POST | `/api/v1/auth/login` | Public; IP rate-limited |
| GET | `/api/v1/auth/me` | Authenticated |
| GET | `/api/v1/health` | Public |
| GET | `/api/v1/trips`, `/api/v1/trips/{id}` | Public |
| POST | `/api/v1/trips/{id}/lock-seat` | Authenticated |
| POST | `/api/v1/trips/{id}/confirm-booking` | Authenticated |
| POST | `/api/v1/bookings/quote` | Authenticated by fallback rule |
| POST | `/api/v1/bookings` | Authenticated |
| GET | `/api/v1/bookings/my`, `/api/v1/bookings/{id}` | Authenticated |
| POST | `/api/v1/bookings/{id}/cancel`, `/api/v1/bookings/{id}/payments` | Authenticated/owner checked in service |
| POST | `/api/v1/payments/webhook/razorpay` | Public; signature verified in service |
| POST | `/api/v1/war-room/lobby` | Authenticated |
| GET | `/api/v1/war-room/lobby/{id}` | Authenticated but no membership check |
| POST | `/api/v1/war-room/lobby/{id}/vote`, `/pay` | Authenticated/member checked in service |
| POST | `/api/v1/ai/generate-itinerary` | Authenticated |
| Vendor | `/api/v1/vendor/trips`, bookings, occupancy endpoints | `ROLE_VENDOR` or `ROLE_ADMIN` |
| POST | `/api/v1/admin/users/{id}/roles` | `ROLE_ADMIN` |
| Admin knowledge | `/api/v1/admin/knowledge`, `/batch`, `/seed`, `/stats` | `ROLE_ADMIN` |

Actuator exposes `health` and `info`; Swagger/OpenAPI paths are public.

## 6. Authentication Architecture

`SecurityConfig` configures a stateless chain, BCrypt, CORS, method security, public routes, route-level vendor/admin role checks, and `JwtAuthenticationFilter` before username/password authentication. No `AuthenticationManager`, `UserDetailsService`, refresh token, logout/revocation, or password-reset flow exists. Roles are stored as enum names already prefixed `ROLE_`.

## 7. JWT Flow

`JwtUtils` creates HS256 tokens with email as `sub`, a `roles` array claim, issue time, and 24-hour expiration. It rejects secrets below 32 UTF-8 bytes at startup. The HTTP filter parses `Authorization: Bearer ...`, verifies claims, and puts the email plus authorities created directly from the roles claim in the `SecurityContext`. It does not query MongoDB, so role changes do not affect existing token authorities until a new token is issued.

STOMP CONNECT is separately authenticated by `JwtChannelInterceptor` using its native `Authorization` header. `/ws/**` HTTP handshake is public by design.

## 8. Registration Flow

`POST /auth/register` -> `RegisterRequest` validation (`email`, password 8–72 characters, `fullName`) -> `AuthService.registerUser` -> duplicate email pre-check -> BCrypt hash -> Mongo `users` persistence -> 201 `UserResponse`. The request cannot supply roles; it always receives `ROLE_TRAVELER`.

**Difference from intended working flow:** source is structurally aligned, but the project does not compile; it therefore cannot receive, validate, hash, or persist a registration request in the audited state. Duplicate-email protection is a check followed by a unique index rather than a single atomic application operation, and the resulting Mongo duplicate-key exception is not mapped specifically.

## 9. Login Flow

`POST /auth/login` -> Bean Validation -> IP Bucket4j filter -> `AuthService.login` -> `findByEmail` -> `passwordEncoder.matches(rawPassword, passwordHash)` -> JWT with enum role names -> controller reloads the user by input email -> 200 `AuthResponse`.

Unknown user and wrong password deliberately map to the same custom 401 error. The controller performs a second database lookup after token generation.

**Difference from intended working flow:** no runtime login is possible while compilation fails. There is no Spring Security authentication-provider authentication; login is a direct service-level BCrypt comparison.

## 10. Current Login Bug

The verified current login failure is that the application cannot compile/start, so a newly registered user cannot log in because no new user can be registered through this backend. The build has no test source and no successful application startup evidence.

## 11. Exact Root Cause

`mvn test` was run in `backend/` and failed while compiling 84 source files. Errors include absent `User.builder()`, `User.getPasswordHash()`, DTO getters, response builders, and Lombok `log` fields across the project. The common root is missing Lombok-generated code during annotation processing. The POM declares Lombok only as optional and does not declare it as a Maven compiler annotation processor. The configured Java release is 21, while Maven actually ran with JDK 25.0.1, whose explicit annotation-processing behavior exposes this POM gap.

This is the exact evidence-backed root cause of the current inability to run registration/login. It is **not** evidence that BCrypt encoding, password-field persistence, email lookup, or JWT generation is wrong: those source paths are consistent. A separate post-build login root cause cannot be determined from source alone and must not be guessed.

## 12. Existing Security Controls

- BCrypt password hashing; password field ignored in JSON.
- HS256 key-length validation, expiration, stateless sessions, protected default route.
- Public registration/login, public trip reads, public signed payment webhook, and public WebSocket handshake with STOMP JWT authentication.
- RBAC through URL and method security; admin grants roles.
- Login rate limit: five attempts per client IP per 60 seconds in process memory.
- CORS has configured origins/methods/headers and credentials enabled.
- Request ID logging and generic production-safe unexpected-error response.

Limitations: CSRF is disabled (appropriate for bearer APIs but requires safe token handling), no token revocation/refresh, no account verification/reset, trusting `X-Forwarded-For` without trusted-proxy configuration, in-memory rate-limit state, no security headers configured, and WebSocket message authorization is authentication-only.

## 13. Existing MongoDB Models

`User` (unique indexed email, BCrypt hash, roles); `ScheduledTrip` (embedded seat map, add-ons, optimistic-lock version); `Booking` (user/trip/seat IDs, pricing, status, payments, version/auditing); `Payment` (booking/order/reference/status/idempotency); `WarRoomLobby` (members/polls); and `TravelKnowledgeDocument` (knowledge source). Booking and Payment use auditing annotations enabled by `MongoConfig`; trip and booking use `@Version`.

## 14. Existing MongoDB Repositories

`UserRepository`, `ScheduledTripRepository`, `BookingRepository`, `PaymentRepository`, `WarRoomLobbyRepository`, and `TravelKnowledgeDocumentRepository` all extend `MongoRepository`. Query methods cover email, vendor, destination, booking owner/trip/status, payment booking/order/idempotency, and lobby trip. Declared indexes are user email unique; booking user/group and trip+status; payment booking/order/idempotency unique; and lobby trip. No Atlas vector index is created in code.

## 15. Booking Architecture

Booking creation requires the current user to have first locked each requested seat. It validates the lock, calculates a server-side quote, then persists a `PENDING_PAYMENT` booking. Payments later invoke `BookingService.confirmBooking`, which books seats and changes booking state. Owner-only read/cancel is implemented. The legacy trip `confirm-booking` endpoint can book seats directly without creating a `Booking` or payment.

## 16. Seat Locking Architecture

Seats are embedded in a trip document and carry status, owner user ID, and expiry. A lock lasts ten minutes. Trip writes use optimistic locking and retry three times. Scheduled cleanup releases expired locks every configured 60 seconds; another scheduled job expires pending bookings whose locks are no longer active. A user may overwrite their own lock; an expired lock can be taken by another user.

There is no Mongo transaction spanning trip seat mutation, booking, payment, and cancellation. The booking creation validates a lock then saves a booking without atomically reserving/advancing the seat document. Duplicate seat IDs are rejected by quoting but not before the direct trip confirmation endpoint mutates its list.

## 17. Vendor Architecture

Vendor/admin routes create, list, update, price, delete trips and list booking/occupancy data. The vendor ID comes from the authenticated Mongo user. Per-trip update/delete/stat access permits admins; however, the list endpoints fetch only trips owned by the authenticated admin's user ID, contrary to their comments. Vendor trip creation does not enforce unique seat IDs. Deletion blocks only pending/confirmed bookings.

## 18. War Room Architecture

A signed-in host creates a lobby with client-provided member IDs/names and equal shares. Voting and payment require the caller to be listed as a member. Polls are initialized empty and there is no endpoint to add them. No trip ID can be supplied in `CreateLobbyRequest` despite the model supporting one. Lobby reads lack membership/host authorization. Member payment verifies a provider payment reference but does not bind the payment to a lobby/member/order or record it as a `Payment` document.

## 19. WebSocket Architecture

STOMP endpoint `/ws` uses SockJS, `/app` application destinations, and simple broker `/topic`. Handlers support `/app/war-room/{lobbyId}/vote` and `/pay`, broadcasting the complete lobby to `/topic/war-room/{lobbyId}`. CONNECT validates JWT. There is no subscription authorization, so any authenticated STOMP client can subscribe to a lobby topic; validation/exception handling for STOMP is not defined.

## 20. AI Architecture

`/api/v1/ai/generate-itinerary` calls `AIArchitectService`, which explicitly returns a static three-day mock itinerary. It does not call Gemini, Spring AI chat, the vector store, or the knowledge service. Input `durationDays` is returned but does not change the three generated days.

## 21. RAG / Vector Search Status

Admin-only ingestion saves source documents, splits content, stores chunks/metadata, and supports manual or optional dev startup seeding. In dev, Atlas vector-store auto-configuration is excluded; `LocalMongoVectorStore` performs full collection scans and in-process cosine similarity using either Gemini embeddings or a deterministic hash fallback. No controller/service uses similarity search for an end-user response. Atlas configuration names a collection/index/path but source does not create or verify the Atlas index. Thus RAG ingestion scaffolding exists, while user-facing RAG and verified Atlas Vector Search do not.

## 22. Payment Architecture

An owner can create one idempotent Razorpay order per booking. The Razorpay implementation creates an order and verifies signed webhook payloads; captured/order-paid webhooks check amount, mark a payment captured, then confirm the booking. With Razorpay disabled (default dev), a stub throws unsupported-operation errors. There is no client-side payment verification endpoint, refund flow, invoice/PDF generation, payment retry policy, transaction/outbox, or webhook event-id deduplication. Webhook processing can mark a payment captured before a subsequent booking-seat confirmation fails.

## 23. Exception Handling

`GlobalExceptionHandler` maps not found (404), invalid credentials (401), seat/resource conflicts and optimistic locks (409), validation/bad argument/state (400), payment gateway (502), and unexpected errors (500). Missing request headers and Spring Security authentication failures rely on framework defaults. WebSocket exceptions are not covered by this MVC advice.

## 24. Validation

DTOs use `@Valid` and common constraints for auth, trips, bookings, knowledge, lobby, payment reference, and roles. Service-level validation covers seat lock state, pricing IDs/duplicates, payment state, and ownership. Gaps include no uniqueness constraint for seat definitions, no validation that lobby member IDs correspond to users or include the host, no trip-date consistency beyond `@Future`, and no custom validator for cross-field rules.

## 25. Scheduled Jobs

Scheduling is enabled. `ScheduledTripService.releaseExpiredLocks` frees expired trip locks; `BookingService.expireStaleBookings` releases relevant stale locks and marks pending bookings expired. Both default to 60 seconds. Jobs scan all trips/pending bookings and have no distributed lock, so multi-instance execution can race (optimistic-lock handling is partial).

## 26. Existing Tests

No test source files exist. The POM declares Spring Boot Test, Spring Security Test, and Testcontainers, but none are used. `mvn test` fails at compile time before any test can run.

## 27. Configuration and Environment Variables

The committed `application.yml` defaults to the `dev` profile. Dev uses localhost Mongo, mail localhost:1025, a committed development JWT fallback, demo users/trips, disabled Razorpay, and optional RAG seed. Prod requires `MONGODB_URI`, `WANDERSYNC_JWT_SECRET`, and `WANDERSYNC_ALLOWED_ORIGINS`; it accepts Gemini, mail, rate-limit, and Razorpay variables. There are only `dev` and `prod` YAML profile documents—no separate profile files. Production lacks an explicit `wandersync.rag.seed-on-startup` property (the code default is false).

## 28. What Is Working

At source-design level: structured REST routing, validation annotations, JWT claim creation/parsing, BCrypt registration/login logic, request logging, rate limiting, Mongo model/repository definitions, optimistic trip version fields, pricing calculations, payment abstraction/webhook signature path, and STOMP CONNECT authentication are implemented.

No backend behavior is runtime-verified as working because compilation fails and there are no tests.

## 29. What Is Broken

- The build fails due to non-running Lombok annotation processing; backend cannot start.
- Current registration/login cannot execute for that reason.
- AI endpoint is a static mock, not Gemini/RAG.
- Default dev payment gateway cannot create orders or verify payments.
- The direct trip confirmation endpoint permits paid-booking bypass and creates no booking record.

## 30. What Is Partially Implemented

Booking/seat locking, vendor management, payment capture, War Room, RAG ingestion/local vector store, OpenAPI, development seeding, mail/PDF dependencies, and admin management are partial. In particular, mail and OpenPDF are declared but no implementation uses them.

## 31. What Is Missing

Custom Trip Builder, Destination Radar/teaser trips, Traveler HQ, notifications, PDF tickets/invoices, travel memories, WanderCoins/referrals, split-payment order orchestration, user-facing RAG chat, real dynamic pricing rules, production deployment/config files, database migrations/index deployment, and automated tests are absent from backend source.

## 32. Risks / Technical Debt

- Build/runtime Java mismatch and Lombok processor configuration prevent delivery.
- No automated tests or reproducible runtime/container setup.
- Seat/booking/payment updates are not transactional; direct confirmation bypasses payment.
- Admin vendor-list behavior conflicts with comment/expected authority.
- War Room data exposure through REST reads and STOMP subscriptions; no poll creation or robust vote model.
- Production configuration has no demonstrated secret/deployment policy beyond environment placeholders.
- Dev profile includes known default credentials and a JWT fallback secret; production deployment must never activate/use them.
- Local vector search is O(n), uses deterministic nonsemantic fallback embeddings, and is not tied to an AI answer path.
- Payment webhook status transition lacks an atomic transaction and durable webhook idempotency.

## 33. Alignment With Current WanderSync Vision

| Current Capability | Current Status | Required by Vision | Action |
|---|---|---|---|
| Authentication | Source implemented; build-blocked; no refresh/reset/verification | Yes | MODIFY |
| RBAC | Traveler/vendor/admin roles and checks exist; token roles stale after changes | Yes | MODIFY |
| Planned trips / inventory | Trip model and public reads exist | Yes | KEEP/MODIFY |
| Booking | Pending/confirmed/cancelled/expired model exists; non-transactional and bypass endpoint | Yes | MODIFY |
| Seat locking | Embedded locks, expiry, optimistic versioning, cleanup exist | Yes | MODIFY |
| Dynamic pricing | Manual multiplier and quote calculation only | Yes | MODIFY |
| Payments | Razorpay order/webhook code; disabled by default; gaps above | Yes | MODIFY |
| Group travel / War Room | Lobby, vote, STOMP handlers present but incomplete/authorization gaps | Yes | MODIFY |
| Split payments | Equal-share member state only | Yes | REBUILD |
| AI Travel Architect | Static mock response | Yes | REBUILD |
| RAG / vector search | Ingestion scaffolding/local fallback; no answer retrieval and no index creation | Yes | MODIFY |
| Custom Trip Builder | Absent | Yes | REBUILD |
| Destination Radar / teaser trips | Absent | Yes | REBUILD |
| Vendor Portal backend | Trip CRUD, bookings, occupancy exist | Yes | MODIFY |
| Admin Portal backend | Role replacement and knowledge management only | Yes | MODIFY |
| Traveler HQ | Absent | Yes | REBUILD |
| Notifications | Absent (mail dependency only) | Yes | REBUILD |
| PDF tickets / invoices | Absent (OpenPDF dependency only) | Yes | REBUILD |
| Memories / post-trip | Absent | Yes | REBUILD |
| WanderCoins / referrals | Absent | Yes | REBUILD |
| Tests / production operational controls | No tests/container/deployment definitions; compilation blocked | Yes | REBUILD |

## Priority Findings and Recommended Next Steps

### A. Critical bugs

1. Maven compilation fails because Lombok-generated API is unavailable under the audit runtime; no backend flow can run.
2. `POST /api/v1/trips/{id}/confirm-booking` marks seats booked without a booking/payment record, bypassing the paid booking flow.

### B. High-priority bugs

1. Booking, seat state, payment status, and webhook handling have no cross-document transaction/outbox; partial states are possible.
2. War Room lobby GET and STOMP topic subscriptions do not enforce membership.
3. Payment webhook may capture payment before booking confirmation, with no compensating/retry behavior if seat confirmation fails.
4. Admin vendor list endpoints are restricted by ownership and do not list all trips despite accepting admin authority.

### C. Medium-priority bugs

1. Login limiter trusts unconfigured `X-Forwarded-For` and stores buckets only in process memory.
2. Direct confirmation accepts duplicate seat IDs without pricing-layer duplicate protection.
3. Lobby creation trusts arbitrary member IDs/names and does not connect its optional trip ID.
4. AI duration is ignored and response is a fixed three-day mock.

### D. Architecture gaps

Build reproducibility, test coverage, transaction/idempotency strategy, real RAG answer orchestration, payment reconciliation/refunds, notification/PDF services, observability beyond request logs/health, deployments, and the product modules listed as absent require design and implementation.

### E. Recommended Next Steps

1. Preserve source and first make the build reproducible with the intended Java 21 toolchain and explicit Lombok annotation-processor configuration; rerun a clean compile.
2. Add a minimal integration test that registers then logs in against MongoDB, so the reported symptom is tested after startup is restored.
3. Decide and protect one booking-confirmation path; remove or redesign the payment-bypassing trip confirmation endpoint only after team review.
4. Add transactional/idempotent state handling around seats, bookings, payments, and webhooks before frontend integration.
5. Address War Room authorization and then prioritize the product modules against the table above.
