package com.opentext.automation.common.autenv.request.get;

import com.opentext.automation.common.sdk.Client;
import com.opentext.automation.common.sdk.request.GeneralGetRequest;

public class GetAutEnvironmentConfigurationByIdRequest extends GeneralGetRequest {
   private final String autEnvironmentConfigurationId;

   public GetAutEnvironmentConfigurationByIdRequest(Client client, String autEnvironmentConfigurationId) {
      super(client);
      this.autEnvironmentConfigurationId = autEnvironmentConfigurationId;
   }

   protected String getSuffix() {
      return "aut-environment-configurations";
   }

   protected String getQueryString() {
      return String.format("query={id[%s]}", this.autEnvironmentConfigurationId);
   }
}
