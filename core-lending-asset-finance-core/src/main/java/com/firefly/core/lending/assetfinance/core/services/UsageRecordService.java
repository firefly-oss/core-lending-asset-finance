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
import com.firefly.core.lending.assetfinance.interfaces.dtos.UsageRecordDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Service interface for managing Usage Records.
 * <p>
 * Provides operations for creating, reading, updating, and deleting usage records
 * that track the utilization of financed assets. Usage records capture metrics such as
 * mileage for vehicles, operating hours for equipment, or other usage-based measurements
 * that may affect lease terms, maintenance schedules, or end-of-term charges.
 * </p>
 */
public interface UsageRecordService {

    /**
     * Retrieve a paginated list of usage records for a specific asset with optional filtering and sorting.
     *
     * @param assetFinanceAgreementId the unique identifier of the parent agreement
     * @param assetFinanceAssetId the unique identifier of the parent asset
     * @param filterRequest the filter criteria including pagination, sorting, and search parameters
     * @return a Mono emitting a PaginationResponse containing the list of usage records
     */
    Mono<PaginationResponse<UsageRecordDTO>> findAll(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, FilterRequest<UsageRecordDTO> filterRequest);

    /**
     * Create a new usage record for a specific asset.
     *
     * @param assetFinanceAgreementId the unique identifier of the parent agreement
     * @param assetFinanceAssetId the unique identifier of the parent asset
     * @param dto the usage record data to create
     * @return a Mono emitting the created usage record with generated ID and timestamps
     */
    Mono<UsageRecordDTO> create(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, UsageRecordDTO dto);

    /**
     * Retrieve a specific usage record by its unique identifier.
     *
     * @param assetFinanceAgreementId the unique identifier of the parent agreement
     * @param assetFinanceAssetId the unique identifier of the parent asset
     * @param usageRecordId the unique identifier of the usage record
     * @return a Mono emitting the usage record if found
     */
    Mono<UsageRecordDTO> getById(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, UUID usageRecordId);

    /**
     * Update an existing usage record.
     *
     * @param assetFinanceAgreementId the unique identifier of the parent agreement
     * @param assetFinanceAssetId the unique identifier of the parent asset
     * @param usageRecordId the unique identifier of the usage record to update
     * @param dto the updated usage record data
     * @return a Mono emitting the updated usage record
     */
    Mono<UsageRecordDTO> update(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, UUID usageRecordId, UsageRecordDTO dto);

    /**
     * Delete a usage record.
     *
     * @param assetFinanceAgreementId the unique identifier of the parent agreement
     * @param assetFinanceAssetId the unique identifier of the parent asset
     * @param usageRecordId the unique identifier of the usage record to delete
     * @return a Mono that completes when the deletion is successful
     */
    Mono<Void> delete(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, UUID usageRecordId);
}
