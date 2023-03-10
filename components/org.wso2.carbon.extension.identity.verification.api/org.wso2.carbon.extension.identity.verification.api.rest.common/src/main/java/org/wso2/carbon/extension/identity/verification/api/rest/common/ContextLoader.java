/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.com). All Rights Reserved.
 *
 * This software is the property of WSO2 Inc. and its suppliers, if any.
 * Dissemination of any information or reproduction of any material contained
 * herein in any form is strictly forbidden, unless permitted by WSO2 expressly.
 * You may not alter or remove any copyright or other notice from copies of this content.
 */

package org.wso2.carbon.extension.identity.verification.api.rest.common;

import org.wso2.carbon.identity.core.util.IdentityUtil;

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
}
