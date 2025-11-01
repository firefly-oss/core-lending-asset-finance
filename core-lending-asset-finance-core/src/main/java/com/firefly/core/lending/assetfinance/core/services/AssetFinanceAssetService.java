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
import com.firefly.core.lending.assetfinance.interfaces.dtos.assets.v1.AssetFinanceAssetDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface AssetFinanceAssetService {

    Mono<PaginationResponse<AssetFinanceAssetDTO>> findAll(UUID assetFinanceAgreementId, FilterRequest<AssetFinanceAssetDTO> filterRequest);

    Mono<AssetFinanceAssetDTO> create(UUID assetFinanceAgreementId, AssetFinanceAssetDTO dto);

    Mono<AssetFinanceAssetDTO> getById(UUID assetFinanceAgreementId, UUID assetFinanceAssetId);

    Mono<AssetFinanceAssetDTO> update(UUID assetFinanceAgreementId, UUID assetFinanceAssetId, AssetFinanceAssetDTO dto);

    Mono<Void> delete(UUID assetFinanceAgreementId, UUID assetFinanceAssetId);
}
