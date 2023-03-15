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

import org.wso2.carbon.extension.identity.verification.claim.mgt.model.IdVClaim;

import java.util.List;

/**
 * This interface contains the methods to manage the IdVClaim.
 */
public interface IdVClaimManager {

    /**
     * Get the IdVClaim.
     *
     * @param userId      User Id.
     * @param idvClaimId  IdVClaim Id.
     * @param tenantId    Tenant Id.
     * @return IdVClaim.
     * @throws IdVClaimMgtException IdVClaimMgtException.
     */
    IdVClaim getIdVClaim(String userId, String idvClaimId, int tenantId) throws IdVClaimMgtException;

    /**
     * Get the IdVClaims of a user.
     *
     * @param userId   User Id.
     * @param tenantId Tenant Id.
     * @return IdVClaims.
     * @throws IdVClaimMgtException IdVClaimMgtException.
     */
    IdVClaim[] getIDVClaims(String userId, int tenantId) throws IdVClaimMgtException;

    /**
     * Add the IdVClaim.
     *
     * @param idvClaim   IdVClaim.
     * @param tenantId   Tenant Id.
     * @return IdVClaim.
     * @throws IdVClaimMgtException IdVClaimMgtException.
     */
    List<IdVClaim> addIdVClaims(List<IdVClaim> idvClaim, int tenantId) throws IdVClaimMgtException;

    /**
     * Update the IdVClaim.
     *
     * @param idvClaim   IdVClaim.
     * @param tenantId   Tenant Id.
     * @throws IdVClaimMgtException IdVClaimMgtException.
     */
    IdVClaim updateIdVClaim(IdVClaim idvClaim, int tenantId) throws IdVClaimMgtException;

    /**
     * Delete the IdVClaim.
     *
     * @param idvClaimId  IdVClaim Id.
     * @param tenantId    Tenant Id.
     * @throws IdVClaimMgtException IdVClaimMgtException.
     */
    void deleteIDVClaim(String userId, String idvClaimId, int tenantId) throws IdVClaimMgtException;

    boolean isIdVClaimDataExists(String userId, String idvId, String uri, int tenantId) throws IdVClaimMgtException;

    boolean isIdVClaimExists(String idVClaimId, int tenantId) throws IdVClaimMgtException;
}
