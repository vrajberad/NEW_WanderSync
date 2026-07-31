# WanderSync Observability Architecture

---

# Purpose

Observability enables engineers to understand the health, behavior, and performance of WanderSync in real time.

Instead of reacting to user complaints, the engineering team should detect, diagnose, and resolve issues before they impact travelers.

Observability combines logging, metrics, tracing, monitoring, and alerting into one unified operational platform.

---

# Vision

Every request, booking, payment, AI response, and notification should be traceable.

Every production issue should answer three questions:

• What happened?

• Why did it happen?

• How can we prevent it again?

---

# Pillars of Observability

WanderSync follows the three pillars of modern observability.

Logs

Metrics

Distributed Traces

Together they provide complete visibility into the platform.

---

# System Architecture

```

User

↓

Frontend (React)

↓

Backend (Spring Boot)

↓

Application Logs

↓

Metrics

↓

Distributed Traces

↓

Monitoring Stack

↓

Dashboards

↓

Alerts

↓

Engineering Team

```

---

# Logging Strategy

Every important action generates structured logs.

Categories

Authentication

Authorization

Bookings

Payments

Notifications

War Rooms

AI Requests

Vendor Actions

Admin Actions

Database Queries

Security Events

System Errors

---

# Log Levels

TRACE

Very detailed debugging.

DEBUG

Developer troubleshooting.

INFO

Normal application events.

WARN

Unexpected but recoverable situations.

ERROR

Failures requiring attention.

FATAL

Critical application failures.

---

# Structured Logging

Every log contains

Timestamp

Request ID

Correlation ID

User ID (when available)

Session ID

API Endpoint

HTTP Method

Response Time

Status Code

Service Name

Environment

Example

```
INFO

Request ID: 1A8F93

User: user_104

POST /api/bookings

Response Time: 214 ms

Status: 201 Created
```

---

# Correlation IDs

Every incoming request receives a unique Correlation ID.

Example

```
Frontend

↓

Booking Service

↓

Payment Service

↓

Notification Service

↓

Email Service
```

All logs share the same Correlation ID for end-to-end debugging.

---

# Metrics Collection

Monitor

CPU Usage

Memory Usage

Disk Usage

Thread Count

Heap Usage

GC Activity

Database Connections

API Response Time

Error Rate

Request Count

---

# Business Metrics

Bookings Per Hour

Revenue Per Day

Conversion Rate

Average Booking Value

Trip Occupancy

Wallet Usage

Referral Conversions

AI Usage

Split Payment Success Rate

War Room Activity

---

# AI Metrics

Prompt Count

Average Tokens

Average Cost

Latency

Vector Search Time

Embedding Time

Fallback Usage

Recommendation Accuracy

Hallucination Reports

User Feedback Score

---

# Payment Metrics

Successful Payments

Failed Payments

Webhook Delay

Refund Rate

Duplicate Payment Attempts

Payment Gateway Latency

Settlement Success

Average Checkout Time

---

# Notification Metrics

Emails Sent

WhatsApp Sent

Push Notifications

Failures

Delivery Rate

Open Rate

Click Rate

Retry Count

---

# Database Metrics

Query Latency

Slow Queries

Connection Pool Usage

Collection Growth

Storage Usage

Index Utilization

Cache Hit Rate

Backup Status

---

# WebSocket Metrics

Active Connections

Messages Per Second

Connection Failures

Average Session Duration

Reconnect Attempts

Lobby Count

---

# Distributed Tracing

Trace

API Request

↓

Authentication

↓

Business Logic

↓

Database

↓

Payment Gateway

↓

Notification

↓

Response

Every request becomes traceable.

---

# Health Checks

Application

Database

MongoDB Atlas

Gemini API

Razorpay

Cloudinary

SMTP

Redis (Future)

Weather API

Maps API

---

# Monitoring Dashboard

Dashboard Sections

System Health

API Performance

Bookings

Payments

Traveler Activity

Vendor Activity

AI

Notifications

Infrastructure

---

# Alerting Strategy

Critical Alerts

Application Down

Database Down

Payment Failure

JWT Failure

AI Service Down

Email Failure

Memory > 90%

CPU > 90%

Disk > 90%

---

# Warning Alerts

Slow APIs

High AI Latency

Increasing Error Rate

Large Queue

High Retry Count

Slow Database Queries

---

# Incident Response

Detection

↓

Alert

↓

Investigation

↓

Fix

↓

Deployment

↓

Verification

↓

Postmortem

---

# Service Level Indicators (SLIs)

API Availability

Booking Success Rate

Payment Success Rate

AI Success Rate

Notification Delivery Rate

Average Response Time

Database Availability

---

# Service Level Objectives (SLOs)

API Availability

99.9%

Booking Success

99.5%

Payment Success

99.9%

AI Availability

99%

Notification Delivery

99%

---

# Error Budget

Acceptable Downtime

Monthly

43 Minutes

Yearly

8 Hours 45 Minutes

---

# Tools

Application Metrics

Spring Boot Actuator

Metrics

Micrometer

Visualization

Grafana

Monitoring

Prometheus

Logging

Loki

Tracing

OpenTelemetry

Error Tracking

Sentry

Availability

UptimeRobot

---

# Production Dashboards

Executive Dashboard

Revenue

Bookings

Growth

Traveler Dashboard

Bookings

Wallet

AI Usage

Engineering Dashboard

Errors

Latency

Deployments

Infrastructure

CPU

Memory

Database

Networking

---

# Audit Logs

Track

Role Changes

Permission Changes

Vendor Verification

Trip Creation

Booking Updates

Payment Updates

Refund Approval

Admin Actions

AI Prompt History

---

# Security Monitoring

Failed Login Attempts

JWT Tampering

Rate Limit Violations

Prompt Injection Attempts

SQL/NoSQL Injection Attempts

Suspicious Activity

Account Lockouts

---

# Disaster Monitoring

Database Backup Status

Deployment Rollback

Replication Health

Storage Capacity

Infrastructure Availability

---

# Future Enhancements

Real User Monitoring

Synthetic Monitoring

Session Replay

Chaos Engineering

AI Drift Detection

Predictive Incident Detection

Anomaly Detection

Automatic Root Cause Analysis

---

# Engineering Principles

You cannot improve what you cannot measure.

Every production incident should be observable, traceable, measurable, and actionable.

Monitoring is not a feature—it is a core engineering capability that protects the traveler experience.