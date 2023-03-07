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

package org.wso2.carbon.extension.identity.verification.claim.mgt.dao;

import org.wso2.carbon.extension.identity.verification.claim.mgt.IdVClaimMgtException;
import org.wso2.carbon.extension.identity.verification.claim.mgt.model.IdVClaim;

/**
 * This interface of IdentityVerificationClaimDAO.
 */
public interface IdentityVerificationClaimDAO {

    /**
     * Add the identity verification claim.
     *
     * @param idVClaim IdentityVerificationClaim.
     * @param tenantId Tenant id.
     * @throws IdVClaimMgtException Identity verification claim management exception.
     */
    void addIdVClaim(IdVClaim idVClaim, int tenantId) throws IdVClaimMgtException;

    /**
     * Update the identity verification claim by the user id.
     *
     * @param idVClaim IdentityVerificationClaim.
     * @param tenantId Tenant id.
     * @throws IdVClaimMgtException Identity verification claim management exception.
     */
    void updateIdVClaim(IdVClaim idVClaim, int tenantId) throws IdVClaimMgtException;

    /**
     * Get the identity verification claim.
     *
     * @param idVClaimId Identity verification claim id.
     * @return Identity verification claim.
     * @throws IdVClaimMgtException Identity verification claim management exception.
     */
    IdVClaim getIDVClaim(String idVClaimId, int tenantId) throws IdVClaimMgtException;

    /**
     * Get the identity verification claims.
     *
     * @param tenantId Identity verification claim id.
     * @param userId   User id.
     * @return Identity verification claim.
     * @throws IdVClaimMgtException Identity verification claim management exception.
     */
    IdVClaim[] getIDVClaims(String userId, int tenantId) throws IdVClaimMgtException;

    /**
     * Delete the identity verification claim.
     *
     * @param idVClaimId Identity verification claim id.
     * @param tenantId   Tenant id.
     * @throws IdVClaimMgtException Identity verification claim management exception.
     */
    void deleteIdVClaim(String idVClaimId, int tenantId) throws IdVClaimMgtException;

    boolean isIdVClaimExist(String userId, String idvId, String uri, int tenantId) throws IdVClaimMgtException;
}
