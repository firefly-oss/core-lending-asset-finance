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

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.filters.FilterUtils;
import org.fireflyframework.core.queries.PaginationResponse;
import com.firefly.core.lending.assetfinance.core.mappers.PickupRecordMapper;
import com.firefly.core.lending.assetfinance.core.services.PickupRecordService;
import com.firefly.core.lending.assetfinance.interfaces.dtos.PickupRecordDTO;
import com.firefly.core.lending.assetfinance.models.entities.PickupRecord;
import com.firefly.core.lending.assetfinance.models.repositories.PickupRecordRepository;
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
public class PickupRecordServiceImpl implements PickupRecordService {

    private final PickupRecordRepository repository;
    private final PickupRecordMapper mapper;

    @Override
    public Mono<PaginationResponse<PickupRecordDTO>> findAll(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, FilterRequest<PickupRecordDTO> filterRequest) {
        return FilterUtils.createFilter(
                PickupRecord.class,
                mapper::toDTO
        ).filter(filterRequest);
    }

    @Override
    public Mono<PickupRecordDTO> create(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, PickupRecordDTO dto) {
        return Mono.just(dto)
                .doOnNext(d -> d.setAssetFinanceAssetId(assetFinanceAssetId))
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<PickupRecordDTO> getById(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, UUID pickupRecordId) {
        return repository.findById(pickupRecordId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Pickup Record not found with id: " + pickupRecordId
                )))
                .map(mapper::toDTO);
    }

    @Override
    public Mono<PickupRecordDTO> update(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, UUID pickupRecordId, PickupRecordDTO dto) {
        return repository.findById(pickupRecordId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Pickup Record not found with id: " + pickupRecordId
                )))
                .flatMap(existingRecord -> {
                    PickupRecord updatedEntity = mapper.toEntity(dto);
                    updatedEntity.setPickupRecordId(existingRecord.getPickupRecordId());
                    updatedEntity.setAssetFinanceAssetId(assetFinanceAssetId);
                    return repository.save(updatedEntity);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> delete(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, UUID pickupRecordId) {
        return repository.findById(pickupRecordId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Pickup Record not found with id: " + pickupRecordId
                )))
                .flatMap(repository::delete);
    }
}

