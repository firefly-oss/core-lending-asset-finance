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
import com.firefly.core.lending.assetfinance.interfaces.dtos.ReturnRecordDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Service interface for managing Return Records.
 * <p>
 * Provides operations for creating, reading, updating, and deleting return records
 * that document the condition assessment of assets upon return. Return records capture
 * information about asset condition, damage, wear and tear, and any charges or refunds.
 * Note: This is separate from PickupRecord which handles the logistics of collection.
 * </p>
 */
public interface ReturnRecordService {

    /**
     * Retrieve a paginated list of return records for a specific asset with optional filtering and sorting.
     *
     * @param assetFinanceAgreementId the unique identifier of the parent agreement
     * @param assetFinanceAssetId the unique identifier of the parent asset
     * @param filterRequest the filter criteria including pagination, sorting, and search parameters
     * @return a Mono emitting a PaginationResponse containing the list of return records
     */
    Mono<PaginationResponse<ReturnRecordDTO>> findAll(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, FilterRequest<ReturnRecordDTO> filterRequest);

    /**
     * Create a new return record for a specific asset.
     *
     * @param assetFinanceAgreementId the unique identifier of the parent agreement
     * @param assetFinanceAssetId the unique identifier of the parent asset
     * @param dto the return record data to create
     * @return a Mono emitting the created return record with generated ID and timestamps
     */
    Mono<ReturnRecordDTO> create(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, ReturnRecordDTO dto);

    /**
     * Retrieve a specific return record by its unique identifier.
     *
     * @param assetFinanceAgreementId the unique identifier of the parent agreement
     * @param assetFinanceAssetId the unique identifier of the parent asset
     * @param returnRecordId the unique identifier of the return record
     * @return a Mono emitting the return record if found
     */
    Mono<ReturnRecordDTO> getById(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, UUID returnRecordId);

    /**
     * Update an existing return record.
     *
     * @param assetFinanceAgreementId the unique identifier of the parent agreement
     * @param assetFinanceAssetId the unique identifier of the parent asset
     * @param returnRecordId the unique identifier of the return record to update
     * @param dto the updated return record data
     * @return a Mono emitting the updated return record
     */
    Mono<ReturnRecordDTO> update(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, UUID returnRecordId, ReturnRecordDTO dto);

    /**
     * Delete a return record.
     *
     * @param assetFinanceAgreementId the unique identifier of the parent agreement
     * @param assetFinanceAssetId the unique identifier of the parent asset
     * @param returnRecordId the unique identifier of the return record to delete
     * @return a Mono that completes when the deletion is successful
     */
    Mono<Void> delete(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, UUID returnRecordId);
}
