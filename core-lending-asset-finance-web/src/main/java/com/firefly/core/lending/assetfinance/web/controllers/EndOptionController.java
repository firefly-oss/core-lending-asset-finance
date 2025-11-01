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
import com.firefly.core.lending.assetfinance.core.services.EndOptionService;
import com.firefly.core.lending.assetfinance.interfaces.dtos.EndOptionDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    @Operation(summary = "List/Search end options for an agreement")
    public Mono<ResponseEntity<PaginationResponse<EndOptionDTO>>> findAll(
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @Valid @RequestBody FilterRequest<EndOptionDTO> filterRequest) {

        return service.findAll(assetFinanceAgreementId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create a new end option for an agreement")
    public Mono<ResponseEntity<EndOptionDTO>> create(
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @Valid @RequestBody EndOptionDTO dto) {

        return service.create(assetFinanceAgreementId, dto)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{optionId}")
    @Operation(summary = "Get an end option by ID")
    public Mono<ResponseEntity<EndOptionDTO>> getById(
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @PathVariable("optionId") UUID endOptionId) {

        return service.getById(assetFinanceAgreementId, endOptionId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{optionId}")
    @Operation(summary = "Update an existing end option")
    public Mono<ResponseEntity<EndOptionDTO>> update(
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @PathVariable("optionId") UUID endOptionId,
            @Valid @RequestBody EndOptionDTO dto) {

        return service.update(assetFinanceAgreementId, endOptionId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{optionId}")
    @Operation(summary = "Delete an end option")
    public Mono<ResponseEntity<Void>> delete(
            @PathVariable("agreementId") UUID assetFinanceAgreementId,
            @PathVariable("optionId") UUID endOptionId) {

        return service.delete(assetFinanceAgreementId, endOptionId)
                .thenReturn(ResponseEntity.noContent().build());
    }
}
