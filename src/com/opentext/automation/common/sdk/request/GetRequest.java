package com.opentext.automation.common.sdk.request;

import com.opentext.automation.common.sdk.Client;

public abstract class GetRequest extends GeneralGetRequest {
   protected final String _runId;

   protected GetRequest(Client client, String runId) {
      super(client);
      this._runId = runId;
   }
}
