package org.wso2.carbon.extension.identity.verification.provider.internal.service.impl;

import org.wso2.carbon.extension.identity.verification.provider.IdVProviderMgtException;
import org.wso2.carbon.extension.identity.verification.provider.IdentityVerificationService;
import org.wso2.carbon.extension.identity.verification.provider.IdentityVerifierFactory;
import org.wso2.carbon.extension.identity.verification.provider.dao.IdVProviderManagementDAO;
import org.wso2.carbon.extension.identity.verification.provider.internal.IdVProviderMgtDataHolder;
import org.wso2.carbon.extension.identity.verification.provider.model.IdentityVerificationProvider;
import org.wso2.carbon.extension.identity.verification.provider.model.IdentityVerifierResponse;

import java.util.List;

public class IdentityVerificationServiceImpl implements IdentityVerificationService {

    IdVProviderManagementDAO idVProviderManagementDAO = new IdVProviderManagementDAO();

    @Override
    public IdentityVerifierResponse verifyIdentity(String userId, String identityVerifierName) throws IdVProviderMgtException {

        IdentityVerifierFactory factory = IdVProviderMgtDataHolder.getIdentityVerifierFactory(identityVerifierName);
        return factory.getIdentityVerifier(identityVerifierName).verifyIdentity(userId, identityVerifierName);
    }

    public IdentityVerificationProvider addIdVProvider(IdentityVerificationProvider identityVerificationProvider,
                                                       int tenantId) throws IdVProviderMgtException {

        idVProviderManagementDAO.addIdVProvider(identityVerificationProvider, tenantId);
        return identityVerificationProvider;
    }

    public IdentityVerificationProvider getIdVProvider(String idVProviderId, int tenantId)
            throws IdVProviderMgtException {

        return idVProviderManagementDAO.getIdVProvider(idVProviderId, tenantId);
    }

    public void deleteIdVProvider(String idVProviderId, int tenantId) throws IdVProviderMgtException {

        idVProviderManagementDAO.deleteIdVProvider(idVProviderId, tenantId);
    }

    public IdentityVerificationProvider updateIdVProvider(String idVProviderId,
                                                          IdentityVerificationProvider identityVerificationProvider,
                                                          int tenantId) throws IdVProviderMgtException {

        idVProviderManagementDAO.updateIdVProvider(idVProviderId, tenantId, identityVerificationProvider);
        return identityVerificationProvider;
    }

    public List<IdentityVerificationProvider> getIdVProviders(int tenantId)
            throws IdVProviderMgtException {

        return idVProviderManagementDAO.getIdVProviders(tenantId);
    }
}
