/*
* Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.wso2.carbon.extension.identity.verification.api.rest.v1.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.extension.identity.verification.api.rest.v1.IdentityVerificationProvidersApiService;
import org.wso2.carbon.extension.identity.verification.api.rest.v1.core.IdentityVerificationProviderService;
import org.wso2.carbon.extension.identity.verification.api.rest.v1.model.IdentityVerificationProvider;
import org.wso2.carbon.extension.identity.verification.api.rest.v1.model.Patch;

import java.util.List;
import javax.ws.rs.core.Response;

public class IdentityVerificationProvidersApiServiceImpl implements IdentityVerificationProvidersApiService {

    @Autowired
    private IdentityVerificationProviderService identityVerificationProviderService;

    @Override
    public Response addIDV(IdentityVerificationProvider identityVerificationProvider) {

//        identityVerificationProviderService.();
        return Response.ok().entity("").build();
    }

    @Override
    public Response deleteIDV(String identityVerificationProviderId) {

        identityVerificationProviderService.deleteIDV(identityVerificationProviderId);
        return Response.noContent().build();
    }

    @Override
    public Response getIDV(String identityVerificationProviderId) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response getIDVs(Integer limit, Integer offset) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response patchIDV(String identityVerificationProviderId, List<Patch> patch) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response putIDV(String identityVerificationProviderId, IdentityVerificationProvider identityVerificationProvider) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }
}
