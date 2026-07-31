# WanderSync

# Database Design

---

# Purpose

This document serves as the single source of truth for the database architecture of WanderSync.

It defines every MongoDB collection, relationships, naming conventions, indexing strategy, validation rules, document structure, and future scalability plans.

Every backend service, frontend module, AI component, analytics engine, notification workflow, and payment module references this document.

---

# 1. Database Philosophy

WanderSync is built on MongoDB Atlas using a document-oriented database architecture.

Instead of forcing highly relational SQL tables, the system stores related information together inside flexible JSON-like documents.

This allows the platform to evolve rapidly while keeping development simple and scalable.

The database is designed around business domains rather than technical layers.

Each collection represents a core business capability of the platform.

Examples include:

- Users
- Trips
- Bookings
- Payments
- AI Knowledge Base
- War Rooms
- Notifications
- Wallet
- Traveler Statistics

The database is optimized for

- Fast read operations
- Flexible schema evolution
- Horizontal scalability
- AI vector search
- Real-time collaboration
- Event-driven architecture

---

# 2. Why MongoDB Atlas

MongoDB Atlas has been selected because it provides all capabilities required for the platform without introducing multiple databases.

Benefits include

- Document Database
- Cloud Managed
- Automatic Backups
- Global Availability
- Atlas Search
- Atlas Vector Search
- Aggregation Pipelines
- Geospatial Queries
- Full Text Search
- Change Streams
- Horizontal Scaling

MongoDB Atlas also integrates directly with Spring Boot and Spring AI.

No additional vector database is required because embeddings are stored inside MongoDB Atlas itself.

---

# 3. Database Design Principles

The database follows these principles.

## Single Source of Truth

Every piece of business data exists in only one authoritative location.

For example

Bookings own booking information.

Payments own payment information.

Trips own inventory.

Statistics are derived instead of duplicated.

---

## Domain Driven Collections

Collections are grouped according to business capability instead of technical implementation.

Examples

Authentication

Booking

Payments

Traveler

Vendor

AI

War Room

Notifications

Analytics

---

## Document First Design

Frequently accessed information is embedded whenever appropriate.

Large reusable entities remain independent collections.

This reduces joins and improves performance.

---

## Event Driven Updates

Modules communicate using events rather than directly modifying unrelated collections.

Example

Booking Confirmed

↓

Payment Captured

↓

Notification Created

↓

Traveler Statistics Updated

↓

Wallet Reward Added

---

## Read Optimized

The majority of platform traffic is reading information.

Trip browsing

AI recommendations

Traveler dashboard

Vendor dashboard

Notifications

Analytics

Collections are designed to minimize database lookups.

---

## Future Scalability

Every collection should be capable of moving into an independent microservice without requiring major schema redesign.

---

# 4. Naming Conventions

## Collections

All collection names use

lowercase

snake_case

Examples

users

scheduled_trips

future_teaser_trips

wallet_transactions

saved_companions

notification_logs

travel_knowledge

custom_catalog_options

war_room_lobbies

---

## Fields

Fields use camelCase.

Examples

firstName

createdAt

bookingStatus

paymentStatus

totalAmount

dynamicPriceMultiplier

seatLockExpiry

---

## IDs

Every document uses MongoDB ObjectId.

External identifiers may additionally exist.

Examples

bookingCode

tripCode

invoiceNumber

paymentReference

referralCode

---

## Dates

All timestamps are stored in UTC.

Examples

createdAt

updatedAt

paymentTime

bookingExpiry

departureDate

arrivalDate

---

## Currency

All monetary values are stored in INR.

Decimal precision is preserved.

Example

19999.00

Never store formatted strings.

Incorrect

"₹19,999"

Correct

19999.00

---

## Images

The database stores only URLs.

Images are uploaded to Cloudinary.

Example

coverImage

galleryImages

profilePhoto

ticketPdf

---

# 5. Collection Categories

The database is divided into multiple business domains.

Each domain owns its own collections.

## Authentication

Responsible for

- Users
- Roles
- Authentication Data
- Permissions

---

## Travel

Responsible for

- Trips
- Destinations
- Seat Maps
- Itineraries

---

## Booking

Responsible for

- Bookings
- Payments
- Invoices
- Refunds

---

## AI

Responsible for

- Travel Knowledge
- Embeddings
- Prompt Logs
- Recommendations

---

## Traveler

Responsible for

- Dashboard
- Badges
- Wallet
- Squad
- Memories

---

## Vendor

Responsible for

- Vendor Profiles
- Vendor Analytics
- Trip Inventory

---

## War Room

Responsible for

- Lobbies
- Polls
- Messages
- Voting
- Split Payments

---

## Notifications

Responsible for

- Emails
- WhatsApp
- Push Notifications
- Notification Logs

---

## Platform

Responsible for

- Audit Logs
- Platform Settings
- System Configuration

---

# 6. Complete Collection Overview

The current database consists of the following collections.

## Core Collections

- users
- scheduled_trips
- bookings
- payments
- vendors

---

## AI Collections

- travel_knowledge
- ai_prompt_logs
- embedding_metadata

---

## War Room Collections

- war_room_lobbies
- war_room_polls
- war_room_messages

---

## Future Destination Collections

- future_teaser_trips
- teaser_interest

---

## Custom Package Builder Collections

- custom_catalog_options
- saved_trip_quotes

---

## Traveler Collections

- traveler_stats
- traveler_badges
- wallet_transactions
- saved_companions
- memory_gallery

---

## Notification Collections

- notifications
- notification_logs

---

## Review Collections

- trip_reviews

---

## Administration Collections

- audit_logs
- platform_settings

---

## Analytics Collections

- analytics_snapshots


---

# 7. Collection Design

This section defines every MongoDB collection used in WanderSync.

Each collection includes

- Purpose
- Owner Module
- Fields
- Relationships
- Indexes
- Business Rules
- Sample Document

---

# Collection 1 : users

## Purpose

Stores every registered user on the platform.

A user can be

- Traveler
- Vendor
- Admin

This collection is the foundation of authentication and user management.

---

## Owner Module

Authentication Module

---

## Relationships

User

↓

Bookings

↓

Payments

↓

Traveler Stats

↓

Wallet

↓

Notifications

↓

Reviews

↓

Saved Companions

↓

War Rooms

---

## Fields

| Field | Type | Required | Description |
|--------|------|----------|-------------|
| id | ObjectId | Yes | Primary Identifier |
| firstName | String | Yes | User First Name |
| lastName | String | Yes | User Last Name |
| email | String | Yes | Unique Email |
| password | String | Yes | BCrypt Password |
| phoneNumber | String | Yes | Mobile Number |
| profileImage | String | No | Cloudinary URL |
| role | Enum | Yes | ADMIN / VENDOR / TRAVELER |
| accountStatus | Enum | Yes | ACTIVE / BLOCKED / PENDING |
| emailVerified | Boolean | Yes | Email Verification |
| phoneVerified | Boolean | Yes | Phone Verification |
| referralCode | String | Yes | Unique Referral Code |
| referredBy | ObjectId | No | Referring User |
| preferences | Object | No | Travel Preferences |
| lastLogin | Date | No | Last Login Time |
| createdAt | Date | Yes | Creation Time |
| updatedAt | Date | Yes | Update Time |

---

## Preferences Object

The preferences object stores user interests.

Example

- Preferred Budget
- Favorite Destinations
- Preferred Travel Style
- Dietary Preference
- Accessibility Requirements

---

## Indexes

Unique

email

phoneNumber

referralCode

Indexes

role

accountStatus

createdAt

---

## Business Rules

Email must always be unique.

Passwords are never stored in plain text.

Passwords must use BCrypt hashing.

Referral codes cannot be duplicated.

Users cannot change their role directly.

Admin controls role assignment.

Soft delete should be preferred over permanent deletion.

---

## Sample Document

```json
{
  "_id": "6654ab1234",
  "firstName": "Viraj",
  "lastName": "Berad",
  "email": "viraj@gmail.com",
  "password": "$2a$10$...",
  "phoneNumber": "9876543210",
  "profileImage": "https://cloudinary.com/profile.jpg",
  "role": "TRAVELER",
  "accountStatus": "ACTIVE",
  "emailVerified": true,
  "phoneVerified": false,
  "referralCode": "VIRAJ001",
  "referredBy": null,
  "preferences": {
    "budget": "MID",
    "vibe": "Adventure",
    "diet": "Veg"
  },
  "createdAt": "2026-07-01T10:00:00Z",
  "updatedAt": "2026-07-01T10:00:00Z"
}
```

---

# Collection 2 : scheduled_trips

## Purpose

Stores all planned trips created by Vendors or Admins.

Users browse these trips and create bookings.

---

## Owner Module

Trip Management

---

## Relationships

Trip

↓

Bookings

↓

Payments

↓

Reviews

↓

War Room

↓

Notifications

↓

Analytics

---

## Fields

| Field | Type | Required | Description |
|--------|------|----------|-------------|
| id | ObjectId | Yes | Primary Identifier |
| vendorId | ObjectId | Yes | Trip Owner |
| title | String | Yes | Trip Title |
| destination | String | Yes | Destination Name |
| description | String | Yes | Complete Description |
| coverImage | String | Yes | Cloudinary URL |
| galleryImages | Array | No | Additional Images |
| departureDate | Date | Yes | Trip Start |
| returnDate | Date | Yes | Trip End |
| durationDays | Integer | Yes | Total Days |
| pickupLocation | String | Yes | Starting Point |
| dropLocation | String | Yes | Ending Point |
| totalSeats | Integer | Yes | Total Capacity |
| availableSeats | Integer | Yes | Remaining Seats |
| seatMap | Array | Yes | Seat Layout |
| basePrice | Decimal | Yes | Base Price |
| dynamicPriceMultiplier | Decimal | Yes | Pricing Engine |
| tripStatus | Enum | Yes | DRAFT / OPEN / FULL / COMPLETED |
| itinerary | Array | Yes | Daily Plan |
| inclusions | Array | Yes | Included Services |
| exclusions | Array | Yes | Excluded Services |
| createdAt | Date | Yes | Creation Time |
| updatedAt | Date | Yes | Update Time |

---

## Indexes

destination

vendorId

tripStatus

departureDate

createdAt

Compound

destination + departureDate

vendorId + tripStatus

---

## Business Rules

Available seats cannot become negative.

Trip cannot open without itinerary.

Vendor must own the trip.

Dynamic pricing never changes manually.

Booking confirmation automatically reduces seats.

---

## Sample Document

```json
{
  "_id": "trip001",
  "vendorId": "vendor123",
  "title": "Ladakh Adventure",
  "destination": "Leh Ladakh",
  "departureDate": "2026-09-12",
  "returnDate": "2026-09-18",
  "durationDays": 7,
  "pickupLocation": "Delhi",
  "dropLocation": "Delhi",
  "totalSeats": 30,
  "availableSeats": 18,
  "basePrice": 18000,
  "dynamicPriceMultiplier": 1.10,
  "tripStatus": "OPEN"
}
```

---

# Collection 3 : bookings

## Purpose

The bookings collection represents every reservation made on the platform.

A booking is the central business entity that connects users, trips, payments, tickets, notifications, reviews, and traveler history.

Every booking follows a lifecycle from seat reservation to trip completion.

---

## Owner Module

Booking Engine

---

## Relationships

Booking

↓

User

↓

Trip

↓

Payment

↓

Invoice

↓

QR Ticket

↓

Notification

↓

Traveler Dashboard

↓

Wallet Rewards

↓

Trip Review

↓

Analytics

---

## Fields

| Field | Type | Required | Description |
|--------|------|----------|-------------|
| id | ObjectId | Yes | Primary Identifier |
| bookingCode | String | Yes | Public Booking Reference |
| userId | ObjectId | Yes | Traveler |
| tripId | ObjectId | Yes | Scheduled Trip |
| vendorId | ObjectId | Yes | Trip Owner |
| bookingStatus | Enum | Yes | Current Booking Status |
| bookingType | Enum | Yes | SOLO / GROUP |
| warRoomId | ObjectId | No | Linked Group Lobby |
| passengers | Array | Yes | Passenger Details |
| selectedSeats | Array | Yes | Reserved Seats |
| addOns | Array | No | Selected Add-ons |
| couponCode | String | No | Applied Coupon |
| couponDiscount | Decimal | No | Discount Amount |
| subtotal | Decimal | Yes | Base Amount |
| taxes | Decimal | Yes | GST |
| platformFee | Decimal | Yes | Service Fee |
| finalAmount | Decimal | Yes | Total Payable |
| paymentStatus | Enum | Yes | Payment Status |
| ticketStatus | Enum | Yes | Ticket Generation Status |
| qrTicketUrl | String | No | QR Ticket |
| invoiceUrl | String | No | PDF Invoice |
| cancellationReason | String | No | Reason |
| cancelledBy | ObjectId | No | User/Admin |
| refundAmount | Decimal | No | Refund Value |
| refundStatus | Enum | No | Refund State |
| createdAt | Date | Yes | Booking Time |
| updatedAt | Date | Yes | Update Time |

---

## Passenger Object

Each booking contains passenger details.

Fields

- Full Name
- Age
- Gender
- Phone
- Emergency Contact
- Government ID Type
- Government ID Number
- Medical Notes

---

## Add-on Object

Each booking may contain

- Insurance
- Photographer
- Equipment Rental
- Tour Guide
- Meal Upgrade
- VIP Package

Each add-on stores

Title

Quantity

Price

Tax

Total

---

## Price Breakdown

The booking permanently stores the exact price calculation used during checkout.

This ensures future price changes never affect existing bookings.

Price Breakdown includes

Base Price

Dynamic Pricing

Transport

Accommodation

Activities

Meals

Add-ons

Taxes

Platform Fee

Coupon Discount

Final Amount

---

## Booking Status

Possible values

PENDING

SEAT_LOCKED

PENDING_PAYMENT

CONFIRMED

PARTIALLY_PAID

CHECKED_IN

ONGOING

COMPLETED

CANCELLED

EXPIRED

REFUNDED

---

## Payment Status

CREATED

PENDING

PARTIAL

PAID

FAILED

REFUNDED

---

## Ticket Status

NOT_GENERATED

GENERATING

GENERATED

DELIVERED

---

## Indexes

bookingCode (Unique)

userId

tripId

vendorId

bookingStatus

paymentStatus

createdAt

Compound Indexes

userId + bookingStatus

tripId + bookingStatus

vendorId + bookingStatus

bookingCode + userId

---

## Business Rules

Booking code must always be unique.

One seat cannot belong to multiple confirmed bookings.

Price becomes immutable after payment.

Refund amount cannot exceed payment amount.

Seat availability updates only after successful confirmation.

Cancelled bookings automatically release seats.

Completed bookings become read-only.

---

## Sample Document

```json
{
  "_id": "booking001",
  "bookingCode": "WS202600124",
  "userId": "user001",
  "tripId": "trip001",
  "vendorId": "vendor001",
  "bookingStatus": "CONFIRMED",
  "bookingType": "SOLO",
  "selectedSeats": [
    "A1",
    "A2"
  ],
  "subtotal": 36000,
  "taxes": 6480,
  "platformFee": 1200,
  "finalAmount": 43680,
  "paymentStatus": "PAID",
  "ticketStatus": "GENERATED",
  "qrTicketUrl": "https://cloudinary.com/ticket.pdf",
  "invoiceUrl": "https://cloudinary.com/invoice.pdf",
  "createdAt": "2026-07-12T10:00:00Z"
}
```

---

# Collection 4 : payments

## Purpose

Stores every payment transaction performed on WanderSync.

Payments are immutable financial records.

A booking may contain one payment or multiple payments in case of split payment.

---

## Owner Module

Payment Module

---

## Relationships

Payment

↓

Booking

↓

User

↓

Vendor

↓

Wallet

↓

Refund

↓

Notification

↓

Analytics

---

## Fields

| Field | Type | Required | Description |
|--------|------|----------|-------------|
| id | ObjectId | Yes | Primary Identifier |
| paymentReference | String | Yes | Internal Payment ID |
| gatewayPaymentId | String | No | Razorpay Payment ID |
| gatewayOrderId | String | No | Razorpay Order |
| bookingId | ObjectId | Yes | Booking |
| payerId | ObjectId | Yes | User |
| amount | Decimal | Yes | Amount Paid |
| currency | String | Yes | INR |
| paymentMethod | Enum | Yes | UPI / CARD / NETBANKING / WALLET |
| paymentStatus | Enum | Yes | CREATED / CAPTURED / FAILED |
| paymentGateway | String | Yes | Razorpay |
| gatewayResponse | Object | No | Raw Gateway Response |
| refundStatus | Enum | No | Refund |
| refundAmount | Decimal | No | Refund Amount |
| transactionTime | Date | Yes | Payment Time |
| createdAt | Date | Yes | Creation Time |

---

## Payment Status

CREATED

AUTHORIZED

CAPTURED

FAILED

REFUNDED

PARTIALLY_REFUNDED

---

## Refund Status

NOT_REQUESTED

REQUESTED

PROCESSING

COMPLETED

FAILED

---

## Indexes

paymentReference

bookingId

payerId

paymentStatus

gatewayPaymentId

transactionTime

---

## Business Rules

Payments cannot be edited.

Refunds create new gateway transactions.

Gateway webhook is the source of truth.

Client-side success responses are never trusted.

Duplicate payment references are not allowed.

---

## Sample Document

```json
{
  "_id": "payment001",
  "paymentReference": "PAY20260001",
  "bookingId": "booking001",
  "payerId": "user001",
  "amount": 43680,
  "currency": "INR",
  "paymentMethod": "UPI",
  "paymentStatus": "CAPTURED",
  "paymentGateway": "Razorpay",
  "transactionTime": "2026-07-12T10:15:00Z"
}
```

---

# Collection 5 : saved_trip_quotes

## Purpose

Stores unfinished and saved custom trip configurations created by users.

Unlike bookings, quotes do not reserve inventory or process payments.

They allow travelers to design, compare, edit, and revisit personalized itineraries before making a booking decision.

Each quote stores a complete snapshot of the selected configuration to preserve pricing and choices even if the live catalog changes later.

---

## Owner Module

Custom Trip Builder

---

## Relationships

Saved Quote

↓

User

↓

Destination

↓

Catalog Options

↓

Price Calculator

↓

Booking

↓

Analytics

↓

Notification Engine

---

## Fields

| Field | Type | Required | Description |
|--------|------|----------|-------------|
| id | ObjectId | Yes | Primary Identifier |
| quoteCode | String | Yes | Public Quote Reference |
| userId | ObjectId | Yes | Owner |
| destination | String | Yes | Selected Destination |
| startDate | Date | No | Planned Departure |
| endDate | Date | No | Planned Return |
| travelers | Integer | Yes | Number of Travelers |
| filters | Object | Yes | Selected Filters |
| transport | Object | Yes | Selected Transport |
| accommodation | Object | Yes | Selected Stay |
| activities | Array | Yes | Selected Activities |
| meals | Object | No | Meal Plan |
| addOns | Array | No | Extra Services |
| priceBreakdown | Object | Yes | Detailed Cost Snapshot |
| totalPrice | Decimal | Yes | Total Quote Value |
| priceLockedUntil | Date | No | Quote Lock Expiry |
| quoteStatus | Enum | Yes | ACTIVE / LOCKED / EXPIRED / CONVERTED |
| createdAt | Date | Yes | Creation Time |
| updatedAt | Date | Yes | Update Time |

---

## Business Rules

Quotes never reserve inventory.

Quotes can expire automatically.

Quotes can be converted directly into bookings.

Users may own multiple saved quotes.

Price locking is optional.

Expired quotes remain visible for analytics but cannot be booked without recalculation.

---

## Indexes

quoteCode

userId

destination

quoteStatus

createdAt

---

## Sample Document

```json
{
  "_id": "quote001",
  "quoteCode": "QT2026001",
  "userId": "user001",
  "destination": "Goa",
  "travelers": 4,
  "quoteStatus": "ACTIVE",
  "totalPrice": 64800
}
```

---

# Why This Collection Exists

Business Benefits

• Users never lose their custom itinerary.

• Enables "Continue Planning" functionality.

• Supports abandoned quote recovery emails.

• Allows price lock campaigns.

• Generates valuable analytics about traveler intent.

• Improves booking conversion rates.

• Supports AI itinerary refinement in future versions.


---

# Collection 6 : destinations

## Purpose

The destinations collection acts as the master catalog for every place supported by WanderSync.

Instead of storing destination information repeatedly inside Trips, AI Knowledge, Future Trips, Reviews, and Custom Builder modules, all modules reference a single destination document.

This creates consistency across the platform and greatly simplifies maintenance.

---

## Owner Module

Destination Management

---

## Relationships

Destination

↓

Scheduled Trips

↓

Future Teaser Trips

↓

Travel Knowledge

↓

Custom Catalog Options

↓

AI Recommendations

↓

Traveler Reviews

↓

Analytics

↓

Weather Integration

↓

Memory Gallery

---

## Fields

| Field | Type | Required | Description |
|--------|------|----------|-------------|
| id | ObjectId | Yes | Primary Identifier |
| slug | String | Yes | SEO Friendly URL |
| name | String | Yes | Destination Name |
| city | String | Yes | City |
| state | String | No | State |
| country | String | Yes | Country |
| latitude | Decimal | Yes | GPS Latitude |
| longitude | Decimal | Yes | GPS Longitude |
| description | String | Yes | Destination Overview |
| heroImage | String | Yes | Main Cover Image |
| galleryImages | Array | No | Additional Images |
| bestSeason | Array | No | Best Visiting Months |
| averageBudget | Object | No | Budget Estimates |
| climate | String | No | Climate Summary |
| languages | Array | No | Spoken Languages |
| currency | String | No | Currency |
| timeZone | String | No | Time Zone |
| emergencyContacts | Object | No | Police, Hospital etc. |
| popularActivities | Array | No | Activities |
| travelTags | Array | No | Adventure, Beach etc. |
| accessibility | Object | No | Accessibility Details |
| featured | Boolean | Yes | Featured Destination |
| active | Boolean | Yes | Available on Platform |
| averageRating | Decimal | No | Rating |
| totalReviews | Integer | No | Review Count |
| totalBookings | Integer | No | Booking Count |
| createdAt | Date | Yes | Creation Time |
| updatedAt | Date | Yes | Update Time |

---

## Business Rules

Every destination must have a unique slug.

Trips must reference a destination instead of duplicating destination information.

Deleting destinations is prohibited.

Inactive destinations remain available for historical bookings.

Hero image is mandatory.

Coordinates must be valid.

---

## Indexes

slug (Unique)

country

city

featured

travelTags

active

averageRating

Compound Indexes

country + city

featured + active

travelTags + active

---

## Sample Document

```json
{
  "_id": "dest001",
  "slug": "goa",
  "name": "Goa",
  "city": "Panaji",
  "country": "India",
  "featured": true,
  "travelTags": [
    "Beach",
    "Nightlife",
    "Adventure"
  ],
  "averageRating": 4.8,
  "totalBookings": 12842
}
```

---

# Why This Collection Exists

Business Benefits

• One source of truth.

• AI can search destinations directly.

• Easier SEO.

• Better analytics.

• Shared images.

• Shared weather.

• Faster frontend.

• No duplicated destination data.

---

# Collection 7 : future_teaser_trips

## Purpose

Allows the platform to validate market demand before investing time and money into creating a complete scheduled trip.

Admins publish teaser trips with minimal information and collect user interest.

If demand reaches a predefined threshold, the teaser can be converted into a full scheduled trip.

---

## Owner Module

Demand Validation Engine

---

## Relationships

Future Teaser Trip

↓

Destination

↓

Interested Travelers

↓

Notifications

↓

Admin Dashboard

↓

Analytics

↓

Scheduled Trip

---

## Fields

| Field | Type | Required | Description |
|--------|------|----------|-------------|
| id | ObjectId | Yes | Primary Identifier |
| destinationId | ObjectId | Yes | Linked Destination |
| title | String | Yes | Display Title |
| targetMonth | Integer | Yes | Planned Month |
| targetYear | Integer | Yes | Planned Year |
| vibe | String | Yes | Adventure / Wellness etc. |
| heroImage | String | Yes | Preview Image |
| teaserDescription | String | Yes | Short Description |
| interestCount | Integer | Yes | Cached Count |
| demandThreshold | Integer | Yes | Required Interest |
| teaserStatus | Enum | Yes | DRAFT / LIVE / CONVERTED / CLOSED |
| createdBy | ObjectId | Yes | Admin |
| publishedAt | Date | No | Publish Time |
| convertedTripId | ObjectId | No | Generated Trip |
| createdAt | Date | Yes | Creation Time |
| updatedAt | Date | Yes | Update Time |

---

## Business Rules

Only Admins can publish teaser trips.

Users cannot register interest twice.

Interest count updates automatically.

Conversion creates a Scheduled Trip.

Converted teaser becomes read-only.

---

## Indexes

destinationId

teaserStatus

targetYear

interestCount

featured

Compound Indexes

destinationId + teaserStatus

targetYear + targetMonth

---

## Sample Document

```json
{
  "_id": "teaser001",
  "destinationId": "dest001",
  "title": "Spiti Valley September Expedition",
  "targetMonth": 9,
  "targetYear": 2026,
  "interestCount": 324,
  "demandThreshold": 250,
  "teaserStatus": "LIVE"
}
```

---

# Recommended Improvement

Instead of storing every interested user inside this document, create another collection called

teaser_interest

This avoids documents becoming too large as popularity increases.

Each interest becomes one document.

Example

User A

↓

Spiti Valley

↓

Interested

↓

Group Size = 4

↓

Timestamp

This makes analytics dramatically easier.


---

# Collection 8 : custom_catalogs

## Purpose

Acts as the master configuration for the Custom Trip Builder of a destination.

Instead of storing every transport, hotel, activity, meal, and add-on directly inside one document, this collection references specialized option collections.

It defines which configuration modules are available for a destination and serves as the entry point for building a custom package.

---

## Owner Module

Custom Trip Builder

---

## Relationships

Custom Catalog

↓

Destination

↓

Transport Options

↓

Accommodation Options

↓

Activity Options

↓

Meal Plan Options

↓

Add-on Options

↓

Saved Trip Quotes

↓

Bookings

---

## Fields

| Field | Type | Required | Description |
|--------|------|----------|-------------|
| id | ObjectId | Yes | Primary Identifier |
| destinationId | ObjectId | Yes | Linked Destination |
| catalogName | String | Yes | Display Name |
| budgetTiers | Array | Yes | Budget / Standard / Luxury |
| supportedTravelStyles | Array | Yes | Adventure, Wellness, Heritage, etc. |
| supportedTransportModes | Array | Yes | Enabled Transport Categories |
| supportedAccommodationTypes | Array | Yes | Enabled Stay Categories |
| supportedMealPlans | Array | Yes | Enabled Meal Categories |
| supportedAddOns | Array | Yes | Enabled Add-ons |
| active | Boolean | Yes | Catalog Status |
| version | Integer | Yes | Catalog Version |
| createdAt | Date | Yes | Creation Time |
| updatedAt | Date | Yes | Update Time |

---

## Business Rules

Every destination can have only one active catalog version.

New catalog updates should create a new version instead of overwriting the previous one.

Old quotes continue referencing their original catalog version.

---

## Indexes

destinationId

active

version

---

## Sample Document

```json
{
  "_id": "catalog001",
  "destinationId": "dest001",
  "catalogName": "Goa Builder",
  "budgetTiers": [
    "BUDGET",
    "STANDARD",
    "LUXURY"
  ],
  "version": 1,
  "active": true
}
```

---

# Collection 9 : transport_options

## Purpose

Stores all transport choices available for a destination.

---

## Fields

| Field | Type |
|--------|------|
| id | ObjectId |
| destinationId | ObjectId |
| category | Enum |
| title | String |
| description | String |
| capacity | Integer |
| priceType | Enum |
| basePrice | Decimal |
| pricePerPerson | Decimal |
| image |
| filters |
| availability |
| active |
| createdAt |

---

## Categories

BUS

TRAIN

FLIGHT

SUV

SEDAN

TEMPO_TRAVELLER

SELF_DRIVE

BIKE_RENTAL

HELICOPTER

---

## Price Types

PER_PERSON

PER_VEHICLE

PER_DAY

---

## Sample

```json
{
"title":"Private Innova",
"category":"SUV",
"capacity":6,
"priceType":"PER_VEHICLE",
"basePrice":12000
}
```

---

# Collection 10 : accommodation_options

## Purpose

Stores every stay available for a destination.

---

## Fields

Hotel Name

Hotel Type

Star Rating

Room Types

Nightly Price

Meal Included

Images

Amenities

Location

Latitude

Longitude

Cancellation Policy

Availability

Vendor

---

## Hotel Types

Hostel

Homestay

Hotel

Resort

Villa

Treehouse

Glamping

Houseboat

---

## Sample

Boutique Homestay

Twin Sharing

₹4500/night

---

# Collection 11 : activity_options

## Purpose

Stores every activity that users can add to their custom package.

---

## Fields

Title

Destination

Difficulty

Duration

Price

Age Limit

Season

Equipment Included

Adventure Score

Indoor/Outdoor

Image

Vendor

Availability

---

## Examples

Scuba Diving

ATV Ride

Snow Trek

River Rafting

Food Walk

Wine Tour

Museum Pass

Helicopter Ride

Camping

Photography Tour

---

# Collection 12 : meal_plan_options

## Purpose

Stores every available meal package.

---

## Types

Breakfast

CP

MAP

AP

Premium Dining

BBQ Night

Candlelight Dinner

Wine Tasting

Local Cuisine Experience

---

# Collection 13 : addon_options

## Purpose

Stores optional services that enhance the travel experience.

---

## Examples

Travel Insurance

GoPro Rental

Drone Shoot

Photographer

Tour Guide

Medical Kit

Sleeping Bag

Trekking Pole

Luxury Pickup

Airport Lounge Access

SIM Card

Portable WiFi

Birthday Decoration

Anniversary Decoration

Surprise Celebration

Private Chef

Camping Equipment

Emergency Rescue


---

# Collection 14 : traveler_stats

## Purpose

Stores the lifetime travel statistics of every traveler.

Instead of calculating these metrics repeatedly from bookings, this collection maintains a continuously updated summary of each user's travel journey.

It powers the Traveler HQ dashboard, achievement system, and personalized recommendations.

---

## Owner Module

Traveler HQ

---

## Relationships

Traveler Stats

↓

User

↓

Bookings

↓

Badges

↓

Wallet

↓

Analytics

↓

Recommendations

---

## Fields

| Field | Type | Description |
|--------|------|-------------|
| id | ObjectId | Primary Identifier |
| userId | ObjectId | Traveler |
| completedTrips | Integer | Trips Completed |
| upcomingTrips | Integer | Upcoming Trips |
| cancelledTrips | Integer | Cancelled Trips |
| totalDistanceKm | Double | Total Distance |
| countriesVisited | Integer | Countries |
| statesVisited | Integer | States |
| citiesVisited | Integer | Cities |
| totalMoneySpent | Decimal | Lifetime Spend |
| totalDaysTravelled | Integer | Days |
| adventureTrips | Integer | Adventure Trips |
| familyTrips | Integer | Family Trips |
| soloTrips | Integer | Solo Trips |
| groupTrips | Integer | Group Trips |
| averageTripRating | Double | Avg Rating |
| travelerLevel | Integer | Level |
| experiencePoints | Integer | XP |
| createdAt | Date | Creation |
| updatedAt | Date | Update |

---

## Business Rules

Statistics update only after trip completion.

XP never decreases.

Cancelled trips do not contribute to achievements.

Distance is calculated automatically using destination coordinates.

---

## Indexes

userId (Unique)

travelerLevel

experiencePoints

completedTrips

---

## Sample

```json
{
  "userId":"user001",
  "completedTrips":8,
  "upcomingTrips":2,
  "totalDistanceKm":12480,
  "travelerLevel":6,
  "experiencePoints":4820
}
```

---

# Collection 15 : achievement_badges

## Purpose

Stores every badge available in WanderSync.

Badges encourage exploration and long-term engagement.

---

## Fields

| Field | Type |
|--------|------|
| id | ObjectId |
| badgeName | String |
| badgeIcon | String |
| description | String |
| category | Enum |
| xpReward | Integer |
| unlockCondition | Object |
| rarity | Enum |
| active | Boolean |

---

## Categories

Adventure

Explorer

Social

Early Bird

Luxury

Budget

Community

Safety

Loyalty

---

## Example Badges

🏔 Highlander

Complete 5 Mountain Trips

🌊 Ocean Explorer

Complete 5 Beach Trips

🛕 Heritage Hunter

Visit 10 Historical Places

🚀 Early Bird

Book within 1 hour of launch

🎉 Social Traveler

Complete 5 War Room trips

🌍 Globe Trotter

Visit 5 Countries

---

## Rarity

COMMON

RARE

EPIC

LEGENDARY

MYTHIC

---

# Collection 16 : user_badges

## Purpose

Stores badges unlocked by users.

Separating unlocked badges from badge definitions keeps the design scalable.

---

## Fields

| Field | Type |
|--------|------|
| id | ObjectId |
| userId | ObjectId |
| badgeId | ObjectId |
| unlockedAt | Date |
| sourceBookingId | ObjectId |

---

# Collection 17 : saved_companions

## Purpose

Allows travelers to save friends and family for faster booking.

---

## Fields

Passenger Name

Age

Gender

Phone

Emergency Contact

Government ID Type

Government ID Number

Relationship

Medical Notes

Favorite Companion

Created At

---

## Benefits

One-click checkout.

No repeated passenger forms.

Family-friendly booking.

---

# Collection 18 : trip_memories

## Purpose

Stores permanent memories from completed trips.

Acts as the user's travel journal.

---

## Fields

Trip

User

Photo Gallery URL

Video Gallery URL

Captain Notes

Trip Highlights

Certificate URL

Feedback

Created At

---

## Future Integrations

AWS S3

Cloudinary

Google Photos

Instagram Share

AI Generated Album

---

# Collection 19 : wandercoin_wallet

## Purpose

Stores the current WanderCoin balance of every traveler.

---

## Fields

Wallet ID

User ID

Current Balance

Lifetime Earned

Lifetime Redeemed

Wallet Status

Created At

Updated At

---

# Collection 20 : wallet_transactions

## Purpose

Immutable ledger of every wallet transaction.

Never edited.

Only appended.

---

## Transaction Types

Booking Reward

Referral Bonus

Manual Adjustment

Trip Cancellation

Wallet Redemption

Festival Bonus

Birthday Reward

Review Reward

Bug Bounty

Admin Credit

---

## Fields

Transaction ID

Wallet ID

Booking ID

Amount

Transaction Type

Reference

Balance After Transaction

Created At

---

# Collection 21 : referrals

## Purpose

Tracks the complete referral lifecycle.

---

## Fields

Referrer

Referred User

Referral Code

Status

Reward Amount

Reward Released

Booking ID

Created At

---

## Status

PENDING

REGISTERED

FIRST_BOOKING_DONE

REWARDED

EXPIRED

---

# Why Separate Wallet and Transactions?

Wallet

↓

Current Balance

Fast Reads

Transactions

↓

Complete Financial History

Audit Friendly

No Data Loss

Industry Standard

Exactly how Paytm, PhonePe, Amazon Pay, and banking systems work.


---

# Collection 22 : war_rooms

## Purpose

Represents a collaborative trip planning space where multiple travelers can discuss, vote, customize, and book a trip together.

Every War Room has one host and multiple members.

---

## Owner Module

War Room Engine

---

## Relationships

War Room

↓

Members

↓

Polls

↓

Votes

↓

Split Payments

↓

Messages

↓

Bookings

↓

Notifications

↓

Activity Logs

---

## Fields

| Field | Type | Description |
|--------|------|-------------|
| id | ObjectId | Primary Identifier |
| roomCode | String | Shareable Invite Code |
| hostUserId | ObjectId | Creator |
| tripId | ObjectId | Linked Trip |
| destinationId | ObjectId | Destination |
| bookingId | ObjectId | Generated Booking |
| roomStatus | Enum | ACTIVE / LOCKED / COMPLETED / EXPIRED |
| maxMembers | Integer | Maximum Members |
| currentMembers | Integer | Current Members |
| expiresAt | Date | Auto Expiry |
| createdAt | Date | Creation |
| updatedAt | Date | Update |

---

## Business Rules

Only Host can lock itinerary.

Room automatically expires after inactivity.

One active booking per room.

Host can transfer ownership.

---

## Indexes

roomCode

hostUserId

roomStatus

tripId

---

## Sample

```json
{
  "roomCode":"GOA2026",
  "hostUserId":"user001",
  "tripId":"trip001",
  "roomStatus":"ACTIVE",
  "currentMembers":5
}
```

---

# Collection 23 : war_room_members

## Purpose

Stores every member inside a War Room.

---

## Fields

Room ID

User ID

Role

Joined At

Invitation Status

Payment Status

Ready Status

Last Seen

---

## Roles

HOST

MEMBER

CO_HOST

---

## Invitation Status

INVITED

JOINED

DECLINED

LEFT

REMOVED

---

## Ready Status

READY

NOT_READY

---

# Collection 24 : war_room_polls

## Purpose

Stores every poll created inside a War Room.

---

## Examples

Travel Dates

Hotel Choice

Vehicle Choice

Budget Approval

Meal Plan

Activities

Room Sharing

Pickup Point

---

## Fields

Poll Title

Description

Poll Type

Multiple Choice

Anonymous

Created By

Expires At

Winning Option

Created At

---

## Poll Types

SINGLE_SELECT

MULTI_SELECT

YES_NO

RANKING

---

# Collection 25 : war_room_votes

## Purpose

Stores individual votes.

Never embed votes inside polls.

---

## Fields

Poll ID

User ID

Selected Option

Created At

---

## Business Rules

One vote per member.

Vote updates overwrite previous vote.

Poll automatically recalculates winner.

---

# Collection 26 : split_payments

## Purpose

Tracks every member's contribution.

Supports partial payments.

---

## Fields

Booking ID

War Room ID

User ID

Amount

Amount Paid

Balance

Payment Status

Payment Link

Due Date

Reminder Count

Paid At

---

## Status

PENDING

PARTIALLY_PAID

PAID

FAILED

REFUNDED

---

## Business Rules

Booking confirms only after everyone pays.

Timeout cancels booking.

Automatic refund flow.

---

# Collection 27 : war_room_messages

## Purpose

Stores lightweight chat messages.

---

## Fields

Room ID

Sender

Message

Attachments

Read By

Created At

---

## Future

GIF Support

Images

Location Sharing

Voice Notes

AI Suggestions

---

# Collection 28 : war_room_activity_logs

## Purpose

Maintains an audit trail of every important action.

---

## Examples

Rahul joined.

Priya voted.

Vehicle changed.

Hotel upgraded.

Booking locked.

Payment completed.

Host changed.

Trip cancelled.

---

## Fields

Room ID

User ID

Activity Type

Description

Metadata

Timestamp

---

## Benefits

Complete audit trail.

Analytics.

Timeline.

Debugging.

Fraud detection.


---

# Collection 29 : travel_knowledge

## Purpose

Acts as the AI Knowledge Base powering the RAG (Retrieval-Augmented Generation) engine.

Instead of allowing Gemini to hallucinate answers, every response is grounded using verified travel knowledge stored inside MongoDB Atlas Vector Search.

---

## Owner Module

AI Travel Architect

---

## Relationships

Travel Knowledge

↓

Destination

↓

AI Planner

↓

Vector Search

↓

Embeddings

↓

Traveler Queries

---

## Fields

| Field | Type | Description |
|--------|------|-------------|
| id | ObjectId | Primary Identifier |
| destinationId | ObjectId | Linked Destination |
| title | String | Knowledge Title |
| source | String | Source URL |
| sourceType | Enum | GOV / BLOG / INTERNAL / MANUAL |
| content | String | Original Text |
| embedding | Vector | AI Embedding |
| tags | Array | Metadata |
| language | String | Language |
| verified | Boolean | Admin Verified |
| createdAt | Date | Creation |
| updatedAt | Date | Update |

---

## Business Rules

Only verified documents participate in RAG.

Embeddings regenerate whenever content changes.

Deleted documents remain archived.

---

## Indexes

destinationId

verified

tags

Vector Index

embedding

---

# Collection 30 : ai_conversations

## Purpose

Stores AI chat history.

Allows users to revisit previous conversations.

---

## Fields

Conversation ID

User ID

Prompt

AI Response

Prompt Tokens

Completion Tokens

Model

Latency

Created At

---

# Collection 31 : ai_itineraries

## Purpose

Stores every itinerary generated by AI.

Users may later convert it into

Saved Quote

↓

Booking

---

## Fields

Itinerary ID

Conversation ID

Destination

Travel Dates

Travelers

Budget

Activities

Generated JSON

Confidence Score

Converted To Quote

Created At

---

# Collection 32 : ai_feedback

## Purpose

Improves AI quality over time.

---

## Fields

Conversation ID

User ID

Thumbs Up

Thumbs Down

Comment

Created At


---

# Collection 33 : notification_templates

## Purpose

Stores reusable Email, Push, SMS and WhatsApp templates.

---

## Channels

EMAIL

PUSH

SMS

WHATSAPP

IN_APP

---

## Examples

Booking Confirmation

Payment Reminder

Trip Starts Tomorrow

Teaser Live

War Room Invite

Refund Processed

Review Reminder

---

# Collection 34 : notification_preferences

Stores user notification settings.

Examples

Receive Marketing Emails

Receive WhatsApp

Trip Reminders

Price Drop Alerts

AI Suggestions

---

# Collection 35 : notifications

Every notification sent to users.

Fields

User

Title

Body

Channel

Priority

Read

Clicked

Sent At

---

# Collection 36 : notification_logs

Stores provider responses.

Useful for debugging.

Email Delivered

WhatsApp Failed

SMS Pending

---

# Collection 37 : event_queue

This powers the asynchronous notification engine.

Instead of

Booking

↓

Send Email

↓

Wait

↓

Return

we do

Booking

↓

Publish Event

↓

Return Response

↓

Background Worker

↓

Email

↓

WhatsApp

↓

Push

Much faster.

