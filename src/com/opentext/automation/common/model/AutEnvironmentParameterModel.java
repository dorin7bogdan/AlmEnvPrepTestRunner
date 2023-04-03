package com.opentext.automation.common.model;

public class AutEnvironmentParameterModel {
    private final String name;
    private final String value;
    private final AutEnvironmentParameterType paramType;
    private final boolean shouldGetOnlyFirstValueFromJson;
    private String resolvedValue;

    public String getResolvedValue() {
        return this.resolvedValue;
    }

    public void setResolvedValue(String resolvedValue) {
        this.resolvedValue = resolvedValue;
    }

    public AutEnvironmentParameterModel(String name, String value, AutEnvironmentParameterType paramType, boolean shouldGetOnlyFirstValueFromJson) {
        this.name = name;
        this.value = value;
        this.paramType = paramType;
        this.shouldGetOnlyFirstValueFromJson = shouldGetOnlyFirstValueFromJson;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public AutEnvironmentParameterType getParamType() {
        return this.paramType;
    }

    public boolean isShouldGetOnlyFirstValueFromJson() {
        return this.shouldGetOnlyFirstValueFromJson;
    }
}
