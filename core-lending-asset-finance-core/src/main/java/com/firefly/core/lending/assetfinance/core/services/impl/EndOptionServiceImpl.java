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
import com.firefly.core.lending.assetfinance.core.mappers.EndOptionMapper;
import com.firefly.core.lending.assetfinance.core.services.EndOptionService;
import com.firefly.core.lending.assetfinance.interfaces.dtos.option.v1.EndOptionDTO;
import com.firefly.core.lending.assetfinance.models.entities.EndOption;
import com.firefly.core.lending.assetfinance.models.repositories.EndOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class EndOptionServiceImpl implements EndOptionService {

    private final EndOptionRepository repository;
    private final EndOptionMapper mapper;

    @Override
    public Mono<PaginationResponse<EndOptionDTO>> findAll(UUID assetFinanceAgreementId, FilterRequest<EndOptionDTO> filterRequest) {
        return FilterUtils.createFilter(
                EndOption.class,
                mapper::toDTO
        ).filter(filterRequest);
    }

    @Override
    public Mono<EndOptionDTO> create(UUID assetFinanceAgreementId, EndOptionDTO dto) {
        return Mono.just(dto)
                .doOnNext(d -> d.setAssetFinanceAgreementId(assetFinanceAgreementId))
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<EndOptionDTO> getById(UUID assetFinanceAgreementId, UUID endOptionId) {
        return repository.findById(endOptionId)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<EndOptionDTO> update(UUID assetFinanceAgreementId, UUID endOptionId, EndOptionDTO dto) {
        return repository.findById(endOptionId)
                .flatMap(existing -> {
                    EndOption updatedEntity = mapper.toEntity(dto);
                    updatedEntity.setEndOptionId(existing.getEndOptionId());
                    updatedEntity.setAssetFinanceAgreementId(assetFinanceAgreementId);
                    return repository.save(updatedEntity);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> delete(UUID assetFinanceAgreementId, UUID endOptionId) {
        return repository.findById(endOptionId)
                .flatMap(repository::delete);
    }
}
