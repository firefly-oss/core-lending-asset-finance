-- Create custom enum types for Asset Finance domain

-- Finance Type Enum
CREATE TYPE finance_type_enum AS ENUM (
    'RENTING',
    'LEASING'
);

-- Agreement Status Enum
CREATE TYPE agreement_status_enum AS ENUM (
    'ACTIVE',
    'SUSPENDED',
    'CLOSED',
    'TERMINATED',
    'DEFAULTED'
);

-- Event Type Enum
CREATE TYPE event_type_enum AS ENUM (
    'MAINTENANCE',
    'DAMAGE',
    'RETURN',
    'INSPECTION',
    'MODIFICATION'
);

