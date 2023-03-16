/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.com). All Rights Reserved.
 *
 * This software is the property of WSO2 Inc. and its suppliers, if any.
 * Dissemination of any information or reproduction of any material contained
 * herein in any form is strictly forbidden, unless permitted by WSO2 expressly.
 * You may not alter or remove any copyright or other notice from copies of this content.
 */

package org.wso2.carbon.extension.identity.verification.api.rest.common;

import org.wso2.carbon.base.MultitenantConstants;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.extension.identity.verification.claim.mgt.exception.IdvClaimMgtServerException;
import org.wso2.carbon.extension.identity.verification.claim.mgt.util.IdVClaimMgtConstants;
import org.wso2.carbon.extension.identity.verification.claim.mgt.util.IdVClaimMgtExceptionManagement;
import org.wso2.carbon.identity.application.common.model.User;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.user.api.UserRealm;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.user.api.UserStoreManager;
import org.wso2.carbon.user.core.UniqueIDUserStoreManager;
import org.wso2.carbon.user.core.UserStoreConfigConstants;
import org.wso2.carbon.user.core.common.AbstractUserStoreManager;
import org.wso2.carbon.user.core.service.RealmService;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.extension.identity.verification.api.rest.common.Constants.TENANT_NAME_FROM_CONTEXT;

/**
 * Load information from context.
 */
public class ContextLoader {

    public static String getTenantDomainFromAuthUser() {

        String tenantDomain = null;
        if (IdentityUtil.threadLocalProperties.get().get(Constants.AUTH_USER_TENANT_DOMAIN) != null) {
            tenantDomain = (String) IdentityUtil.threadLocalProperties.get().get(Constants.AUTH_USER_TENANT_DOMAIN);
        }
        return tenantDomain;
    }

    /**
     * Retrieves authenticated username from carbon context.
     *
     * @return username of the authenticated user.
     */
    public static String getUsernameFromContext() {

        return PrivilegedCarbonContext.getThreadLocalCarbonContext().getUsername();
    }

    /**
     * Retrieves authenticated username from carbon context.
     *
     * @return username of the authenticated user.
     */
    public static String getUserIdFromContext() {

        try {
            UserRealm userRealm = CarbonContext.getThreadLocalCarbonContext().getUserRealm();
            AbstractUserStoreManager userStoreManager = (AbstractUserStoreManager) userRealm.getUserStoreManager();

//            if (userStoreManager == null) {
//                if (log.isDebugEnabled()) {
//                    log.debug("Userstore Manager is null");
//                }
//                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
//            }
            return userStoreManager.getUserIDFromUserName(getUsernameFromContext());
        } catch (org.wso2.carbon.user.core.UserStoreException e) {
            throw new RuntimeException(e);
        } catch (UserStoreException e) {
            throw new RuntimeException(e);
        }
    }

    private static UniqueIDUserStoreManager getUniqueIdEnabledUserStoreManager(RealmService realmService,
                                                                               String tenantDomain)
            throws IdvClaimMgtServerException, UserStoreException {

        UserStoreManager userStoreManager = realmService.getTenantUserRealm(
                IdentityTenantUtil.getTenantId(tenantDomain)).getUserStoreManager();
        if (!(userStoreManager instanceof UniqueIDUserStoreManager)) {
            throw IdVClaimMgtExceptionManagement.handleServerException(
                    IdVClaimMgtConstants.ErrorMessage.ERROR_INVALID_USER_ID);
        }
        return (UniqueIDUserStoreManager) userStoreManager;
    }

    /**
     * Retrieves loaded tenant domain from carbon context.
     *
     * @return tenant domain of the request is being served.
     */
    public static String getTenantDomainFromContext() {

        String tenantDomain = MultitenantConstants.SUPER_TENANT_DOMAIN_NAME;
        if (IdentityUtil.threadLocalProperties.get().get(TENANT_NAME_FROM_CONTEXT) != null) {
            tenantDomain = (String) IdentityUtil.threadLocalProperties.get().get(TENANT_NAME_FROM_CONTEXT);
        }
        return tenantDomain;
    }

    /**
     * Build user object from tenant domain and username.
     *
     * @param tenantDomain
     * @param decodedUsername
     * @return
     */
    public static User getUser(String tenantDomain, String decodedUsername) {

        String realm = UserStoreConfigConstants.PRIMARY;
        String username = null;
        String[] strComponent = decodedUsername.split("/");

        if (strComponent.length == 1) {
            username = strComponent[0];
        } else if (strComponent.length == 2) {
            realm = strComponent[0];
            username = strComponent[1];
        } else {
//            throw new APIError(Response.Status.BAD_REQUEST, new ErrorResponse.Builder().withDescription("Provided " +
//                            "Username is not in the correct format.")
//                    .withCode(ERROR_CODE_INVALID_USERNAME.getCode())
//                    .withMessage(ERROR_CODE_INVALID_USERNAME.getMessage()).build());
        }

        User user = new User();
        user.setUserName(username);
        user.setUserStoreDomain(realm);
        user.setTenantDomain(tenantDomain);
        return user;
    }
}
