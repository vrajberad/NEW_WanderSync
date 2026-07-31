# WanderSync API Specification

---

# Purpose

This document defines every major REST API exposed by the WanderSync backend.

It serves as the contract between:

- Frontend Team
- Backend Team
- AI Agents
- QA Team

All APIs follow REST principles.

Base URL

/api/v1

---

# Authentication

Authentication Method

JWT Bearer Token

Header

Authorization: Bearer <JWT_TOKEN>

---

# Standard API Response

Success

{
    "success": true,
    "message": "Operation successful",
    "data": {}
}

Failure

{
    "success": false,
    "message": "Validation failed",
    "errors": []
}

---

# Authentication Module

POST

/auth/register

Description

Register new traveler/vendor.

Request

{
    "name":"",
    "email":"",
    "password":"",
    "role":"TRAVELER"
}

Response

JWT Token

---

POST

/auth/login

Request

{
    "email":"",
    "password":""
}

Response

JWT Token

User Information

Role

---

POST

/auth/refresh

Returns new JWT token.

---

GET

/auth/me

Returns currently logged in user.

---

# User Module

GET

/users/profile

Returns profile.

---

PUT

/users/profile

Update profile.

---

GET

/users/bookings

Booking history.

---

GET

/users/upcoming-trips

Upcoming trips.

---

GET

/users/dashboard

Traveler HQ dashboard data.

---

# Trip Module

GET

/trips

Search trips.

Filters

Destination

Budget

Month

Category

Difficulty

Seats

Price

Sort

Pagination

---

GET

/trips/{id}

Trip Details.

Includes

Itinerary

Seats

Vendor

Pricing

Activities

Meals

Gallery

Reviews

FAQs

---

POST

/trips

Vendor only.

Create trip.

---

PUT

/trips/{id}

Vendor only.

Update trip.

---

DELETE

/trips/{id}

Vendor only.

Delete trip.

---

# Booking Module

POST

/bookings/lock

Lock seats.

---

POST

/bookings/create

Create booking.

---

POST

/bookings/confirm

Confirm booking.

---

DELETE

/bookings/{id}

Cancel booking.

---

GET

/bookings/my

User bookings.

---

GET

/bookings/{id}

Booking details.

---

# Pricing Module

POST

/pricing/quote

Calculate live quotation.

Inputs

Destination

Group Size

Transport

Hotel

Meals

Activities

Add-ons

Returns

Detailed Price Breakdown

Taxes

Discount

Platform Fee

Grand Total

---

# Payment Module

POST

/payments/create-order

Generate Razorpay Order.

---

POST

/payments/webhook

Payment callback.

---

GET

/payments/history

Payment history.

---

POST

/payments/refund

Initiate refund.

Admin only.

---

# AI Travel Architect

POST

/ai/plan

Input

Natural Language Prompt

Returns

Structured itinerary.

---

POST

/ai/improve

Improve itinerary.

---

POST

/ai/regenerate-day

Regenerate specific day.

---

POST

/ai/save

Save AI itinerary.

---

# Future Destination Radar

GET

/future-trips

List teaser trips.

---

POST

/future-trips/{id}/interest

Mark interested.

---

DELETE

/future-trips/{id}/interest

Remove interest.

---

GET

/admin/future-trips

Admin analytics.

---

POST

/admin/future-trips

Create teaser trip.

---

PUT

/admin/future-trips/{id}/publish

Convert teaser to planned trip.

---

# Custom Trip Builder

GET

/catalog

Returns available options.

Supports Filters

Budget

Vibe

Transport

Meals

Activities

Accessibility

Luxury Tier

---

POST

/custom-trip/quote

Generate quote.

---

POST

/custom-trip/save

Save itinerary.

---

POST

/custom-trip/book

Lock custom booking.

---

# War Room

POST

/war-room/create

Create room.

---

POST

/war-room/join

Join room.

---

POST

/war-room/vote

Vote.

---

GET

/war-room/{id}

Room details.

---

POST

/war-room/lock-booking

Start payment.

---

# Vendor Module

GET

/vendor/dashboard

Analytics.

---

GET

/vendor/trips

Vendor trips.

---

GET

/vendor/bookings

Bookings.

---

GET

/vendor/revenue

Revenue.

---

GET

/vendor/occupancy

Occupancy.

---

PUT

/vendor/pricing

Dynamic pricing.

---

# Admin Module

GET

/admin/dashboard

Platform analytics.

---

GET

/admin/users

Users.

---

GET

/admin/vendors

Vendors.

---

GET

/admin/trips

Trips.

---

GET

/admin/payments

Payments.

---

GET

/admin/reports

Business reports.

---

# Notification Module

GET

/notifications

User notifications.

---

PUT

/notifications/read

Mark as read.

---

PUT

/notifications/read-all

Read all.

---

# Wallet Module

GET

/wallet

Balance.

---

GET

/wallet/history

Ledger.

---

POST

/wallet/redeem

Redeem coins.

---

# Referral Module

GET

/referrals

Referral dashboard.

---

POST

/referrals/apply

Apply referral code.

---

# Review Module

POST

/reviews

Add review.

---

PUT

/reviews/{id}

Edit review.

---

DELETE

/reviews/{id}

Delete review.

---

GET

/reviews/trip/{id}

Trip reviews.

---

# Media Module

POST

/media/upload

Upload image.

---

DELETE

/media/delete

Delete image.

---

GET

/media/gallery/{tripId}

Trip gallery.

---

# Common Status Codes

200 OK

201 Created

204 No Content

400 Bad Request

401 Unauthorized

403 Forbidden

404 Not Found

409 Conflict

422 Validation Failed

429 Too Many Requests

500 Internal Server Error

---

# API Versioning

Current Version

v1

Future

v2

Breaking changes will only be introduced in new API versions.

---

# Security

JWT Authentication

Role Based Authorization

Rate Limiting

Request Validation

Input Sanitization

HTTPS Only

Idempotent Payment APIs

Webhook Signature Verification

Optimistic Locking

Audit Logging