package com.opentext.automation.common.sdk.request;

import com.opentext.automation.common.sdk.Client;

public class GetLabRunEntityTestSetRunsRequest extends GetRequest {
   public GetLabRunEntityTestSetRunsRequest(Client client, String runId) {
      super(client, runId);
   }

   protected String getSuffix() {
      return "procedure-testset-instance-runs";
   }

   protected String getQueryString() {
      return String.format("query={procedure-run[%s]}&page-size=2000", this._runId);
   }
}
