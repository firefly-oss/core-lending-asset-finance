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
import com.firefly.core.lending.assetfinance.core.mappers.ServiceEventMapper;
import com.firefly.core.lending.assetfinance.core.services.ServiceEventService;
import com.firefly.core.lending.assetfinance.interfaces.dtos.event.v1.ServiceEventDTO;
import com.firefly.core.lending.assetfinance.models.entities.ServiceEvent;
import com.firefly.core.lending.assetfinance.models.repositories.ServiceEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ServiceEventServiceImpl implements ServiceEventService {

    private final ServiceEventRepository repository;
    private final ServiceEventMapper mapper;

    @Override
    public Mono<PaginationResponse<ServiceEventDTO>> findAll(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, FilterRequest<ServiceEventDTO> filterRequest) {
        return FilterUtils.createFilter(
                ServiceEvent.class,
                mapper::toDTO
        ).filter(filterRequest);
    }

    @Override
    public Mono<ServiceEventDTO> create(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, ServiceEventDTO dto) {
        return Mono.just(dto)
                .doOnNext(d -> d.setAssetFinanceAssetId(assetFinanceAssetId))
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<ServiceEventDTO> getById(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, UUID serviceEventId) {
        return repository.findById(serviceEventId)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<ServiceEventDTO> update(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, UUID serviceEventId, ServiceEventDTO dto) {
        return repository.findById(serviceEventId)
                .flatMap(existing -> {
                    ServiceEvent updatedEntity = mapper.toEntity(dto);
                    updatedEntity.setServiceEventId(existing.getServiceEventId());
                    updatedEntity.setAssetFinanceAssetId(assetFinanceAssetId);
                    return repository.save(updatedEntity);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> delete(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, UUID serviceEventId) {
        return repository.findById(serviceEventId)
                .flatMap(repository::delete);
    }
}
