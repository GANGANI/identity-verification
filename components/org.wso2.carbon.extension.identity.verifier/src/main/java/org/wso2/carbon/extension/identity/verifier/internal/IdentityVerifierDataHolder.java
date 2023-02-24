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
package org.wso2.carbon.extension.identity.verifier.internal;

import org.wso2.carbon.extension.identity.verifier.IdentityVerifierFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Service holder class for Identity Verifier.
 */
public class IdentityVerifierDataHolder {

    private static final IdentityVerifierDataHolder instance = new IdentityVerifierDataHolder();
    private Map<String, IdentityVerifierFactory> identityVerifierFactoryMap;

    private IdentityVerifierDataHolder() {

    }

    public static IdentityVerifierDataHolder getInstance() {

        return instance;
    }

    public void setIdentityVerifierFactory(IdentityVerifierFactory identityVerifierFactory) {

        if (identityVerifierFactoryMap == null) {
            identityVerifierFactoryMap = new HashMap<>();
        }
        identityVerifierFactoryMap.put(identityVerifierFactory.getIdentityVerifierName(), identityVerifierFactory);
    }

    public IdentityVerifierFactory getIdentityVerifierFactory(String identityVerifierName) {

        if (identityVerifierFactoryMap == null) {
            throw new RuntimeException("IdentityVerifierFactory was not set during the " +
                    "IdentityVerifierServiceComponent startup");
        }
        return identityVerifierFactoryMap.get(identityVerifierName);
    }

    public void unbindIdentityVerifierFactory(IdentityVerifierFactory identityVerifierFactory) {

        identityVerifierFactoryMap.remove(identityVerifierFactory.getIdentityVerifierName());
    }
}
