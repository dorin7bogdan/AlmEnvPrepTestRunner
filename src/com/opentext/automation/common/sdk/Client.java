package com.opentext.automation.common.sdk;

import java.util.Map;

public interface Client {
    Response httpGet(String var1, String var2, Map<String, String> var3, ResourceAccessLevel var4);

    Response httpPost(String var1, byte[] var2, Map<String, String> var3, ResourceAccessLevel var4);

    Response httpPut(String var1, byte[] var2, Map<String, String> var3, ResourceAccessLevel var4);

    String build(String var1);

    String buildRestRequest(String var1);

    String buildWebUIRequest(String var1);

    String getServerUrl();

    String getUsername();

    String getXsrfTokenValue();
}