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
import com.firefly.core.lending.assetfinance.interfaces.dtos.EndOptionDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Service interface for managing End Options.
 * <p>
 * Provides operations for creating, reading, updating, and deleting end-of-term options
 * for asset finance agreements. End options define what happens at the end of a lease term,
 * such as purchase options, return requirements, or renewal terms.
 * </p>
 */
public interface EndOptionService {

    /**
     * Retrieve a paginated list of end options for a specific agreement with optional filtering and sorting.
     *
     * @param assetFinanceAgreementId the unique identifier of the parent agreement
     * @param filterRequest the filter criteria including pagination, sorting, and search parameters
     * @return a Mono emitting a PaginationResponse containing the list of end options
     */
    Mono<PaginationResponse<EndOptionDTO>> findAll(UUID assetFinanceAgreementId, FilterRequest<EndOptionDTO> filterRequest);

    /**
     * Create a new end option for a specific agreement.
     *
     * @param assetFinanceAgreementId the unique identifier of the parent agreement
     * @param dto the end option data to create
     * @return a Mono emitting the created end option with generated ID and timestamps
     */
    Mono<EndOptionDTO> create(UUID assetFinanceAgreementId, EndOptionDTO dto);

    /**
     * Retrieve a specific end option by its unique identifier.
     *
     * @param assetFinanceAgreementId the unique identifier of the parent agreement
     * @param endOptionId the unique identifier of the end option
     * @return a Mono emitting the end option if found
     */
    Mono<EndOptionDTO> getById(UUID assetFinanceAgreementId, UUID endOptionId);

    /**
     * Update an existing end option.
     *
     * @param assetFinanceAgreementId the unique identifier of the parent agreement
     * @param endOptionId the unique identifier of the end option to update
     * @param dto the updated end option data
     * @return a Mono emitting the updated end option
     */
    Mono<EndOptionDTO> update(UUID assetFinanceAgreementId, UUID endOptionId, EndOptionDTO dto);

    /**
     * Delete an end option.
     *
     * @param assetFinanceAgreementId the unique identifier of the parent agreement
     * @param endOptionId the unique identifier of the end option to delete
     * @return a Mono that completes when the deletion is successful
     */
    Mono<Void> delete(UUID assetFinanceAgreementId, UUID endOptionId);
}
