package com.opentext.automation.common.sdk.request;

import com.opentext.automation.common.sdk.Client;

public class GetLabRunEntityDataRequest extends GetRequest {
   public GetLabRunEntityDataRequest(Client client, String runId) {
      super(client, runId);
   }

   protected String getSuffix() {
      return String.format("procedure-runs/%s", this._runId);
   }
}
