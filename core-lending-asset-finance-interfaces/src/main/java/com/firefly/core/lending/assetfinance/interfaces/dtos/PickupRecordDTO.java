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


package com.firefly.core.lending.assetfinance.interfaces.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.firefly.annotations.ValidDate;
import com.firefly.core.lending.assetfinance.interfaces.enums.PickupStatusEnum;
import com.firefly.core.utils.annotations.FilterableId;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PickupRecordDTO {

    @JsonProperty(access = READ_ONLY)
    private UUID pickupRecordId;

    @NotNull(message = "Asset finance asset ID is required")
    @FilterableId
    private UUID assetFinanceAssetId;

    @NotNull(message = "Pickup status is required")
    private PickupStatusEnum pickupStatus;

    @ValidDate
    private LocalDate scheduledPickupDate;

    @ValidDate
    @PastOrPresent(message = "Actual pickup date cannot be in the future")
    private LocalDate actualPickupDate;

    @NotBlank(message = "Pickup address is required")
    @Size(max = 500, message = "Pickup address must not exceed 500 characters")
    private String pickupAddress;

    @Size(max = 100, message = "Pickup city must not exceed 100 characters")
    private String pickupCity;

    @Size(max = 100, message = "Pickup state must not exceed 100 characters")
    private String pickupState;

    @Size(max = 20, message = "Pickup postal code must not exceed 20 characters")
    private String pickupPostalCode;

    @Size(max = 100, message = "Pickup country must not exceed 100 characters")
    private String pickupCountry;

    @Size(max = 200, message = "Carrier name must not exceed 200 characters")
    private String carrierName;

    @Size(max = 200, message = "Tracking number must not exceed 200 characters")
    private String trackingNumber;

    @Size(max = 200, message = "Collector name must not exceed 200 characters")
    private String collectorName;

    @Size(max = 50, message = "Collector phone must not exceed 50 characters")
    @Pattern(regexp = "^[+]?[0-9\\s\\-()]*$", message = "Invalid phone number format")
    private String collectorPhone;

    @Size(max = 200, message = "Collector email must not exceed 200 characters")
    @Email(message = "Invalid email format")
    private String collectorEmail;

    private Boolean signatureReceived;

    @Size(max = 2000, message = "Pickup condition notes must not exceed 2000 characters")
    private String pickupConditionNotes;

    private List<@Size(max = 500, message = "Photo URL must not exceed 500 characters") String> pickupPhotoUrls;

    @Size(max = 1000, message = "Failed pickup reason must not exceed 1000 characters")
    private String failedPickupReason;

    @Min(value = 0, message = "Pickup attempts cannot be negative")
    private Integer pickupAttempts;

    @Size(max = 1000, message = "Note must not exceed 1000 characters")
    private String note;

    @PastOrPresent(message = "Created at cannot be in the future")
    private LocalDateTime createdAt;

    @PastOrPresent(message = "Updated at cannot be in the future")
    private LocalDateTime updatedAt;
}

