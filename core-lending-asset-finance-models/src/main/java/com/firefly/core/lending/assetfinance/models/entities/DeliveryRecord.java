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

import com.firefly.core.lending.assetfinance.interfaces.enums.DeliveryStatusEnum;
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
@Table("delivery_record")
public class DeliveryRecord {

    @Id
    @Column("delivery_record_id")
    private UUID deliveryRecordId;

    @Column("asset_finance_asset_id")
    private UUID assetFinanceAssetId;

    @Column("delivery_status")
    private DeliveryStatusEnum deliveryStatus;

    @Column("scheduled_delivery_date")
    private LocalDate scheduledDeliveryDate;

    @Column("actual_delivery_date")
    private LocalDate actualDeliveryDate;

    @Column("delivery_address")
    private String deliveryAddress;

    @Column("delivery_city")
    private String deliveryCity;

    @Column("delivery_state")
    private String deliveryState;

    @Column("delivery_postal_code")
    private String deliveryPostalCode;

    @Column("delivery_country")
    private String deliveryCountry;

    @Column("carrier_name")
    private String carrierName;

    @Column("tracking_number")
    private String trackingNumber;

    @Column("recipient_name")
    private String recipientName;

    @Column("recipient_phone")
    private String recipientPhone;

    @Column("recipient_email")
    private String recipientEmail;

    @Column("signature_received")
    private Boolean signatureReceived;

    @Column("delivery_condition_notes")
    private String deliveryConditionNotes;

    @Column("delivery_photo_urls")
    private List<String> deliveryPhotoUrls;

    @Column("failed_delivery_reason")
    private String failedDeliveryReason;

    @Column("delivery_attempts")
    private Integer deliveryAttempts;

    @Column("note")
    private String note;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}

