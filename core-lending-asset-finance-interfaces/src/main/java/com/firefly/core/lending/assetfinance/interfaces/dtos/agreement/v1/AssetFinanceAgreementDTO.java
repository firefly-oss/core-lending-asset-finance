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


package com.firefly.core.lending.assetfinance.interfaces.dtos.agreement.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.firefly.annotations.ValidAmount;
import com.firefly.annotations.ValidDate;
import com.firefly.core.lending.assetfinance.interfaces.enums.agreement.v1.AgreementStatusEnum;
import com.firefly.core.lending.assetfinance.interfaces.enums.finance.v1.FinanceTypeEnum;
import com.firefly.core.utils.annotations.FilterableId;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetFinanceAgreementDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID assetFinanceAgreementId;

    @NotNull(message = "Contract ID is required")
    @FilterableId
    private UUID contractId;

    @NotNull(message = "Customer ID is required")
    @FilterableId
    private UUID customerId;

    @FilterableId
    private UUID loanServicingCaseId; // FK to LoanServicingCase in loan-servicing microservice

    @NotNull(message = "Finance type is required")
    private FinanceTypeEnum financeType;

    @NotNull(message = "Agreement status is required")
    private AgreementStatusEnum agreementStatus;

    @NotNull(message = "Start date is required")
    @ValidDate(message = "Start date must be a valid date")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @ValidDate(message = "End date must be a valid date")
    private LocalDate endDate;

    // Common financial terms
    @NotNull(message = "Total value is required")
    @Positive(message = "Total value must be positive")
    @ValidAmount(message = "Total value must be a valid amount")
    private BigDecimal totalValue;

    @NotNull(message = "Payment frequency is required")
    @Size(max = 50, message = "Payment frequency cannot exceed 50 characters")
    private String paymentFrequency;  // MONTHLY, QUARTERLY, YEARLY

    // Renting-specific fields (null for leasing)
    private Boolean servicesIncluded;

    @Positive(message = "Deposit amount must be positive")
    @ValidAmount(message = "Deposit amount must be a valid amount")
    private BigDecimal depositAmount;

    @PositiveOrZero(message = "Early termination fee cannot be negative")
    @ValidAmount(message = "Early termination fee must be a valid amount")
    private BigDecimal earlyTerminationFee;

    // Leasing-specific fields (null for renting)
    @Positive(message = "Residual value must be positive")
    @ValidAmount(message = "Residual value must be a valid amount")
    private BigDecimal residualValue;

    private Boolean purchaseOptionAvailable;

    @Positive(message = "Purchase option price must be positive")
    @ValidAmount(message = "Purchase option price must be a valid amount")
    private BigDecimal purchaseOptionPrice;

    @Size(max = 1000, message = "Remarks cannot exceed 1000 characters")
    private String remarks;

    @PastOrPresent(message = "Created date cannot be in the future")
    private LocalDateTime createdAt;

    @PastOrPresent(message = "Updated date cannot be in the future")
    private LocalDateTime updatedAt;
}
