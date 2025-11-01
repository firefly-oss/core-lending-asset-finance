-- Create delivery status enum type
CREATE TYPE delivery_status_enum AS ENUM (
    'PENDING',
    'SCHEDULED',
    'IN_TRANSIT',
    'DELIVERED',
    'FAILED',
    'CANCELLED'
);

-- Create Delivery Record table
CREATE TABLE delivery_record (
    delivery_record_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    asset_finance_asset_id UUID NOT NULL,
    delivery_status delivery_status_enum NOT NULL DEFAULT 'PENDING',
    scheduled_delivery_date DATE,
    actual_delivery_date DATE,
    delivery_address TEXT NOT NULL,
    delivery_city VARCHAR(100),
    delivery_state VARCHAR(100),
    delivery_postal_code VARCHAR(20),
    delivery_country VARCHAR(100),
    carrier_name VARCHAR(200),
    tracking_number VARCHAR(200),
    recipient_name VARCHAR(200),
    recipient_phone VARCHAR(50),
    recipient_email VARCHAR(200),
    signature_received BOOLEAN DEFAULT FALSE,
    delivery_condition_notes TEXT,
    delivery_photo_urls TEXT[],
    failed_delivery_reason TEXT,
    delivery_attempts INTEGER DEFAULT 0,
    note TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_delivery_record_asset FOREIGN KEY (asset_finance_asset_id) 
        REFERENCES asset_finance_asset(asset_finance_asset_id) ON DELETE CASCADE
);

-- Create indexes for better query performance
CREATE INDEX idx_delivery_record_asset_id ON delivery_record(asset_finance_asset_id);
CREATE INDEX idx_delivery_record_status ON delivery_record(delivery_status);
CREATE INDEX idx_delivery_record_tracking_number ON delivery_record(tracking_number);
CREATE INDEX idx_delivery_record_scheduled_date ON delivery_record(scheduled_delivery_date);
CREATE INDEX idx_delivery_record_actual_date ON delivery_record(actual_delivery_date);

-- Add comment to table
COMMENT ON TABLE delivery_record IS 'Records asset delivery/shipment information for asset finance agreements';
COMMENT ON COLUMN delivery_record.delivery_status IS 'Current status of the delivery (PENDING, SCHEDULED, IN_TRANSIT, DELIVERED, FAILED, CANCELLED)';
COMMENT ON COLUMN delivery_record.delivery_attempts IS 'Number of delivery attempts made';
COMMENT ON COLUMN delivery_record.delivery_photo_urls IS 'Array of URLs to delivery proof photos';

