package com.opentext.automation.common.autenv;

import com.opentext.automation.common.StringUtils;
import com.opentext.automation.common.SSEException;
import com.opentext.automation.common.XPathUtils;
import com.opentext.automation.common.autenv.request.get.GetAutEnvironmentByIdOldApiRequest;
import com.opentext.automation.common.autenv.request.get.GetAutEnvironmentByIdRequest;
import com.opentext.automation.common.autenv.request.get.GetAutEnvironmentConfigurationByIdRequest;
import com.opentext.automation.common.autenv.request.post.CreateAutEnvConfRequest;
import com.opentext.automation.common.model.AutEnvironmentConfigModel;
import com.opentext.automation.common.sdk.Client;
import com.opentext.automation.common.sdk.Logger;
import com.opentext.automation.common.sdk.Response;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class AUTEnvironmentManager {
    public static final String ALM_AUT_ENVIRONMENT_CONFIGURATION_ID_FIELD = "id";
    private Logger logger;
    private Client client;

    public AUTEnvironmentManager(Client client, Logger logger) {
        this.client = client;
        this.logger = logger;
    }

    public String getParametersRootFolderIdByAutEnvId(String autEnvironmentId) {
        String parametersRootFolderId = null;
        Response response = (new GetAutEnvironmentByIdRequest(this.client, autEnvironmentId)).execute();
        if (!response.isOk() && response.getStatusCode() == 404) {
            response = (new GetAutEnvironmentByIdOldApiRequest(this.client, autEnvironmentId)).execute();
        }

        try {
            List<Map<String, String>> entities = XPathUtils.toEntities(response.toString());
            if (!response.isOk() || entities.size() != 1) {
                throw new SSEException(String.format("Failed to get AUT Environment with ID: [%s]", autEnvironmentId), response.getFailure());
            }

            Map<String, String> autEnvironment = (Map)entities.get(0);
            parametersRootFolderId = autEnvironment == null ? null : (String)autEnvironment.get("root-app-param-folder-id");
        } catch (Throwable var6) {
            this.logger.log(String.format("Failed to parse response: %s", response));
        }

        return parametersRootFolderId;
    }

    public String createNewAutEnvironmentConfiguration(String autEnvironmentId, AutEnvironmentConfigModel autEnvironmentModel) {
        String newConfigurationName = !autEnvironmentModel.isUseExistingAutEnvConf() && !StringUtils.isNullOrEmpty(autEnvironmentModel.getAutEnvConf()) ? autEnvironmentModel.getAutEnvConf() : this.createTempConfigurationName();
        return this.createNewAutEnvironmentConfiguration(autEnvironmentId, newConfigurationName);
    }

    private String createNewAutEnvironmentConfiguration(String autEnvironmentId, String newAutEnvConfigurationName) {
        String newAutEnvironmentConfigurationId = null;
        Response response = (new CreateAutEnvConfRequest(this.client, autEnvironmentId, newAutEnvConfigurationName)).execute();
        if (!response.isOk()) {
            this.logger.log(String.format("Failed to create new AUT Environment Configuration named: [%s] for AUT Environment with id: [%s]", newAutEnvConfigurationName, autEnvironmentId));
            return null;
        } else {
            try {
                newAutEnvironmentConfigurationId = XPathUtils.getAttributeValue(response.toString(), "id");
            } catch (Throwable var6) {
                this.logger.log(String.format("Failed to parse response: %s", response));
            }

            return newAutEnvironmentConfigurationId;
        }
    }

    public boolean shouldUseExistingConfiguration(AutEnvironmentConfigModel autEnvironmentModel) {
        return autEnvironmentModel.isUseExistingAutEnvConf() && this.isAutEnvironmentConfigurationExists(autEnvironmentModel.getAutEnvConf());
    }

    private boolean isAutEnvironmentConfigurationExists(String existingAutEnvConfId) {
        Response response = (new GetAutEnvironmentConfigurationByIdRequest(this.client, existingAutEnvConfId)).execute();
        if (response.isOk() && XPathUtils.toEntities(response.toString()).size() == 1) {
            return true;
        } else {
            this.logger.log(String.format("Failed to get AUT Environment Configuration with ID: [%s]. Will try to create a new one", existingAutEnvConfId));
            return false;
        }
    }

    private String createTempConfigurationName() {
        return "Configuration_" + Calendar.getInstance().getTime().toString();
    }
}
