-- Create pickup_status_enum type
CREATE TYPE pickup_status_enum AS ENUM (
    'PENDING',
    'SCHEDULED',
    'IN_TRANSIT',
    'PICKED_UP',
    'FAILED',
    'CANCELLED'
);

-- Create pickup_record table
CREATE TABLE pickup_record (
    pickup_record_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    asset_finance_asset_id UUID NOT NULL,
    pickup_status pickup_status_enum NOT NULL DEFAULT 'PENDING',
    scheduled_pickup_date DATE,
    actual_pickup_date DATE,
    pickup_address TEXT NOT NULL,
    pickup_city VARCHAR(100),
    pickup_state VARCHAR(100),
    pickup_postal_code VARCHAR(20),
    pickup_country VARCHAR(100),
    carrier_name VARCHAR(200),
    tracking_number VARCHAR(200),
    collector_name VARCHAR(200),
    collector_phone VARCHAR(50),
    collector_email VARCHAR(200),
    signature_received BOOLEAN DEFAULT FALSE,
    pickup_condition_notes TEXT,
    pickup_photo_urls TEXT[],
    failed_pickup_reason TEXT,
    pickup_attempts INTEGER DEFAULT 0,
    note TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_pickup_record_asset FOREIGN KEY (asset_finance_asset_id)
        REFERENCES asset_finance_asset(asset_finance_asset_id) ON DELETE CASCADE
);

-- Create indexes for better query performance
CREATE INDEX idx_pickup_record_asset_id ON pickup_record(asset_finance_asset_id);
CREATE INDEX idx_pickup_record_status ON pickup_record(pickup_status);
CREATE INDEX idx_pickup_record_scheduled_date ON pickup_record(scheduled_pickup_date);
CREATE INDEX idx_pickup_record_actual_date ON pickup_record(actual_pickup_date);
CREATE INDEX idx_pickup_record_tracking ON pickup_record(tracking_number);

-- Add comment to table
COMMENT ON TABLE pickup_record IS 'Stores asset pickup/collection records for tracking logistics when retrieving assets from customers';

