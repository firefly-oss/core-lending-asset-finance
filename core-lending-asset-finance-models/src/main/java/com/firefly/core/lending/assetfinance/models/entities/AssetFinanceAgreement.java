/*
 * Copyright 2025 Firefly Software Solutions Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.firefly.core.lending.assetfinance.models.entities;

import com.firefly.core.lending.assetfinance.interfaces.enums.AgreementStatusEnum;
import com.firefly.core.lending.assetfinance.interfaces.enums.FinanceTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("asset_finance_agreement")
public class AssetFinanceAgreement {

    @Id
    @Column("asset_finance_agreement_id")
    private UUID assetFinanceAgreementId;

    @Column("contract_id")
    private UUID contractId;

    @Column("customer_id")
    private UUID customerId;

    @Column("loan_servicing_case_id")
    private UUID loanServicingCaseId; // FK to LoanServicingCase in loan-servicing microservice

    @Column("finance_type")
    private FinanceTypeEnum financeType;

    @Column("agreement_status")
    private AgreementStatusEnum agreementStatus;

    @Column("start_date")
    private LocalDate startDate;

    @Column("end_date")
    private LocalDate endDate;

    // Common financial terms
    @Column("total_value")
    private BigDecimal totalValue;

    @Column("payment_frequency")
    private String paymentFrequency;  // MONTHLY, QUARTERLY, etc.

    // Renting-specific fields
    @Column("services_included")
    private Boolean servicesIncluded;

    @Column("deposit_amount")
    private BigDecimal depositAmount;

    @Column("early_termination_fee")
    private BigDecimal earlyTerminationFee;

    // Leasing-specific fields  
    @Column("residual_value")
    private BigDecimal residualValue;

    @Column("purchase_option_available")
    private Boolean purchaseOptionAvailable;

    @Column("purchase_option_price")
    private BigDecimal purchaseOptionPrice;

    @Column("remarks")
    private String remarks;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}
