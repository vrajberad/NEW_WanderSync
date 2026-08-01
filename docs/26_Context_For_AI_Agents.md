# WanderSync — AI Agent Context

## Project Overview

WanderSync is an AI-first experiential travel platform built as a production-quality full-stack application.

The goal is NOT to become another MakeMyTrip or Thrillophilia.

The goal is to become a Travel Operating System that manages the complete journey:

Discovery
↓

AI Planning
↓

Group Collaboration

↓

Booking

↓

Payments

↓

Trip Execution

↓

Memory Vault

↓

Retention

The application must always prioritize clean architecture, scalability, modularity, security and production-grade engineering.

---

# Tech Stack

Backend

Java 21

Spring Boot 3

Spring Security

JWT Authentication

Spring AI

MongoDB Atlas

MongoDB Atlas Vector Search

WebSocket (STOMP)

Maven

Lombok

Swagger

Bucket4J

Razorpay

Docker

Frontend

React 19

Vite

TailwindCSS

Material UI

Axios

React Router

React Query

React Hook Form

Zod

Shadcn UI (where appropriate)

Lucide Icons

Deployment

Render

Railway

MongoDB Atlas

Cloudinary

GitHub Actions

---

# Coding Principles

Always generate production-quality code.

Never generate placeholder implementations unless explicitly requested.

Always use DTOs.

Always validate requests.

Keep Controllers thin.

Business logic belongs in Services.

Repository layer only communicates with MongoDB.

Never hardcode secrets.

Always use environment variables.

Use constructor injection.

Follow SOLID principles.

Prefer composition over inheritance.

Return consistent API responses.

Write readable code over clever code.

---

# Existing Features

Authentication

JWT Login

JWT Registration

Role Based Access

Traveler

Vendor

Admin

Booking Engine

Seat Locking

Seat Confirmation

Seat Lock Expiry

Optimistic Locking

Pricing Engine

Booking Records

Vendor Module

Vendor Dashboard APIs

Trip CRUD

Dynamic Pricing

War Room

Real-time Lobby

Voting

Split Payment Foundation

WebSocket

AI Module

Spring AI

Gemini

RAG

Mongo Vector Search

Knowledge Ingestion

Travel Knowledge Collection

Payments

Razorpay Integration

Webhook Verification

Booking Payment Flow

Security

Rate Limiting

Swagger

Request Logging

Global Exception Handling

---

# Upcoming Modules

Future Destination Radar

Custom Trip Builder

Traveler HQ

Notification Engine

Wallet

Referral System

WanderCoins

Vendor Analytics

Admin Analytics

Trip Captain Module

SOS Module

QR Boarding

Digital Ticket

Memory Vault

Photo Gallery

Review Engine

Loyalty Engine

Recommendation Engine

Weather Integration

Packing Assistant

Expense Sharing

---

# Project Philosophy

Every module must feel like a startup product.

Every screen must solve a real user problem.

Every API should have a clear responsibility.

Avoid overengineering.

Avoid unnecessary abstractions.

Optimize for readability.

Optimize for maintainability.

Think like a senior software engineer.

---

# Architecture Rules

Use Modular Monolith Architecture.

No Microservices.

Package by Feature whenever possible.

Follow Clean Architecture.

Every feature should contain:

Controller

DTO

Service

Repository

Model

Mapper (if required)

Validator

Exception

Tests

---

# AI Expectations

When generating code:

Understand the existing architecture first.

Do not rewrite unrelated files.

Do not duplicate functionality.

Maintain backward compatibility.

Explain architectural decisions.

Generate complete implementations.

Avoid TODO comments.

Avoid fake implementations.

Avoid pseudocode.

If changing one class affects another class,
list every affected file before generating code.

Always think through the entire feature before writing code.

Never hallucinate APIs.

Never invent project structure.

Use the existing project structure.

---

# Team Structure

Backend Lead

Frontend Developer 1

Frontend Developer 2

AI / Architecture Lead

The AI should produce work that can easily be distributed among four developers.

---

# Documentation

The repository contains architecture documents inside /docs.

Whenever possible, consult those documents before generating new code.

---

End of Context.