# WanderSync Coding Standards

---

# Purpose

This document defines the coding standards and engineering practices followed across the WanderSync project.

The objective is to ensure consistency, readability, maintainability, scalability, and collaboration among all contributors.

Every contributor should write code that looks as if it was written by a single engineering team.

---

# Engineering Principles

• Readability over cleverness

• Simplicity over complexity

• Reusability over duplication

• Composition over inheritance

• Security by default

• API-first development

• Mobile-first UI

• Clean architecture

• Fail fast and fail clearly

---

# Tech Stack

Backend

Java 21

Spring Boot 3

Spring Security

Spring AI

MongoDB Atlas

JWT

WebSocket

Maven

JUnit

Mockito

---

Frontend

React

Vite

Tailwind CSS

ShadCN UI

React Router

Axios

React Hook Form

Zod

TanStack Query

---

# Naming Conventions

## Java Classes

PascalCase

Examples

UserService

BookingController

PaymentGatewayClient

TravelerDashboardService

---

## Interfaces

Use descriptive names.

Examples

PaymentGateway

NotificationProvider

RecommendationEngine

StorageProvider

Avoid prefixes like

IUserService

IBooking

---

## Methods

camelCase

Examples

createBooking()

calculatePrice()

sendNotification()

generateItinerary()

---

## Variables

camelCase

Examples

bookingPrice

tripDuration

travelerCount

selectedActivities

---

## Constants

UPPER_SNAKE_CASE

Examples

MAX_GROUP_SIZE

JWT_EXPIRATION

DEFAULT_CURRENCY

---

## Packages

Lowercase

Example

```
com.wandersync.booking.service
```

---

# Folder Structure

Backend

```
controller

service

repository

entity

dto

mapper

config

security

exception

util

event

listener

websocket

validation
```

Frontend

```
components

pages

layouts

hooks

contexts

services

api

utils

types

assets

constants

routes
```

---

# Clean Code Rules

Functions should perform one task.

Avoid methods larger than 50 lines.

Avoid nested conditions beyond three levels.

Avoid duplicated logic.

Extract reusable utilities.

Never hardcode secrets.

Never hardcode URLs.

Never hardcode API keys.

---

# Controller Rules

Controllers should

Validate input

Call services

Return responses

Nothing more.

Never place business logic inside controllers.

---

# Service Rules

Services contain business logic.

Services should not directly communicate with external UI.

Services should remain testable.

---

# Repository Rules

Repositories only interact with MongoDB.

No business logic.

No calculations.

No validation.

---

# DTO Rules

Never expose entities directly.

Always use DTOs.

Separate

Request DTO

Response DTO

Validation DTO

Example

CreateTripRequest

TripResponse

BookingSummaryResponse

---

# Validation Rules

Use Bean Validation.

Examples

@NotBlank

@NotNull

@Email

@Positive

@Size

Never manually validate request bodies.

---

# Exception Handling

Never use generic Exception.

Create custom exceptions.

Examples

BookingNotFoundException

PaymentFailedException

TripFullException

UnauthorizedAccessException

Use GlobalExceptionHandler.

---

# Logging Standards

Never use

System.out.println()

Always use

SLF4J Logger

Log

INFO

WARN

ERROR

DEBUG

Never log

Passwords

JWT

OTP

Card Details

API Secrets

---

# API Standards

REST Naming

GET /trips

POST /bookings

PUT /users/{id}

DELETE /wishlist/{id}

Plural resources only.

Use HTTP status codes correctly.

---

# Response Format

Every API should return

Success

Message

Data

Timestamp

RequestId

Example

{
  "success": true,
  "message": "Booking created successfully",
  "data": {},
  "timestamp": "...",
  "requestId": "..."
}

---

# Git Branches

main

develop

feature/booking-engine

feature/war-room

feature/ai-planner

bugfix/login

hotfix/payment

---

# Commit Messages

Format

type(scope): message

Examples

feat(auth): implement JWT login

feat(ai): add recommendation service

fix(payment): resolve duplicate webhook

docs(api): update booking endpoints

refactor(user): simplify validation

test(auth): add login tests

---

# Pull Request Rules

One feature per PR.

No unrelated changes.

Must pass all tests.

At least one review.

Clear PR description.

Screenshots for UI changes.

---

# Code Review Checklist

Readable

Tested

Secure

Reusable

No duplication

No dead code

No secrets

Proper validation

Proper logging

Good naming

---

# Frontend Standards

Component names

PascalCase

Examples

TripCard

Navbar

TravelerStats

WarRoomLobby

Hooks

useBooking()

useAuth()

useTravelerDNA()

---

# React Rules

Functional Components only.

Use Hooks.

Avoid prop drilling.

Prefer Context or Zustand.

Memoize expensive computations.

Keep components focused.

---

# Tailwind Rules

Avoid inline styles.

Reuse utility classes.

Extract repeated layouts into components.

Use responsive utilities.

---

# AI Standards

Prompts stored separately.

Never hardcode prompts inside services.

Use structured JSON output.

Always validate AI responses.

Always implement fallback logic.

Log AI failures.

---

# Security Rules

Validate every request.

Escape user input.

Use HTTPS.

Encrypt sensitive fields.

Never trust client-side calculations.

Server calculates pricing.

Server validates permissions.

---

# Documentation Rules

Every module must include

README

Architecture

API

Examples

Future Improvements

Every public method should contain meaningful documentation where necessary.

---

# Testing Rules

Every new feature requires

Unit Test

Integration Test

Manual Verification

Critical flows require E2E testing.

---

# Performance Rules

Paginate large lists.

Lazy load images.

Cache expensive operations.

Index frequently queried fields.

Avoid N+1 queries.

---

# Definition of Done

A task is complete only when

Feature implemented

Tests passing

Documentation updated

Reviewed

Merged

Deployable

No critical bugs

---

# Engineering Principles

Write code for the next developer—not just for the compiler.

Consistency is more valuable than individual coding style.

Clean code reduces bugs, improves collaboration, and accelerates future development.

Every commit should make WanderSync better than it was before.