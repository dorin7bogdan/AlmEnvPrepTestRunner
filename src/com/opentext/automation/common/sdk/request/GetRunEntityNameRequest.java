package com.opentext.automation.common.sdk.request;

import com.opentext.automation.common.sdk.Client;

public class GetRunEntityNameRequest extends GetRequest {
   private final String _nameSuffix;

   public GetRunEntityNameRequest(Client client, String suffix, String entityId) {
      super(client, entityId);
      this._nameSuffix = suffix;
   }

   protected String getSuffix() {
      return this._nameSuffix;
   }
}
