package com.opentext.automation.common.sdk.request;

import com.opentext.automation.common.sdk.Client;

public class GetPCRunEntityDataRequest extends GetRequest {
   public GetPCRunEntityDataRequest(Client client, String runId) {
      super(client, runId);
   }

   protected String getSuffix() {
      return String.format("runs/%s", this._runId);
   }
}
