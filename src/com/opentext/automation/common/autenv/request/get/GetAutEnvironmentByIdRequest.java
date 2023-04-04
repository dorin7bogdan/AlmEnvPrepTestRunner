package com.opentext.automation.common.autenv.request.get;

import com.opentext.automation.common.sdk.Client;
import com.opentext.automation.common.sdk.request.GeneralGetRequest;

public class GetAutEnvironmentByIdRequest extends GeneralGetRequest {
   private final String autEnvironmentId;

   public GetAutEnvironmentByIdRequest(Client client, String autEnvironmentId) {
      super(client);
      this.autEnvironmentId = autEnvironmentId;
   }

   protected String getSuffix() {
      return "aut-environments";
   }

   protected String getQueryString() {
      return String.format("query={id[%s]}", this.autEnvironmentId);
   }
}
