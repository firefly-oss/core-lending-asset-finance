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

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import com.firefly.core.lending.assetfinance.interfaces.dtos.PickupRecordDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Service interface for managing Pickup Records.
 * <p>
 * Provides operations for creating, reading, updating, and deleting pickup records
 * that track the logistics of collecting assets from customers. Pickup records capture
 * information about collection scheduling, carrier, tracking, pickup dates, and handover details.
 * </p>
 */
public interface PickupRecordService {

    /**
     * Retrieve a paginated list of pickup records for a specific asset with optional filtering and sorting.
     *
     * @param assetFinanceAgreementId the unique identifier of the parent agreement
     * @param assetFinanceAssetId the unique identifier of the parent asset
     * @param filterRequest the filter criteria including pagination, sorting, and search parameters
     * @return a Mono emitting a PaginationResponse containing the list of pickup records
     */
    Mono<PaginationResponse<PickupRecordDTO>> findAll(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, FilterRequest<PickupRecordDTO> filterRequest);

    /**
     * Create a new pickup record for a specific asset.
     *
     * @param assetFinanceAgreementId the unique identifier of the parent agreement
     * @param assetFinanceAssetId the unique identifier of the parent asset
     * @param dto the pickup record data to create
     * @return a Mono emitting the created pickup record with generated ID and timestamps
     */
    Mono<PickupRecordDTO> create(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, PickupRecordDTO dto);

    /**
     * Retrieve a specific pickup record by its unique identifier.
     *
     * @param assetFinanceAgreementId the unique identifier of the parent agreement
     * @param assetFinanceAssetId the unique identifier of the parent asset
     * @param pickupRecordId the unique identifier of the pickup record
     * @return a Mono emitting the pickup record if found
     */
    Mono<PickupRecordDTO> getById(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, UUID pickupRecordId);

    /**
     * Update an existing pickup record.
     *
     * @param assetFinanceAgreementId the unique identifier of the parent agreement
     * @param assetFinanceAssetId the unique identifier of the parent asset
     * @param pickupRecordId the unique identifier of the pickup record to update
     * @param dto the updated pickup record data
     * @return a Mono emitting the updated pickup record
     */
    Mono<PickupRecordDTO> update(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, UUID pickupRecordId, PickupRecordDTO dto);

    /**
     * Delete a pickup record.
     *
     * @param assetFinanceAgreementId the unique identifier of the parent agreement
     * @param assetFinanceAssetId the unique identifier of the parent asset
     * @param pickupRecordId the unique identifier of the pickup record to delete
     * @return a Mono that completes when the deletion is successful
     */
    Mono<Void> delete(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, UUID pickupRecordId);
}

