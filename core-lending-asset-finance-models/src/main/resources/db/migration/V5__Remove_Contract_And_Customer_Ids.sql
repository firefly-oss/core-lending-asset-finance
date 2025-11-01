-- Remove contract_id and customer_id from asset_finance_agreement
-- This information is already available through the loan_servicing_case_id relationship

ALTER TABLE asset_finance_agreement
    DROP COLUMN contract_id,
    DROP COLUMN customer_id;

