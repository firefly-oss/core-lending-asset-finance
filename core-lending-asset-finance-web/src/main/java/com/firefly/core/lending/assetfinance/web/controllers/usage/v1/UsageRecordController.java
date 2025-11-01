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


package com.firefly.core.lending.assetfinance.web.controllers.usage.v1;

import com.firefly.common.core.filters.FilterRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.core.lending.assetfinance.core.services.UsageRecordService;
import com.firefly.core.lending.assetfinance.interfaces.dtos.usage.v1.UsageRecordDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/asset-finance-agreements/{agreementId}/assets/{assetId}/usage-records")
@Tag(name = "UsageRecord", description = "Operations for Asset Usage Records (Mileage, Hours, etc.)")
@RequiredArgsConstructor
public class UsageRecordController {

    private final UsageRecordService service;

    @GetMapping
    @Operation(summary = "List/Search usage records for an asset")
    public Mono<ResponseEntity<PaginationResponse<UsageRecordDTO>>> findAll(
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @ModelAttribute FilterRequest<UsageRecordDTO> filterRequest) {

        return service.findAll(assetFinanceAgreementId, assetFinanceAssetId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create a new usage record for an asset")
    public Mono<ResponseEntity<UsageRecordDTO>> create(
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @Valid @RequestBody UsageRecordDTO dto) {

        return service.create(assetFinanceAgreementId, assetFinanceAssetId, dto)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{recordId}")
    @Operation(summary = "Get a usage record by ID")
    public Mono<ResponseEntity<UsageRecordDTO>> getById(
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @PathVariable("recordId") UUID usageRecordId) {

        return service.getById(assetFinanceAgreementId, assetFinanceAssetId, usageRecordId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{recordId}")
    @Operation(summary = "Update an existing usage record")
    public Mono<ResponseEntity<UsageRecordDTO>> update(
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @PathVariable("recordId") UUID usageRecordId,
            @Valid @RequestBody UsageRecordDTO dto) {

        return service.update(assetFinanceAgreementId, assetFinanceAssetId, usageRecordId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{recordId}")
    @Operation(summary = "Delete a usage record")
    public Mono<ResponseEntity<Void>> delete(
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @PathVariable("recordId") UUID usageRecordId) {

        return service.delete(assetFinanceAgreementId, assetFinanceAssetId, usageRecordId)
                .thenReturn(ResponseEntity.noContent().build());
    }
}
