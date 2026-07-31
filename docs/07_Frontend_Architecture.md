# WanderSync Frontend Architecture

Version: 1.0

---

# 1. Introduction

## Overview

The WanderSync frontend is built using React, Vite and Tailwind CSS.

The frontend follows a modular feature-based architecture where every feature owns its own pages, components, hooks and API calls.

The objective is to build a highly interactive, responsive and scalable travel platform capable of supporting AI-powered planning, real-time collaboration and rich traveler experiences.

---

# 2. Frontend Technology Stack

| Technology | Purpose |
|------------|----------|
| React 19 | UI Library |
| Vite | Build Tool |
| Tailwind CSS | Styling |
| React Router | Routing |
| Axios | REST API |
| SockJS | WebSocket Transport |
| STOMP | WebSocket Protocol |
| React Hook Form | Forms |
| Zod / Yup | Validation |
| Lucide Icons | Icons |
| Recharts | Analytics Charts |
| Framer Motion | Animations |
| React Query (TanStack Query) | Server State |
| Zustand | Global Client State |

---

# 3. Frontend Goals

The frontend is designed with the following principles.

- Fast
- Responsive
- Mobile First
- Component Driven
- Accessible
- Scalable
- AI Friendly
- Real-Time
- Easy to Maintain

---

# 4. Frontend Folder Structure

```text

src

├── api
├── assets
├── components
├── config
├── constants
├── contexts
├── features
├── hooks
├── layouts
├── pages
├── routes
├── services
├── store
├── styles
├── types
├── utils

```

---

# 5. Feature Based Organization

Every feature should own its own files.

Example

```text

features

booking

components

hooks

pages

services

types

utils

```

This prevents the project from becoming difficult to maintain as it grows.

---

# 6. Application Layout

The application is divided into multiple experiences.

- Landing Website
- Traveler Portal
- Vendor Dashboard
- Admin Dashboard

Each has its own layout and navigation while sharing the same design system.

---

# 7. Routing Architecture

React Router manages navigation across the application.

```mermaid
graph TD

Landing

--> Login

Landing

--> Register

Landing

--> AI Planner

Landing

--> Trips

Trips

--> Trip Details

Trip Details

--> Booking

Booking

--> Payment

Traveler Dashboard

--> Traveler HQ

Traveler Dashboard

--> Bookings

Traveler Dashboard

--> Wallet

Traveler Dashboard

--> Memories

Traveler Dashboard

--> AI Planner

Traveler Dashboard

--> War Room

Vendor Dashboard

--> Trips

Vendor Dashboard

--> Analytics

Vendor Dashboard

--> Inventory

Admin Dashboard

--> Users

Admin Dashboard

--> Vendors

Admin Dashboard

--> Analytics

Admin Dashboard

--> Future Trips

```

---

# 8. Route Protection

Routes are protected according to user roles.

| Route | Access |
|--------|--------|
| Home | Public |
| Trips | Public |
| Login | Public |
| Register | Public |
| AI Planner | Authenticated |
| Bookings | Traveler |
| Vendor Dashboard | Vendor |
| Admin Dashboard | Admin |
| War Room | Authenticated |

---

# 9. Layout Architecture

There are four primary layouts.

Landing Layout

↓

Traveler Layout

↓

Vendor Layout

↓

Admin Layout

Each layout contains

- Navbar
- Sidebar (where applicable)
- Footer
- Notifications
- Global Modals

---

# 10. State Management Strategy

Different state types use different tools.

| State | Tool |
|--------|------|
| Authentication | Context API |
| Server Data | React Query |
| Theme | Context |
| Booking Wizard | Zustand |
| War Room | Zustand |
| Notifications | Zustand |
| UI State | Local State |

---

# Why React Query?

Server data should never be managed manually.

React Query provides

- Automatic caching
- Refetching
- Background updates
- Loading states
- Retry support

This significantly reduces frontend complexity.

---

# 11. API Layer

All API calls pass through Axios.

```mermaid
flowchart LR

React Component

-->

Feature Service

-->

Axios Client

-->

Spring Boot

```

The Axios client handles

- JWT
- Token Refresh
- Error Handling
- Logging
- Request Interceptors
- Response Interceptors

---

# 12. Component Architecture

The frontend follows Atomic Design principles.

```text

Pages

↓

Sections

↓

Components

↓

Reusable UI Elements

```

## Component Hierarchy

Example

```text

Trip Details Page

│

├── Hero Banner

├── Trip Information

├── Price Card

├── Seat Selector

├── Itinerary Timeline

├── FAQ Section

├── Reviews

└── Booking Sidebar

```

---

# 13. Reusable Components

The following components should be reusable throughout the application.

Buttons

Cards

Inputs

Text Areas

Dropdowns

Badges

Avatars

Modals

Dialogs

Alerts

Tabs

Accordions

Breadcrumbs

Progress Bars

Skeleton Loaders

Pagination

Empty States

Error States

Toast Notifications

Image Carousel

Timeline

Price Breakdown Card

These components should be built once and reused everywhere.

---

# 14. Design System

The UI should maintain a consistent design language.

## Typography

| Element | Size |
|----------|------|
| H1 | 48px |
| H2 | 36px |
| H3 | 30px |
| H4 | 24px |
| Body | 16px |
| Caption | 14px |

---

## Border Radius

Cards

12px

Buttons

10px

Inputs

10px

Dialogs

16px

---

## Shadows

Small

Cards

Medium

Dropdowns

Large

Dialogs

Extra Large

Hero Components

---

## Color Palette

Primary

Emerald

Secondary

Blue

Accent

Orange

Success

Green

Warning

Amber

Danger

Red

Background

Slate

---

# 15. Landing Page Architecture

The Landing Page acts as the product showcase.

Sections

Hero Section

AI Search Box

Popular Destinations

Trending Trips

Future Destination Radar

How It Works

Traveler Reviews

Statistics

FAQ

Footer

---

## Hero Section

Contains

- Cinematic Background
- AI Search Prompt
- CTA Buttons
- Animated Statistics
- Featured Destination

---

## AI Search Experience

Instead of a normal search box,

Users type

"I want a peaceful workation under ₹20k."

The AI Planner displays curated travel cards with reasons for recommendation.

This is the primary entry point into WanderSync.

---

# 16. Trip Details Page

Displays

Destination Gallery

Trip Summary

Available Dates

Seat Availability

Live Price

Trip Highlights

Inclusions

Exclusions

Itinerary

Map

Reviews

Organizer Information

FAQ

Sticky Booking Card

---

## Sticky Booking Card

Always visible while scrolling.

Contains

Trip Price

Remaining Seats

Travel Dates

Book Now Button

Save Trip

Share Trip

---

# 17. AI Planner Interface

The AI Planner is one of WanderSync's flagship features.

Layout

```text

Prompt Box

↓

Loading Animation

↓

Recommended Trips

↓

Why AI Chose This

↓

Modify Prompt

↓

Generate Again

```

---

## Features

Natural Language Search

Suggested Prompts

Streaming Response

Loading Skeleton

Conversation History

Saved Plans

Share Plan

Export as PDF

Book Suggested Trip

---

# 18. War Room Interface

The War Room provides live collaboration for group travel planning.

Modules

Member List

Live Chat

Voting Panel

Trip Summary

Split Payment Tracker

Activity Feed

Shared Notes

Invite Link

---

## Real-Time Updates

Users instantly see

New Members

Votes

Chat Messages

Payment Progress

Trip Changes

Seat Locks

No page refresh is required.

---

# 19. Custom Trip Builder

The Custom Builder follows a guided wizard.

```text

Destination

↓

Filters

↓

Transportation

↓

Accommodation

↓

Activities

↓

Meals

↓

Add-ons

↓

Price Summary

↓

Save Quote

↓

Checkout

```

---

## Live Pricing

The sidebar updates automatically whenever the user changes any option.

Displays

Transport

Accommodation

Activities

Meals

Taxes

Discounts

Platform Fee

Grand Total

Price Per Traveler

---

## Save Quote

Users can

Save

Duplicate

Share

Export

Continue Later

Lock Price

---

# 20. Future Destination Radar

Displays teaser trips before they officially launch.

Each card contains

Destination

Target Month

Vibe

Interest Count

Interested Button

Notify Me Button

Progress Indicator

If enough users show interest,

Admins can convert it into a Planned Trip directly from the dashboard.


---

# 21. Traveler HQ

Traveler HQ is the personalized dashboard for every traveler.

Instead of showing a simple booking history, it acts as the user's travel profile, passport and command center.

---

## Dashboard Layout

```text

Upcoming Trip Banner

↓

Travel Passport

↓

Achievements

↓

Travel Squad

↓

Memory Vault

↓

Wallet

↓

Recent Notifications

↓

Recommended Trips

```

---

## Travel Passport

Displays

- Profile Picture
- Name
- Member Since
- Current Level
- Total XP
- Trips Completed
- States Visited
- Countries Visited
- Total Distance Traveled

---

## Achievement Badges

Badges are unlocked automatically.

Examples

🏔 Mountain Explorer

🌊 Beach Lover

🥾 Trekking Expert

🌅 Sunrise Chaser

🍜 Food Explorer

🛶 Adventure Seeker

🥇 Early Bird

🤝 Group Leader

💎 Premium Traveler

Each badge displays

- Icon
- Title
- Description
- Unlock Date

---

## Upcoming Trip Widget

Displays

Destination

Countdown Timer

Trip Captain

Pickup Point

Weather

Packing Checklist

Quick Actions

Actions include

Download Ticket

Join War Room

Chat with Captain

Emergency Contacts

View Itinerary

Track Pickup Vehicle

---

## Travel Squad

Users can save their frequent travel companions.

Each companion stores

- Name
- Age
- Gender
- Emergency Contact
- Government ID
- Medical Notes

Companions can be selected during checkout with one click.

---

## Memory Vault

Displays previous trips in a timeline.

Each trip includes

Gallery

Trip Story

Invoices

Certificates

Reviews

Shared Memories

Users can download or share albums directly.

---

## WanderCoin Wallet

Displays

Current Balance

Lifetime Earned

Lifetime Redeemed

Pending Rewards

Referral Earnings

Transaction History

Users can redeem coins during checkout.

---

## Personalized Recommendations

AI recommends trips based on

Past Trips

Budget

Favorite Activities

Preferred Travel Style

Season

Travel Group Size

---

# 22. Vendor Dashboard

The Vendor Dashboard allows partners to manage their trips efficiently.

Main Sections

Dashboard

Trips

Bookings

Revenue

Customers

Inventory

Pricing

Reviews

Settings

---

## Dashboard Widgets

Today's Revenue

Active Trips

Occupancy

Upcoming Departures

Pending Payments

Customer Ratings

Monthly Revenue Chart

Recent Bookings

---

## Trip Management

Vendor can

Create Trip

Edit Trip

Delete Trip

Pause Bookings

Manage Seats

Manage Pricing

Upload Images

Configure Add-ons

---

## Pricing Manager

Allows dynamic pricing adjustments.

Factors

Base Price

Demand Multiplier

Seasonal Pricing

Festival Pricing

Discount Campaigns

Coupon Rules

Live Preview updates before publishing.

---

## Booking Management

Vendor sees

Booking Status

Passenger List

Payment Status

Boarding Status

Cancellation Requests

Special Requests

---

# 23. Admin Dashboard

Admin has complete system control.

Main Sections

Dashboard

Users

Vendors

Trips

Future Radar

Analytics

Payments

Reports

Notifications

Settings

---

## Dashboard Widgets

Registered Users

Active Trips

Revenue

Pending Approvals

Upcoming Trips

Bookings Today

Conversion Rate

AI Usage

---

## User Management

Admin can

View Users

Suspend Users

Reset Passwords

Assign Roles

Verify Accounts

View Reports

---

## Vendor Management

Approve Vendors

Reject Vendors

Suspend Vendors

View Performance

Revenue Reports

Trip Quality Score

---

## Future Destination Radar

Displays

Destination

Interest Count

Growth Trend

Estimated Revenue

Suggested Launch Date

Convert to Planned Trip Button

---

## AI Analytics

Tracks

Most Asked Destinations

Average Prompt Length

Popular Budgets

Top Travel Interests

AI Success Rate

Fallback Rate

Average Response Time

---

# 24. Notification Center

The notification center provides a unified inbox.

Categories

Bookings

Payments

Trips

AI

War Room

Offers

System Alerts

Users can

Mark as Read

Archive

Delete

Filter

Search

Notifications synchronize across all devices.

---

# 25. Search Experience

Search is available globally.

Supports

Trips

Destinations

Activities

Hotels

Users

Vendors

War Rooms

Recent Searches

Popular Searches

AI Suggestions


---

# 26. Responsive Design Strategy

The application follows a Mobile First approach.

Breakpoints

| Device | Width |
|----------|---------|
| Mobile | <640px |
| Tablet | 640px–1024px |
| Laptop | 1024px–1440px |
| Desktop | >1440px |

Navigation adapts automatically.

Desktop

Sidebar + Navbar

Tablet

Collapsible Sidebar

Mobile

Bottom Navigation + Drawer

---

# 27. Performance Optimization

Strategies

Lazy Loading

Route Splitting

Image Optimization

Memoization

Virtual Lists

Request Caching

Skeleton Loading

Optimistic Updates

Debounced Search

Infinite Scroll

These reduce loading time and improve user experience.

---

# 28. Accessibility

The UI follows WCAG guidelines.

Features

Keyboard Navigation

Screen Reader Support

ARIA Labels

Color Contrast

Focus Indicators

Large Click Targets

Semantic HTML

Accessible Forms

---

# 29. Frontend Coding Standards

General Guidelines

- Functional Components
- Custom Hooks for Logic
- Reusable Components
- Type-safe APIs
- Small Components
- No Inline Business Logic
- Feature-first Folder Structure
- Meaningful Naming
- Lazy Loading where applicable
- Consistent Styling

---

# 30. Frontend Architecture Summary

The WanderSync frontend is designed to deliver a premium, responsive and highly interactive travel experience.

Key principles include

- Modular Feature Architecture
- Component Reusability
- Mobile First Design
- Real-Time Collaboration
- AI-Driven Experiences
- Accessible Interfaces
- Scalable State Management
- High Performance
- Modern Design System

This architecture provides a strong foundation for building a production-ready travel platform while keeping the codebase organized and maintainable.