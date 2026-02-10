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
import com.firefly.core.lending.assetfinance.core.services.AssetFinanceAgreementService;
import com.firefly.core.lending.assetfinance.interfaces.dtos.AssetFinanceAgreementDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    @Operation(
            summary = "List/Search asset finance agreements",
            description = "Retrieve a paginated list of asset finance agreements with optional filtering and sorting"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of agreements",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PaginationResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid filter request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Mono<ResponseEntity<PaginationResponse<AssetFinanceAgreementDTO>>> findAll(
            @Parameter(description = "Filter criteria for searching agreements")
            @Valid @RequestBody FilterRequest<AssetFinanceAgreementDTO> filterRequest) {

        return service.findAll(filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(
            summary = "Create a new asset finance agreement",
            description = "Create a new asset finance agreement (leasing or renting)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Agreement created successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AssetFinanceAgreementDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Mono<ResponseEntity<AssetFinanceAgreementDTO>> create(
            @Parameter(description = "Agreement data to create", required = true)
            @Valid @RequestBody AssetFinanceAgreementDTO dto) {

        return service.create(dto)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{assetFinanceAgreementId}")
    @Operation(
            summary = "Get an asset finance agreement by ID",
            description = "Retrieve a specific asset finance agreement by its unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Agreement found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AssetFinanceAgreementDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Agreement not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Mono<ResponseEntity<AssetFinanceAgreementDTO>> getById(
            @Parameter(description = "Unique identifier of the agreement", required = true)
            @PathVariable("assetFinanceAgreementId") UUID assetFinanceAgreementId) {

        return service.getById(assetFinanceAgreementId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{assetFinanceAgreementId}")
    @Operation(
            summary = "Update an existing asset finance agreement",
            description = "Update all fields of an existing asset finance agreement"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Agreement updated successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AssetFinanceAgreementDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Agreement not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Mono<ResponseEntity<AssetFinanceAgreementDTO>> update(
            @Parameter(description = "Unique identifier of the agreement to update", required = true)
            @PathVariable("assetFinanceAgreementId") UUID assetFinanceAgreementId,
            @Parameter(description = "Updated agreement data", required = true)
            @Valid @RequestBody AssetFinanceAgreementDTO dto) {

        return service.update(assetFinanceAgreementId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{assetFinanceAgreementId}")
    @Operation(
            summary = "Delete an asset finance agreement",
            description = "Permanently delete an asset finance agreement and all related data (cascading delete)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Agreement deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Agreement not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Mono<ResponseEntity<Void>> delete(
            @Parameter(description = "Unique identifier of the agreement to delete", required = true)
            @PathVariable("assetFinanceAgreementId") UUID assetFinanceAgreementId) {

        return service.delete(assetFinanceAgreementId)
                .thenReturn(ResponseEntity.noContent().build());
    }
}
