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
import com.firefly.core.lending.assetfinance.interfaces.dtos.ServiceEventDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Service interface for managing Service Events.
 * <p>
 * Provides operations for creating, reading, updating, and deleting service events
 * that track maintenance, repairs, damage, and other service-related activities for assets.
 * Service events help maintain a complete history of asset servicing and condition changes.
 * </p>
 */
public interface ServiceEventService {

    /**
     * Retrieve a paginated list of service events for a specific asset with optional filtering and sorting.
     *
     * @param assetFinanceAgreementId the unique identifier of the parent agreement
     * @param assetFinanceAssetId the unique identifier of the parent asset
     * @param filterRequest the filter criteria including pagination, sorting, and search parameters
     * @return a Mono emitting a PaginationResponse containing the list of service events
     */
    Mono<PaginationResponse<ServiceEventDTO>> findAll(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, FilterRequest<ServiceEventDTO> filterRequest);

    /**
     * Create a new service event for a specific asset.
     *
     * @param assetFinanceAgreementId the unique identifier of the parent agreement
     * @param assetFinanceAssetId the unique identifier of the parent asset
     * @param dto the service event data to create
     * @return a Mono emitting the created service event with generated ID and timestamps
     */
    Mono<ServiceEventDTO> create(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, ServiceEventDTO dto);

    /**
     * Retrieve a specific service event by its unique identifier.
     *
     * @param assetFinanceAgreementId the unique identifier of the parent agreement
     * @param assetFinanceAssetId the unique identifier of the parent asset
     * @param serviceEventId the unique identifier of the service event
     * @return a Mono emitting the service event if found
     */
    Mono<ServiceEventDTO> getById(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, UUID serviceEventId);

    /**
     * Update an existing service event.
     *
     * @param assetFinanceAgreementId the unique identifier of the parent agreement
     * @param assetFinanceAssetId the unique identifier of the parent asset
     * @param serviceEventId the unique identifier of the service event to update
     * @param dto the updated service event data
     * @return a Mono emitting the updated service event
     */
    Mono<ServiceEventDTO> update(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, UUID serviceEventId, ServiceEventDTO dto);

    /**
     * Delete a service event.
     *
     * @param assetFinanceAgreementId the unique identifier of the parent agreement
     * @param assetFinanceAssetId the unique identifier of the parent asset
     * @param serviceEventId the unique identifier of the service event to delete
     * @return a Mono that completes when the deletion is successful
     */
    Mono<Void> delete(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, UUID serviceEventId);
}
