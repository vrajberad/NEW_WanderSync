# WanderSync AI Prompt Library

---

# Purpose

This document contains standardized prompts for building WanderSync.

Every developer should use these prompts instead of writing prompts from scratch.

These prompts ensure:

• Consistent architecture
• Clean code
• Production-ready implementations
• Better AI outputs
• Less hallucination
• Faster development

---

# UNIVERSAL PROMPT

Use this before every coding task.

------------------------------------------------------------

You are a Senior Software Architect with 20+ years of experience building scalable startup products.

Project Name:
WanderSync

Always follow the architecture defined in the project documentation.

Before writing code:

1. Understand the existing codebase.
2. List every file that will be modified.
3. Explain the approach.
4. Mention edge cases.
5. Generate production-ready code only.

Requirements:

• No placeholder code
• No TODO comments
• No fake implementations
• No duplicated logic
• Use DTOs
• Constructor Injection
• SOLID Principles
• Validation
• Proper Exception Handling
• Backward compatible
• Keep controllers thin
• Business logic inside services
• Mongo repositories only access database
• Secure APIs
• Clean naming

If additional files are required,
generate them automatically.

After implementation,
review your own code and point out any possible issues.

------------------------------------------------------------

---

# Backend Feature Prompt

Implement the following backend feature.

Requirements:

- Follow the current project architecture.
- Explain the implementation.
- Mention affected files.
- Generate complete code.
- Include DTOs.
- Include Controller.
- Include Service.
- Include Repository.
- Include Validation.
- Include Exception Handling.
- Update existing code where required.
- Explain API endpoints.
- Mention testing strategy.

Do NOT generate placeholders.

---

# Frontend Feature Prompt

Build a complete React feature.

Requirements:

React 19

Vite

Tailwind

Material UI

Axios

React Query

React Hook Form

Responsive Design

Reusable Components

Loading States

Error States

Empty States

Form Validation

Role Based UI

Proper Folder Structure

Explain component hierarchy before writing code.

---

# API Integration Prompt

Connect frontend with backend.

Requirements:

Generate:

API Service

Axios Client

Interceptors

Error Handling

Loading States

Authentication Handling

Token Refresh (if applicable)

Explain request flow.

---

# Bug Fix Prompt

Act as a Senior Debugging Engineer.

Do NOT rewrite the entire feature.

Find the root cause.

Explain why the issue occurs.

Suggest multiple solutions.

Choose the safest solution.

Generate only the required code.

List regression risks.

---

# Refactoring Prompt

Improve the following code.

Requirements:

Keep functionality identical.

Improve readability.

Improve performance.

Improve maintainability.

Follow SOLID.

Avoid breaking changes.

Explain improvements.

---

# Code Review Prompt

Review the following code.

Look for:

Architecture

Naming

Security

Performance

Memory

Concurrency

Validation

Error Handling

Code Smells

Scalability

Rate every category from 1–10.

Suggest improvements.

---

# Security Review Prompt

Review this code like a Security Engineer.

Look for:

JWT Issues

Authorization

Authentication

Injection

Rate Limiting

Sensitive Data

Secrets

Validation

File Upload Risks

WebSocket Security

Mention every vulnerability.

Suggest fixes.

---

# Database Prompt

Design MongoDB collections.

Include:

Indexes

Relationships

Embedded Documents

Referenced Documents

Query Optimization

Scalability

Future Expansion

Aggregation Opportunities

---

# AI Module Prompt

Build using Spring AI.

Requirements:

Gemini

RAG

Mongo Vector Search

Structured Output

Embeddings

Metadata Filters

Fallback Strategy

Explain token usage.

---

# Testing Prompt

Generate

Unit Tests

Integration Tests

Edge Cases

Mock Services

Testcontainers

API Tests

Mention expected coverage.

---

# Documentation Prompt

Generate professional documentation.

Include:

Architecture

Flow

API

Folder Structure

Examples

Best Practices

Future Improvements

Use Markdown.

---

# Performance Prompt

Optimize this feature.

Look for:

Slow Queries

Memory Usage

Thread Blocking

Caching

Indexes

Async Processing

Large Responses

Explain improvements.

---

# Final Verification Prompt

Before finishing:

Review every generated file.

Check imports.

Check compilation.

Check missing methods.

Check broken references.

Check validation.

Check security.

Check formatting.

Check architecture.

Mention anything that still requires manual work.

Never assume the project compiles.

Verify it mentally before responding.