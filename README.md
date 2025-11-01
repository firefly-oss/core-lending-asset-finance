# Core Lending Asset Finance

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)

A unified microservice for managing both **renting** and **leasing** asset finance agreements within the Firefly lending platform.

---

## Table of Contents

- [Overview](#overview)
- [Purpose](#purpose)
- [Architecture](#architecture)
  - [Module Structure](#module-structure)
  - [Technology Stack](#technology-stack)
  - [Key Design Principles](#key-design-principles)
- [Domain Model](#domain-model)
  - [Entity Relationship Diagram](#entity-relationship-diagram)
  - [Entities](#entities)
  - [Enumerations](#enumerations)
- [API Endpoints](#api-endpoints)
  - [Asset Finance Agreements](#asset-finance-agreements)
  - [Assets](#assets)
  - [End Options](#end-options)
  - [Service Events](#service-events)
  - [Usage Records](#usage-records)
  - [Return Records](#return-records)
  - [Delivery Records](#delivery-records)
  - [Pickup Records](#pickup-records)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Environment Variables](#environment-variables)
  - [Build and Run](#build-and-run)
- [Development](#development)
  - [Project Structure](#project-structure)
  - [Database Migrations](#database-migrations)
  - [Testing](#testing)
- [Integration](#integration)
- [License](#license)

---

## Overview

This microservice consolidates the previously separate `core-lending-renting` and `core-lending-leasing` services into a single, unified codebase. The two modules shared approximately 90% of their code, making this merge a significant improvement in maintainability and consistency.

**Key Benefits:**
- ✅ 90% code reduction through elimination of duplication
- ✅ Unified API for both renting and leasing
- ✅ Single deployment reduces operational complexity
- ✅ Easier maintenance with one codebase
- ✅ Consistent business logic across finance types

---

## Purpose

The **Core Lending Asset Finance** microservice is responsible for:

1. **Managing Asset Finance Agreements** - Creating and maintaining both renting and leasing agreements with customers
2. **Asset Tracking** - Managing individual assets associated with finance agreements, including their descriptions, serial numbers, and values
3. **Delivery & Shipment Tracking** - Managing asset delivery logistics, tracking shipments, carrier information, and delivery confirmations
4. **Pickup & Collection Tracking** - Managing asset pickup/collection logistics when retrieving assets from customers at agreement end
5. **Lease-End Options** - Handling purchase options available at the end of leasing agreements
6. **Service Event Management** - Tracking maintenance, damage, inspections, and modifications to financed assets
7. **Usage Monitoring** - Recording asset usage metrics such as mileage and operating hours
8. **Return Processing** - Managing the return of assets at the end of agreements, including condition assessments and damage costs
9. **Integration with Loan Servicing** - Linking finance agreements to loan servicing cases for payment processing and account management

This microservice does **NOT** handle:
- Payment processing (handled by `core-lending-loan-servicing`)
- Repayment schedules (handled by `core-lending-loan-servicing`)
- Installment calculations (handled by `core-lending-loan-servicing`)

### Asset Lifecycle

The complete lifecycle of an asset in a finance agreement follows this flow:

```
1. Agreement Created
   ↓
2. Asset Assigned to Agreement
   ↓
3. 📦 DELIVERY (DeliveryRecord)
   │  Status: PENDING → SCHEDULED → IN_TRANSIT → DELIVERED
   │  Tracks: Carrier, tracking number, delivery address, recipient, signature, photos
   ↓
4. Asset In Use
   │  ├─ Service Events (maintenance, damage, inspections)
   │  └─ Usage Records (mileage, operating hours)
   ↓
5. 🚚 PICKUP (PickupRecord)
   │  Status: PENDING → SCHEDULED → IN_TRANSIT → PICKED_UP
   │  Tracks: Carrier, tracking number, pickup address, collector, signature, photos
   ↓
6. 📋 RETURN (ReturnRecord)
   │  Condition assessment, damage costs, finalization
   ↓
7. Agreement Closed
```

**Key Points:**
- **DeliveryRecord**: Tracks the logistics of delivering the asset TO the customer at the start of the agreement
- **PickupRecord**: Tracks the logistics of collecting the asset FROM the customer at the end of the agreement
- **ReturnRecord**: Tracks the condition and assessment of the asset upon return (separate from pickup logistics)
- Both delivery and pickup support multiple attempts, failure tracking, and proof of delivery/collection

---

## Architecture

### Module Structure

```
core-lending-asset-finance/
├── core-lending-asset-finance-interfaces/  # DTOs, Enums, API Contracts
├── core-lending-asset-finance-models/      # JPA Entities, Repositories, Database Schema
├── core-lending-asset-finance-core/        # Business Logic, Services, Mappers
├── core-lending-asset-finance-web/         # REST Controllers, Spring Boot Application
└── core-lending-asset-finance-sdk/         # Client SDK, OpenAPI Specification
```

**Module Responsibilities:**

- **interfaces**: Data Transfer Objects (DTOs) and enumerations used across all layers
- **models**: Database entities, R2DBC repositories, and Flyway migration scripts
- **core**: Service interfaces and implementations, MapStruct mappers, business logic
- **web**: REST API controllers, Spring Boot configuration, application entry point
- **sdk**: Client SDK generation and OpenAPI specification

### Technology Stack

- **Language**: Java 21
- **Framework**: Spring Boot 3.x with Spring WebFlux (Reactive)
- **Database**: PostgreSQL with R2DBC (Reactive Database Connectivity)
- **Migration**: Flyway
- **API Documentation**: SpringDoc OpenAPI 3.0
- **Mapping**: MapStruct
- **Build Tool**: Maven
- **Architecture**: Reactive, Non-blocking I/O

### Key Design Principles

1. **Finance Type Discriminator**: The unified model uses a `financeType` field (`RENTING` or `LEASING`) to distinguish between use cases, allowing shared infrastructure while maintaining business logic flexibility.

2. **Reactive Programming**: Built on Spring WebFlux and R2DBC for non-blocking, scalable operations.

3. **Domain-Driven Design**: Clear separation between domain entities, DTOs, and service layers.

4. **External Integration**: Links to `core-lending-loan-servicing` via `loanServicingCaseId` for payment and account management.

---

## Domain Model

### Entity Relationship Diagram

```mermaid
erDiagram
    ASSET_FINANCE_AGREEMENT ||--o{ ASSET_FINANCE_ASSET : contains
    ASSET_FINANCE_AGREEMENT ||--o{ END_OPTION : "has purchase option"
    ASSET_FINANCE_ASSET ||--o{ SERVICE_EVENT : "has events"
    ASSET_FINANCE_ASSET ||--o{ USAGE_RECORD : "has usage"
    ASSET_FINANCE_ASSET ||--o{ RETURN_RECORD : "has returns"
    ASSET_FINANCE_ASSET ||--o{ DELIVERY_RECORD : "has deliveries"
    ASSET_FINANCE_ASSET ||--o{ PICKUP_RECORD : "has pickups"

    ASSET_FINANCE_AGREEMENT {
        uuid asset_finance_agreement_id PK
        uuid contract_id FK "NOT NULL"
        uuid customer_id FK "NOT NULL"
        uuid loan_servicing_case_id FK "NULLABLE - Links to loan-servicing"
        finance_type_enum finance_type "NOT NULL - RENTING or LEASING"
        agreement_status_enum agreement_status "NOT NULL"
        date start_date "NOT NULL"
        date end_date "NOT NULL"
        decimal total_value "NOT NULL"
        varchar payment_frequency "NOT NULL - MONTHLY, QUARTERLY, etc"
        boolean services_included "NULLABLE - Renting specific"
        decimal deposit_amount "NULLABLE - Renting specific"
        decimal early_termination_fee "NULLABLE - Renting specific"
        decimal residual_value "NULLABLE - Leasing specific"
        boolean purchase_option_available "NULLABLE - Leasing specific"
        decimal purchase_option_price "NULLABLE - Leasing specific"
        text remarks "NULLABLE"
        timestamp created_at "NOT NULL"
        timestamp updated_at "NOT NULL"
    }

    ASSET_FINANCE_ASSET {
        uuid asset_finance_asset_id PK
        uuid asset_finance_agreement_id FK "NOT NULL"
        uuid asset_type_id FK "NOT NULL"
        varchar asset_description "NULLABLE - Max 500 chars"
        varchar asset_serial_number "NULLABLE - Max 100 chars"
        decimal asset_value "NOT NULL"
        boolean is_active "DEFAULT TRUE"
        text note "NULLABLE"
        timestamp created_at "NOT NULL"
        timestamp updated_at "NOT NULL"
    }

    END_OPTION {
        uuid end_option_id PK
        uuid asset_finance_agreement_id FK "NOT NULL"
        date option_exercise_date "NULLABLE"
        decimal option_paid_amount "NULLABLE"
        boolean is_exercised "DEFAULT FALSE"
        text note "NULLABLE"
        timestamp created_at "NOT NULL"
        timestamp updated_at "NOT NULL"
    }

    SERVICE_EVENT {
        uuid service_event_id PK
        uuid asset_finance_asset_id FK "NOT NULL"
        date event_date "NOT NULL"
        event_type_enum event_type "NOT NULL"
        decimal cost "NULLABLE"
        text note "NULLABLE"
        timestamp created_at "NOT NULL"
        timestamp updated_at "NOT NULL"
    }

    USAGE_RECORD {
        uuid usage_record_id PK
        uuid asset_finance_asset_id FK "NOT NULL"
        date usage_date "NOT NULL"
        integer mileage "NULLABLE"
        text usage_detail "NULLABLE"
        timestamp created_at "NOT NULL"
        timestamp updated_at "NOT NULL"
    }

    RETURN_RECORD {
        uuid return_record_id PK
        uuid asset_finance_asset_id FK "NOT NULL"
        date actual_return_date "NOT NULL"
        text condition_report "NULLABLE"
        decimal damage_cost "NULLABLE"
        boolean is_finalized "DEFAULT FALSE"
        text note "NULLABLE"
        timestamp created_at "NOT NULL"
        timestamp updated_at "NOT NULL"
    }

    DELIVERY_RECORD {
        uuid delivery_record_id PK
        uuid asset_finance_asset_id FK "NOT NULL"
        delivery_status_enum delivery_status "NOT NULL - DEFAULT PENDING"
        date scheduled_delivery_date "NULLABLE"
        date actual_delivery_date "NULLABLE"
        text delivery_address "NOT NULL"
        varchar delivery_city "NULLABLE - Max 100 chars"
        varchar delivery_state "NULLABLE - Max 100 chars"
        varchar delivery_postal_code "NULLABLE - Max 20 chars"
        varchar delivery_country "NULLABLE - Max 100 chars"
        varchar carrier_name "NULLABLE - Max 200 chars"
        varchar tracking_number "NULLABLE - Max 200 chars"
        varchar recipient_name "NULLABLE - Max 200 chars"
        varchar recipient_phone "NULLABLE - Max 50 chars"
        varchar recipient_email "NULLABLE - Max 200 chars"
        boolean signature_received "DEFAULT FALSE"
        text delivery_condition_notes "NULLABLE"
        text_array delivery_photo_urls "NULLABLE - Array of URLs"
        text failed_delivery_reason "NULLABLE"
        integer delivery_attempts "DEFAULT 0"
        text note "NULLABLE"
        timestamp created_at "NOT NULL"
        timestamp updated_at "NOT NULL"
    }

    PICKUP_RECORD {
        uuid pickup_record_id PK
        uuid asset_finance_asset_id FK "NOT NULL"
        pickup_status_enum pickup_status "NOT NULL - DEFAULT PENDING"
        date scheduled_pickup_date "NULLABLE"
        date actual_pickup_date "NULLABLE"
        text pickup_address "NOT NULL"
        varchar pickup_city "NULLABLE - Max 100 chars"
        varchar pickup_state "NULLABLE - Max 100 chars"
        varchar pickup_postal_code "NULLABLE - Max 20 chars"
        varchar pickup_country "NULLABLE - Max 100 chars"
        varchar carrier_name "NULLABLE - Max 200 chars"
        varchar tracking_number "NULLABLE - Max 200 chars"
        varchar collector_name "NULLABLE - Max 200 chars"
        varchar collector_phone "NULLABLE - Max 50 chars"
        varchar collector_email "NULLABLE - Max 200 chars"
        boolean signature_received "DEFAULT FALSE"
        text pickup_condition_notes "NULLABLE"
        text_array pickup_photo_urls "NULLABLE - Array of URLs"
        text failed_pickup_reason "NULLABLE"
        integer pickup_attempts "DEFAULT 0"
        text note "NULLABLE"
        timestamp created_at "NOT NULL"
        timestamp updated_at "NOT NULL"
    }
```

### Entities

#### AssetFinanceAgreement

The core entity representing a renting or leasing agreement.

**Key Fields:**
- `assetFinanceAgreementId` (UUID, PK): Unique identifier
- `contractId` (UUID, FK): Reference to contract in contract management system
- `customerId` (UUID, FK): Reference to customer
- `loanServicingCaseId` (UUID, FK): Links to loan servicing case for payment processing
- `financeType` (FinanceTypeEnum): RENTING or LEASING
- `agreementStatus` (AgreementStatusEnum): Current status of the agreement
- `startDate` (LocalDate): Agreement start date
- `endDate` (LocalDate): Agreement end date
- `totalValue` (BigDecimal): Total value of the agreement
- `paymentFrequency` (String): Payment frequency (MONTHLY, QUARTERLY, YEARLY)

**Renting-Specific Fields:**
- `servicesIncluded` (Boolean): Whether services are included
- `depositAmount` (BigDecimal): Security deposit amount
- `earlyTerminationFee` (BigDecimal): Fee for early termination

**Leasing-Specific Fields:**
- `residualValue` (BigDecimal): Residual value at end of lease
- `purchaseOptionAvailable` (Boolean): Whether purchase option is available
- `purchaseOptionPrice` (BigDecimal): Price for purchase option

**DTO:** `AssetFinanceAgreementDTO` in `com.firefly.core.lending.assetfinance.interfaces.dtos.agreement.v1`

---

#### AssetFinanceAsset

Represents individual assets associated with a finance agreement.

**Key Fields:**
- `assetFinanceAssetId` (UUID, PK): Unique identifier
- `assetFinanceAgreementId` (UUID, FK): Parent agreement
- `assetTypeId` (UUID, FK): Reference to asset type
- `assetDescription` (String): Description of the asset (max 500 chars)
- `assetSerialNumber` (String): Serial number (max 100 chars)
- `assetValue` (BigDecimal): Value of the asset
- `isActive` (Boolean): Whether the asset is currently active

**DTO:** `AssetFinanceAssetDTO` in `com.firefly.core.lending.assetfinance.interfaces.dtos.assets.v1`

---

#### EndOption

Represents lease-end purchase options for leasing agreements.

**Key Fields:**
- `endOptionId` (UUID, PK): Unique identifier
- `assetFinanceAgreementId` (UUID, FK): Parent agreement
- `optionExerciseDate` (LocalDate): Date when option was/will be exercised
- `optionPaidAmount` (BigDecimal): Amount paid for the option
- `isExercised` (Boolean): Whether the option has been exercised

**DTO:** `EndOptionDTO` in `com.firefly.core.lending.assetfinance.interfaces.dtos.option.v1`

---

#### ServiceEvent

Tracks maintenance, damage, inspections, and modifications to assets.

**Key Fields:**
- `serviceEventId` (UUID, PK): Unique identifier
- `assetFinanceAssetId` (UUID, FK): Parent asset
- `eventDate` (LocalDate): Date of the event
- `eventType` (EventTypeEnum): Type of event (MAINTENANCE, DAMAGE, RETURN, INSPECTION, MODIFICATION)
- `cost` (BigDecimal): Cost associated with the event

**DTO:** `ServiceEventDTO` in `com.firefly.core.lending.assetfinance.interfaces.dtos.event.v1`

---

#### UsageRecord

Records usage metrics for assets (e.g., mileage, operating hours).

**Key Fields:**
- `usageRecordId` (UUID, PK): Unique identifier
- `assetFinanceAssetId` (UUID, FK): Parent asset
- `usageDate` (LocalDate): Date of usage recording
- `mileage` (Integer): Mileage or usage metric
- `usageDetail` (String): Additional usage details

**DTO:** `UsageRecordDTO` in `com.firefly.core.lending.assetfinance.interfaces.dtos.usage.v1`

---

#### ReturnRecord

Manages asset returns at the end of agreements.

**Key Fields:**
- `returnRecordId` (UUID, PK): Unique identifier
- `assetFinanceAssetId` (UUID, FK): Parent asset
- `actualReturnDate` (LocalDate): Date asset was returned
- `conditionReport` (String): Condition assessment report (max 2000 chars)
- `damageCost` (BigDecimal): Cost of damages
- `isFinalized` (Boolean): Whether the return is finalized

**DTO:** `ReturnRecordDTO` in `com.firefly.core.lending.assetfinance.interfaces.dtos.usage.v1`

---

#### DeliveryRecord

Manages asset delivery and shipment tracking.

**Key Fields:**
- `deliveryRecordId` (UUID, PK): Unique identifier
- `assetFinanceAssetId` (UUID, FK): Parent asset
- `deliveryStatus` (DeliveryStatusEnum): Current delivery status
- `scheduledDeliveryDate` (LocalDate): Scheduled delivery date
- `actualDeliveryDate` (LocalDate): Actual delivery date
- `deliveryAddress` (String): Full delivery address (required)
- `deliveryCity` (String): Delivery city (max 100 chars)
- `deliveryState` (String): Delivery state/province (max 100 chars)
- `deliveryPostalCode` (String): Postal/ZIP code (max 20 chars)
- `deliveryCountry` (String): Delivery country (max 100 chars)
- `carrierName` (String): Shipping carrier name (max 200 chars)
- `trackingNumber` (String): Shipment tracking number (max 200 chars)
- `recipientName` (String): Name of recipient (max 200 chars)
- `recipientPhone` (String): Recipient phone number (max 50 chars)
- `recipientEmail` (String): Recipient email address (max 200 chars)
- `signatureReceived` (Boolean): Whether delivery signature was received
- `deliveryConditionNotes` (String): Notes on asset condition at delivery (max 2000 chars)
- `deliveryPhotoUrls` (List<String>): URLs to delivery proof photos
- `failedDeliveryReason` (String): Reason for failed delivery (max 1000 chars)
- `deliveryAttempts` (Integer): Number of delivery attempts made

**DTO:** `DeliveryRecordDTO` in `com.firefly.core.lending.assetfinance.interfaces.dtos.delivery.v1`

---

#### PickupRecord

Manages asset pickup and collection tracking when retrieving assets from customers.

**Key Fields:**
- `pickupRecordId` (UUID, PK): Unique identifier
- `assetFinanceAssetId` (UUID, FK): Parent asset
- `pickupStatus` (PickupStatusEnum): Current pickup status
- `scheduledPickupDate` (LocalDate): Scheduled pickup date
- `actualPickupDate` (LocalDate): Actual pickup date
- `pickupAddress` (String): Full pickup address (required)
- `pickupCity` (String): Pickup city (max 100 chars)
- `pickupState` (String): Pickup state/province (max 100 chars)
- `pickupPostalCode` (String): Postal/ZIP code (max 20 chars)
- `pickupCountry` (String): Pickup country (max 100 chars)
- `carrierName` (String): Shipping carrier name (max 200 chars)
- `trackingNumber` (String): Shipment tracking number (max 200 chars)
- `collectorName` (String): Name of person collecting the asset (max 200 chars)
- `collectorPhone` (String): Collector phone number (max 50 chars)
- `collectorEmail` (String): Collector email address (max 200 chars)
- `signatureReceived` (Boolean): Whether pickup signature was received
- `pickupConditionNotes` (String): Notes on asset condition at pickup (max 2000 chars)
- `pickupPhotoUrls` (List<String>): URLs to pickup proof photos
- `failedPickupReason` (String): Reason for failed pickup (max 1000 chars)
- `pickupAttempts` (Integer): Number of pickup attempts made

**DTO:** `PickupRecordDTO` in `com.firefly.core.lending.assetfinance.interfaces.dtos.pickup.v1`

---

### Enumerations

#### FinanceTypeEnum

Distinguishes between renting and leasing agreements.

**Values:**
- `RENTING`: Rental agreement
- `LEASING`: Lease agreement

**Package:** `com.firefly.core.lending.assetfinance.interfaces.enums.finance.v1`

---

#### AgreementStatusEnum

Represents the current status of a finance agreement.

**Values:**
- `ACTIVE`: Agreement is currently active
- `SUSPENDED`: Agreement is temporarily suspended
- `CLOSED`: Agreement has been closed normally
- `TERMINATED`: Agreement was terminated early
- `DEFAULTED`: Agreement is in default

**Package:** `com.firefly.core.lending.assetfinance.interfaces.enums.agreement.v1`

---

#### EventTypeEnum

Categorizes service events for assets.

**Values:**
- `MAINTENANCE`: Regular maintenance event
- `DAMAGE`: Damage to the asset
- `RETURN`: Asset return event
- `INSPECTION`: Inspection event
- `MODIFICATION`: Modification to the asset

**Package:** `com.firefly.core.lending.assetfinance.interfaces.enums.event.v1`

---

#### DeliveryStatusEnum

Represents the current status of an asset delivery.

**Values:**
- `PENDING`: Delivery is pending/not yet scheduled
- `SCHEDULED`: Delivery has been scheduled
- `IN_TRANSIT`: Asset is currently in transit
- `DELIVERED`: Asset has been successfully delivered
- `FAILED`: Delivery attempt failed
- `CANCELLED`: Delivery was cancelled

**Package:** `com.firefly.core.lending.assetfinance.interfaces.enums.delivery.v1`

---

#### PickupStatusEnum

Represents the current status of an asset pickup/collection.

**Values:**
- `PENDING`: Pickup is pending/not yet scheduled
- `SCHEDULED`: Pickup has been scheduled
- `IN_TRANSIT`: Asset is currently in transit (after pickup)
- `PICKED_UP`: Asset has been successfully picked up
- `FAILED`: Pickup attempt failed
- `CANCELLED`: Pickup was cancelled

**Package:** `com.firefly.core.lending.assetfinance.interfaces.enums.pickup.v1`

---

## API Endpoints

All endpoints are prefixed with `/api/v1` and support reactive (non-blocking) operations.

### Asset Finance Agreements

**Base Path:** `/api/v1/asset-finance-agreements`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/` | List/Search all agreements with filtering and pagination |
| POST | `/` | Create a new asset finance agreement |
| GET | `/{assetFinanceAgreementId}` | Get agreement by ID |
| PUT | `/{assetFinanceAgreementId}` | Update an existing agreement |
| DELETE | `/{assetFinanceAgreementId}` | Delete an agreement |

**Controller:** `AssetFinanceAgreementController`
**Service:** `AssetFinanceAgreementService`

---

### Assets

**Base Path:** `/api/v1/asset-finance-agreements/{agreementId}/assets`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/` | List/Search assets for an agreement |
| POST | `/` | Create a new asset for an agreement |
| GET | `/{assetId}` | Get asset by ID |
| PUT | `/{assetId}` | Update an existing asset |
| DELETE | `/{assetId}` | Delete an asset |

**Controller:** `AssetFinanceAssetController`
**Service:** `AssetFinanceAssetService`

---

### End Options

**Base Path:** `/api/v1/asset-finance-agreements/{agreementId}/end-options`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/` | List/Search end options for an agreement |
| POST | `/` | Create a new end option |
| GET | `/{optionId}` | Get end option by ID |
| PUT | `/{optionId}` | Update an existing end option |
| DELETE | `/{optionId}` | Delete an end option |

**Controller:** `EndOptionController`
**Service:** `EndOptionService`

---

### Service Events

**Base Path:** `/api/v1/asset-finance-agreements/{agreementId}/assets/{assetId}/service-events`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/` | List/Search service events for an asset |
| POST | `/` | Create a new service event |
| GET | `/{eventId}` | Get service event by ID |
| PUT | `/{eventId}` | Update an existing service event |
| DELETE | `/{eventId}` | Delete a service event |

**Controller:** `ServiceEventController`
**Service:** `ServiceEventService`

---

### Usage Records

**Base Path:** `/api/v1/asset-finance-agreements/{agreementId}/assets/{assetId}/usage-records`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/` | List/Search usage records for an asset |
| POST | `/` | Create a new usage record |
| GET | `/{usageRecordId}` | Get usage record by ID |
| PUT | `/{usageRecordId}` | Update an existing usage record |
| DELETE | `/{usageRecordId}` | Delete a usage record |

**Controller:** `UsageRecordController`
**Service:** `UsageRecordService`

---

### Return Records

**Base Path:** `/api/v1/asset-finance-agreements/{agreementId}/assets/{assetId}/return-records`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/` | List/Search return records for an asset |
| POST | `/` | Create a new return record |
| GET | `/{returnRecordId}` | Get return record by ID |
| PUT | `/{returnRecordId}` | Update an existing return record |
| DELETE | `/{returnRecordId}` | Delete a return record |

**Controller:** `ReturnRecordController`
**Service:** `ReturnRecordService`

---

### Delivery Records

**Base Path:** `/api/v1/asset-finance-agreements/{agreementId}/assets/{assetId}/delivery-records`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/` | List/Search delivery records for an asset |
| POST | `/` | Create a new delivery record |
| GET | `/{deliveryRecordId}` | Get delivery record by ID |
| PUT | `/{deliveryRecordId}` | Update an existing delivery record |
| DELETE | `/{deliveryRecordId}` | Delete a delivery record |

**Controller:** `DeliveryRecordController`
**Service:** `DeliveryRecordService`

---

### Pickup Records

**Base Path:** `/api/v1/asset-finance-agreements/{agreementId}/assets/{assetId}/pickup-records`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/` | List/Search pickup records for an asset |
| POST | `/` | Create a new pickup record |
| GET | `/{pickupRecordId}` | Get pickup record by ID |
| PUT | `/{pickupRecordId}` | Update an existing pickup record |
| DELETE | `/{pickupRecordId}` | Delete a pickup record |

**Controller:** `PickupRecordController`
**Service:** `PickupRecordService`

---

## Getting Started

### Prerequisites

- **Java 21** or higher
- **Maven 3.8+**
- **PostgreSQL 14+**
- **Docker** (optional, for local PostgreSQL)

### Environment Variables

The following environment variables must be set:

```bash
# Database Configuration
DB_HOST=localhost
DB_PORT=5432
DB_NAME=asset_finance_db
DB_USERNAME=postgres
DB_PASSWORD=postgres
DB_SSL_MODE=disable

# Server Configuration
SERVER_ADDRESS=localhost
SERVER_PORT=8080
```

### Build and Run

```bash
# Clone the repository
git clone https://github.com/firefly-oss/core-lending-asset-finance.git
cd core-lending-asset-finance

# Build the project
mvn clean install

# Run the application
cd core-lending-asset-finance-web
mvn spring-boot:run

# Or run with a specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

**Access Points:**
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI Spec**: http://localhost:8080/v3/api-docs
- **Health Check**: http://localhost:8080/actuator/health

---

## Development

### Project Structure

```
core-lending-asset-finance/
│
├── core-lending-asset-finance-interfaces/
│   └── src/main/java/.../interfaces/
│       ├── dtos/           # Data Transfer Objects
│       │   ├── agreement/v1/
│       │   ├── assets/v1/
│       │   ├── delivery/v1/
│       │   ├── event/v1/
│       │   ├── option/v1/
│       │   ├── pickup/v1/
│       │   ├── returnrecord/v1/
│       │   └── usage/v1/
│       └── enums/          # Enumerations
│           ├── agreement/v1/
│           ├── delivery/v1/
│           ├── event/v1/
│           ├── finance/v1/
│           └── pickup/v1/
│
├── core-lending-asset-finance-models/
│   └── src/main/
│       ├── java/.../models/
│       │   ├── entities/   # JPA Entities
│       │   └── repositories/ # R2DBC Repositories
│       └── resources/
│           └── db/migration/ # Flyway Scripts
│
├── core-lending-asset-finance-core/
│   └── src/main/java/.../core/
│       ├── services/       # Service Interfaces & Implementations
│       └── mappers/        # MapStruct Mappers
│
├── core-lending-asset-finance-web/
│   └── src/main/
│       ├── java/.../web/
│       │   ├── controllers/ # REST Controllers
│       │   └── AssetFinanceApplication.java
│       └── resources/
│           └── application.yml
│
└── core-lending-asset-finance-sdk/
    └── src/main/resources/
        └── api-spec/
            └── openapi.yml
```

### Database Migrations

Database schema is managed using Flyway. Migration scripts are located in:

```
core-lending-asset-finance-models/src/main/resources/db/migration/
```

**Migration Files:**
- `V1__Create_Enums.sql` - Creates custom enum types (finance_type, agreement_status, event_type)
- `V2__Create_Tables.sql` - Creates core tables (asset_finance_agreement, asset_finance_asset, end_option, service_event, usage_record, return_record)
- `V3__Create_Delivery_Record_Table.sql` - Creates delivery_record table and delivery_status enum for shipment tracking
- `V4__Create_Pickup_Record_Table.sql` - Creates pickup_record table and pickup_status enum for collection tracking

Migrations run automatically on application startup.

### Testing

```bash
# Run all tests
mvn test

# Run tests with coverage
mvn clean verify

# Run integration tests
mvn verify -P integration-tests
```

---

## Integration

This microservice integrates with:

1. **core-lending-loan-servicing**: Links via `loanServicingCaseId` for payment processing and account management
2. **Contract Management System**: References contracts via `contractId`
3. **Customer Management System**: References customers via `customerId`
4. **Asset Type Catalog**: References asset types via `assetTypeId`

---

## License

Copyright 2025 Firefly Software Solutions Inc

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
