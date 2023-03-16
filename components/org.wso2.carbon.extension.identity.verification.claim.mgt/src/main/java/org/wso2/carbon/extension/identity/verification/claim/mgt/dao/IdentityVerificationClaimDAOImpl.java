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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.wso2.carbon.extension.identity.verification.claim.mgt.exception.IdVClaimMgtException;
import org.wso2.carbon.extension.identity.verification.claim.mgt.model.IdVClaim;
import org.wso2.carbon.extension.identity.verification.claim.mgt.util.IdVClaimMgtConstants;
import org.wso2.carbon.extension.identity.verification.claim.mgt.util.IdVClaimMgtExceptionManagement;
import org.wso2.carbon.identity.core.util.IdentityDatabaseUtil;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.wso2.carbon.extension.identity.verification.claim.mgt.util.IdVClaimMgtConstants.ErrorMessage.ERROR_ADDING_IDV_CLAIM;
import static org.wso2.carbon.extension.identity.verification.claim.mgt.util.IdVClaimMgtConstants.ErrorMessage.ERROR_CHECKING_IDV_CLAIM_EXISTENCE;
import static org.wso2.carbon.extension.identity.verification.claim.mgt.util.IdVClaimMgtConstants.ErrorMessage.ERROR_DELETING_IDV_CLAIM;
import static org.wso2.carbon.extension.identity.verification.claim.mgt.util.IdVClaimMgtConstants.ErrorMessage.ERROR_RETRIEVING_IDV_CLAIM;
import static org.wso2.carbon.extension.identity.verification.claim.mgt.util.IdVClaimMgtConstants.ErrorMessage.ERROR_RETRIEVING_IDV_CLAIMS;
import static org.wso2.carbon.extension.identity.verification.claim.mgt.util.IdVClaimMgtConstants.ErrorMessage.ERROR_UPDATING_IDV_CLAIM;

/**
 * Identity verification claim DAO class.
 */
public class IdentityVerificationClaimDAOImpl implements IdentityVerificationClaimDAO {

    private static final Log log = LogFactory.getLog(IdentityVerificationClaimDAOImpl.class);

    /**
     * Add the identity verification claim.
     *
     * @param idvClaimList IdentityVerificationClaim list.
     * @param tenantId Tenant id.
     * @throws IdVClaimMgtException Identity verification claim management exception.
     */
    @Override
    public void addIdVClaimList(List<IdVClaim> idvClaimList, int tenantId) throws IdVClaimMgtException {

        try (Connection connection = IdentityDatabaseUtil.getDBConnection(false)) {
            for (IdVClaim idVClaim : idvClaimList) {
                try (PreparedStatement addIdVProviderStmt = connection.prepareStatement(IdVClaimMgtConstants.
                        SQLQueries.ADD_IDV_CLAIM_SQL)) {
                    addIdVProviderStmt.setString(1, idVClaim.getUuid());
                    addIdVProviderStmt.setString(2, idVClaim.getUserId());
                    addIdVProviderStmt.setString(3, idVClaim.getClaimUri());
                    addIdVProviderStmt.setString(4, idVClaim.getIdvProviderId());
                    addIdVProviderStmt.setInt(5, tenantId);
                    addIdVProviderStmt.setBoolean(6, idVClaim.getStatus());
                    addIdVProviderStmt.setBytes(7, getMetadata(idVClaim));
                    addIdVProviderStmt.executeUpdate();
                } catch (SQLException e1) {
                    throw IdVClaimMgtExceptionManagement.handleServerException(ERROR_ADDING_IDV_CLAIM, e1);
                }
            }
        } catch (SQLException e) {
            throw IdVClaimMgtExceptionManagement.handleServerException(ERROR_ADDING_IDV_CLAIM, e);
        }
    }

    /**
     * Update the identity verification claim by the user id.
     *
     * @param idVClaim IdentityVerificationClaim.
     * @param tenantId Tenant id.
     * @throws IdVClaimMgtException Identity verification claim management exception.
     */
    @Override
    public void updateIdVClaim(IdVClaim idVClaim, int tenantId) throws IdVClaimMgtException {

        try (Connection connection = IdentityDatabaseUtil.getDBConnection(false)) {
            try (PreparedStatement updateIdVProviderStmt = connection.prepareStatement(IdVClaimMgtConstants.
                    SQLQueries.UPDATE_IDV_CLAIM_SQL)) {
                updateIdVProviderStmt.setBoolean(1, idVClaim.getStatus());
                updateIdVProviderStmt.setObject(2, getMetadata(idVClaim));
                updateIdVProviderStmt.setString(3, idVClaim.getUserId());
                updateIdVProviderStmt.setString(4, idVClaim.getUuid());
                updateIdVProviderStmt.setInt(5, tenantId);
                updateIdVProviderStmt.executeUpdate();
                IdentityDatabaseUtil.commitTransaction(connection);
            } catch (SQLException e1) {
                IdentityDatabaseUtil.rollbackTransaction(connection);
                throw IdVClaimMgtExceptionManagement.handleServerException(ERROR_UPDATING_IDV_CLAIM, e1);
            }
        } catch (SQLException e) {
            throw IdVClaimMgtExceptionManagement.handleServerException(ERROR_UPDATING_IDV_CLAIM, e);
        }
    }

    /**
     * Get the identity verification claim.
     *
     * @param userId   User id.
     * @param idVClaimId Identity verification claim id.
     * @return Identity verification claim.
     * @throws IdVClaimMgtException Identity verification claim management exception.
     */
    @Override
    public IdVClaim getIDVClaim(String userId, String idVClaimId, int tenantId) throws IdVClaimMgtException {

        IdVClaim idVClaim = null;
        try (Connection connection = IdentityDatabaseUtil.getDBConnection(false);
             PreparedStatement getIdVProviderStmt = connection.prepareStatement(IdVClaimMgtConstants.
                     SQLQueries.GET_IDV_CLAIM_SQL)) {
            getIdVProviderStmt.setString(1, userId);
            getIdVProviderStmt.setString(2, idVClaimId);
            getIdVProviderStmt.setInt(3, tenantId);
            getIdVProviderStmt.execute();
            try (ResultSet idVProviderResultSet = getIdVProviderStmt.executeQuery()) {
                while (idVProviderResultSet.next()) {
                    idVClaim = new IdVClaim();
                    idVClaim.setUuid(idVProviderResultSet.getString("UUID"));
                    idVClaim.setUserId(idVProviderResultSet.getString("USER_ID"));
                    idVClaim.setClaimUri(idVProviderResultSet.getString("CLAIM_URI"));
                    idVClaim.setIdvProviderId(idVProviderResultSet.getString("IDVP_ID"));
                    idVClaim.setStatus(idVProviderResultSet.getBoolean("IS_VERIFIED"));
                    idVClaim.setMetadata(getMetadataJsonObject(idVProviderResultSet.getBytes("METADATA")));
                }
            }
        } catch (SQLException e) {
            throw IdVClaimMgtExceptionManagement.handleServerException(ERROR_RETRIEVING_IDV_CLAIM, e);
        }
        return idVClaim;
    }

    /**
     * Get the identity verification claims.
     *
     * @param tenantId Identity verification claim id.
     * @param userId   User id.
     * @return Identity verification claim.
     * @throws IdVClaimMgtException Identity verification claim management exception.
     */
    @Override
    public IdVClaim[] getIDVClaims(String userId, int tenantId) throws IdVClaimMgtException {

        List<IdVClaim> idVClaims = new ArrayList<>();
        try (Connection connection = IdentityDatabaseUtil.getDBConnection(false);
             PreparedStatement getIdVProviderStmt = connection.prepareStatement(IdVClaimMgtConstants.
                     SQLQueries.GET_IDV_CLAIMS_SQL)) {
            getIdVProviderStmt.setString(1, userId);
            getIdVProviderStmt.setInt(2, tenantId);
            getIdVProviderStmt.execute();
            try (ResultSet idVProviderResultSet = getIdVProviderStmt.executeQuery()) {
                while (idVProviderResultSet.next()) {
                    IdVClaim idVClaim = new IdVClaim();
                    idVClaim.setUuid(idVProviderResultSet.getString("UUID"));
                    idVClaim.setUserId(idVProviderResultSet.getString("USER_ID"));
                    idVClaim.setClaimUri(idVProviderResultSet.getString("CLAIM_URI"));
                    idVClaim.setStatus(idVProviderResultSet.getBoolean("IS_VERIFIED"));
                    idVClaim.setMetadata(getMetadataJsonObject(idVProviderResultSet.getBytes("METADATA")));
                    idVClaims.add(idVClaim);
                }
            }
        } catch (SQLException e) {
            throw IdVClaimMgtExceptionManagement.handleServerException(ERROR_RETRIEVING_IDV_CLAIMS, e);
        }
        return idVClaims.toArray(new IdVClaim[0]);
    }

    /**
     * Delete the identity verification claim.
     *
     * @param idVClaimId Identity verification claim id.
     * @throws IdVClaimMgtException Identity verification claim management exception.
     */
    @Override
    public void deleteIdVClaim(String idVClaimId, int tenantId) throws IdVClaimMgtException {

        try (Connection connection = IdentityDatabaseUtil.getDBConnection(false)) {
            try (PreparedStatement deleteIdVProviderStmt = connection.prepareStatement(IdVClaimMgtConstants.
                    SQLQueries.DELETE_IDV_CLAIM_SQL)) {
                deleteIdVProviderStmt.setString(1, idVClaimId);
                deleteIdVProviderStmt.setInt(2, tenantId);
                deleteIdVProviderStmt.executeUpdate();
                IdentityDatabaseUtil.commitTransaction(connection);
            } catch (SQLException e1) {
                IdentityDatabaseUtil.rollbackTransaction(connection);
                throw IdVClaimMgtExceptionManagement.handleServerException(ERROR_DELETING_IDV_CLAIM, e1);
            }
        } catch (SQLException e) {
            throw IdVClaimMgtExceptionManagement.handleServerException(ERROR_DELETING_IDV_CLAIM, e);
        }
    }

    /**
     * Check whether the identity verification claim data already exist.
     *
     * @param userId   User id.
     * @param idvId    Identity verification id.
     * @param uri      Claim uri.
     * @param tenantId Tenant id.
     * @return True if the identity verification claim exist.
     * @throws IdVClaimMgtException Identity verification claim management exception.
     */
    @Override
    public boolean isIdVClaimDataExist(String userId, String idvId, String uri, int tenantId)
            throws IdVClaimMgtException {

        try (Connection connection = IdentityDatabaseUtil.getDBConnection(false);
             PreparedStatement getIdVProviderStmt = connection.prepareStatement(IdVClaimMgtConstants.
                     SQLQueries.IS_IDV_CLAIM_DATA_EXIST_SQL)) {
            getIdVProviderStmt.setString(1, userId);
            getIdVProviderStmt.setString(2, idvId);
            getIdVProviderStmt.setString(3, uri);
            getIdVProviderStmt.setInt(4, tenantId);
            getIdVProviderStmt.execute();
            try (ResultSet idVProviderResultSet = getIdVProviderStmt.executeQuery()) {
                if (idVProviderResultSet.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            throw IdVClaimMgtExceptionManagement.handleServerException(ERROR_CHECKING_IDV_CLAIM_EXISTENCE, e);
        }
        return false;
    }

    /**
     * Check whether the identity verification claim data already exist.
     *
     * @param claimId Identity verification claim id.
     * @param tenantId Tenant id.
     * @return True if the identity verification claim exist.
     * @throws IdVClaimMgtException Identity verification claim management exception.
     */
    @Override
    public boolean isIdVClaimExist(String claimId, int tenantId) throws IdVClaimMgtException {

        try (Connection connection = IdentityDatabaseUtil.getDBConnection(false);
             PreparedStatement getIdVProviderStmt = connection.prepareStatement(IdVClaimMgtConstants.
                     SQLQueries.IS_IDV_CLAIM_EXIST_SQL)) {
            getIdVProviderStmt.setString(1, claimId);
            getIdVProviderStmt.setInt(2, tenantId);
            getIdVProviderStmt.execute();
            try (ResultSet idVProviderResultSet = getIdVProviderStmt.executeQuery()) {
                if (idVProviderResultSet.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            throw IdVClaimMgtExceptionManagement.handleServerException(ERROR_CHECKING_IDV_CLAIM_EXISTENCE, e);
        }
        return false;
    }

    private byte[] getMetadata(IdVClaim idVClaim) {

        String metadataString = idVClaim.getMetadata().toString();
        return metadataString.getBytes(StandardCharsets.UTF_8);
    }

    private JSONObject getMetadataJsonObject(byte[] metadata) {

        String metadataString = new String(metadata, StandardCharsets.UTF_8);
        return new JSONObject(metadataString);
    }
}
