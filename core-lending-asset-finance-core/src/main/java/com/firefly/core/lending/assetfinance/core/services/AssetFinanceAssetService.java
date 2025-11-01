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
import com.firefly.core.lending.assetfinance.interfaces.dtos.AssetFinanceAssetDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Service interface for managing Asset Finance Assets.
 * <p>
 * Provides operations for creating, reading, updating, and deleting individual assets
 * that are part of an asset finance agreement. Each asset represents a physical item
 * (vehicle, equipment, machinery, etc.) being financed under the agreement.
 * </p>
 */
public interface AssetFinanceAssetService {

    /**
     * Retrieve a paginated list of assets for a specific agreement with optional filtering and sorting.
     *
     * @param assetFinanceAgreementId the unique identifier of the parent agreement
     * @param filterRequest the filter criteria including pagination, sorting, and search parameters
     * @return a Mono emitting a PaginationResponse containing the list of assets
     */
    Mono<PaginationResponse<AssetFinanceAssetDTO>> findAll(UUID assetFinanceAgreementId, FilterRequest<AssetFinanceAssetDTO> filterRequest);

    /**
     * Create a new asset under a specific agreement.
     *
     * @param assetFinanceAgreementId the unique identifier of the parent agreement
     * @param dto the asset data to create
     * @return a Mono emitting the created asset with generated ID and timestamps
     */
    Mono<AssetFinanceAssetDTO> create(UUID assetFinanceAgreementId, AssetFinanceAssetDTO dto);

    /**
     * Retrieve a specific asset by its unique identifier.
     *
     * @param assetFinanceAgreementId the unique identifier of the parent agreement
     * @param assetFinanceAssetId the unique identifier of the asset
     * @return a Mono emitting the asset if found
     */
    Mono<AssetFinanceAssetDTO> getById(UUID assetFinanceAgreementId, UUID assetFinanceAssetId);

    /**
     * Update an existing asset.
     *
     * @param assetFinanceAgreementId the unique identifier of the parent agreement
     * @param assetFinanceAssetId the unique identifier of the asset to update
     * @param dto the updated asset data
     * @return a Mono emitting the updated asset
     */
    Mono<AssetFinanceAssetDTO> update(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, AssetFinanceAssetDTO dto);

    /**
     * Delete an asset.
     * <p>
     * Note: This will cascade delete all related records (delivery, pickup, return, service events, usage records)
     * due to database foreign key constraints.
     * </p>
     *
     * @param assetFinanceAgreementId the unique identifier of the parent agreement
     * @param assetFinanceAssetId the unique identifier of the asset to delete
     * @return a Mono that completes when the deletion is successful
     */
    Mono<Void> delete(UUID assetFinanceAgreementId, UUID assetFinanceAssetId);
}
