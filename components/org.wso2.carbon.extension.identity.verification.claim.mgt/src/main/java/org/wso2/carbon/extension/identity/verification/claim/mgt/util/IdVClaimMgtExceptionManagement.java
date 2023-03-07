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

import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.extension.identity.verification.claim.mgt.IdVClaimMgtClientException;
import org.wso2.carbon.extension.identity.verification.claim.mgt.IdvClaimMgtServerException;

/**
 * This class contains the implementation for the exception management.
 */
public class IdVClaimMgtExceptionManagement {

    public static IdVClaimMgtClientException handleClientException(
            IdVClaimMgtConstants.ErrorMessage error, String data) {

        String message = includeData(error, data);
        return new IdVClaimMgtClientException(error.getCode(), message);
    }

    public static IdVClaimMgtClientException handleClientException(
            IdVClaimMgtConstants.ErrorMessage error, String data, Throwable e) {

        String message = includeData(error, data);
        return new IdVClaimMgtClientException(error.getCode(), message, e);
    }

    public static IdVClaimMgtClientException handleClientException(
            IdVClaimMgtConstants.ErrorMessage error) {

        String message = error.getMessage();
        return new IdVClaimMgtClientException(error.getCode(), message);
    }

    public static IdvClaimMgtServerException handleServerException(
            IdVClaimMgtConstants.ErrorMessage error, String data, Throwable e) {

        String message = includeData(error, data);
        return new IdvClaimMgtServerException(error.getCode(), message, e);
    }

    public static IdvClaimMgtServerException handleServerException(
            IdVClaimMgtConstants.ErrorMessage error) {

        String message = error.getMessage();
        return new IdvClaimMgtServerException(error.getCode(), message);
    }

    public static IdvClaimMgtServerException handleServerException(
            IdVClaimMgtConstants.ErrorMessage error, Throwable e) {

        String message = error.getMessage();
        return new IdvClaimMgtServerException(error.getCode(), message, e);
    }

    /**
     * Include the data to the error message.
     *
     * @param error FunctionLibraryManagementConstants.ErrorMessage.
     * @param data  data to replace if message needs to be replaced.
     * @return message format with data.
     */
    private static String includeData(IdVClaimMgtConstants.ErrorMessage error, String data) {

        String message;
        if (StringUtils.isNotBlank(data)) {
            message = String.format(error.getMessage(), data);
        } else {
            message = error.getMessage();
        }
        return message;
    }
}
