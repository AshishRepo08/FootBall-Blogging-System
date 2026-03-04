# Football Blog Platform

A production-grade REST API for a multi-user football blogging system built with Spring Boot. Public users can browse and search published posts freely. Authenticated users can create and manage their own posts, organise content with categories and tags, and control visibility through a draft/publish workflow.

---

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
- [Environment Variables](#environment-variables)
- [API Endpoints](#api-endpoints)
- [Authentication](#authentication)
- [Draft & Publish Workflow](#draft--publish-workflow)
- [Search & Filtering](#search--filtering)

---

## Features

- **Public read access** — anyone can browse all published posts without logging in
- **JWT & password authentication** — login via credentials or JWT token to access write operations
- **Author-scoped editing** — users can only edit or delete their own posts
- **Category & Tag management** — authenticated users can create and assign categories and tags to posts
- **Flexible search** — filter posts by category, tag, both, or neither
- **Draft/publish workflow** — save posts as drafts (private) and publish when ready (public)
- **Role-based access control (RBAC)** — distinct permissions enforced at the endpoint level

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot, Spring MVC, Spring Security |
| Authentication | JWT (JSON Web Tokens) |
| Database | PostgreSQL |
| ORM | JPA / Hibernate |
| Build Tool | Maven |
| API Docs | Swagger / OpenAPI |
| Logging | SLF4J |

---

## Architecture

The application follows a standard **3-tier layered architecture**:

```
┌─────────────────────────────────────────┐
│             Controller Layer            │  ← Handles HTTP requests, input validation
├─────────────────────────────────────────┤
│              Service Layer              │  ← Business logic, auth rules, workflow
├─────────────────────────────────────────┤
│            Repository Layer             │  ← JPA repositories, database queries
└─────────────────────────────────────────┘
```

**Key design decisions:**

- **Stateless authentication** — JWT tokens are validated on every request; no server-side session state is maintained
- **Author ownership enforced in service layer** — edit/delete operations check that the authenticated user matches the post author before proceeding, keeping security logic centralised
- **Draft visibility controlled at query level** — draft posts are filtered out of all public-facing queries; authenticated authors see their own drafts via a separate scoped query
- **Search built with dynamic query composition** — category and tag filters are applied conditionally so the same endpoint handles all combinations without redundant query methods
- **Global exception handler** — a `@ControllerAdvice` class intercepts all exceptions and returns consistent, structured error responses across the API

---

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- PostgreSQL 14+

### 1. Clone the repository

```bash
git clone https://github.com/your-username/football-blog-platform.git
cd football-blog-platform
```

### 2. Create the database

```sql
CREATE DATABASE football_blog;
```

### 3. Configure environment variables

Copy the example environment file and fill in your values:

```bash
cp .env.example .env
```

See [Environment Variables](#environment-variables) for all required values.

### 4. Run the application

```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`.

### 5. View API documentation

Swagger UI is available at:

```
http://localhost:8080/swagger-ui.html
```

---

## Environment Variables

| Variable | Description | Example |
|---|---|---|
| `DB_URL` | PostgreSQL JDBC URL | `jdbc:postgresql://localhost:5432/football_blog` |
| `DB_USERNAME` | Database username | `postgres` |
| `DB_PASSWORD` | Database password | `yourpassword` |
| `JWT_SECRET` | Secret key for signing JWT tokens | `your-256-bit-secret` |
| `JWT_EXPIRATION_MS` | Token expiry in milliseconds | `86400000` (24 hours) |
| `SERVER_PORT` | Port to run the application on | `8080` |

---

## API Endpoints

### Auth

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `POST` | `/api/auth/register` | Public | Register a new user |
| `POST` | `/api/auth/login` | Public | Login and receive JWT token |

### Posts

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/api/posts` | Public | Get all published posts (supports filtering) |
| `GET` | `/api/posts/{id}` | Public | Get a single published post |
| `GET` | `/api/posts/my` | Authenticated | Get all posts by the logged-in user (including drafts) |
| `POST` | `/api/posts` | Authenticated | Create a new post |
| `PUT` | `/api/posts/{id}` | Author only | Update a post |
| `DELETE` | `/api/posts/{id}` | Author only | Delete a post |
| `PATCH` | `/api/posts/{id}/publish` | Author only | Publish a draft post |

### Categories

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/api/categories` | Public | List all categories |
| `POST` | `/api/categories` | Authenticated | Create a new category |
| `DELETE` | `/api/categories/{id}` | Authenticated | Delete a category |

### Tags

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/api/tags` | Public | List all tags |
| `POST` | `/api/tags` | Authenticated | Create a new tag |
| `DELETE` | `/api/tags/{id}` | Authenticated | Delete a tag |

---

## Authentication

The API uses **JWT-based stateless authentication**.

### Register

```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "ashish",
  "email": "ashish@example.com",
  "password": "yourpassword"
}
```

### Login

```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "ashish@example.com",
  "password": "yourpassword"
}
```

**Response:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "username": "ashish"
}
```

### Using the token

Include the token in the `Authorization` header for all protected requests:

```http
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

## Draft & Publish Workflow

Posts can exist in one of two states:

| State | Visible to public | Visible to author |
|---|---|---|
| `DRAFT` | No | Yes (via `/api/posts/my`) |
| `PUBLISHED` | Yes | Yes |

**Creating a draft:**

```http
POST /api/posts
Authorization: Bearer <token>
Content-Type: application/json

{
  "title": "Top 10 Premier League Goals of 2024",
  "content": "...",
  "categoryId": 1,
  "tagIds": [2, 5],
  "status": "DRAFT"
}
```

**Publishing a draft:**

```http
PATCH /api/posts/{id}/publish
Authorization: Bearer <token>
```

Once published, a post becomes visible to all public users and cannot be reverted to draft.

---

## Search & Filtering

Published posts can be filtered using query parameters on `GET /api/posts`.

| Parameter | Type | Description |
|---|---|---|
| `categoryId` | Long | Filter by category |
| `tagId` | Long | Filter by tag |

**Examples:**

```bash
# All published posts
GET /api/posts

# Filter by category
GET /api/posts?categoryId=1

# Filter by tag
GET /api/posts?tagId=3

# Filter by both category and tag
GET /api/posts?categoryId=1&tagId=3
```

All combinations are handled by the same endpoint. Omitting both parameters returns all published posts.
