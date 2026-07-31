# WanderSync Custom Trip Builder

---

# Purpose

The Custom Trip Builder enables travelers to design their own travel package by selecting modular components instead of purchasing fixed itineraries.

The system provides complete flexibility while maintaining real-time pricing, backend validation, and a transparent cost breakdown.

Users can customize every aspect of their journey without sacrificing simplicity.

---

# Vision

Planning a trip should feel like configuring a premium product.

Instead of selecting one predefined package, users create their own itinerary using modular travel components.

Every change instantly updates the itinerary and the live quote.

---

# Builder Flow

```
Select Destination

↓

Choose Travelers

↓

Apply Filters

↓

Customize Package

↓

Review Live Quote

↓

Save Quote

↓

Lock Quote

↓

Proceed to Booking
```

---

# Core Objectives

- Complete travel flexibility
- Transparent pricing
- Real-time quote generation
- Personalized recommendations
- Modular package creation
- AI-assisted planning
- Seamless booking conversion

---

# Step 1 — Destination Selection

Users can begin by selecting:

- Destination
- Travel Dates
- Number of Travelers
- Number of Nights
- Starting City

The system loads all available catalog options for that destination.

---

# Step 2 — Smart Filter Engine

To reduce decision fatigue, users can apply filters.

## Budget Tier

- Budget
- Standard
- Premium
- Luxury

---

## Travel Vibe

- Adventure
- Wellness
- Family
- Romantic
- Solo
- Backpacking
- Food Explorer
- Nightlife
- Heritage
- Wildlife

---

## Pace

- Relaxed
- Balanced
- Fast Paced

---

## Dietary Preference

- Veg
- Jain
- Vegan
- Non-Veg

---

## Accessibility

- Wheelchair Friendly
- Senior Citizen Friendly
- Child Friendly
- Pet Friendly

---

## Transport Preference

- Bus
- Train
- Flight
- Private Cab
- Self Drive

---

# Step 3 — Package Modules

Users customize each travel component independently.

---

## Transportation

Options

- Volvo Bus
- Train
- Flight
- Private Sedan
- SUV
- Tempo Traveller
- Self Drive Rental

Configuration

Pickup

Drop

Vehicle Type

Seat Preference

Luggage

---

## Accommodation

Categories

Hostel

Homestay

Hotel

Luxury Resort

Camping

Glamping

Tree House

Houseboat

Options

Room Type

Twin Sharing

Triple Sharing

Private Room

Extra Mattress

Breakfast Included

Refund Policy

---

## Activities

Each activity contains

Title

Duration

Difficulty

Price

Location

Rating

Availability

Equipment Included

Maximum Capacity

Examples

Scuba Diving

ATV Ride

Paragliding

Camping

Food Walk

River Rafting

Museum Tour

Cooking Workshop

---

## Meal Plans

CP

Breakfast

MAP

Breakfast + Dinner

AP

All Meals

Optional

Wine Tasting

BBQ Night

Candlelight Dinner

Local Cuisine Experience

---

## Add-ons

Travel Insurance

Professional Photographer

Tour Guide

Equipment Rental

Airport Pickup

Priority Boarding

Medical Assistance

Emergency Support

SIM Card

Portable Wi-Fi

---

# Step 4 — AI Recommendations

The AI Planner can recommend

Better Hotels

Alternative Activities

Lower Cost Options

Premium Upgrades

Hidden Gems

Weather-Based Suggestions

Crowd Avoidance

Local Festivals

The user always has full control.

---

# Live Pricing Engine

Every modification recalculates the quote instantly.

Formula

```
Total Cost =
Transport
+
Accommodation
+
Activities
+
Meals
+
Add-ons
+
Taxes
-
Discounts
-
Wallet Coins
```

No page refresh required.

---

# Price Breakdown

The UI always displays

Transport Cost

Accommodation Cost

Activity Cost

Meal Cost

Taxes

Platform Fee

Discount

Coupon Savings

Wallet Deduction

Grand Total

Per Person Cost

---

# Quote Locking

Users can lock a quote for a configurable duration.

Example

Lock Price

↓

24 Hours

↓

Same Price Guaranteed

↓

Book Later

---

# Saved Quotes

Users can

Rename Quote

Duplicate Quote

Share Quote

Delete Quote

Resume Editing

Convert Quote to Booking

---

# Catalog Management

Every destination contains modular catalog items.

Categories

Transport

Hotels

Activities

Meals

Equipment

Guides

Insurance

Coupons

Each option can be enabled or disabled independently.

---

# Backend Components

Collections

custom_trip_quotes

custom_catalog_options

pricing_rules

discount_rules

quote_history

Repositories

QuoteRepository

CatalogRepository

PricingRepository

Services

QuoteService

PricingService

CatalogService

RecommendationService

Controllers

QuoteController

CatalogController

PricingController

---

# APIs

GET /catalog/{destination}

POST /quotes

PUT /quotes/{id}

GET /quotes/{id}

GET /quotes/my

POST /quotes/{id}/lock

POST /quotes/{id}/book

DELETE /quotes/{id}

---

# Validation Rules

Destination Required

Travelers > 0

Valid Travel Dates

Available Inventory

Maximum Group Size

Valid Coupon

Supported Add-ons

Inventory Availability

---

# Security

JWT Authentication

Ownership Validation

Server-side Pricing

Rate Limiting

Input Validation

Audit Logging

No Client Price Trust

---

# Analytics

Most Selected Destination

Most Popular Activities

Average Quote Value

Quote to Booking Conversion

Drop-off Rate

Luxury vs Budget Trends

Most Used Filters

Average Group Size

Average Quote Build Time

---

# Future Enhancements

Drag-and-Drop Itinerary Builder

Interactive Map Planning

Google Maps Integration

Weather-aware Recommendations

AI Budget Optimizer

Carbon Footprint Calculator

Multi-City Planning

Corporate Travel Packages

Offline Quote Support

Voice-based Trip Builder

Travel Companion Suggestions

---

# Engineering Principles

The Custom Trip Builder should feel intuitive, transparent, and flexible.

Every change must update pricing instantly.

All calculations must be validated on the server.

Users should always understand why a price changed.

The builder must scale to support thousands of destinations and millions of configurable travel combinations.