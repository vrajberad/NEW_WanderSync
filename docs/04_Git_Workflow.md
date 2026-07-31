# WanderSync
# Git & GitHub Workflow

---

# Purpose

This document defines the Git workflow, branching strategy, pull request process, commit standards, and collaboration rules followed by the WanderSync team.

The objective is to maintain a clean repository, prevent merge conflicts, and ensure stable development.

---

# Repository Strategy

Repository Structure

main
│
├── develop
│
├── feature/*
│
├── bugfix/*
│
├── hotfix/*
│
└── release/*

---

# Branches

## main

Production Ready Code

Rules

• Never commit directly

• Only merge reviewed Pull Requests

• Must always be deployable

---

## develop

Current Development Branch

Rules

• All completed features merge here first

• Used for integration testing

---

## feature/*

Every new feature gets its own branch.

Examples

feature/auth

feature/booking

feature/payment

feature/war-room

feature/traveler-dashboard

feature/vendor-dashboard

feature/custom-trip-builder

feature/notification-engine

feature/teaser-trips

feature/rag

feature/ai-planner

feature/deployment

---

## bugfix/*

Used only for fixing bugs.

Examples

bugfix/login

bugfix/payment

bugfix/security

bugfix/websocket

---

## hotfix/*

Critical production fixes.

Example

hotfix/payment-webhook

---

## release/*

Final release preparation.

Example

release/v1.0

---

# Team Workflow

Every morning

↓

Pull latest develop

↓

Create new feature branch

↓

Development

↓

Local Testing

↓

Commit

↓

Push

↓

Create Pull Request

↓

Review

↓

Merge into develop

↓

Delete Feature Branch

---

# Feature Development Flow

Requirement

↓

Architecture Discussion

↓

Issue Created

↓

Branch Created

↓

Implementation

↓

Testing

↓

Documentation

↓

Pull Request

↓

Review

↓

Merge

↓

Deployment

---

# Branch Naming Convention

feature/auth

feature/payment

feature/booking

feature/vendor-dashboard

feature/war-room

feature/rag

feature/custom-trip-builder

feature/traveler-hq

bugfix/login

bugfix/payment

hotfix/security

release/v1.0

---

# Commit Message Standard

Format

TYPE: Short Description

Examples

feat: add booking service

feat: implement war room voting

fix: resolve login issue

fix: correct payment validation

docs: update api documentation

style: improve dashboard layout

refactor: simplify pricing engine

test: add booking integration tests

chore: update dependencies

---

# Pull Request Template

Title

Feature Name

Description

What was implemented

Screenshots (if frontend)

API changes

Database changes

Testing completed

Known issues

Reviewer

---

# Pull Request Rules

Every PR must

Compile successfully

Pass testing

Follow coding standards

Have updated documentation

Contain meaningful commit history

Have no merge conflicts

---

# Code Review Checklist

Controllers

✓ REST naming

✓ Validation

✓ ResponseEntity

Services

✓ Business logic

✓ No duplicate code

✓ Proper exception handling

Repositories

✓ Correct queries

✓ Index usage

Security

✓ Authentication

✓ Authorization

✓ JWT validation

Frontend

✓ Responsive

✓ Error handling

✓ Loading states

✓ Form validation

AI

✓ Prompt quality

✓ JSON validation

✓ Fallback handling

---

# Merge Rules

Never merge

Broken code

Compilation errors

Untested features

Incomplete APIs

Hardcoded secrets

Debug statements

Console logs

Commented code

---

# GitHub Labels

feature

bug

enhancement

documentation

frontend

backend

ai

security

database

high-priority

medium-priority

low-priority

good-first-issue

---

# GitHub Milestones

MVP

Frontend

Backend

AI

Testing

Deployment

Version 1.0

---

# Issue Template

Title

Description

Priority

Assigned To

Branch

Status

Expected Result

Current Result

Steps to Reproduce

Possible Solution

---

# Merge Conflict Resolution

Always

Pull latest develop

Resolve locally

Test again

Commit

Push

Never force push without discussion.

---

# Daily Standup

Each member answers

Yesterday

What did I complete?

Today

What will I build?

Blockers

What is stopping me?

---

# GitHub Project Board

Columns

Backlog

To Do

In Progress

Review

Testing

Done

---

# Release Process

Feature Complete

↓

Testing

↓

Bug Fixing

↓

Release Branch

↓

Final Review

↓

Merge into main

↓

Tag Release

↓

Deploy

---

# Emergency Rollback

If a release fails

Stop deployment

Revert last merge

Redeploy previous stable version

Investigate issue

Fix in hotfix branch

Deploy again

---

# Repository Rules

Never push directly to main

Every feature needs a branch

Every bug needs an issue

Every merge requires review

Documentation must stay updated

Environment variables must never be committed

Secrets must remain in .env files

---

# Branch Ownership

Backend Lead

Authentication

Booking

Payments

Vendor APIs

Database

AI Lead

AI Planner

RAG

Prompt Engineering

Knowledge Base

Architecture

Documentation

Frontend Lead

Landing Page

Authentication UI

Booking UI

Traveler Dashboard

Frontend Developer

War Room

Vendor Dashboard

Admin Dashboard

Animations

Custom Package Builder

---

# Definition of Done

A task is complete only if

✓ Code compiles

✓ Feature works

✓ API tested

✓ Documentation updated

✓ Code reviewed

✓ Merged into develop

✓ No known critical bugs

---

# Final Goal

Maintain a professional Git workflow that allows all team members to work independently while keeping the project stable, organized, and ready for deployment at any time.