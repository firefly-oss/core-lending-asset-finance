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
import com.firefly.core.lending.assetfinance.core.services.ReturnRecordService;
import com.firefly.core.lending.assetfinance.interfaces.dtos.ReturnRecordDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/asset-finance-agreements/{agreementId}/assets/{assetId}/return-records")
@Tag(name = "ReturnRecord", description = "Operations for Asset Return Records")
@RequiredArgsConstructor
public class ReturnRecordController {

    private final ReturnRecordService service;

    @GetMapping
    @Operation(summary = "List/Search return records for an asset")
    public Mono<ResponseEntity<PaginationResponse<ReturnRecordDTO>>> findAll(
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @Valid @RequestBody FilterRequest<ReturnRecordDTO> filterRequest) {

        return service.findAll(assetFinanceAgreementId, assetFinanceAssetId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create a new return record for an asset")
    public Mono<ResponseEntity<ReturnRecordDTO>> create(
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @Valid @RequestBody ReturnRecordDTO dto) {

        return service.create(assetFinanceAgreementId, assetFinanceAssetId, dto)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{recordId}")
    @Operation(summary = "Get a return record by ID")
    public Mono<ResponseEntity<ReturnRecordDTO>> getById(
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @PathVariable("recordId") UUID returnRecordId) {

        return service.getById(assetFinanceAgreementId, assetFinanceAssetId, returnRecordId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{recordId}")
    @Operation(summary = "Update an existing return record")
    public Mono<ResponseEntity<ReturnRecordDTO>> update(
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @PathVariable("recordId") UUID returnRecordId,
            @Valid @RequestBody ReturnRecordDTO dto) {

        return service.update(assetFinanceAgreementId, assetFinanceAssetId, returnRecordId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{recordId}")
    @Operation(summary = "Delete a return record")
    public Mono<ResponseEntity<Void>> delete(
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @PathVariable("recordId") UUID returnRecordId) {

        return service.delete(assetFinanceAgreementId, assetFinanceAssetId, returnRecordId)
                .thenReturn(ResponseEntity.noContent().build());
    }
}
