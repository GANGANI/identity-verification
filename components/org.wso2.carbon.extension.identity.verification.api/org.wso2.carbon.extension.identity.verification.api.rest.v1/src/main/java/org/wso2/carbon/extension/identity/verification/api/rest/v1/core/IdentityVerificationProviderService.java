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

package org.wso2.carbon.extension.identity.verification.api.rest.v1.core;

import org.apache.commons.collections.CollectionUtils;
import org.wso2.carbon.extension.identity.verification.api.rest.common.Constants;
import org.wso2.carbon.extension.identity.verification.api.rest.common.IdentityVerificationServiceHolder;
import org.wso2.carbon.extension.identity.verification.api.rest.v1.model.ConfigProperty;
import org.wso2.carbon.extension.identity.verification.api.rest.v1.model.IdVProviderListResponse;
import org.wso2.carbon.extension.identity.verification.api.rest.v1.model.IdVProviderRequest;
import org.wso2.carbon.extension.identity.verification.api.rest.v1.model.IdVProviderResponse;
import org.wso2.carbon.extension.identity.verification.api.rest.v1.model.Verificationclaim;
import org.wso2.carbon.extension.identity.verification.provider.IdVProviderMgtException;
import org.wso2.carbon.extension.identity.verification.provider.model.IdVConfigProperty;
import org.wso2.carbon.extension.identity.verification.provider.model.IdentityVerificationProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.extension.identity.verification.api.rest.v1.core.IdentityVerificationUtils.getTenantId;
import static org.wso2.carbon.extension.identity.verification.api.rest.v1.core.IdentityVerificationUtils.handleException;

/**
 * Service class for identity verification providers.
 */
public class IdentityVerificationProviderService {

    /**
     * Add an identity verification provider.
     *
     * @param idVProviderRequest Identity verification provider request.
     * @return Identity verification providers.
     */
    public IdVProviderResponse addIdVProvider(IdVProviderRequest idVProviderRequest) {

        IdentityVerificationProvider identityVerificationProvider;
        int tenantId = getTenantId();
        try {
            identityVerificationProvider = IdentityVerificationServiceHolder.getIdVProviderManager().
                    addIdVProvider(createIdVProvider(idVProviderRequest), tenantId);
        } catch (IdVProviderMgtException e) {
            throw IdentityVerificationUtils.handleException(e,
                    Constants.ErrorMessage.ERROR_ADDING_IDVP, null);
        }
        return getIdVProviderResponse(identityVerificationProvider, true);
    }

    /**
     * Update identity verification provider.
     *
     * @param idVProviderId      Identity verification provider id.
     * @param idVProviderRequest Identity verification provider request.
     * @return Identity verification provider response.
     */
    public IdVProviderResponse updateIdVProvider(String idVProviderId, IdVProviderRequest idVProviderRequest) {

        IdentityVerificationProvider oldIdVProvider;
        IdentityVerificationProvider newIdVProvider;
        int tenantId = getTenantId();
        try {
            oldIdVProvider =
                    IdentityVerificationServiceHolder.getIdVProviderManager().getIdVProvider(idVProviderId, tenantId);

            if (oldIdVProvider == null) {
                throw handleException(Response.Status.NOT_FOUND,
                        Constants.ErrorMessage.ERROR_CODE_IDVP_NOT_FOUND, null);
            }
            IdentityVerificationProvider updatedIdVProvider =
                    createUpdatedIdVProvider(oldIdVProvider, idVProviderRequest);
            newIdVProvider = IdentityVerificationServiceHolder.getIdVProviderManager().
                    updateIdVProvider(oldIdVProvider, updatedIdVProvider, tenantId);
        } catch (IdVProviderMgtException e) {
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR,
                    Constants.ErrorMessage.ERROR_UPDATING_IDVP, null);
        }
        return getIdVProviderResponse(newIdVProvider, true);
    }

    /**
     * Get identity verification provider by id.
     *
     * @param idVProviderId Identity verification provider id.
     * @return Identity verification provider response.
     */
    public IdVProviderResponse getIdVProvider(String idVProviderId) {

        try {
            int tenantId = getTenantId();
            IdentityVerificationProvider identityVerificationProvider =
                    IdentityVerificationServiceHolder.getIdVProviderManager().getIdVProvider(idVProviderId, tenantId);
            if (identityVerificationProvider == null) {
                throw handleException(Response.Status.NOT_FOUND,
                        Constants.ErrorMessage.ERROR_CODE_IDVP_NOT_FOUND, null);
            }
            return getIdVProviderResponse(identityVerificationProvider, true);
        } catch (IdVProviderMgtException e) {
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR,
                    Constants.ErrorMessage.ERROR_RETRIEVING_IDVP, null);
        }
    }

    /**
     * Get all identity verification providers.
     *
     * @param limit  Limit per page.
     * @param offset Offset value.
     * @return Identity verification providers.
     */
    public IdVProviderListResponse getIdVProviders(Integer limit, Integer offset) {

        try {
            int tenantId = getTenantId();
            int totalResults = IdentityVerificationServiceHolder.getIdVProviderManager().
                    getCountOfIdVProviders(tenantId);
            List<IdentityVerificationProvider> identityVerificationProviders =
                    IdentityVerificationServiceHolder.getIdVProviderManager().getIdVProviders(limit, offset, tenantId);
            IdVProviderListResponse idVProviderListResponse = new IdVProviderListResponse();
            if (CollectionUtils.isNotEmpty(identityVerificationProviders)) {
                List<IdVProviderResponse> identityVerificationProvidersList = new ArrayList<>();
                for (IdentityVerificationProvider idVP : identityVerificationProviders) {
                    IdVProviderResponse idVPlistItem = getIdVProviderResponse(idVP, false);
                    identityVerificationProvidersList.add(idVPlistItem);
                }
                idVProviderListResponse.setIdentityVerificationProviders(identityVerificationProvidersList);
                idVProviderListResponse.setCount(identityVerificationProviders.size());
            } else {
                idVProviderListResponse.setCount(0);
            }
            offset = (offset == null) ? Integer.valueOf(0) : offset;
            idVProviderListResponse.setStartIndex(offset + 1);
            idVProviderListResponse.setTotalResults(totalResults);
            return idVProviderListResponse;
        } catch (IdVProviderMgtException e) {
            throw handleException(e,
                    Constants.ErrorMessage.ERROR_RETRIEVING_IDVP, null);
        }
    }

    /**
     * Delete identity verification provider by id.
     *
     * @param identityVerificationProviderId Identity verification provider id.
     */
    public void deleteIdVProvider(String identityVerificationProviderId) {

        int tenantId = getTenantId();
        try {
            IdentityVerificationServiceHolder.getIdVProviderManager().
                    deleteIdVProvider(identityVerificationProviderId, tenantId);
        } catch (IdVProviderMgtException e) {
            throw handleException(e,
                    Constants.ErrorMessage.ERROR_DELETING_IDVP, null);
        }
    }

    private List<Verificationclaim> getIdVClaimMappings(IdentityVerificationProvider identityVerificationProvider) {

        Map<String, String> claimMappings = identityVerificationProvider.getClaimMappings();
        if (claimMappings == null) {
            return null;
        }
        return claimMappings.entrySet().stream().map(entry -> {
            Verificationclaim verificationclaim = new Verificationclaim();
            verificationclaim.setLocalClaim(entry.getKey());
            verificationclaim.setIdvpClaim(entry.getValue());
            return verificationclaim;
        }).collect(Collectors.toList());
    }

    private IdVProviderResponse getIdVProviderResponse(IdentityVerificationProvider
                                                               identityVerificationProvider, boolean addProperties) {

        IdVProviderResponse idvProviderResponse = new IdVProviderResponse();
        idvProviderResponse.setId(identityVerificationProvider.getIdVPUUID());
        idvProviderResponse.setName(identityVerificationProvider.getIdVProviderName());
        idvProviderResponse.setDisplayName(identityVerificationProvider.getDisplayName());
        idvProviderResponse.setIsEnable(identityVerificationProvider.isEnable());
        idvProviderResponse.setDescription(identityVerificationProvider.getIdVProviderDescription());
        addIdVProviderProperties(idvProviderResponse, identityVerificationProvider);
        return idvProviderResponse;
    }

    private IdVProviderResponse addIdVProviderProperties(IdVProviderResponse idvProviderResponse,
                                                         IdentityVerificationProvider identityVerificationProvider) {

        List<ConfigProperty> configProperties =
                Arrays.stream(identityVerificationProvider.getIdVConfigProperties()).
                        map(propertyToExternal).collect(Collectors.toList());

        idvProviderResponse.setConfigProperties(configProperties);
        idvProviderResponse.setClaims(getIdVClaimMappings(identityVerificationProvider));
        return idvProviderResponse;
    }

    private IdentityVerificationProvider createIdVProvider(IdVProviderRequest idVProviderRequest) {

        IdentityVerificationProvider identityVerificationProvider = new IdentityVerificationProvider();
        identityVerificationProvider.setIdVPUUID(UUID.randomUUID().toString());
        identityVerificationProvider.setIdVProviderName(idVProviderRequest.getName());
        identityVerificationProvider.setDisplayName(idVProviderRequest.getDisplayName());
        identityVerificationProvider.setIdVProviderDescription(idVProviderRequest.getDescription());
        identityVerificationProvider.setEnable(idVProviderRequest.getIsEnable());
        identityVerificationProvider.setClaimMappings(getClaimMap(idVProviderRequest.getClaims()));
        List<ConfigProperty> properties = idVProviderRequest.getConfigProperties();

        identityVerificationProvider.setIdVConfigProperties(
                properties.stream().map(propertyToInternal).toArray(IdVConfigProperty[]::new));
        identityVerificationProvider.setEnable(true);
        return identityVerificationProvider;
    }

    private IdentityVerificationProvider createUpdatedIdVProvider(IdentityVerificationProvider oldIdVProvider,
                                                                  IdVProviderRequest idVProviderRequest) {

        IdentityVerificationProvider identityVerificationProvider = new IdentityVerificationProvider();
        identityVerificationProvider.setIdVPUUID(oldIdVProvider.getIdVPUUID());
        identityVerificationProvider.setIdVProviderName(idVProviderRequest.getName());
        identityVerificationProvider.setDisplayName(idVProviderRequest.getDisplayName());
        identityVerificationProvider.setIdVProviderDescription(idVProviderRequest.getDescription());
        identityVerificationProvider.setEnable(idVProviderRequest.getIsEnable());
        identityVerificationProvider.setClaimMappings(getClaimMap(idVProviderRequest.getClaims()));
        List<ConfigProperty> properties = idVProviderRequest.getConfigProperties();

        identityVerificationProvider.setIdVConfigProperties(
                properties.stream().map(propertyToInternal).toArray(IdVConfigProperty[]::new));
        identityVerificationProvider.setEnable(true);
        return identityVerificationProvider;
    }

    private final Function<ConfigProperty, IdVConfigProperty> propertyToInternal = apiProperty -> {

        IdVConfigProperty idVConfigProperty = new IdVConfigProperty();
        idVConfigProperty.setName(apiProperty.getKey());
        idVConfigProperty.setValue(apiProperty.getValue());
        return idVConfigProperty;
    };

    private final Function<IdVConfigProperty, ConfigProperty> propertyToExternal = apiProperty -> {

        ConfigProperty configProperty = new ConfigProperty();
        configProperty.setKey(apiProperty.getName());
        configProperty.setValue(apiProperty.getValue());
        return configProperty;
    };

    private Map<String, String> getClaimMap(List<Verificationclaim> verificationclaimList) {

        Map<String, String> claimMap = new HashMap<>();
        for (Verificationclaim verificationclaim : verificationclaimList) {
            claimMap.put(verificationclaim.getLocalClaim(), verificationclaim.getIdvpClaim());
        }
        return claimMap;
    }
}
