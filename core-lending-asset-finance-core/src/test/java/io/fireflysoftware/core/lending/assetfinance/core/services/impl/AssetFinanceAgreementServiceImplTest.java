/*
 * Copyright 2026 Firefly Software Foundation
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

import io.fireflysoftware.core.lending.assetfinance.core.mappers.AssetFinanceAgreementMapper;
import io.fireflysoftware.core.lending.assetfinance.interfaces.dtos.AssetFinanceAgreementDTO;
import io.fireflysoftware.core.lending.assetfinance.interfaces.enums.FinanceTypeEnum;
import io.fireflysoftware.core.lending.assetfinance.models.repositories.AssetFinanceAgreementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Product-kind rules for the one asset finance agreement.
 */
@ExtendWith(MockitoExtension.class)
class AssetFinanceAgreementServiceImplTest {

    @Mock
    private AssetFinanceAgreementRepository repository;

    @Mock
    private AssetFinanceAgreementMapper mapper;

    private AssetFinanceAgreementServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new AssetFinanceAgreementServiceImpl(repository, mapper);
    }

    @Test
    void create_rejectsRentingWithAPurchaseOption() {
        AssetFinanceAgreementDTO dto = AssetFinanceAgreementDTO.builder()
                .financeType(FinanceTypeEnum.RENTING)
                .purchaseOptionAvailable(true)
                .purchaseOptionPrice(new BigDecimal("1000.00"))
                .build();

        StepVerifier.create(service.create(dto))
                .expectErrorSatisfies(error -> assertThat(error)
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("operating rental"))
                .verify();
    }

    @Test
    void create_rejectsLeasingWithoutAPurchaseOption() {
        AssetFinanceAgreementDTO dto = AssetFinanceAgreementDTO.builder()
                .financeType(FinanceTypeEnum.LEASING)
                .purchaseOptionAvailable(false)
                .build();

        StepVerifier.create(service.create(dto))
                .expectErrorSatisfies(error -> assertThat(error)
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("purchase option"))
                .verify();
    }
}
