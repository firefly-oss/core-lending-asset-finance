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


package com.firefly.core.lending.assetfinance.web.controllers.assets.v1;

import com.firefly.common.core.filters.FilterRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.core.lending.assetfinance.core.services.AssetFinanceAssetService;
import com.firefly.core.lending.assetfinance.interfaces.dtos.assets.v1.AssetFinanceAssetDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/asset-finance-agreements/{agreementId}/assets")
@Tag(name = "AssetFinanceAsset", description = "Operations for Asset Finance Assets")
@RequiredArgsConstructor
public class AssetFinanceAssetController {

    private final AssetFinanceAssetService service;

    @GetMapping
    @Operation(summary = "List/Search assets for an agreement")
    public Mono<ResponseEntity<PaginationResponse<AssetFinanceAssetDTO>>> findAll(
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @ModelAttribute FilterRequest<AssetFinanceAssetDTO> filterRequest) {

        return service.findAll(assetFinanceAgreementId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create a new asset for an agreement")
    public Mono<ResponseEntity<AssetFinanceAssetDTO>> create(
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @Valid @RequestBody AssetFinanceAssetDTO dto) {

        return service.create(assetFinanceAgreementId, dto)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{assetId}")
    @Operation(summary = "Get an asset by ID")
    public Mono<ResponseEntity<AssetFinanceAssetDTO>> getById(
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @PathVariable("assetId") UUID assetFinanceAssetId) {

        return service.getById(assetFinanceAgreementId, assetFinanceAssetId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{assetId}")
    @Operation(summary = "Update an existing asset")
    public Mono<ResponseEntity<AssetFinanceAssetDTO>> update(
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @Valid @RequestBody AssetFinanceAssetDTO dto) {

        return service.update(assetFinanceAgreementId, assetFinanceAssetId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{assetId}")
    @Operation(summary = "Delete an asset")
    public Mono<ResponseEntity<Void>> delete(
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @PathVariable("assetId") UUID assetFinanceAssetId) {

        return service.delete(assetFinanceAgreementId, assetFinanceAssetId)
                .thenReturn(ResponseEntity.noContent().build());
    }
}
