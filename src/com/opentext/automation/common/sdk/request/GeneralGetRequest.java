package com.opentext.automation.common.sdk.request;

import com.opentext.automation.common.sdk.Client;
import com.opentext.automation.common.sdk.ResourceAccessLevel;
import com.opentext.automation.common.sdk.Response;

public abstract class GeneralGetRequest extends GeneralRequest {
   protected GeneralGetRequest(Client client) {
      super(client);
   }

   protected String getQueryString() {
      return null;
   }

   public Response perform() {
      return this._client.httpGet(this.getUrl(), this.getQueryString(), this.getHeaders(), ResourceAccessLevel.PROTECTED);
   }
}
