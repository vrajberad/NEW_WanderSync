# WanderSync War Room Architecture

---

# Purpose

The War Room is WanderSync's flagship collaboration module.

It enables groups to plan, discuss, vote, customize and book trips together in real time.

Unlike traditional travel platforms where one person makes all decisions, the War Room creates a collaborative planning experience.

It combines:

• Real-time Collaboration

• Live Voting

• Split Payments

• Shared Itinerary Editing

• Presence Tracking

• Group Chat (Future)

• Decision History

This module is the primary differentiator of WanderSync.

---

# Vision

Planning a trip should feel like collaborating inside Figma or Google Docs.

Everyone should see updates instantly.

No refreshing.

No WhatsApp confusion.

No manual calculations.

---

# Core Features

## Live Collaboration

Multiple users can participate simultaneously.

Supported actions

- Join Room
- Leave Room
- Update Preferences
- Vote
- Select Activities
- Choose Hotels
- Select Transport
- Confirm Booking

All updates are synchronized through WebSockets.

---

# War Room Lifecycle

Create Room

↓

Generate Invite Link

↓

Friends Join

↓

Discuss Trip

↓

Vote

↓

Customize Package

↓

Price Updates

↓

Lock Booking

↓

Split Payments

↓

Booking Confirmed

↓

Trip Dashboard

---

# User Roles

Host

Creates room

Invites members

Locks booking

Cancels room

Can remove members

---

Member

Join room

Vote

Customize

Pay share

Leave room

---

Admin

Monitor rooms

Resolve issues

View analytics

---

# Room States

CREATED

OPEN

VOTING

CUSTOMIZING

PAYMENT_PENDING

CONFIRMED

EXPIRED

CANCELLED

---

# Room Data

Room ID

Host ID

Destination

Travel Dates

Members

Votes

Selected Package

Current Price

Payment Status

Countdown Timer

Created At

Updated At

---

# Invite System

Host creates room.

↓

Signed Invite Token Generated.

↓

Share Link

↓

Friends Join

↓

JWT Validation

↓

Room Membership Created

Invite links expire after configurable duration.

---

# Presence System

Every member has a status.

ONLINE

OFFLINE

VIEWING

EDITING

PAYING

Typing indicators can be added later.

---

# Voting Engine

Supported Votes

Destination

Travel Dates

Hotel

Transport

Activities

Meals

Budget

Rules

One vote per member.

Vote changes allowed until finalized.

Live vote counts.

Host can close voting.

---

# Shared Customization

Members can collaboratively modify

Hotel

Transport

Activities

Meals

Insurance

Equipment

Guide

Every change immediately updates

Live itinerary

Live pricing

Group summary

---

# Live Price Engine

Price recalculates instantly whenever

Traveler count changes

Hotel changes

Transport changes

Activities added

Meals changed

Coupons applied

Wallet used

Everyone sees the same updated amount.

---

# Split Payment

Booking Locked

↓

Total Cost

↓

Split Calculation

↓

Payment Link per Member

↓

Individual Payments

↓

Live Progress

↓

Booking Confirmation

---

# Split Payment Rules

Host cannot pay for others by default.

Each member pays independently.

Booking confirmed only when all required payments are captured.

Timeout configurable.

Automatic cancellation on expiry.

---

# WebSocket Events

Client → Server

JOIN_ROOM

LEAVE_ROOM

CAST_VOTE

UPDATE_SELECTION

START_PAYMENT

PING

---

Server → Client

ROOM_UPDATED

MEMBER_JOINED

MEMBER_LEFT

PRICE_UPDATED

VOTE_UPDATED

PAYMENT_STATUS_UPDATED

ROOM_LOCKED

BOOKING_CONFIRMED

ERROR

---

# Synchronization Strategy

Every client receives

Latest Room State

↓

Diff Update

↓

UI Refresh

Optimistic UI where appropriate.

Conflict resolution handled by backend.

---

# Notifications

Member Joined

Vote Started

Vote Closed

Price Changed

Booking Locked

Payment Reminder

Booking Confirmed

Trip Reminder

---

# Security

JWT Authentication

Room Membership Validation

Signed Invite Tokens

Role Validation

Rate Limiting

Ownership Checks

Audit Logging

Secure WebSocket Handshake

---

# MongoDB Collections

war_room_lobbies

war_room_members

war_room_votes

group_bookings

group_payments

notification_logs

---

# Analytics

Admin Dashboard

Rooms Created

Average Members

Vote Participation

Average Planning Time

Conversion Rate

Abandoned Rooms

Completed Group Bookings

Most Popular Destinations

---

# Future Enhancements

Integrated Group Chat

Voice Chat

Video Planning Room

Shared Whiteboard

AI Trip Assistant inside War Room

Expense Tracker

Shared Checklist

Collaborative Packing List

Trip Poll Templates

Calendar Integration

Google Meet Integration

Discord Integration

Spotify Collaborative Playlist

Photo Voting

Live Map Planning

---

# Engineering Principles

The War Room must always feel:

Real-time

Reliable

Collaborative

Transparent

Fast

Scalable

Every participant should experience the same synchronized state regardless of device or network latency.

The War Room is not just a booking feature.

It is the social engine of WanderSync.