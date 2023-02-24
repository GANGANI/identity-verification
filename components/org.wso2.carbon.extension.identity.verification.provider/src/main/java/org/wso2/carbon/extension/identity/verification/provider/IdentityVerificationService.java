package org.wso2.carbon.extension.identity.verification.provider;

import org.wso2.carbon.extension.identity.verification.provider.model.IdentityVerificationProvider;
import org.wso2.carbon.extension.identity.verification.provider.model.IdentityVerifierResponse;

import java.util.List;

public interface IdentityVerificationService {

    IdentityVerifierResponse verifyIdentity(String userId, String identityVerifierName)
            throws IdVProviderMgtException;

    IdentityVerificationProvider addIdVProvider(IdentityVerificationProvider identityVerificationProvider, int tenantId)
            throws IdVProviderMgtException;

    IdentityVerificationProvider getIdVProvider(String idVProviderId, int tenantId) throws IdVProviderMgtException;

    void deleteIdVProvider(String idVProviderId, int tenantId) throws IdVProviderMgtException;

    IdentityVerificationProvider updateIdVProvider(String idVProviderId,
                                                   IdentityVerificationProvider identityVerificationProvider,
                                                   int tenantId) throws IdVProviderMgtException;

    List<IdentityVerificationProvider> getIdVProviders(int tenantId) throws IdVProviderMgtException;
}
