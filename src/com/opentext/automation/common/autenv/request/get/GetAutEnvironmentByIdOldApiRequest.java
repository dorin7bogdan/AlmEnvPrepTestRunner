package com.opentext.automation.common.autenv.request.get;

import com.opentext.automation.common.sdk.Client;
import com.opentext.automation.common.sdk.request.GeneralGetRequest;

public class GetAutEnvironmentByIdOldApiRequest extends GeneralGetRequest {
   private final String autEnvironmentId;

   public GetAutEnvironmentByIdOldApiRequest(Client client, String autEnvironmentId) {
      super(client);
      this.autEnvironmentId = autEnvironmentId;
   }

   protected String getSuffix() {
      return "AppParamSets";
   }

   protected String getQueryString() {
      return String.format("query={id[%s]}", this.autEnvironmentId);
   }
}
