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
import com.firefly.core.lending.assetfinance.core.services.DeliveryRecordService;
import com.firefly.core.lending.assetfinance.interfaces.dtos.DeliveryRecordDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/asset-finance-agreements/{agreementId}/assets/{assetId}/delivery-records")
@Tag(name = "DeliveryRecord", description = "Operations for Asset Delivery/Shipment Records")
@RequiredArgsConstructor
public class DeliveryRecordController {

    private final DeliveryRecordService service;

    @GetMapping
    @Operation(summary = "List/Search delivery records for an asset")
    public Mono<ResponseEntity<PaginationResponse<DeliveryRecordDTO>>> findAll(
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @Valid @RequestBody FilterRequest<DeliveryRecordDTO> filterRequest) {

        return service.findAll(assetFinanceAgreementId, assetFinanceAssetId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create a new delivery record for an asset")
    public Mono<ResponseEntity<DeliveryRecordDTO>> create(
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @Valid @RequestBody DeliveryRecordDTO dto) {

        return service.create(assetFinanceAgreementId, assetFinanceAssetId, dto)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{deliveryRecordId}")
    @Operation(summary = "Get a delivery record by ID")
    public Mono<ResponseEntity<DeliveryRecordDTO>> getById(
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @PathVariable("deliveryRecordId") UUID deliveryRecordId) {

        return service.getById(assetFinanceAgreementId, assetFinanceAssetId, deliveryRecordId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{deliveryRecordId}")
    @Operation(summary = "Update an existing delivery record")
    public Mono<ResponseEntity<DeliveryRecordDTO>> update(
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @PathVariable("deliveryRecordId") UUID deliveryRecordId,
            @Valid @RequestBody DeliveryRecordDTO dto) {

        return service.update(assetFinanceAgreementId, assetFinanceAssetId, deliveryRecordId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{deliveryRecordId}")
    @Operation(summary = "Delete a delivery record")
    public Mono<ResponseEntity<Void>> delete(
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @PathVariable("deliveryRecordId") UUID deliveryRecordId) {

        return service.delete(assetFinanceAgreementId, assetFinanceAssetId, deliveryRecordId)
                .thenReturn(ResponseEntity.noContent().build());
    }
}

