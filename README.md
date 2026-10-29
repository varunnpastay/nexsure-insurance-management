# NexSure - Insurance Management System

Spring Boot (REST API) + React + PostgreSQL + Tailwind CSS

```
nexsure/
├── backend/    Spring Boot REST API  (port 8080)
└── frontend/   React + Vite + Tailwind (port 5173)
```

## Backend structure (com.Dcl)

```
entity/      User, PolicyCategory, Policy, Purchase, Claim      (JPA tables)
dto/         UserDto, CategoryDto, PolicyDto, PurchaseDto, ClaimDto  (data sent to React)
request/     RegisterRequest, LoginRequest, AddCategory, AddPolicy, UpdatePolicy, BuyPolicy, AddClaim
response/    ApiResponse<T>  { message, data, httpStatus }
exception/   AppException + GlobalExceptionHandler
config/      AppConfig  (ModelMapper bean + CORS for React)
repo/        JpaRepository interfaces
service/     service interfaces  ->  service/impl/  implementations
controller/  REST controllers
runner/      DataLoader (creates admin + demo policies on first start)
```

## 1. Database

```sql
CREATE DATABASE insurance_db;
```
Set your PostgreSQL username/password in `backend/src/main/resources/application.properties`.
Tables are created automatically (`ddl-auto=update`).

Default admin: **admin@gmail.com / admin123**

## 2. Run backend
Import `backend` into Eclipse/STS as a Maven project and run `Application.java`
(or `mvn spring-boot:run`).

## 3. Run frontend
```bash
cd frontend
npm install
npm run dev
```
Open http://localhost:5173

## Two portals

| Portal | URL | Login API | Who |
|---|---|---|---|
| Customer | http://localhost:5173/login | POST /user/login | role CUSTOMER only |
| Admin | http://localhost:5173/admin/login | POST /admin/login | role ADMIN only |

Each portal keeps its own session, so admin and customer can be logged in at the same time.

## API

| Method | URL | Body / Params |
|---|---|---|
| POST | /user/register | `{fullName, email, password}` |
| POST | /user/login | `{email, password}` (customers only) |
| POST | /admin/login | `{email, password}` (admin only) |
| GET | /user/get | |
| DELETE | /user/delete/{userId} | |
| POST | /category/add | `{categoryName, description}` |
| GET | /category/get | |
| DELETE | /category/delete/{categoryId} | |
| POST | /policy/add | `{categoryId, policyName, premiumAmount, coverageAmount, durationMonths, description}` |
| GET | /policy/get | all policies (admin) |
| GET | /policy/active?categoryId= | active policies (customer) |
| GET | /policy/get/{policyId} | |
| PUT | /policy/update/{policyId} | same as add + `status` |
| DELETE | /policy/delete/{policyId} | |
| POST | /purchase/buy | `{userId, policyId}` |
| GET | /purchase/get | all purchases (admin) |
| GET | /purchase/user/{userId} | my purchases |
| PUT | /purchase/status/{purchaseId}?status=CANCELLED | |
| DELETE | /purchase/delete/{purchaseId} | |
| POST | /claim/add | `{userId, purchaseId, claimAmount, claimReason}` |
| GET | /claim/get | all claims (admin) |
| GET | /claim/user/{userId} | my claims |
| PUT | /claim/status/{claimId}?status=APPROVED | |
| DELETE | /claim/delete/{claimId} | |
| GET | /admin/stats | dashboard counts |

Every response looks like:
```json
{ "message": "Policy added successfully!", "data": { ... }, "httpStatus": "OK" }
```
