package com.opentext.automation.common.autenv.request.get;

import com.opentext.automation.common.sdk.Client;
import com.opentext.automation.common.sdk.request.GeneralGetRequest;

public class GetParametersByAutEnvConfIdRequest extends GeneralGetRequest {
   String configurationId;

   public GetParametersByAutEnvConfIdRequest(Client client, String configurationId) {
      super(client);
      this.configurationId = configurationId;
   }

   protected String getSuffix() {
      return "aut-environment-parameter-values";
   }

   protected String getQueryString() {
      return String.format("query={app-param-value-set-id[%s]}&page-size=2000", this.configurationId);
   }
}
