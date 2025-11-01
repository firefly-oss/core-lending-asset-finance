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
import com.firefly.core.lending.assetfinance.core.services.AssetFinanceAgreementService;
import com.firefly.core.lending.assetfinance.interfaces.dtos.AssetFinanceAgreementDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/asset-finance-agreements")
@Tag(name = "AssetFinanceAgreement", description = "Operations for Asset Finance Agreements (Leasing & Renting)")
@RequiredArgsConstructor
public class AssetFinanceAgreementController {

    private final AssetFinanceAgreementService service;

    @GetMapping
    @Operation(summary = "List/Search asset finance agreements")
    public Mono<ResponseEntity<PaginationResponse<AssetFinanceAgreementDTO>>> findAll(
            @Valid @RequestBody FilterRequest<AssetFinanceAgreementDTO> filterRequest) {

        return service.findAll(filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create a new asset finance agreement")
    public Mono<ResponseEntity<AssetFinanceAgreementDTO>> create(
            @Valid @RequestBody AssetFinanceAgreementDTO dto) {

        return service.create(dto)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{assetFinanceAgreementId}")
    @Operation(summary = "Get an asset finance agreement by ID")
    public Mono<ResponseEntity<AssetFinanceAgreementDTO>> getById(
            @PathVariable("assetFinanceAgreementId") UUID assetFinanceAgreementId) {

        return service.getById(assetFinanceAgreementId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{assetFinanceAgreementId}")
    @Operation(summary = "Update an existing asset finance agreement")
    public Mono<ResponseEntity<AssetFinanceAgreementDTO>> update(
            @PathVariable("assetFinanceAgreementId") UUID assetFinanceAgreementId,
            @Valid @RequestBody AssetFinanceAgreementDTO dto) {

        return service.update(assetFinanceAgreementId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{assetFinanceAgreementId}")
    @Operation(summary = "Delete an asset finance agreement")
    public Mono<ResponseEntity<Void>> delete(
            @PathVariable("assetFinanceAgreementId") UUID assetFinanceAgreementId) {

        return service.delete(assetFinanceAgreementId)
                .thenReturn(ResponseEntity.noContent().build());
    }
}
