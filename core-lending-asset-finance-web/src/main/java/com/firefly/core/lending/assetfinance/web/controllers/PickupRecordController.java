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
import com.firefly.core.lending.assetfinance.core.services.PickupRecordService;
import com.firefly.core.lending.assetfinance.interfaces.dtos.PickupRecordDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/asset-finance-agreements/{agreementId}/assets/{assetId}/pickup-records")
@Tag(name = "PickupRecord", description = "Operations for Asset Pickup/Collection Records")
@RequiredArgsConstructor
public class PickupRecordController {

    private final PickupRecordService service;

    @GetMapping
    @Operation(summary = "List/Search pickup records for an asset")
    public Mono<ResponseEntity<PaginationResponse<PickupRecordDTO>>> findAll(
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @Valid @RequestBody FilterRequest<PickupRecordDTO> filterRequest) {

        return service.findAll(assetFinanceAgreementId, assetFinanceAssetId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create a new pickup record for an asset")
    public Mono<ResponseEntity<PickupRecordDTO>> create(
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @Valid @RequestBody PickupRecordDTO dto) {

        return service.create(assetFinanceAgreementId, assetFinanceAssetId, dto)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{pickupRecordId}")
    @Operation(summary = "Get a pickup record by ID")
    public Mono<ResponseEntity<PickupRecordDTO>> getById(
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @PathVariable("pickupRecordId") UUID pickupRecordId) {

        return service.getById(assetFinanceAgreementId, assetFinanceAssetId, pickupRecordId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{pickupRecordId}")
    @Operation(summary = "Update an existing pickup record")
    public Mono<ResponseEntity<PickupRecordDTO>> update(
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @PathVariable("pickupRecordId") UUID pickupRecordId,
            @Valid @RequestBody PickupRecordDTO dto) {

        return service.update(assetFinanceAgreementId, assetFinanceAssetId, pickupRecordId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{pickupRecordId}")
    @Operation(summary = "Delete a pickup record")
    public Mono<ResponseEntity<Void>> delete(
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @PathVariable("pickupRecordId") UUID pickupRecordId) {

        return service.delete(assetFinanceAgreementId, assetFinanceAssetId, pickupRecordId)
                .thenReturn(ResponseEntity.noContent().build());
    }
}


