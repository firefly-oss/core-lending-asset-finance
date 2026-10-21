/*
 * Copyright 2025 Firefly Software Foundation
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


package io.fireflysoftware.core.lending.assetfinance.core.services.impl;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.filters.FilterUtils;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.lending.assetfinance.core.mappers.AssetFinanceAgreementMapper;
import io.fireflysoftware.core.lending.assetfinance.core.services.AssetFinanceAgreementService;
import io.fireflysoftware.core.lending.assetfinance.interfaces.dtos.AssetFinanceAgreementDTO;
import io.fireflysoftware.core.lending.assetfinance.interfaces.enums.AgreementStatusEnum;
import io.fireflysoftware.core.lending.assetfinance.interfaces.enums.FinanceTypeEnum;
import io.fireflysoftware.core.lending.assetfinance.models.entities.AssetFinanceAgreement;
import io.fireflysoftware.core.lending.assetfinance.models.repositories.AssetFinanceAgreementRepository;
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
        return validateFinanceType(dto)
                .then(Mono.just(dto))
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<AssetFinanceAgreementDTO> getById(UUID assetFinanceAgreementId) {
        return repository.findById(assetFinanceAgreementId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Asset Finance Agreement not found with id: " + assetFinanceAgreementId
                )))
                .map(mapper::toDTO);
    }

    @Override
    public Mono<AssetFinanceAgreementDTO> update(UUID assetFinanceAgreementId, AssetFinanceAgreementDTO dto) {
        return repository.findById(assetFinanceAgreementId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Asset Finance Agreement not found with id: " + assetFinanceAgreementId
                )))
                .flatMap(existingAgreement -> {
                    if (dto.getFinanceType() == null) {
                        dto.setFinanceType(existingAgreement.getFinanceType());
                    }
                    return validateFinanceType(dto).thenReturn(existingAgreement);
                })
                .flatMap(existingAgreement -> {
                    AssetFinanceAgreement updatedEntity = mapper.toEntity(dto);
                    updatedEntity.setAssetFinanceAgreementId(existingAgreement.getAssetFinanceAgreementId());
                    AgreementStatusEnum current = existingAgreement.getAgreementStatus();
                    if (updatedEntity.getAgreementStatus() == null) {
                        updatedEntity.setAgreementStatus(current);
                    }
                    if (!AssetFinanceAgreementLifecycle.canTransition(current, updatedEntity.getAgreementStatus())) {
                        return Mono.error(new IllegalStateException(
                                "Agreement " + assetFinanceAgreementId + " cannot move from " + current
                                        + " to " + updatedEntity.getAgreementStatus()));
                    }
                    return repository.save(updatedEntity);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> delete(UUID assetFinanceAgreementId) {
        return repository.findById(assetFinanceAgreementId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Asset Finance Agreement not found with id: " + assetFinanceAgreementId
                )))
                .flatMap(repository::delete);
    }

    /**
     * LEASING is a financial lease and must carry a purchase option.
     * RENTING is an operating rental and must not carry one.
     */
    private Mono<Void> validateFinanceType(AssetFinanceAgreementDTO dto) {
        if (dto.getFinanceType() == null) {
            return Mono.error(new IllegalArgumentException("Finance type is required"));
        }
        if (dto.getFinanceType() == FinanceTypeEnum.RENTING) {
            if (Boolean.TRUE.equals(dto.getPurchaseOptionAvailable()) || dto.getPurchaseOptionPrice() != null) {
                return Mono.error(new IllegalArgumentException(
                        "Renting is an operating rental and cannot include a purchase option"));
            }
            return Mono.empty();
        }
        if (dto.getFinanceType() == FinanceTypeEnum.LEASING) {
            if (!Boolean.TRUE.equals(dto.getPurchaseOptionAvailable()) || dto.getPurchaseOptionPrice() == null) {
                return Mono.error(new IllegalArgumentException(
                        "Leasing requires a purchase option and a purchase option price"));
            }
            return Mono.empty();
        }
        return Mono.error(new IllegalArgumentException("Unsupported finance type: " + dto.getFinanceType()));
    }
}
