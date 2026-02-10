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
import com.firefly.core.lending.assetfinance.core.services.ReturnRecordService;
import com.firefly.core.lending.assetfinance.interfaces.dtos.ReturnRecordDTO;
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
@RequestMapping("/api/v1/asset-finance-agreements/{agreementId}/assets/{assetId}/return-records")
@Tag(name = "ReturnRecord", description = "Operations for Asset Return Records")
@RequiredArgsConstructor
public class ReturnRecordController {

    private final ReturnRecordService service;

    @GetMapping
    @Operation(
            summary = "List/Search return records for an asset",
            description = "Retrieve a paginated list of return/condition assessment records for a specific asset"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of return records",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PaginationResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid filter request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Asset or agreement not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Mono<ResponseEntity<PaginationResponse<ReturnRecordDTO>>> findAll(
            @Parameter(description = "Unique identifier of the agreement", required = true)
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @Parameter(description = "Unique identifier of the asset", required = true)
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @Parameter(description = "Filter criteria for searching return records")
            @Valid @RequestBody FilterRequest<ReturnRecordDTO> filterRequest) {

        return service.findAll(assetFinanceAgreementId, assetFinanceAssetId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(
            summary = "Create a new return record for an asset",
            description = "Create a new return record to document asset condition assessment upon return"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Return record created successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ReturnRecordDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Asset or agreement not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Mono<ResponseEntity<ReturnRecordDTO>> create(
            @Parameter(description = "Unique identifier of the agreement", required = true)
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @Parameter(description = "Unique identifier of the asset", required = true)
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @Parameter(description = "Return record data to create", required = true)
            @Valid @RequestBody ReturnRecordDTO dto) {

        return service.create(assetFinanceAgreementId, assetFinanceAssetId, dto)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{recordId}")
    @Operation(
            summary = "Get a return record by ID",
            description = "Retrieve a specific return record by its unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Return record found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ReturnRecordDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Return record, asset, or agreement not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Mono<ResponseEntity<ReturnRecordDTO>> getById(
            @Parameter(description = "Unique identifier of the agreement", required = true)
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @Parameter(description = "Unique identifier of the asset", required = true)
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @Parameter(description = "Unique identifier of the return record", required = true)
            @PathVariable("recordId") UUID returnRecordId) {

        return service.getById(assetFinanceAgreementId, assetFinanceAssetId, returnRecordId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{recordId}")
    @Operation(
            summary = "Update an existing return record",
            description = "Update all fields of an existing return record"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Return record updated successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ReturnRecordDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Return record, asset, or agreement not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Mono<ResponseEntity<ReturnRecordDTO>> update(
            @Parameter(description = "Unique identifier of the agreement", required = true)
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @Parameter(description = "Unique identifier of the asset", required = true)
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @Parameter(description = "Unique identifier of the return record to update", required = true)
            @PathVariable("recordId") UUID returnRecordId,
            @Parameter(description = "Updated return record data", required = true)
            @Valid @RequestBody ReturnRecordDTO dto) {

        return service.update(assetFinanceAgreementId, assetFinanceAssetId, returnRecordId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{recordId}")
    @Operation(
            summary = "Delete a return record",
            description = "Permanently delete a return record"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Return record deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Return record, asset, or agreement not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Mono<ResponseEntity<Void>> delete(
            @Parameter(description = "Unique identifier of the agreement", required = true)
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @Parameter(description = "Unique identifier of the asset", required = true)
            @PathVariable("assetId") UUID assetFinanceAssetId,
            @Parameter(description = "Unique identifier of the return record to delete", required = true)
            @PathVariable("recordId") UUID returnRecordId) {

        return service.delete(assetFinanceAgreementId, assetFinanceAssetId, returnRecordId)
                .thenReturn(ResponseEntity.noContent().build());
    }
}
