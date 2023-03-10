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

package org.wso2.carbon.extension.identity.verification.claim.mgt.util;

/**
 * Holds constants related to IdV Claim Management components.
 */
public class IdVClaimMgtConstants {

    public static final String IDVC_ERROR_PREFIX = "IDVC-";

    /**
     * Holds constants related to IdV Claim Management database tables.
     */
    public static class SQLQueries {

        public static final String ADD_IDV_CLAIM_SQL =
                "INSERT INTO IDV_CLAIM (UUID, USER_ID, CLAIM_URI, IDVP_ID, TENANT_ID, IS_VERIFIED, METADATA) " +
                        "VALUES (?,?,?,?,?,?,?)";

        public static final String GET_IDV_CLAIM_SQL =
                "SELECT UUID, USER_ID, CLAIM_URI, TENANT_ID, IDVP_ID, IS_VERIFIED, METADATA FROM IDV_CLAIM WHERE " +
                        "USER_ID = ? AND UUID = ? AND TENANT_ID = ?";

        public static final String GET_IDV_CLAIMS_SQL =
                "SELECT UUID, USER_ID, CLAIM_URI, IS_VERIFIED, METADATA FROM IDV_CLAIM WHERE " +
                        "USER_ID = ? AND TENANT_ID = ?";
        public static final String UPDATE_IDV_CLAIM_SQL =
                "UPDATE IDV_CLAIM SET IS_VERIFIED = ?, METADATA = ? WHERE USER_ID = ? AND UUID = ? AND TENANT_ID = ?";

        public static final String DELETE_IDV_CLAIM_SQL =
                "DELETE FROM IDV_CLAIM WHERE USER_ID = ? AND UUID = ? AND TENANT_ID = ?";

        public static final String IS_IDV_CLAIM_DATA_EXIST_SQL =
                "SELECT ID FROM IDV_CLAIM WHERE USER_ID = ? AND IDVP_ID = ? AND CLAIM_URI = ? AND TENANT_ID = ?";

        public static final String IS_IDV_CLAIM_EXIST_SQL =
                "SELECT ID FROM IDV_CLAIM WHERE UUID = ? AND TENANT_ID = ?";
    }

    /**
     * Error messages.
     */
    public enum ErrorMessage {

        ERROR_IDV_CLAIM_DATA_ALREADY_EXISTS("65000",
                "Identity Verification Claim data already exists.", ""),
        ERROR_CODE_INVALID_INPUTS("65001", "Provided inputs are invalid.", ""),
        ERROR_CODE_INVALID_STATUS("65002", "Added an invalid status.", ""),
        ERROR_INVALID_IDV_PROVIDER_ID("65002", "Invalid Identity Provider Id provided.", ""),
        ERROR_CHECKING_IDV_CLAIM_EXISTENCE("65003",
                "Error while checking the existence of the Identity Verification Claim.", ""),
        ERROR_INVALID_USER_ID("60004", "Invalid UserID provided.",
                "User is not existing with the given user id"),

        ERROR_INVALID_IDV_CLAIM_ID("65004", "Invalid claim id provided.", ""),
        ERROR_DELETING_IDV_CLAIM("65004",
                "Error deleting the Identity Verification Claim.", ""),
        ERROR_RETRIEVING_IDV_CLAIM("65005",
                "Error retrieving the Identity Verification Claim.", ""),
        ERROR_UPDATING_IDV_CLAIM("65005",
                "Error updating the Identity Verification Claim.", ""),
        ERROR_ADDING_IDV_CLAIM("65005",
                "Error adding the Identity Verification Claim.", ""),
        ERROR_RETRIEVING_IDV_CLAIMS("65005",
                "Error retrieving the Identity Verification Claims.", ""),
        ERROR_VALIDATING_IDV_PROVIDER_ID("65005",
                "Error while validating identity provider id.", ""),
        ERROR_CHECKING_USER_ID_EXISTENCE("65005",
                "Error while checking the user id existence.", "");
        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {

            return IDVC_ERROR_PREFIX + code;
        }

        public String getMessage() {

            return message;
        }

        public String getDescription() {

            return description;
        }

        @Override
        public String toString() {

            return code + ":" + message;
        }
    }
}
