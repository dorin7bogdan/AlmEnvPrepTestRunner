package com.opentext.automation.common.sdk.request;

import com.opentext.automation.common.rest.RESTConstants;
import com.opentext.automation.common.sdk.Client;
import com.opentext.automation.common.sdk.Response;

import java.util.HashMap;
import java.util.Map;

public abstract class GeneralRequest {
   protected final Client _client;

   protected GeneralRequest(Client client) {
      this._client = client;
   }

   public final Response execute() {
      Response ret = new Response();

      try {
         ret = this.perform();
      } catch (Throwable t) {
         ret.setFailure(t);
      }

      return ret;
   }

   protected abstract Response perform();

   protected String getSuffix() {
      return null;
   }

   protected Map<String, String> getHeaders() {
      Map<String, String> ret = new HashMap<String, String>();
      ret.put(RESTConstants.X_XSRF_TOKEN, _client.getXsrfTokenValue());
      return ret;
   }

   protected String getBody() {
      return null;
   }

   protected String getUrl() {
      return this._client.buildRestRequest(this.getSuffix());
   }
}
