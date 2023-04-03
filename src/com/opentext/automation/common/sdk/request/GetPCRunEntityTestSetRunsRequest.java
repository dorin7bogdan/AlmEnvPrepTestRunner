package com.opentext.automation.common.sdk.request;

import com.opentext.automation.common.sdk.Client;

public class GetPCRunEntityTestSetRunsRequest extends GetRequest {
   public GetPCRunEntityTestSetRunsRequest(Client client, String runId) {
      super(client, runId);
   }

   protected String getSuffix() {
      return String.format("runs/%s", this._runId);
   }
}
