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


package com.firefly.core.lending.assetfinance.core.services.impl;

import com.firefly.common.core.filters.FilterRequest;
import com.firefly.common.core.filters.FilterUtils;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.core.lending.assetfinance.core.mappers.AssetFinanceAssetMapper;
import com.firefly.core.lending.assetfinance.core.services.AssetFinanceAssetService;
import com.firefly.core.lending.assetfinance.interfaces.dtos.AssetFinanceAssetDTO;
import com.firefly.core.lending.assetfinance.models.entities.AssetFinanceAsset;
import com.firefly.core.lending.assetfinance.models.repositories.AssetFinanceAssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AssetFinanceAssetServiceImpl implements AssetFinanceAssetService {

    private final AssetFinanceAssetRepository repository;
    private final AssetFinanceAssetMapper mapper;

    @Override
    public Mono<PaginationResponse<AssetFinanceAssetDTO>> findAll(UUID assetFinanceAgreementId, FilterRequest<AssetFinanceAssetDTO> filterRequest) {
        return FilterUtils.createFilter(
                AssetFinanceAsset.class,
                mapper::toDTO
        ).filter(filterRequest);
    }

    @Override
    public Mono<AssetFinanceAssetDTO> create(UUID assetFinanceAgreementId, AssetFinanceAssetDTO dto) {
        return Mono.just(dto)
                .doOnNext(d -> d.setAssetFinanceAgreementId(assetFinanceAgreementId))
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<AssetFinanceAssetDTO> getById(UUID assetFinanceAgreementId, UUID assetFinanceAssetId) {
        return repository.findById(assetFinanceAssetId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Asset Finance Asset not found with id: " + assetFinanceAssetId
                )))
                .map(mapper::toDTO);
    }

    @Override
    public Mono<AssetFinanceAssetDTO> update(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, AssetFinanceAssetDTO dto) {
        return repository.findById(assetFinanceAssetId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Asset Finance Asset not found with id: " + assetFinanceAssetId
                )))
                .flatMap(existing -> {
                    AssetFinanceAsset updatedEntity = mapper.toEntity(dto);
                    updatedEntity.setAssetFinanceAssetId(existing.getAssetFinanceAssetId());
                    updatedEntity.setAssetFinanceAgreementId(assetFinanceAgreementId);
                    return repository.save(updatedEntity);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> delete(UUID assetFinanceAgreementId, UUID assetFinanceAssetId) {
        return repository.findById(assetFinanceAssetId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Asset Finance Asset not found with id: " + assetFinanceAssetId
                )))
                .flatMap(repository::delete);
    }
}
