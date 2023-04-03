package com.opentext.automation.common.sdk.request;

import com.opentext.automation.common.sdk.Client;

public abstract class PostRequest extends GeneralPostRequest {
   protected final String _runId;

   protected PostRequest(Client client, String runId) {
      super(client);
      this._runId = runId;
   }
}
