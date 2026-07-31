# WanderSync Development Guide

---

# Purpose

This guide provides everything required to set up, develop, test, debug, and contribute to the WanderSync project.

It serves as the onboarding manual for every developer joining the team.

Following this guide ensures that all developers work in a consistent, predictable, and efficient development environment.

---

# Team Structure

Current Team

Backend Lead

Responsible for

Spring Boot

MongoDB

Authentication

Booking Engine

Payments

War Room Backend

Notifications

---

Frontend Developer 1

Responsible for

Consumer Website

Authentication UI

Trip Pages

Traveler Dashboard

---

Frontend Developer 2

Responsible for

Vendor Dashboard

War Room UI

Admin Dashboard

Responsive Design

---

AI Engineer

Responsible for

Spring AI

Gemini

RAG

Prompt Engineering

AI Recommendation Engine

Vector Search

AI Documentation

---

# Required Software

Install

Git

GitHub Desktop (Optional)

Java 21

Maven

Node.js (LTS)

MongoDB Compass

VS Code

IntelliJ IDEA Community

Postman

Docker Desktop

Google Chrome

---

# Required Accounts

GitHub

MongoDB Atlas

Google AI Studio

Cloudinary

Razorpay Test Account

Render

Vercel

UptimeRobot

Resend (Email)

---

# VS Code Extensions

Java Extension Pack

Spring Boot Extension Pack

ESLint

Prettier

Tailwind CSS IntelliSense

GitLens

Docker

Thunder Client

Error Lens

Material Icon Theme

MongoDB for VS Code

Markdown All in One

---

# Project Structure

Backend

/backend

Frontend

/frontend

Documentation

/docs

Assets

/assets

Scripts

/scripts

---

# Environment Variables

Backend (.env)

JWT_SECRET

JWT_EXPIRATION

MONGODB_URI

GEMINI_API_KEY

RAZORPAY_KEY

RAZORPAY_SECRET

CLOUDINARY_CLOUD_NAME

CLOUDINARY_API_KEY

CLOUDINARY_API_SECRET

EMAIL_API_KEY

---

Frontend (.env)

VITE_API_BASE_URL

VITE_RAZORPAY_KEY

VITE_CLOUDINARY_URL

---

# Initial Setup

Clone Repository

↓

Open Backend

↓

Install Dependencies

↓

Configure Environment Variables

↓

Start MongoDB

↓

Run Spring Boot

↓

Open Frontend

↓

Install Dependencies

↓

Run React

↓

Access Application

---

# Running Backend

Navigate

/backend

Command

./mvnw spring-boot:run

Default Port

8080

Swagger

/api/docs

---

# Running Frontend

Navigate

/frontend

Command

npm install

Command

npm run dev

Default Port

5173

---

# Git Workflow

Create Feature Branch

↓

Develop Feature

↓

Commit

↓

Push

↓

Create Pull Request

↓

Code Review

↓

Merge

↓

Delete Branch

---

# Branch Naming

feature/auth

feature/booking

feature/war-room

feature/ai

feature/payment

feature/frontend

bugfix/login

hotfix/payment

release/v1

---

# Commit Naming

Examples

feat(auth): add JWT authentication

feat(ai): implement recommendation engine

fix(payment): resolve webhook issue

docs(api): update API documentation

refactor(frontend): improve sidebar

test(auth): add login tests

---

# Daily Development Workflow

Pull latest develop branch

↓

Create feature branch

↓

Implement feature

↓

Run tests

↓

Commit changes

↓

Push branch

↓

Create PR

↓

Review

↓

Merge

---

# Backend Development Flow

Controller

↓

Service

↓

Repository

↓

MongoDB

Always keep business logic inside Services.

---

# Frontend Development Flow

Page

↓

Components

↓

Hooks

↓

API

↓

Backend

---

# AI Development Flow

Prompt

↓

RAG Retrieval

↓

Gemini

↓

Structured JSON

↓

Validation

↓

Frontend

---

# Debugging Checklist

Backend

Application Starts

Database Connected

Environment Variables Loaded

JWT Valid

API Responding

Frontend

No Console Errors

API Connected

State Updated

Responsive Layout

AI

Prompt Valid

Gemini Responding

Fallback Available

---

# API Testing

Use

Postman

Thunder Client

Swagger UI

Test

Authentication

Bookings

Payments

War Room

Traveler HQ

AI

Vendor APIs

---

# Code Quality

Run

Backend Tests

Frontend Build

Lint

Format Code

Security Scan

Before Every Pull Request

---

# Pull Request Checklist

Feature Completed

Tests Passing

No Console Errors

No Dead Code

Documentation Updated

Screenshots Added (UI)

Reviewer Assigned

---

# Merge Rules

No direct commits to main.

All merges require Pull Requests.

At least one approval required.

CI must pass.

---

# Team Communication

GitHub Issues

GitHub Projects

Discord

Slack

WhatsApp

Daily Stand-up

Recommended Format

Yesterday

Today

Blockers

---

# Definition of Done

Code Completed

Tests Passing

Documentation Updated

Code Reviewed

Merged

Deployable

No Critical Bugs

---

# AI Workflow

AI should assist developers, not replace engineering judgment.

Guidelines

Break work into small tasks.

Review generated code.

Never merge AI-generated code without understanding it.

Validate all API responses.

Test every AI-generated feature.

Document architectural decisions.

---

# Common Issues

Backend won't start

Check Java version

Check MongoDB URI

Check environment variables

Frontend won't connect

Verify API URL

Enable CORS

Check backend running

JWT issues

Verify secret

Verify expiration

Clear browser storage

AI errors

Verify Gemini API key

Check quota

Enable fallback mode

---

# Useful Commands

Backend

./mvnw clean install

./mvnw test

Frontend

npm install

npm run dev

npm run build

Docker

docker compose up

docker compose down

Git

git pull

git checkout -b feature/name

git add .

git commit

git push

---

# Future Improvements

Dev Containers

Remote Development

GitHub Codespaces

Automatic Environment Setup

One-Command Project Bootstrap

Internal Developer Portal

AI Pair Programmer

Automated Documentation

---

# Engineering Principles

A developer should be able to clone the repository and start contributing with minimal setup.

Consistency, automation, and documentation are essential for a scalable engineering team.

Every contributor is responsible for maintaining the quality and reliability of WanderSync.