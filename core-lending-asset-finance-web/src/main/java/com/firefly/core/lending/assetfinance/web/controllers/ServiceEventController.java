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


package com.firefly.core.lending.assetfinance.web.controllers;

import com.firefly.common.core.filters.FilterRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.core.lending.assetfinance.core.services.ServiceEventService;
import com.firefly.core.lending.assetfinance.interfaces.dtos.ServiceEventDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/asset-finance-agreements/{agreementId}/assets/{assetId}/service-events")
@Tag(name = "ServiceEvent", description = "Operations for Asset Service Events (Maintenance, Damage, etc.)")
@RequiredArgsConstructor
public class ServiceEventController {

    private final ServiceEventService service;

    @GetMapping
    @Operation(summary = "List/Search service events for an asset")
    public Mono<ResponseEntity<PaginationResponse<ServiceEventDTO>>> findAll(
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @Valid @RequestBody FilterRequest<ServiceEventDTO> filterRequest) {

        return service.findAll(assetFinanceAgreementId, assetFinanceAssetId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create a new service event for an asset")
    public Mono<ResponseEntity<ServiceEventDTO>> create(
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @Valid @RequestBody ServiceEventDTO dto) {

        return service.create(assetFinanceAgreementId, assetFinanceAssetId, dto)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{eventId}")
    @Operation(summary = "Get a service event by ID")
    public Mono<ResponseEntity<ServiceEventDTO>> getById(
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @PathVariable("eventId") UUID serviceEventId) {

        return service.getById(assetFinanceAgreementId, assetFinanceAssetId, serviceEventId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{eventId}")
    @Operation(summary = "Update an existing service event")
    public Mono<ResponseEntity<ServiceEventDTO>> update(
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @PathVariable("eventId") UUID serviceEventId,
            @Valid @RequestBody ServiceEventDTO dto) {

        return service.update(assetFinanceAgreementId, assetFinanceAssetId, serviceEventId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{eventId}")
    @Operation(summary = "Delete a service event")
    public Mono<ResponseEntity<Void>> delete(
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @PathVariable("eventId") UUID serviceEventId) {

        return service.delete(assetFinanceAgreementId, assetFinanceAssetId, serviceEventId)
                .thenReturn(ResponseEntity.noContent().build());
    }
}
