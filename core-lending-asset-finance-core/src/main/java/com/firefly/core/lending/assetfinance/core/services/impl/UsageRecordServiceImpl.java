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
import com.firefly.core.lending.assetfinance.core.mappers.UsageRecordMapper;
import com.firefly.core.lending.assetfinance.core.services.UsageRecordService;
import com.firefly.core.lending.assetfinance.interfaces.dtos.usage.v1.UsageRecordDTO;
import com.firefly.core.lending.assetfinance.models.entities.UsageRecord;
import com.firefly.core.lending.assetfinance.models.repositories.UsageRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UsageRecordServiceImpl implements UsageRecordService {

    private final UsageRecordRepository repository;
    private final UsageRecordMapper mapper;

    @Override
    public Mono<PaginationResponse<UsageRecordDTO>> findAll(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, FilterRequest<UsageRecordDTO> filterRequest) {
        return FilterUtils.createFilter(
                UsageRecord.class,
                mapper::toDTO
        ).filter(filterRequest);
    }

    @Override
    public Mono<UsageRecordDTO> create(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, UsageRecordDTO dto) {
        return Mono.just(dto)
                .doOnNext(d -> d.setAssetFinanceAssetId(assetFinanceAssetId))
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<UsageRecordDTO> getById(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, UUID usageRecordId) {
        return repository.findById(usageRecordId)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<UsageRecordDTO> update(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, UUID usageRecordId, UsageRecordDTO dto) {
        return repository.findById(usageRecordId)
                .flatMap(existing -> {
                    UsageRecord updatedEntity = mapper.toEntity(dto);
                    updatedEntity.setUsageRecordId(existing.getUsageRecordId());
                    updatedEntity.setAssetFinanceAssetId(assetFinanceAssetId);
                    return repository.save(updatedEntity);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> delete(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, UUID usageRecordId) {
        return repository.findById(usageRecordId)
                .flatMap(repository::delete);
    }
}
