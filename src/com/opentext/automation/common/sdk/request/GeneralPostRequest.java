package com.opentext.automation.common.sdk.request;

import com.opentext.automation.common.Pair;
import com.opentext.automation.common.rest.RESTConstants;
import com.opentext.automation.common.rest.RestXmlUtils;
import com.opentext.automation.common.sdk.Client;
import com.opentext.automation.common.sdk.ResourceAccessLevel;
import com.opentext.automation.common.sdk.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class GeneralPostRequest extends GeneralRequest {
   protected GeneralPostRequest(Client client) {
      super(client);
   }

   protected Map<String, String> getHeaders() {
      Map<String, String> ret = new HashMap();
      ret.put(RESTConstants.CONTENT_TYPE, RESTConstants.APP_XML);
      ret.put(RESTConstants.ACCEPT, RESTConstants.APP_XML);
      ret.put(RESTConstants.X_XSRF_TOKEN, _client.getXsrfTokenValue());
      return ret;
   }

   public Response perform() {
      return this._client.httpPost(this.getUrl(), this.getDataBytes(), this.getHeaders(), ResourceAccessLevel.PROTECTED);
   }

   private byte[] getDataBytes() {
      StringBuilder builder = new StringBuilder("<Entity><Fields>");
      Iterator var2 = this.getDataFields().iterator();

      while(var2.hasNext()) {
         Pair<String, String> currPair = (Pair)var2.next();
         builder.append(RestXmlUtils.fieldXml(currPair.getFirst(), currPair.getSecond()));
      }

      return builder.append("</Fields></Entity>").toString().getBytes();
   }

   protected List<Pair<String, String>> getDataFields() {
      return new ArrayList(0);
   }
}
