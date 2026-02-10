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
import org.fireflyframework.annotations.ValidDate;
import com.firefly.core.lending.assetfinance.interfaces.enums.DeliveryStatusEnum;
import org.fireflyframework.utils.annotations.FilterableId;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryRecordDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID deliveryRecordId;

    @NotNull(message = "Asset finance asset ID is required")
    @FilterableId
    private UUID assetFinanceAssetId;

    @NotNull(message = "Delivery status is required")
    private DeliveryStatusEnum deliveryStatus;

    @ValidDate(message = "Scheduled delivery date must be a valid date")
    private LocalDate scheduledDeliveryDate;

    @ValidDate(message = "Actual delivery date must be a valid date")
    private LocalDate actualDeliveryDate;

    @NotBlank(message = "Delivery address is required")
    @Size(max = 500, message = "Delivery address cannot exceed 500 characters")
    private String deliveryAddress;

    @Size(max = 100, message = "Delivery city cannot exceed 100 characters")
    private String deliveryCity;

    @Size(max = 100, message = "Delivery state cannot exceed 100 characters")
    private String deliveryState;

    @Size(max = 20, message = "Delivery postal code cannot exceed 20 characters")
    private String deliveryPostalCode;

    @Size(max = 100, message = "Delivery country cannot exceed 100 characters")
    private String deliveryCountry;

    @Size(max = 200, message = "Carrier name cannot exceed 200 characters")
    private String carrierName;

    @Size(max = 200, message = "Tracking number cannot exceed 200 characters")
    private String trackingNumber;

    @Size(max = 200, message = "Recipient name cannot exceed 200 characters")
    private String recipientName;

    @Size(max = 50, message = "Recipient phone cannot exceed 50 characters")
    @Pattern(regexp = "^[+]?[0-9\\s\\-()]*$", message = "Recipient phone must be a valid phone number")
    private String recipientPhone;

    @Email(message = "Recipient email must be a valid email address")
    @Size(max = 200, message = "Recipient email cannot exceed 200 characters")
    private String recipientEmail;

    private Boolean signatureReceived;

    @Size(max = 2000, message = "Delivery condition notes cannot exceed 2000 characters")
    private String deliveryConditionNotes;

    private List<@Size(max = 500, message = "Photo URL cannot exceed 500 characters") String> deliveryPhotoUrls;

    @Size(max = 1000, message = "Failed delivery reason cannot exceed 1000 characters")
    private String failedDeliveryReason;

    @Min(value = 0, message = "Delivery attempts must be zero or positive")
    private Integer deliveryAttempts;

    @Size(max = 1000, message = "Note cannot exceed 1000 characters")
    private String note;

    @PastOrPresent(message = "Created date cannot be in the future")
    private LocalDateTime createdAt;

    @PastOrPresent(message = "Updated date cannot be in the future")
    private LocalDateTime updatedAt;
}

