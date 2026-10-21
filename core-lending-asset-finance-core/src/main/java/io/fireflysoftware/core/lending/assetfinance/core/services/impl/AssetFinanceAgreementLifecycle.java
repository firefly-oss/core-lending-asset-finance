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

import io.fireflysoftware.core.lending.assetfinance.interfaces.enums.AgreementStatusEnum;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * Allowed status moves for an asset finance agreement. Closed and terminated agreements stay on record.
 */
final class AssetFinanceAgreementLifecycle {

    private static final Map<AgreementStatusEnum, Set<AgreementStatusEnum>> ALLOWED = Map.of(
            AgreementStatusEnum.ACTIVE, EnumSet.of(
                    AgreementStatusEnum.SUSPENDED, AgreementStatusEnum.CLOSED,
                    AgreementStatusEnum.TERMINATED, AgreementStatusEnum.DEFAULTED),
            AgreementStatusEnum.SUSPENDED, EnumSet.of(
                    AgreementStatusEnum.ACTIVE, AgreementStatusEnum.CLOSED,
                    AgreementStatusEnum.TERMINATED, AgreementStatusEnum.DEFAULTED),
            AgreementStatusEnum.DEFAULTED, EnumSet.of(
                    AgreementStatusEnum.CLOSED, AgreementStatusEnum.TERMINATED)
    );

    private AssetFinanceAgreementLifecycle() {
    }

    static boolean canTransition(AgreementStatusEnum from, AgreementStatusEnum to) {
        if (from == to) {
            return true;
        }
        if (from == null || to == null) {
            return false;
        }
        Set<AgreementStatusEnum> next = ALLOWED.get(from);
        return next != null && next.contains(to);
    }
}
