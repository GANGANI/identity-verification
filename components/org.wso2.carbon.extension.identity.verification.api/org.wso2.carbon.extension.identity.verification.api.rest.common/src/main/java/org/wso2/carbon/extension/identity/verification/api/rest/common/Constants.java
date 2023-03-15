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

package org.wso2.carbon.extension.identity.verification.api.rest.common;

/**
 * Constants related to federated user association service.
 */
public class Constants {

    public static final String TENANT_NAME_FROM_CONTEXT = "TenantNameFromContext";
    public static final String AUTH_USER_TENANT_DOMAIN = "authUserTenantDomain";
    public static final String CORRELATION_ID_MDC = "Correlation-ID";
    // todo
    public static final String CONFLICT_ASSOCIATION_CODE = "10004";
    public static final String INVALID_IDP_CODE = "10007";
    public static final String NON_EXISTING_USER_CODE = "30007 - ";

    // Audit constants.
    public static final String ACTION_FEDERATED_ASC_CREATE = "Federated Association - Create Association";
    public static final String USER_NAME = "Username";
    public static final String USER_ID = "UserId";
    public static final String FIDP = "Federated IDP";
    public static final String ERROR_CODE = "Error Code";
    public static final String ERROR_MSG = "Error Message";

    /**
     * Enum for federated user association creation related errors.
     * Error Code - code to identify the error.
     * Error Message - What went wrong.
     * Error Description - Why it went wrong.
     */
    public enum ErrorMessage {

        // todo
        // Client errors.
        ERROR_CODE_IDVP_NOT_FOUND("IDV-60000",
                "Invalid identity verification provider id.",
                "Could not find an identity verification provider with given id."),

        // Server errors.
        ERROR_ADDING_IDVP("IDV-65000",
                "Unable to add identity verification provider.",
                "Server encountered an error while adding the identity verification provider."),
        ERROR_UPDATING_IDVP("IDV-65001",
                "Unable to update identity verification provider.",
                "Server encountered an error while updating the identity verification provider."),
        ERROR_RETRIEVING_IDVP("IDV-65002",
                "Unable to retrieve identity verification provider.",
                "Server encountered an error while retrieving the identity verification provider."),
        ERROR_DELETING_IDVP("IDV-65003",
                "Unable to delete identity verification provider.",
                "Server encountered an error while deleting the identity verification provider."),
        ERROR_RETRIEVING_IDV_CLAIM_METADATA("IDV-65004",
                "Unable to retrieve identity verification claim metadata.",
                "Server encountered an error while retrieving the identity verification claim metadata."),
        ERROR_RETRIEVING_USER_IDV_CLAIMS("IDV-65005",
                "Unable to retrieve identity verification claims of the user %s.",
                "Server encountered an error while retrieving the identity verification claim of the user."),
        ERROR_CODE_ERROR_VERIFYING_IDENTITY("IDV-65006",
                "Unable to verify the identity of the user %s.",
                "Server encountered an error while verifying the identity of the user."),

        ERROR_COMMON_SERVER_ERROR("IDV-65006",
                "Unable to complete operation.",
                "Error occurred while performing operation."),
        ERROR_ADDING_VERIFICATION_CLAIM("IDV-65006", "Unable to add the verification claim.",
                "Error occurred while adding the verification claim."),
        ERROR_GETTING_VERIFICATION_CLAIM("IDV-65006", "Unable to get the verification claim %s.",
                "Error occurred while getting the verification claim."),
        ERROR_UPDATING_VERIFICATION_CLAIM("IDV-65006", "Unable to update the verification claim %s.",
                "Error occurred while updating the verification claim."),
        ERROR_CODE_IDV_CLAIM_NOT_FOUND("IDV-65006", "Resource not found.",
                "Unable to find a resource matching the provided identity claim identifier %s.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {

            return code;
        }

        public String getMessage() {

            return message;
        }

        public String getDescription() {

            return description;
        }

        @Override
        public String toString() {

            return code + " | " + message;
        }
    }
}
