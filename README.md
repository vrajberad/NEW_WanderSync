<div align="center">

# 🌍 WanderSync

### AI-Powered Experiential Travel Operating System

*Plan • Collaborate • Customize • Travel • Remember*

---

[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-green.svg)]()
[![Java](https://img.shields.io/badge/Java-21-orange.svg)]()
[![React](https://img.shields.io/badge/React-19-blue.svg)]()
[![MongoDB](https://img.shields.io/badge/MongoDB-Atlas-success.svg)]()
[![Gemini AI](https://img.shields.io/badge/Gemini-AI-purple.svg)]()
[![License](https://img.shields.io/badge/License-MIT-blue.svg)]()

---

**An AI-first collaborative travel platform that transforms trip planning into a shared, personalized, and unforgettable experience.**

</div>

---

# 📖 Table of Contents

- About WanderSync
- The Problem
- Our Solution
- Core Features
- Product Vision
- Technology Stack
- System Architecture
- Project Structure
- Development Roadmap
- Current Progress
- Team Structure
- Development Workflow
- Installation
- Environment Variables
- Documentation
- Future Scope
- Contributors

---

# 🌟 About WanderSync

WanderSync is an AI-powered travel ecosystem designed to completely change how people discover, plan, customize, book, and experience trips.

Unlike traditional travel booking websites that stop after the payment is completed, WanderSync accompanies travelers throughout their entire journey—from discovering destinations to reliving memories after returning home.

Our vision is to build an intelligent travel operating system rather than just another booking application.

---

# ❌ The Problem

Current travel platforms suffer from several major issues.

### Traditional OTAs (Online Travel Agencies)

Examples:

- MakeMyTrip
- Goibibo
- EaseMyTrip

Problems:

- Only transactional
- Poor group planning
- Hidden pricing
- No trip customization
- Poor after-booking experience

---

### Traditional Tour Operators

Examples:

- Thrillophilia
- Local Tour Companies

Problems:

- Fixed itineraries
- Limited customization
- No collaboration
- Lack of transparency
- Vendor dependency

---

# 💡 Our Solution

WanderSync combines

- Artificial Intelligence
- Real-time Collaboration
- Dynamic Package Builder
- Group Planning
- Split Payments
- Vendor Management
- Personalized Travel Experience

into one integrated ecosystem.

---

# 🚀 Product Vision

WanderSync is designed as an

# AI Powered Experiential Travel Operating System

The platform allows users to

- Discover destinations using AI
- Plan trips collaboratively
- Build fully customized packages
- Lock seats in real-time
- Split payments among friends
- Manage bookings
- Receive smart notifications
- Store travel memories
- Earn loyalty rewards

Everything happens inside one ecosystem.

---

# ✨ Core Features

## 🤖 AI Travel Architect

Generate complete travel itineraries using

- Google Gemini
- Spring AI
- Retrieval Augmented Generation (RAG)
- MongoDB Atlas Vector Search

Users can simply type

> "Plan a peaceful workation for 4 friends in Himachal under ₹15,000."

and receive an intelligent itinerary.

---

## 👥 War Room

Real-time collaborative trip planning.

Features

- Invite friends
- Live voting
- Shared itinerary
- Split payments
- WebSocket updates
- Group booking

---

## 🎯 Future Destination Radar

Demand validation engine.

Instead of building expensive itineraries blindly,

Admins publish teaser destinations.

Users vote

"I'm Interested"

Once demand reaches a threshold,

Admins convert it into an official trip.

---

## 🛠 Custom Trip Builder

Users create their own travel package.

Customize

- Transport
- Hotels
- Activities
- Meals
- Add-ons

Live pricing updates instantly.

---

## 💳 Smart Booking Engine

Supports

- Seat Locking
- Seat Expiry
- Booking Confirmation
- Dynamic Pricing
- Booking History

---

## 💰 Split Payments

Instead of one person paying ₹60,000,

every traveler pays their own share.

Booking confirms automatically after everyone completes payment.

---

## 🧠 RAG Knowledge Engine

Destination knowledge stored using

- Embeddings
- Vector Search
- Metadata Filters

Produces grounded AI responses.

---

## 🧳 Vendor Portal

Vendors can

- Create Trips
- Update Trips
- Manage Inventory
- View Bookings
- Monitor Occupancy
- Control Pricing

---

## 🏅 Traveler HQ

Personal travel dashboard.

Includes

- Upcoming Trips
- Past Trips
- Travel Passport
- Achievement Badges
- WanderCoins Wallet
- Saved Co-Travelers
- Memory Vault

---

## 🔔 Notification Engine

Automated

- Email
- WhatsApp
- Booking Updates
- Reminder Notifications
- Trip Alerts
- Payment Reminders

---

# 🏗 Technology Stack

## Backend

- Java 21
- Spring Boot 3
- Spring Security
- Spring Web
- Spring Validation
- Spring Data MongoDB
- Spring WebSocket
- Spring AI
- JWT Authentication
- Bucket4j
- Maven

---

## Frontend

- React 19
- Vite
- Tailwind CSS
- Material UI
- Axios
- React Router
- STOMP
- SockJS

---

## Database

MongoDB Atlas

Collections include

- Users
- Trips
- Bookings
- Payments
- War Rooms
- Travel Knowledge
- Future Teaser Trips
- Custom Catalog
- Notifications
- Wallet
- Traveler Stats

---

## AI Stack

Google Gemini

Spring AI

Embeddings

MongoDB Atlas Vector Search

RAG Pipeline

---

## Payment

Razorpay Test Mode

Future

- Stripe

---

## Deployment

Backend

- Railway
- Render
- Docker

Frontend

- Vercel

Database

- MongoDB Atlas

---

# 🏛 High Level Architecture

```
                    React Frontend

      Consumer App      Vendor Dashboard

                │

         REST + WebSocket

                │

      Spring Boot Backend

────────────────────────────────────────

Authentication

Booking Engine

AI Architect

War Room

Vendor Module

Notification Engine

Payment Module

Pricing Engine

────────────────────────────────────────

         MongoDB Atlas

────────────────────────────────────────

Users

Trips

Bookings

Payments

Travel Knowledge

Future Trips

Custom Catalog

Notifications

Wallet

```

---

# 📂 Project Structure

```
WanderSync

│

├── backend

├── frontend

├── docs

│

├── README.md

│

└── Documentation Files
```

---

# 📈 Current Development Progress

## Completed

- Authentication
- JWT Security
- Role Based Access
- Booking Engine
- Vendor Module
- War Room
- WebSocket Integration
- Pricing Engine
- Payment Integration
- AI RAG Infrastructure
- Vector Search Configuration
- Knowledge Ingestion
- Swagger
- MongoDB Configuration

---

## In Progress

- Frontend
- AI Planner
- Traveler HQ
- Notification Engine
- Future Teaser Trips
- Custom Trip Builder

---

## Planned

- Deployment
- Testing
- Docker
- CI/CD
- Production Monitoring

---

# 👨‍💻 Team Structure

Project follows a modular development approach.

### AI & Technical Lead

Responsible for

- Architecture
- AI
- RAG
- Documentation
- Code Reviews
- Integration

---

### Backend Team

Responsible for

- APIs
- Security
- Database
- Business Logic

---

### Frontend Team

Responsible for

- User Interface
- Dashboards
- Booking Flow
- Vendor Portal

---

# 🔄 Development Workflow

```
Requirement

↓

Architecture

↓

Task Assignment

↓

Development

↓

Testing

↓

Review

↓

Pull Request

↓

Merge

↓

Deployment
```

---

# 🚀 Getting Started

## Backend

```
cd backend
```

```
mvn clean install
```

```
mvn spring-boot:run
```

---

## Frontend

```
cd frontend
```

```
npm install
```

```
npm run dev
```

---

# 🔑 Environment Variables

Backend requires

```
MONGODB_URI=

JWT_SECRET=

GEMINI_API_KEY=

RAZORPAY_KEY_ID=

RAZORPAY_KEY_SECRET=

EMAIL_USERNAME=

EMAIL_PASSWORD=
```

---

# 📚 Documentation

Complete project documentation is available inside

```
docs/
```

Including

- Vision
- Architecture
- Backend
- Frontend
- Database
- APIs
- AI Module
- Git Workflow
- Prompt Library
- Deployment Guide

---

# 🔮 Future Scope

- Mobile Application
- Live GPS Tracking
- SOS Emergency System
- Weather Integration
- Photo Gallery
- Referral System
- Corporate Travel
- International Packages
- AI Voice Assistant
- Offline Trip Mode

---

# 🤝 Contributors

This project is being developed as a collaborative engineering effort using modern software architecture, AI-assisted development, and agile practices.

---

# ⭐ Final Goal

Our mission is not just to build another travel booking application.

We are building an intelligent travel ecosystem where users can

**Discover. Plan. Collaborate. Customize. Book. Experience. Remember.**

All within one platform.

---

<div align="center">

### 🌍 WanderSync

**Travel Smarter. Travel Together. Travel Beyond.**

</div>