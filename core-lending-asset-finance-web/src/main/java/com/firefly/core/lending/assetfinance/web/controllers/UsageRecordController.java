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
import com.firefly.core.lending.assetfinance.core.services.UsageRecordService;
import com.firefly.core.lending.assetfinance.interfaces.dtos.UsageRecordDTO;
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
@RequestMapping("/api/v1/asset-finance-agreements/{agreementId}/assets/{assetId}/usage-records")
@Tag(name = "UsageRecord", description = "Operations for Asset Usage Records (Mileage, Hours, etc.)")
@RequiredArgsConstructor
public class UsageRecordController {

    private final UsageRecordService service;

    @GetMapping
    @Operation(
            summary = "List/Search usage records for an asset",
            description = "Retrieve a paginated list of usage records (mileage, hours, etc.) for a specific asset"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of usage records",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PaginationResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid filter request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Asset or agreement not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Mono<ResponseEntity<PaginationResponse<UsageRecordDTO>>> findAll(
            @Parameter(description = "Unique identifier of the agreement", required = true)
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @Parameter(description = "Unique identifier of the asset", required = true)
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @Parameter(description = "Filter criteria for searching usage records")
            @Valid @RequestBody FilterRequest<UsageRecordDTO> filterRequest) {

        return service.findAll(assetFinanceAgreementId, assetFinanceAssetId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(
            summary = "Create a new usage record for an asset",
            description = "Create a new usage record to track asset usage metrics (mileage, operating hours, etc.)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usage record created successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UsageRecordDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Asset or agreement not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Mono<ResponseEntity<UsageRecordDTO>> create(
            @Parameter(description = "Unique identifier of the agreement", required = true)
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @Parameter(description = "Unique identifier of the asset", required = true)
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @Parameter(description = "Usage record data to create", required = true)
            @Valid @RequestBody UsageRecordDTO dto) {

        return service.create(assetFinanceAgreementId, assetFinanceAssetId, dto)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{recordId}")
    @Operation(
            summary = "Get a usage record by ID",
            description = "Retrieve a specific usage record by its unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usage record found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UsageRecordDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Usage record, asset, or agreement not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Mono<ResponseEntity<UsageRecordDTO>> getById(
            @Parameter(description = "Unique identifier of the agreement", required = true)
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @Parameter(description = "Unique identifier of the asset", required = true)
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @Parameter(description = "Unique identifier of the usage record", required = true)
            @PathVariable("recordId") UUID usageRecordId) {

        return service.getById(assetFinanceAgreementId, assetFinanceAssetId, usageRecordId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{recordId}")
    @Operation(
            summary = "Update an existing usage record",
            description = "Update all fields of an existing usage record"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usage record updated successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UsageRecordDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usage record, asset, or agreement not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Mono<ResponseEntity<UsageRecordDTO>> update(
            @Parameter(description = "Unique identifier of the agreement", required = true)
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @Parameter(description = "Unique identifier of the asset", required = true)
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @Parameter(description = "Unique identifier of the usage record to update", required = true)
            @PathVariable("recordId") UUID usageRecordId,
            @Parameter(description = "Updated usage record data", required = true)
            @Valid @RequestBody UsageRecordDTO dto) {

        return service.update(assetFinanceAgreementId, assetFinanceAssetId, usageRecordId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{recordId}")
    @Operation(
            summary = "Delete a usage record",
            description = "Permanently delete a usage record"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usage record deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usage record, asset, or agreement not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Mono<ResponseEntity<Void>> delete(
            @Parameter(description = "Unique identifier of the agreement", required = true)
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @Parameter(description = "Unique identifier of the asset", required = true)
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @Parameter(description = "Unique identifier of the usage record to delete", required = true)
            @PathVariable("recordId") UUID usageRecordId) {

        return service.delete(assetFinanceAgreementId, assetFinanceAssetId, usageRecordId)
                .thenReturn(ResponseEntity.noContent().build());
    }
}
