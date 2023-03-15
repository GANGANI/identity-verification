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
package org.wso2.carbon.extension.identity.verification.claim.mgt;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.wso2.carbon.extension.identity.verification.claim.mgt.dao.IdentityVerificationClaimDAOImpl;
import org.wso2.carbon.extension.identity.verification.claim.mgt.internal.IdVClaimMgtDataHolder;
import org.wso2.carbon.extension.identity.verification.claim.mgt.model.IdVClaim;
import org.wso2.carbon.extension.identity.verification.claim.mgt.util.IdVClaimMgtConstants;
import org.wso2.carbon.extension.identity.verification.claim.mgt.util.IdVClaimMgtExceptionManagement;
import org.wso2.carbon.extension.identity.verification.provider.IdVProviderMgtException;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.user.api.UserStoreManager;
import org.wso2.carbon.user.core.UniqueIDUserStoreManager;
import org.wso2.carbon.user.core.common.User;
import org.wso2.carbon.user.core.service.RealmService;

import java.util.List;

/**
 * This class provides methods to manage identity verification claims.
 */
public class IdVClaimManagerImpl implements IdVClaimManager {

    private static final Log log = LogFactory.getLog(IdVClaimManagerImpl.class);

    IdentityVerificationClaimDAOImpl identityVerificationClaimDAO = new IdentityVerificationClaimDAOImpl();

    @Override
    public IdVClaim getIdVClaim(String userId, String idvClaimId, int tenantId) throws IdVClaimMgtException {

        if (StringUtils.isBlank(userId) && StringUtils.isBlank(idvClaimId)) {
            throw IdVClaimMgtExceptionManagement.handleClientException(
                    IdVClaimMgtConstants.ErrorMessage.ERROR_CODE_INVALID_INPUTS);
        }
        validateUserId(userId, tenantId);
        return identityVerificationClaimDAO.getIDVClaim(userId, idvClaimId, tenantId);
    }

    @Override
    public List<IdVClaim> addIdVClaims(List<IdVClaim> idVClaims, int tenantId) throws IdVClaimMgtException {

        for (IdVClaim idVClaim : idVClaims) {
            validateAddIDVClaimInputs(idVClaim, tenantId);
        }
        identityVerificationClaimDAO.addIdVClaimList(idVClaims, tenantId);
        return idVClaims;
    }

    @Override
    public IdVClaim updateIdVClaim(IdVClaim idvClaim, int tenantId) throws IdVClaimMgtException {

        validateUpdateIDVClaimUpdateInputs(idvClaim, tenantId);
        identityVerificationClaimDAO.updateIdVClaim(idvClaim, tenantId);
        return idvClaim;
    }

    @Override
    public void deleteIDVClaim(String userId, String idvClaimId, int tenantId) throws IdVClaimMgtException {

        validateUserId(userId, tenantId);
        identityVerificationClaimDAO.deleteIdVClaim(idvClaimId, tenantId);
    }

    @Override
    public IdVClaim[] getIDVClaims(String userId, int tenantId) throws IdVClaimMgtException {

        validateUserId(userId, tenantId);
        return identityVerificationClaimDAO.getIDVClaims(userId, tenantId);
    }

    @Override
    public boolean isIdVClaimDataExists(String userId, String idvId, String uri, int tenantId)
            throws IdVClaimMgtException {

        return identityVerificationClaimDAO.isIdVClaimDataExist(userId, idvId, uri, tenantId);
    }

    private void validateAddIDVClaimInputs(IdVClaim idVClaim, int tenantId) throws IdVClaimMgtException {

        String userId = idVClaim.getUserId();
        String idvProviderId = idVClaim.getIdvProviderId();
        String claimUri = idVClaim.getClaimUri();
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(idvProviderId) || StringUtils.isBlank(claimUri)) {
            throw IdVClaimMgtExceptionManagement.handleClientException(
                    IdVClaimMgtConstants.ErrorMessage.ERROR_CODE_INVALID_INPUTS, null);
        }
        validateUserId(userId, tenantId);
        boolean idvClaimExists = isIdVClaimDataExists(userId, idvProviderId, claimUri, tenantId);
        if (idvClaimExists) {
            throw IdVClaimMgtExceptionManagement.handleClientException(
                    IdVClaimMgtConstants.ErrorMessage.ERROR_IDV_CLAIM_DATA_ALREADY_EXISTS, null);
        }
        validateIdVProviderId(idvProviderId, tenantId);
    }

    private void validateIdVProviderId(String idvProviderId, int tenantId) throws IdVClaimMgtException {

        try {
            if (!IdVClaimMgtDataHolder.getIdVProviderManager().isIdVProviderExists(idvProviderId, tenantId)) {
                throw IdVClaimMgtExceptionManagement.handleClientException(
                        IdVClaimMgtConstants.ErrorMessage.ERROR_INVALID_IDV_PROVIDER_ID, null);
            }
        } catch (IdVProviderMgtException e) {
            throw IdVClaimMgtExceptionManagement.handleServerException(
                    IdVClaimMgtConstants.ErrorMessage.ERROR_VALIDATING_IDV_PROVIDER_ID, null);
        }
    }

    private void validateUserId(String userId, int tenantId) throws IdVClaimMgtClientException {

        UniqueIDUserStoreManager uniqueIDUserStoreManager;
        try {
            uniqueIDUserStoreManager =
                    getUniqueIdEnabledUserStoreManager(IdVClaimMgtDataHolder.getRealmService(),
                            IdentityTenantUtil.getTenantDomain(tenantId));
            User user = uniqueIDUserStoreManager.getUserWithID(userId, null, null);
            if (user == null) {
                throw IdVClaimMgtExceptionManagement.handleClientException(
                        IdVClaimMgtConstants.ErrorMessage.ERROR_INVALID_USER_ID);
            }
        } catch (UserStoreException e) {
            if (e.getCause().getMessage().contains("30007")) {
                throw IdVClaimMgtExceptionManagement.handleClientException(
                        IdVClaimMgtConstants.ErrorMessage.ERROR_INVALID_USER_ID);
            }
            throw IdVClaimMgtExceptionManagement.handleClientException(
                    IdVClaimMgtConstants.ErrorMessage.ERROR_CHECKING_USER_ID_EXISTENCE);
        } catch (IdvClaimMgtServerException e) {
            throw IdVClaimMgtExceptionManagement.handleClientException(
                    IdVClaimMgtConstants.ErrorMessage.ERROR_CHECKING_USER_ID_EXISTENCE);
        }
    }

    private void validateIdVClaimId(String idVClaimId, int tenantId) throws IdVClaimMgtException {

        if (StringUtils.isBlank(idVClaimId)) {
            throw IdVClaimMgtExceptionManagement.handleClientException(
                    IdVClaimMgtConstants.ErrorMessage.ERROR_CODE_INVALID_INPUTS, "idVClaimId");
        }
        boolean idvClaimExists = isIdVClaimExists(idVClaimId, tenantId);
        if (!idvClaimExists) {
            throw IdVClaimMgtExceptionManagement.handleClientException(
                    IdVClaimMgtConstants.ErrorMessage.ERROR_INVALID_IDV_CLAIM_ID, null);
        }
    }

    public boolean isIdVClaimExists(String idVClaimId, int tenantId) throws IdVClaimMgtException {

        return identityVerificationClaimDAO.isIdVClaimExist(idVClaimId, tenantId);
    }

    private UniqueIDUserStoreManager getUniqueIdEnabledUserStoreManager(RealmService realmService, String tenantDomain)
            throws IdvClaimMgtServerException, UserStoreException {

        UserStoreManager userStoreManager = realmService.getTenantUserRealm(
                IdentityTenantUtil.getTenantId(tenantDomain)).getUserStoreManager();
        if (!(userStoreManager instanceof UniqueIDUserStoreManager)) {
            if (log.isDebugEnabled()) {
                log.debug("Provided user store manager does not support unique user IDs.");
            }
            throw IdVClaimMgtExceptionManagement.handleServerException(
                    IdVClaimMgtConstants.ErrorMessage.ERROR_INVALID_USER_ID);
        }
        return (UniqueIDUserStoreManager) userStoreManager;
    }

    private void validateUpdateIDVClaimUpdateInputs(IdVClaim idVClaim, int tenantId) throws IdVClaimMgtException {

        JSONObject claimMetadata = idVClaim.getMetadata();
        if (claimMetadata == null) {
            throw IdVClaimMgtExceptionManagement.handleClientException(
                    IdVClaimMgtConstants.ErrorMessage.ERROR_CODE_INVALID_INPUTS, null);
        }
    }
}
