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
import com.firefly.core.lending.assetfinance.core.mappers.DeliveryRecordMapper;
import com.firefly.core.lending.assetfinance.core.services.DeliveryRecordService;
import com.firefly.core.lending.assetfinance.interfaces.dtos.DeliveryRecordDTO;
import com.firefly.core.lending.assetfinance.models.entities.DeliveryRecord;
import com.firefly.core.lending.assetfinance.models.repositories.DeliveryRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryRecordServiceImpl implements DeliveryRecordService {

    private final DeliveryRecordRepository repository;
    private final DeliveryRecordMapper mapper;

    @Override
    public Mono<PaginationResponse<DeliveryRecordDTO>> findAll(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, FilterRequest<DeliveryRecordDTO> filterRequest) {
        return FilterUtils.createFilter(
                DeliveryRecord.class,
                mapper::toDTO
        ).filter(filterRequest);
    }

    @Override
    public Mono<DeliveryRecordDTO> create(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, DeliveryRecordDTO dto) {
        return Mono.just(dto)
                .doOnNext(d -> d.setAssetFinanceAssetId(assetFinanceAssetId))
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<DeliveryRecordDTO> getById(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, UUID deliveryRecordId) {
        return repository.findById(deliveryRecordId)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<DeliveryRecordDTO> update(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, UUID deliveryRecordId, DeliveryRecordDTO dto) {
        return repository.findById(deliveryRecordId)
                .flatMap(existingRecord -> {
                    DeliveryRecord updatedEntity = mapper.toEntity(dto);
                    updatedEntity.setDeliveryRecordId(existingRecord.getDeliveryRecordId());
                    updatedEntity.setAssetFinanceAssetId(assetFinanceAssetId);
                    return repository.save(updatedEntity);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> delete(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, UUID deliveryRecordId) {
        return repository.findById(deliveryRecordId)
                .flatMap(repository::delete);
    }
}

