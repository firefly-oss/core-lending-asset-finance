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
import com.firefly.core.lending.assetfinance.core.mappers.AssetFinanceAgreementMapper;
import com.firefly.core.lending.assetfinance.core.services.AssetFinanceAgreementService;
import com.firefly.core.lending.assetfinance.interfaces.dtos.AssetFinanceAgreementDTO;
import com.firefly.core.lending.assetfinance.models.entities.AssetFinanceAgreement;
import com.firefly.core.lending.assetfinance.models.repositories.AssetFinanceAgreementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AssetFinanceAgreementServiceImpl implements AssetFinanceAgreementService {

    private final AssetFinanceAgreementRepository repository;
    private final AssetFinanceAgreementMapper mapper;

    @Override
    public Mono<PaginationResponse<AssetFinanceAgreementDTO>> findAll(FilterRequest<AssetFinanceAgreementDTO> filterRequest) {
        return FilterUtils.createFilter(
                AssetFinanceAgreement.class,
                mapper::toDTO
        ).filter(filterRequest);
    }

    @Override
    public Mono<AssetFinanceAgreementDTO> create(AssetFinanceAgreementDTO dto) {
        return Mono.just(dto)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<AssetFinanceAgreementDTO> getById(UUID assetFinanceAgreementId) {
        return repository.findById(assetFinanceAgreementId)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<AssetFinanceAgreementDTO> update(UUID assetFinanceAgreementId, AssetFinanceAgreementDTO dto) {
        return repository.findById(assetFinanceAgreementId)
                .flatMap(existingAgreement -> {
                    AssetFinanceAgreement updatedEntity = mapper.toEntity(dto);
                    updatedEntity.setAssetFinanceAgreementId(existingAgreement.getAssetFinanceAgreementId());
                    return repository.save(updatedEntity);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> delete(UUID assetFinanceAgreementId) {
        return repository.findById(assetFinanceAgreementId)
                .flatMap(repository::delete);
    }
}
