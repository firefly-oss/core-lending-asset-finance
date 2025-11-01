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
import com.firefly.core.lending.assetfinance.interfaces.dtos.AssetFinanceAgreementDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Service interface for managing Asset Finance Agreements.
 * <p>
 * Provides operations for creating, reading, updating, and deleting asset finance agreements
 * for both leasing and renting finance types. Agreements represent the contractual relationship
 * between the lender and borrower for financing assets.
 * </p>
 */
public interface AssetFinanceAgreementService {

    /**
     * Retrieve a paginated list of asset finance agreements with optional filtering and sorting.
     *
     * @param filterRequest the filter criteria including pagination, sorting, and search parameters
     * @return a Mono emitting a PaginationResponse containing the list of agreements
     */
    Mono<PaginationResponse<AssetFinanceAgreementDTO>> findAll(FilterRequest<AssetFinanceAgreementDTO> filterRequest);

    /**
     * Create a new asset finance agreement.
     *
     * @param dto the agreement data to create
     * @return a Mono emitting the created agreement with generated ID and timestamps
     */
    Mono<AssetFinanceAgreementDTO> create(AssetFinanceAgreementDTO dto);

    /**
     * Retrieve a specific asset finance agreement by its unique identifier.
     *
     * @param assetFinanceAgreementId the unique identifier of the agreement
     * @return a Mono emitting the agreement if found
     */
    Mono<AssetFinanceAgreementDTO> getById(UUID assetFinanceAgreementId);

    /**
     * Update an existing asset finance agreement.
     *
     * @param assetFinanceAgreementId the unique identifier of the agreement to update
     * @param dto the updated agreement data
     * @return a Mono emitting the updated agreement
     */
    Mono<AssetFinanceAgreementDTO> update(UUID assetFinanceAgreementId, AssetFinanceAgreementDTO dto);

    /**
     * Delete an asset finance agreement.
     * <p>
     * Note: This will cascade delete all related assets, end options, and associated records
     * due to database foreign key constraints.
     * </p>
     *
     * @param assetFinanceAgreementId the unique identifier of the agreement to delete
     * @return a Mono that completes when the deletion is successful
     */
    Mono<Void> delete(UUID assetFinanceAgreementId);
}
