/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.extension.identity.verification.api.rest.common;

import org.wso2.carbon.extension.identity.verification.claim.mgt.IdVClaimManager;
import org.wso2.carbon.extension.identity.verification.provider.IdentityVerificationService;
import org.wso2.carbon.extension.identity.verifier.IdentityVerifierFactory;

/**
 * Service holder class for identity providers.
 */
public class IdVProviderServiceHolder {

    private static IdentityVerificationService idVProviderManager;
    private static IdVClaimManager idVClaimManager;
    private static IdentityVerifierFactory identityVerifierFactory;

    /**
     * Get IdVProviderManager osgi service.
     *
     * @return IdVProviderManager
     */
    public static IdentityVerificationService getIdVProviderManager() {

        return idVProviderManager;
    }

    /**
     * Set IdVProviderManager osgi service.
     *
     * @param idVProviderManager IdVProviderManager.
     */
    public static void setIdVProviderManager(IdentityVerificationService idVProviderManager) {

        IdVProviderServiceHolder.idVProviderManager = idVProviderManager;
    }

    public static IdVClaimManager getIdVClaimManager() {

        return idVClaimManager;
    }

    public static void setIdVClaimManager(IdVClaimManager idVClaimManager) {

        IdVProviderServiceHolder.idVClaimManager = idVClaimManager;
    }

    public static void setIdentityVerifierFactory(IdentityVerifierFactory identityVerifierFactory) {

        IdVProviderServiceHolder.identityVerifierFactory = identityVerifierFactory;
    }

    public static IdentityVerifierFactory getIdentityVerifierFactory () {

        return identityVerifierFactory;
    }
}
