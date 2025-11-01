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
@RequestMapping("/api/v1/asset-finance-agreements/{agreementId}/assets/{assetId}/pickup-records")
@Tag(name = "PickupRecord", description = "Operations for Asset Pickup/Collection Records")
@RequiredArgsConstructor
public class PickupRecordController {

    private final PickupRecordService service;

    @GetMapping
    @Operation(
            summary = "List/Search pickup records for an asset",
            description = "Retrieve a paginated list of pickup/collection records for a specific asset"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of pickup records",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PaginationResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid filter request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Asset or agreement not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Mono<ResponseEntity<PaginationResponse<PickupRecordDTO>>> findAll(
            @Parameter(description = "Unique identifier of the agreement", required = true)
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @Parameter(description = "Unique identifier of the asset", required = true)
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @Parameter(description = "Filter criteria for searching pickup records")
            @Valid @RequestBody FilterRequest<PickupRecordDTO> filterRequest) {

        return service.findAll(assetFinanceAgreementId, assetFinanceAssetId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(
            summary = "Create a new pickup record for an asset",
            description = "Create a new pickup/collection record to track asset pickup logistics"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pickup record created successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PickupRecordDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Asset or agreement not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Mono<ResponseEntity<PickupRecordDTO>> create(
            @Parameter(description = "Unique identifier of the agreement", required = true)
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @Parameter(description = "Unique identifier of the asset", required = true)
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @Parameter(description = "Pickup record data to create", required = true)
            @Valid @RequestBody PickupRecordDTO dto) {

        return service.create(assetFinanceAgreementId, assetFinanceAssetId, dto)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{pickupRecordId}")
    @Operation(
            summary = "Get a pickup record by ID",
            description = "Retrieve a specific pickup record by its unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pickup record found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PickupRecordDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Pickup record, asset, or agreement not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Mono<ResponseEntity<PickupRecordDTO>> getById(
            @Parameter(description = "Unique identifier of the agreement", required = true)
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @Parameter(description = "Unique identifier of the asset", required = true)
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @Parameter(description = "Unique identifier of the pickup record", required = true)
            @PathVariable("pickupRecordId") UUID pickupRecordId) {

        return service.getById(assetFinanceAgreementId, assetFinanceAssetId, pickupRecordId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{pickupRecordId}")
    @Operation(
            summary = "Update an existing pickup record",
            description = "Update all fields of an existing pickup record"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pickup record updated successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PickupRecordDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pickup record, asset, or agreement not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Mono<ResponseEntity<PickupRecordDTO>> update(
            @Parameter(description = "Unique identifier of the agreement", required = true)
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @Parameter(description = "Unique identifier of the asset", required = true)
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @Parameter(description = "Unique identifier of the pickup record to update", required = true)
            @PathVariable("pickupRecordId") UUID pickupRecordId,
            @Parameter(description = "Updated pickup record data", required = true)
            @Valid @RequestBody PickupRecordDTO dto) {

        return service.update(assetFinanceAgreementId, assetFinanceAssetId, pickupRecordId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{pickupRecordId}")
    @Operation(
            summary = "Delete a pickup record",
            description = "Permanently delete a pickup record"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pickup record deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pickup record, asset, or agreement not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Mono<ResponseEntity<Void>> delete(
            @Parameter(description = "Unique identifier of the agreement", required = true)
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @Parameter(description = "Unique identifier of the asset", required = true)
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @Parameter(description = "Unique identifier of the pickup record to delete", required = true)
            @PathVariable("pickupRecordId") UUID pickupRecordId) {

        return service.delete(assetFinanceAgreementId, assetFinanceAssetId, pickupRecordId)
                .thenReturn(ResponseEntity.noContent().build());
    }
}


