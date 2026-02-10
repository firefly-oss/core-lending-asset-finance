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

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import com.firefly.core.lending.assetfinance.core.services.AssetFinanceAssetService;
import com.firefly.core.lending.assetfinance.interfaces.dtos.AssetFinanceAssetDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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
    @Operation(
            summary = "List/Search assets for an agreement",
            description = "Retrieve a paginated list of assets associated with a specific agreement"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of assets",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PaginationResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid filter request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Agreement not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Mono<ResponseEntity<PaginationResponse<AssetFinanceAssetDTO>>> findAll(
            @Parameter(description = "Unique identifier of the agreement", required = true)
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @Parameter(description = "Filter criteria for searching assets")
            @Valid @RequestBody FilterRequest<AssetFinanceAssetDTO> filterRequest) {

        return service.findAll(assetFinanceAgreementId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(
            summary = "Create a new asset for an agreement",
            description = "Add a new asset (vehicle, equipment, etc.) to an existing agreement"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Asset created successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AssetFinanceAssetDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Agreement not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Mono<ResponseEntity<AssetFinanceAssetDTO>> create(
            @Parameter(description = "Unique identifier of the agreement", required = true)
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @Parameter(description = "Asset data to create", required = true)
            @Valid @RequestBody AssetFinanceAssetDTO dto) {

        return service.create(assetFinanceAgreementId, dto)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{assetId}")
    @Operation(
            summary = "Get an asset by ID",
            description = "Retrieve a specific asset by its unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Asset found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AssetFinanceAssetDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Asset or agreement not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Mono<ResponseEntity<AssetFinanceAssetDTO>> getById(
            @Parameter(description = "Unique identifier of the agreement", required = true)
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @Parameter(description = "Unique identifier of the asset", required = true)
            @PathVariable("assetId") UUID assetFinanceAssetId) {

        return service.getById(assetFinanceAgreementId, assetFinanceAssetId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{assetId}")
    @Operation(
            summary = "Update an existing asset",
            description = "Update all fields of an existing asset"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Asset updated successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AssetFinanceAssetDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Asset or agreement not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Mono<ResponseEntity<AssetFinanceAssetDTO>> update(
            @Parameter(description = "Unique identifier of the agreement", required = true)
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @Parameter(description = "Unique identifier of the asset to update", required = true)
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @Parameter(description = "Updated asset data", required = true)
            @Valid @RequestBody AssetFinanceAssetDTO dto) {

        return service.update(assetFinanceAgreementId, assetFinanceAssetId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{assetId}")
    @Operation(
            summary = "Delete an asset",
            description = "Permanently delete an asset and all related data (cascading delete)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Asset deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Asset or agreement not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Mono<ResponseEntity<Void>> delete(
            @Parameter(description = "Unique identifier of the agreement", required = true)
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @Parameter(description = "Unique identifier of the asset to delete", required = true)
            @PathVariable("assetId") UUID assetFinanceAssetId) {

        return service.delete(assetFinanceAgreementId, assetFinanceAssetId)
                .thenReturn(ResponseEntity.noContent().build());
    }
}
