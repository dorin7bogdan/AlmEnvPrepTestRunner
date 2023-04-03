package com.opentext.automation.common.sdk.request;

import com.opentext.automation.common.sdk.Client;

public class StopEntityRequest extends PostRequest {
   public StopEntityRequest(Client client, String runId) {
      super(client, runId);
   }

   protected String getSuffix() {
      return String.format("procedure-runs/%s/stop", this._runId);
   }
}
