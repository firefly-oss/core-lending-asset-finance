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
import com.firefly.core.lending.assetfinance.core.mappers.ReturnRecordMapper;
import com.firefly.core.lending.assetfinance.core.services.ReturnRecordService;
import com.firefly.core.lending.assetfinance.interfaces.dtos.ReturnRecordDTO;
import com.firefly.core.lending.assetfinance.models.entities.ReturnRecord;
import com.firefly.core.lending.assetfinance.models.repositories.ReturnRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ReturnRecordServiceImpl implements ReturnRecordService {

    private final ReturnRecordRepository repository;
    private final ReturnRecordMapper mapper;

    @Override
    public Mono<PaginationResponse<ReturnRecordDTO>> findAll(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, FilterRequest<ReturnRecordDTO> filterRequest) {
        return FilterUtils.createFilter(
                ReturnRecord.class,
                mapper::toDTO
        ).filter(filterRequest);
    }

    @Override
    public Mono<ReturnRecordDTO> create(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, ReturnRecordDTO dto) {
        return Mono.just(dto)
                .doOnNext(d -> d.setAssetFinanceAssetId(assetFinanceAssetId))
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<ReturnRecordDTO> getById(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, UUID returnRecordId) {
        return repository.findById(returnRecordId)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<ReturnRecordDTO> update(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, UUID returnRecordId, ReturnRecordDTO dto) {
        return repository.findById(returnRecordId)
                .flatMap(existing -> {
                    ReturnRecord updatedEntity = mapper.toEntity(dto);
                    updatedEntity.setReturnRecordId(existing.getReturnRecordId());
                    updatedEntity.setAssetFinanceAssetId(assetFinanceAssetId);
                    return repository.save(updatedEntity);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> delete(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, UUID returnRecordId) {
        return repository.findById(returnRecordId)
                .flatMap(repository::delete);
    }
}
