package com.opentext.automation.common.model;

import java.util.List;

public class AutEnvironmentConfigModel {
    private final String almServerUrl;
    private final String almUserName;
    private final String almPassword;
    private final String almDomain;
    private final String almProject;
    private final boolean useExistingAutEnvConf;
    private final String autEnvID;
    private final String autEnvConf;
    private final String pathToJsonFile;
    private final List<AutEnvironmentParameterModel> autEnvironmentParameters;
    private String currentConfigID;

    public AutEnvironmentConfigModel(String almServerUrl, String almUserName, String almPassword, String almDomain, String almProject, boolean useExistingAutEnvConf, String autEnvID, String envConf, String pathToJsonFile, List<AutEnvironmentParameterModel> autEnvironmentParameters) {
        this.almUserName = almUserName;
        this.almPassword = almPassword;
        this.almDomain = almDomain;
        this.almProject = almProject;
        this.useExistingAutEnvConf = useExistingAutEnvConf;
        this.autEnvID = autEnvID;
        this.autEnvConf = envConf;
        this.pathToJsonFile = pathToJsonFile;
        this.almServerUrl = almServerUrl;
        this.autEnvironmentParameters = autEnvironmentParameters;
    }

    public String getAlmServerUrl() {
        return this.almServerUrl;
    }

    public String getAlmUserName() {
        return this.almUserName;
    }

    public String getAlmPassword() {
        return this.almPassword;
    }

    public String getAlmDomain() {
        return this.almDomain;
    }

    public String getAlmProject() {
        return this.almProject;
    }

    public boolean isUseExistingAutEnvConf() {
        return this.useExistingAutEnvConf;
    }

    public String getAutEnvID() {
        return this.autEnvID;
    }

    public String getAutEnvConf() {
        return this.autEnvConf;
    }

    public List<AutEnvironmentParameterModel> getAutEnvironmentParameters() {
        return this.autEnvironmentParameters;
    }

    public String getPathToJsonFile() {
        return this.pathToJsonFile;
    }

    public String getCurrentConfigID() {
        return this.currentConfigID;
    }

    public void setCurrentConfigID(String currentConfigID) {
        this.currentConfigID = currentConfigID;
    }
}
