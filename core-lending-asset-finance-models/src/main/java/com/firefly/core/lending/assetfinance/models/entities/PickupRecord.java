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

import com.firefly.core.lending.assetfinance.interfaces.enums.PickupStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("pickup_record")
public class PickupRecord {

    @Id
    @Column("pickup_record_id")
    private UUID pickupRecordId;

    @Column("asset_finance_asset_id")
    private UUID assetFinanceAssetId;

    @Column("pickup_status")
    private PickupStatusEnum pickupStatus;

    @Column("scheduled_pickup_date")
    private LocalDate scheduledPickupDate;

    @Column("actual_pickup_date")
    private LocalDate actualPickupDate;

    @Column("pickup_address")
    private String pickupAddress;

    @Column("pickup_city")
    private String pickupCity;

    @Column("pickup_state")
    private String pickupState;

    @Column("pickup_postal_code")
    private String pickupPostalCode;

    @Column("pickup_country")
    private String pickupCountry;

    @Column("carrier_name")
    private String carrierName;

    @Column("tracking_number")
    private String trackingNumber;

    @Column("collector_name")
    private String collectorName;

    @Column("collector_phone")
    private String collectorPhone;

    @Column("collector_email")
    private String collectorEmail;

    @Column("signature_received")
    private Boolean signatureReceived;

    @Column("pickup_condition_notes")
    private String pickupConditionNotes;

    @Column("pickup_photo_urls")
    private List<String> pickupPhotoUrls;

    @Column("failed_pickup_reason")
    private String failedPickupReason;

    @Column("pickup_attempts")
    private Integer pickupAttempts;

    @Column("note")
    private String note;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}

