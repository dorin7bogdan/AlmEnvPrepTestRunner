package com.opentext.automation.common.sdk;

import com.opentext.automation.common.StringUtils;
import com.opentext.automation.common.autenv.AUTEnvironmentManager;
import com.opentext.automation.common.model.AutEnvironmentConfigModel;
import com.opentext.automation.common.model.AutEnvironmentParameterModel;
import com.opentext.automation.common.SSEException;
import com.opentext.automation.common.autenv.AUTEnvironmentParametersManager;
import com.opentext.automation.common.autenv.request.put.AUTEnvironmnentParameter;
import com.opentext.automation.common.rest.RestAuthenticator;

import java.util.Collection;
import java.util.List;

public class AUTEnvironmentBuilderPerformer {
    private final Client client;
    private final Logger logger;
    private final AutEnvironmentConfigModel model;

    public AUTEnvironmentBuilderPerformer(Client client, Logger logger, AutEnvironmentConfigModel model) {
        this.client = client;
        this.logger = logger;
        this.model = model;
    }

    public void start() throws Throwable {
        try {
            if (this.login()) {
                this.appendQCSessionCookies();
                this.performAutOperations();
            } else {
                throw new SSEException("Failed to login to ALM");
            }
        } catch (Throwable t) {
            this.logger.log(String.format("Failed to update ALM AUT Environment. Cause: %s", t.getMessage()));
            throw t;
        }
    }

    private boolean login() {
        boolean ret;
        try {
            ret = (new RestAuthenticator()).login(this.client, this.model.getAlmUserName(), this.model.getAlmPassword(), this.logger);
        } catch (Throwable t) {
            ret = false;
            this.logger.log(String.format("Failed login to ALM Server URL: %s. Exception: %s", this.model.getAlmServerUrl(), t.getMessage()));
        }

        return ret;
    }

    private void appendQCSessionCookies() {
        Response response = this.client.httpPost(this.client.build("rest/site-session"), null, null, ResourceAccessLevel.PUBLIC);
        if (!response.isOk()) {
            throw new SSEException("Cannot append QCSession cookies", response.getFailure());
        }
    }

    private void performAutOperations() {
        String autEnvironmentId = this.model.getAutEnvID();
        AUTEnvironmentManager autEnvironmentManager = new AUTEnvironmentManager(this.client, this.logger);
        String parametersRootFolderId = autEnvironmentManager.getParametersRootFolderIdByAutEnvId(autEnvironmentId);
        String autEnvironmentConfigurationId = this.getAutEnvironmentConfigurationId(autEnvironmentManager, autEnvironmentId);
        this.model.setCurrentConfigID(autEnvironmentConfigurationId);
        this.assignValuesToAutParameters(autEnvironmentConfigurationId, parametersRootFolderId);
    }

    private String getAutEnvironmentConfigurationId(AUTEnvironmentManager autEnvironmentManager, String autEnvironmentId) {
        String autEnvironmentConfigurationId = autEnvironmentManager.shouldUseExistingConfiguration(this.model) ? this.model.getAutEnvConf() : autEnvironmentManager.createNewAutEnvironmentConfiguration(autEnvironmentId, this.model);
        if (StringUtils.isNullOrEmpty(autEnvironmentConfigurationId)) {
            throw new SSEException("There's no AUT Environment Configuration in order to proceed");
        } else {
            return autEnvironmentConfigurationId;
        }
    }

    private void assignValuesToAutParameters(String autEnvironmentConfigurationId, String parametersRootFolderId) {
        List<AutEnvironmentParameterModel> confParams = this.model.getAutEnvironmentParameters();
        if (confParams != null && confParams.size() != 0) {
            AUTEnvironmentParametersManager parametersManager = new AUTEnvironmentParametersManager(this.client, confParams, parametersRootFolderId, autEnvironmentConfigurationId, this.model.getPathToJsonFile(), this.logger);
            Collection<AUTEnvironmnentParameter> parametersToUpdate = parametersManager.getParametersToUpdate();
            parametersManager.updateParametersValues(parametersToUpdate);
        } else {
            this.logger.log("There's no AUT Environment parameters to assign for this build...");
        }
    }
}
