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


package com.firefly.core.lending.assetfinance.core.services;

import com.firefly.common.core.filters.FilterRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.core.lending.assetfinance.interfaces.dtos.DeliveryRecordDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Service interface for managing Delivery Records.
 * <p>
 * Provides operations for creating, reading, updating, and deleting delivery records
 * that track the logistics of delivering assets to customers. Delivery records capture
 * information about shipment, carrier, tracking, delivery dates, and recipient details.
 * </p>
 */
public interface DeliveryRecordService {

    /**
     * Retrieve a paginated list of delivery records for a specific asset with optional filtering and sorting.
     *
     * @param assetFinanceAgreementId the unique identifier of the parent agreement
     * @param assetFinanceAssetId the unique identifier of the parent asset
     * @param filterRequest the filter criteria including pagination, sorting, and search parameters
     * @return a Mono emitting a PaginationResponse containing the list of delivery records
     */
    Mono<PaginationResponse<DeliveryRecordDTO>> findAll(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, FilterRequest<DeliveryRecordDTO> filterRequest);

    /**
     * Create a new delivery record for a specific asset.
     *
     * @param assetFinanceAgreementId the unique identifier of the parent agreement
     * @param assetFinanceAssetId the unique identifier of the parent asset
     * @param dto the delivery record data to create
     * @return a Mono emitting the created delivery record with generated ID and timestamps
     */
    Mono<DeliveryRecordDTO> create(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, DeliveryRecordDTO dto);

    /**
     * Retrieve a specific delivery record by its unique identifier.
     *
     * @param assetFinanceAgreementId the unique identifier of the parent agreement
     * @param assetFinanceAssetId the unique identifier of the parent asset
     * @param deliveryRecordId the unique identifier of the delivery record
     * @return a Mono emitting the delivery record if found
     */
    Mono<DeliveryRecordDTO> getById(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, UUID deliveryRecordId);

    /**
     * Update an existing delivery record.
     *
     * @param assetFinanceAgreementId the unique identifier of the parent agreement
     * @param assetFinanceAssetId the unique identifier of the parent asset
     * @param deliveryRecordId the unique identifier of the delivery record to update
     * @param dto the updated delivery record data
     * @return a Mono emitting the updated delivery record
     */
    Mono<DeliveryRecordDTO> update(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, UUID deliveryRecordId, DeliveryRecordDTO dto);

    /**
     * Delete a delivery record.
     *
     * @param assetFinanceAgreementId the unique identifier of the parent agreement
     * @param assetFinanceAssetId the unique identifier of the parent asset
     * @param deliveryRecordId the unique identifier of the delivery record to delete
     * @return a Mono that completes when the deletion is successful
     */
    Mono<Void> delete(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, UUID deliveryRecordId);
}

