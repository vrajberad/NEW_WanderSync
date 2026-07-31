# WanderSync Payment Architecture

---

# Purpose

The Payment Module is responsible for securely processing all financial transactions within WanderSync.

It supports:

- Individual Bookings
- Group Split Payments
- Custom Trip Quotes
- Vendor Settlements (Future)
- Wallet (WanderCoins)
- Refund Processing

Payments are designed using a webhook-first architecture where the payment gateway is the source of truth.

---

# Design Principles

- Never trust client payment status
- Webhook is the source of truth
- Every payment is traceable
- Every transaction is idempotent
- Booking and Payment remain separate entities
- Money should never be lost due to retries

---

# Payment Providers

Current

- Razorpay (Test Mode)

Future

- Stripe
- PhonePe
- Cashfree
- UPI Direct
- International Cards

Use a PaymentGateway interface so providers can be swapped without changing business logic.

---

# High-Level Architecture

```
Frontend

↓

Create Booking

↓

Booking Service

↓

Payment Service

↓

Payment Gateway (Razorpay)

↓

Customer Pays

↓

Webhook

↓

Webhook Verification

↓

Payment Confirmation

↓

Booking Status Updated

↓

Notification Event

↓

Email + WhatsApp + Dashboard
```

---

# Payment Lifecycle

Step 1

Create Booking

↓

Status

PENDING_PAYMENT

↓

Create Razorpay Order

↓

Return Order ID

↓

Customer Pays

↓

Gateway Webhook

↓

Verify Signature

↓

Payment Captured

↓

Create Payment Record

↓

Booking Confirmed

↓

Generate Ticket

↓

Send Notifications

---

# Payment States

PENDING

INITIATED

AUTHORIZED

CAPTURED

FAILED

REFUNDED

PARTIALLY_REFUNDED

EXPIRED

---

# Booking States

DRAFT

SEATS_LOCKED

PENDING_PAYMENT

CONFIRMED

COMPLETED

CANCELLED

EXPIRED

---

# Individual Booking Flow

User

↓

Lock Seats

↓

Create Booking

↓

Payment Order

↓

Payment Success

↓

Webhook Verification

↓

Booking Confirmed

↓

Ticket Generated

---

# Group Booking Flow

Host Creates War Room

↓

Members Join

↓

Booking Locked

↓

Payment Links Generated

↓

Each Member Pays

↓

Track Individual Payments

↓

All Members Paid

↓

Booking Confirmed

↓

Notifications Sent

If timeout occurs

↓

Booking Expired

↓

Refund Rules Applied

---

# Split Payment Model

Example

Trip Cost

₹60,000

Members

4

Each Member

₹15,000

Payments

Payment A

Payment B

Payment C

Payment D

Booking remains

PENDING_PAYMENT

Until all four payments are captured.

---

# Payment Entity

Fields

Payment ID

Booking ID

User ID

Gateway

Gateway Order ID

Gateway Payment ID

Amount

Currency

Status

Method

Created At

Updated At

Refund Status

Failure Reason

Idempotency Key

---

# Booking Relationship

One Booking

↓

Many Payments

Supports

Split Payments

Partial Refunds

Installments (Future)

---

# Idempotency

Every payment request contains

Idempotency Key

Purpose

Prevent duplicate orders

Prevent duplicate captures

Safe retries

Duplicate webhook protection

---

# Webhook Verification

Webhook receives

Payment Success

↓

Verify Signature

↓

Reject Invalid Signature

↓

Update Payment

↓

Update Booking

↓

Publish BookingConfirmedEvent

Never trust frontend callbacks.

---

# Refund Flow

Customer Requests Refund

↓

Validate Policy

↓

Admin Approval (if required)

↓

Gateway Refund API

↓

Webhook Confirmation

↓

Payment Updated

↓

Booking Updated

↓

Notification Sent

---

# Cancellation Policy

Before Lock Expiry

100%

Before 7 Days

90%

Before 48 Hours

50%

Less than 24 Hours

No Refund

Policies configurable per trip.

---

# WanderCoins Integration

Users earn

Coins after successful trip completion.

Coins can be redeemed during checkout.

Wallet never replaces payment gateway.

---

# Vendor Settlement (Future)

Booking Completed

↓

Settlement Batch

↓

Platform Commission Deducted

↓

Vendor Paid

↓

Settlement Ledger Updated

---

# Payment Security

JWT Authentication

Webhook Signature Verification

HTTPS

Environment Variables

Rate Limiting

Audit Logs

Encrypted Secrets

Idempotency Keys

PCI-DSS Awareness

No Card Storage

---

# Fraud Prevention

Maximum Retry Limits

Duplicate Transaction Detection

Amount Validation

Booking Ownership Validation

Payment Expiry

Replay Protection

Suspicious Activity Logging

---

# Payment Logs

Every payment stores

Request

Response

Gateway Event

Webhook Payload

Verification Result

Retry Count

Timestamp

Purpose

Debugging

Audit

Compliance

---

# Error Handling

Gateway Timeout

↓

Retry

Payment Already Captured

↓

Ignore Duplicate

Webhook Retry

↓

Idempotent Processing

Payment Failed

↓

Booking Remains Pending

↓

Allow Retry

---

# Notification Events

PaymentInitiatedEvent

PaymentCapturedEvent

PaymentFailedEvent

RefundProcessedEvent

SplitPaymentCompletedEvent

BookingConfirmedEvent

---

# Scheduled Jobs

Expire Unpaid Bookings

Retry Failed Webhooks

Refund Reconciliation

Payment Audit

Wallet Settlement

---

# Analytics

Admin Dashboard

Total Revenue

Pending Payments

Refund Amount

Gateway Success Rate

Average Payment Time

Revenue by Destination

Revenue by Vendor

Split Payment Success Rate

---

# Future Enhancements

EMI Support

UPI AutoPay

Subscription Plans

Gift Cards

Corporate Billing

GST Invoice Automation

International Payments

Multi-Currency Support

Vendor Wallet

Escrow Payments

BNPL (Buy Now Pay Later)

---

# Engineering Principles

Payments must always be:

Secure

Reliable

Auditable

Idempotent

Traceable

Recoverable

Scalable

The payment system should continue operating correctly even in the presence of duplicate requests, gateway retries, webhook delays, or temporary failures.