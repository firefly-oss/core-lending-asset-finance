-- Create tables for Asset Finance domain

-- Asset Finance Agreement table
CREATE TABLE asset_finance_agreement (
    asset_finance_agreement_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    contract_id UUID NOT NULL,
    customer_id UUID NOT NULL,
    loan_servicing_case_id UUID,  -- FK to LoanServicingCase in loan-servicing microservice
    finance_type finance_type_enum NOT NULL,
    agreement_status agreement_status_enum NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    total_value DECIMAL(19, 4) NOT NULL,
    payment_frequency VARCHAR(50) NOT NULL,
    services_included BOOLEAN,
    deposit_amount DECIMAL(19, 4),
    early_termination_fee DECIMAL(19, 4),
    residual_value DECIMAL(19, 4),
    purchase_option_available BOOLEAN,
    purchase_option_price DECIMAL(19, 4),
    remarks TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Asset Finance Asset table
CREATE TABLE asset_finance_asset (
    asset_finance_asset_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    asset_finance_agreement_id UUID NOT NULL,
    asset_type_id UUID NOT NULL,
    asset_description VARCHAR(500),
    asset_serial_number VARCHAR(100),
    asset_value DECIMAL(19, 4) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    note TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_asset_agreement FOREIGN KEY (asset_finance_agreement_id)
        REFERENCES asset_finance_agreement(asset_finance_agreement_id) ON DELETE CASCADE
);

-- End Option table (Lease-End Purchase Options)
CREATE TABLE end_option (
    end_option_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    asset_finance_agreement_id UUID NOT NULL,
    option_exercise_date DATE,
    option_paid_amount DECIMAL(19, 4),
    is_exercised BOOLEAN DEFAULT FALSE,
    note TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_end_option_agreement FOREIGN KEY (asset_finance_agreement_id) 
        REFERENCES asset_finance_agreement(asset_finance_agreement_id) ON DELETE CASCADE
);

-- Service Event table (Maintenance, Damage, etc.)
CREATE TABLE service_event (
    service_event_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    asset_finance_asset_id UUID NOT NULL,
    event_date DATE NOT NULL,
    event_type event_type_enum NOT NULL,
    cost DECIMAL(19, 4),
    note TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_service_event_asset FOREIGN KEY (asset_finance_asset_id) 
        REFERENCES asset_finance_asset(asset_finance_asset_id) ON DELETE CASCADE
);

-- Usage Record table (Mileage, Hours, etc.)
CREATE TABLE usage_record (
    usage_record_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    asset_finance_asset_id UUID NOT NULL,
    usage_date DATE NOT NULL,
    mileage INTEGER,
    usage_detail TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_usage_record_asset FOREIGN KEY (asset_finance_asset_id) 
        REFERENCES asset_finance_asset(asset_finance_asset_id) ON DELETE CASCADE
);

-- Return Record table
CREATE TABLE return_record (
    return_record_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    asset_finance_asset_id UUID NOT NULL,
    actual_return_date DATE NOT NULL,
    condition_report TEXT,
    damage_cost DECIMAL(19, 4),
    is_finalized BOOLEAN DEFAULT FALSE,
    note TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_return_record_asset FOREIGN KEY (asset_finance_asset_id) 
        REFERENCES asset_finance_asset(asset_finance_asset_id) ON DELETE CASCADE
);

-- Create indexes for better query performance
CREATE INDEX idx_asset_finance_agreement_contract_id ON asset_finance_agreement(contract_id);
CREATE INDEX idx_asset_finance_agreement_customer_id ON asset_finance_agreement(customer_id);
CREATE INDEX idx_asset_finance_agreement_loan_servicing_case_id ON asset_finance_agreement(loan_servicing_case_id);
CREATE INDEX idx_asset_finance_agreement_status ON asset_finance_agreement(agreement_status);
CREATE INDEX idx_asset_finance_asset_agreement_id ON asset_finance_asset(asset_finance_agreement_id);
CREATE INDEX idx_asset_finance_asset_type_id ON asset_finance_asset(asset_type_id);
CREATE INDEX idx_end_option_agreement_id ON end_option(asset_finance_agreement_id);
CREATE INDEX idx_service_event_asset_id ON service_event(asset_finance_asset_id);
CREATE INDEX idx_usage_record_asset_id ON usage_record(asset_finance_asset_id);
CREATE INDEX idx_return_record_asset_id ON return_record(asset_finance_asset_id);

