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
import com.firefly.core.lending.assetfinance.interfaces.dtos.usage.v1.UsageRecordDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UsageRecordService {

    Mono<PaginationResponse<UsageRecordDTO>> findAll(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, FilterRequest<UsageRecordDTO> filterRequest);

    Mono<UsageRecordDTO> create(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, UsageRecordDTO dto);

    Mono<UsageRecordDTO> getById(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, UUID usageRecordId);

    Mono<UsageRecordDTO> update(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, UUID usageRecordId, UsageRecordDTO dto);

    Mono<Void> delete(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, UUID usageRecordId);
}
