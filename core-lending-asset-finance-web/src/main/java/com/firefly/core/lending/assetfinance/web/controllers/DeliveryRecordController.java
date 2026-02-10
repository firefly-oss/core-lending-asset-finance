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
import com.firefly.core.lending.assetfinance.core.services.DeliveryRecordService;
import com.firefly.core.lending.assetfinance.interfaces.dtos.DeliveryRecordDTO;
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
@RequestMapping("/api/v1/asset-finance-agreements/{agreementId}/assets/{assetId}/delivery-records")
@Tag(name = "DeliveryRecord", description = "Operations for Asset Delivery/Shipment Records")
@RequiredArgsConstructor
public class DeliveryRecordController {

    private final DeliveryRecordService service;

    @GetMapping
    @Operation(
            summary = "List/Search delivery records for an asset",
            description = "Retrieve a paginated list of delivery/shipment records for a specific asset"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of delivery records",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PaginationResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid filter request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Asset or agreement not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Mono<ResponseEntity<PaginationResponse<DeliveryRecordDTO>>> findAll(
            @Parameter(description = "Unique identifier of the agreement", required = true)
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @Parameter(description = "Unique identifier of the asset", required = true)
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @Parameter(description = "Filter criteria for searching delivery records")
            @Valid @RequestBody FilterRequest<DeliveryRecordDTO> filterRequest) {

        return service.findAll(assetFinanceAgreementId, assetFinanceAssetId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(
            summary = "Create a new delivery record for an asset",
            description = "Create a new delivery/shipment record to track asset delivery logistics"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Delivery record created successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DeliveryRecordDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Asset or agreement not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Mono<ResponseEntity<DeliveryRecordDTO>> create(
            @Parameter(description = "Unique identifier of the agreement", required = true)
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @Parameter(description = "Unique identifier of the asset", required = true)
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @Parameter(description = "Delivery record data to create", required = true)
            @Valid @RequestBody DeliveryRecordDTO dto) {

        return service.create(assetFinanceAgreementId, assetFinanceAssetId, dto)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{deliveryRecordId}")
    @Operation(
            summary = "Get a delivery record by ID",
            description = "Retrieve a specific delivery record by its unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Delivery record found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DeliveryRecordDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Delivery record, asset, or agreement not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Mono<ResponseEntity<DeliveryRecordDTO>> getById(
            @Parameter(description = "Unique identifier of the agreement", required = true)
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @Parameter(description = "Unique identifier of the asset", required = true)
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @Parameter(description = "Unique identifier of the delivery record", required = true)
            @PathVariable("deliveryRecordId") UUID deliveryRecordId) {

        return service.getById(assetFinanceAgreementId, assetFinanceAssetId, deliveryRecordId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{deliveryRecordId}")
    @Operation(
            summary = "Update an existing delivery record",
            description = "Update all fields of an existing delivery record"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Delivery record updated successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DeliveryRecordDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Delivery record, asset, or agreement not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Mono<ResponseEntity<DeliveryRecordDTO>> update(
            @Parameter(description = "Unique identifier of the agreement", required = true)
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @Parameter(description = "Unique identifier of the asset", required = true)
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @Parameter(description = "Unique identifier of the delivery record to update", required = true)
            @PathVariable("deliveryRecordId") UUID deliveryRecordId,
            @Parameter(description = "Updated delivery record data", required = true)
            @Valid @RequestBody DeliveryRecordDTO dto) {

        return service.update(assetFinanceAgreementId, assetFinanceAssetId, deliveryRecordId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{deliveryRecordId}")
    @Operation(
            summary = "Delete a delivery record",
            description = "Permanently delete a delivery record"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Delivery record deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Delivery record, asset, or agreement not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Mono<ResponseEntity<Void>> delete(
            @Parameter(description = "Unique identifier of the agreement", required = true)
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @Parameter(description = "Unique identifier of the asset", required = true)
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @Parameter(description = "Unique identifier of the delivery record to delete", required = true)
            @PathVariable("deliveryRecordId") UUID deliveryRecordId) {

        return service.delete(assetFinanceAgreementId, assetFinanceAssetId, deliveryRecordId)
                .thenReturn(ResponseEntity.noContent().build());
    }
}

