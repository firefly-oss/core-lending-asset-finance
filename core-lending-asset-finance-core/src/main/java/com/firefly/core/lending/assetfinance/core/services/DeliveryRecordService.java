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


package com.firefly.core.lending.assetfinance.core.services;

import com.firefly.common.core.filters.FilterRequest;
import com.firefly.common.core.queries.PaginationResponse;
import com.firefly.core.lending.assetfinance.interfaces.dtos.DeliveryRecordDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface DeliveryRecordService {

    Mono<PaginationResponse<DeliveryRecordDTO>> findAll(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, FilterRequest<DeliveryRecordDTO> filterRequest);

    Mono<DeliveryRecordDTO> create(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, DeliveryRecordDTO dto);

    Mono<DeliveryRecordDTO> getById(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, UUID deliveryRecordId);

    Mono<DeliveryRecordDTO> update(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, UUID deliveryRecordId, DeliveryRecordDTO dto);

    Mono<Void> delete(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, UUID deliveryRecordId);
}

