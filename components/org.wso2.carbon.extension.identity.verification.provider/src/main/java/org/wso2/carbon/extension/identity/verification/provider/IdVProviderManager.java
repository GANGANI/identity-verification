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
package org.wso2.carbon.extension.identity.verification.provider;

import org.wso2.carbon.extension.identity.verification.provider.model.IdentityVerificationProvider;

import java.util.List;

public interface IdVProviderManager {

    IdentityVerificationProvider addIdVProvider(IdentityVerificationProvider identityVerificationProvider)
            throws IdVProviderMgtException;

    IdentityVerificationProvider getIdVProvider(String idVProviderId) throws IdVProviderMgtException;

    void deleteIdVProvider(String idVProviderId) throws IdVProviderMgtException;

    IdentityVerificationProvider updateIdVProvider(IdentityVerificationProvider identityVerificationProvider) throws IdVProviderMgtException;

    List<IdentityVerificationProvider> getIdVProviders(String tenantDomain) throws IdVProviderMgtException;
}
