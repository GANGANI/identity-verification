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
package org.wso2.carbon.extension.identity.verification.provider.internal;

import org.wso2.carbon.extension.identity.verification.provider.model.IdentityVerificationProvider;
import org.wso2.carbon.identity.application.common.model.IdentityProvider;
import org.wso2.carbon.identity.secret.mgt.core.SecretsProcessor;
import org.wso2.carbon.user.core.service.RealmService;

/**
 * Service holder class for IdV Provider Mgt.
 */
public class IdVProviderMgtDataHolder {

    public static IdVProviderMgtDataHolder instance = new IdVProviderMgtDataHolder();

    public static IdVProviderMgtDataHolder getInstance() {

        return instance;
    }
    private RealmService realmService;
    private SecretsProcessor<IdentityVerificationProvider> idVPSecretsProcessorService;

    /**
     * Get the RealmService.
     *
     * @return RealmService.
     */
    public RealmService getRealmService() {

        return realmService;
    }

    /**
     * Set the RealmService.
     *
     * @param realmService RealmService.
     */
    public void setRealmService(RealmService realmService) {

        this.realmService = realmService;
    }

    public SecretsProcessor<IdentityVerificationProvider> getIdVPSecretsProcessorService() {

        return idVPSecretsProcessorService;
    }

    public void setIdVPSecretsProcessorService(SecretsProcessor<IdentityVerificationProvider>
                                                      idVPSecretsProcessorService) {

        this.idVPSecretsProcessorService = idVPSecretsProcessorService;
    }
}
