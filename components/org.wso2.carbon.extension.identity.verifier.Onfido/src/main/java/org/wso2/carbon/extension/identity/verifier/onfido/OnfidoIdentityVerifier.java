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
package org.wso2.carbon.extension.identity.verifier.onfido;

import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.wso2.carbon.extension.identity.verification.claim.mgt.exception.IdvClaimMgtServerException;
import org.wso2.carbon.extension.identity.verification.claim.mgt.model.IdVClaim;
import org.wso2.carbon.extension.identity.verification.claim.mgt.util.IdVClaimMgtConstants;
import org.wso2.carbon.extension.identity.verification.claim.mgt.util.IdVClaimMgtExceptionManagement;
import org.wso2.carbon.extension.identity.verifier.IdentityVerifier;
import org.wso2.carbon.extension.identity.verifier.model.IdentityVerifierData;
import org.wso2.carbon.extension.identity.verifier.onfido.web.OnfidoAPIClient;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.user.api.UserStoreManager;
import org.wso2.carbon.user.core.UniqueIDUserStoreManager;
import org.wso2.carbon.user.core.service.RealmService;

import java.io.IOException;
import java.util.List;

/**
 * This class contains the implementation of OnfidoIdentityVerifier.
 */
public class OnfidoIdentityVerifier implements IdentityVerifier {

    private static final Log log = LogFactory.getLog(OnfidoIdentityVerifier.class);

    @Override
    public IdentityVerifierData verifyIdentity(IdentityVerifierData identityVerifierData, int tenantId) {

        List<IdVClaim> idVClaims = identityVerifierData.getIdVClaims();
        try {
            if (StringUtils.equals(identityVerifierData.getIdVProperties().get(0).getName(), "status")) {
                JSONObject jsonObject = new JSONObject();
                switch (identityVerifierData.getIdVProperties().get(0).getValue()) {
                    case "INITIATED":
                        // JSONObject jsonObject = new JSONObject();
                        for (IdVClaim idVClaim : idVClaims) {
                            // jsonObject.put(idVClaim.getClaimUri(), "INITIATED");
                            // todo: get the cliam uri, get the claim mapping from the claim manager,
                            //  get the claim value and create json request.
                        }
                        // pass the json object to client request.
                        JsonObject onFidoJsonObject = OnfidoAPIClient.createApplicantResponse();
                        jsonObject.put("status", "INITIATED");
                        jsonObject.put("source", "ONFIDO");
                        if (onFidoJsonObject != null) {
                            jsonObject.put("applicant_id", onFidoJsonObject.get("id").toString());
                        }


                        break;
                    case "DOCUMENT_UPLOAD":
                        for (IdVClaim idVClaim : idVClaims) {
                            // todo
                        }
                        JsonObject uploadDocumentJsonObject = OnfidoAPIClient.uploadDocument();
                        jsonObject.put("status", "DOCUMENT_UPLOADED");
                        jsonObject.put("source", "ONFIDO");
                        if (uploadDocumentJsonObject != null) {
                            jsonObject.put("applicant_id", uploadDocumentJsonObject.get("applicant_id").toString());
                            jsonObject.put("document_id", uploadDocumentJsonObject.get("id").toString());
                        }
                        break;
                    case "CHECK":
                        for (IdVClaim idVClaim : idVClaims) {
                            // todo
                        }
                        JsonObject checkJsonObject = OnfidoAPIClient.verificationCheck();
                        jsonObject.put("status", "IN_PROGRESS");
                        jsonObject.put("source", "ONFIDO");
                        if (checkJsonObject != null) {
                            jsonObject.put("applicant_id", checkJsonObject.get("applicant_id").toString());
                            jsonObject.put("check_id", checkJsonObject.get("id").toString());
                        }
                        break;
                }
                for (IdVClaim idVClaim : idVClaims) {
                    idVClaim.setMetadata(jsonObject);
                    idVClaim.setStatus(false);
                    idVClaim.setIdvProviderId(identityVerifierData.getIdentityVerifierName());
                    //idVClaim = OnfidoIDVDataHolder.getInstance().getIdVClaimManager().
                    //        addIdVClaim(idVClaim, tenantId);
                }

                identityVerifierData.setIdVClaims(idVClaims);
            }
        } catch (IOException e) {
            log.error("Error while creating the applicant response.", e);
        } catch (JSONException e) {
            log.error("Error while creating the json object.", e);
        } catch (OnfidoException e) {
            throw new RuntimeException(e);
        }
        return identityVerifierData;
    }

    private void getUser(String username, int tenantId) {

        UniqueIDUserStoreManager uniqueIDUserStoreManager;
//        try {
//            uniqueIDUserStoreManager =
//                    getUniqueIdEnabledUserStoreManager(IdVClaimMgtDataHolder.getRealmService(),
//                            IdentityTenantUtil.getTenantDomain(tenantId));
//            User user = uniqueIDUserStoreManager.getUserId(username);
//            if (user == null) {
//                throw IdVClaimMgtExceptionManagement.handleClientException(
//                        IdVClaimMgtConstants.ErrorMessage.ERROR_INVALID_USER_ID);
//            }
//        } catch (IdvClaimMgtServerException | UserStoreException e) {
//            throw IdVClaimMgtExceptionManagement.handleClientException(
//                    IdVClaimMgtConstants.ErrorMessage.ERROR_CHECKING_USER_ID_EXISTENCE);
//        }
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
}
