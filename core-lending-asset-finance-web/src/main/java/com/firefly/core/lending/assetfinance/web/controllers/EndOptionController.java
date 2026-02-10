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
import com.firefly.core.lending.assetfinance.core.services.EndOptionService;
import com.firefly.core.lending.assetfinance.interfaces.dtos.EndOptionDTO;
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
@RequestMapping("/api/v1/asset-finance-agreements/{agreementId}/end-options")
@Tag(name = "EndOption", description = "Operations for Lease-End Purchase Options")
@RequiredArgsConstructor
public class EndOptionController {

    private final EndOptionService service;

    @GetMapping
    @Operation(
            summary = "List/Search end options for an agreement",
            description = "Retrieve a paginated list of lease-end purchase options for a specific agreement"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of end options",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PaginationResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid filter request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Agreement not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Mono<ResponseEntity<PaginationResponse<EndOptionDTO>>> findAll(
            @Parameter(description = "Unique identifier of the agreement", required = true)
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @Parameter(description = "Filter criteria for searching end options")
            @Valid @RequestBody FilterRequest<EndOptionDTO> filterRequest) {

        return service.findAll(assetFinanceAgreementId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(
            summary = "Create a new end option for an agreement",
            description = "Create a new lease-end purchase option for an agreement"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "End option created successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EndOptionDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Agreement not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Mono<ResponseEntity<EndOptionDTO>> create(
            @Parameter(description = "Unique identifier of the agreement", required = true)
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @Parameter(description = "End option data to create", required = true)
            @Valid @RequestBody EndOptionDTO dto) {

        return service.create(assetFinanceAgreementId, dto)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{optionId}")
    @Operation(
            summary = "Get an end option by ID",
            description = "Retrieve a specific lease-end purchase option by its unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "End option found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EndOptionDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "End option or agreement not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Mono<ResponseEntity<EndOptionDTO>> getById(
            @Parameter(description = "Unique identifier of the agreement", required = true)
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @Parameter(description = "Unique identifier of the end option", required = true)
            @PathVariable("optionId") UUID endOptionId) {

        return service.getById(assetFinanceAgreementId, endOptionId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{optionId}")
    @Operation(
            summary = "Update an existing end option",
            description = "Update all fields of an existing lease-end purchase option"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "End option updated successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EndOptionDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "End option or agreement not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Mono<ResponseEntity<EndOptionDTO>> update(
            @Parameter(description = "Unique identifier of the agreement", required = true)
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @Parameter(description = "Unique identifier of the end option to update", required = true)
            @PathVariable("optionId") UUID endOptionId,
            @Parameter(description = "Updated end option data", required = true)
            @Valid @RequestBody EndOptionDTO dto) {

        return service.update(assetFinanceAgreementId, endOptionId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{optionId}")
    @Operation(
            summary = "Delete an end option",
            description = "Permanently delete a lease-end purchase option"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "End option deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "End option or agreement not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Mono<ResponseEntity<Void>> delete(
            @Parameter(description = "Unique identifier of the agreement", required = true)
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @Parameter(description = "Unique identifier of the end option to delete", required = true)
            @PathVariable("optionId") UUID endOptionId) {

        return service.delete(assetFinanceAgreementId, endOptionId)
                .thenReturn(ResponseEntity.noContent().build());
    }
}
