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
import org.fireflyframework.utils.annotations.FilterableId;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsageRecordDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID usageRecordId;

    @NotNull(message = "Asset finance asset ID is required")
    @FilterableId
    private UUID assetFinanceAssetId;

    @NotNull(message = "Usage date is required")
    @ValidDate(message = "Usage date must be a valid date")
    private LocalDate usageDate;

    @Min(value = 0, message = "Mileage must be zero or positive")
    private Integer mileage;

    @Size(max = 1000, message = "Usage detail cannot exceed 1000 characters")
    private String usageDetail;

    @PastOrPresent(message = "Created date cannot be in the future")
    private LocalDateTime createdAt;

    @PastOrPresent(message = "Updated date cannot be in the future")
    private LocalDateTime updatedAt;
}
