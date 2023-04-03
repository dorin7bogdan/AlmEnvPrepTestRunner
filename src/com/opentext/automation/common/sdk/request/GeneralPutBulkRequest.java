package com.opentext.automation.common.sdk.request;

import com.opentext.automation.common.rest.RESTConstants;
import com.opentext.automation.common.rest.RestXmlUtils;
import com.opentext.automation.common.sdk.Client;
import com.opentext.automation.common.sdk.ResourceAccessLevel;
import com.opentext.automation.common.sdk.Response;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class GeneralPutBulkRequest extends GeneralRequest {
   protected GeneralPutBulkRequest(Client client) {
      super(client);
   }

   protected abstract List<Map<String, String>> getFields();

   protected Map<String, String> getHeaders() {
      Map<String, String> ret = new HashMap();
      ret.put(RESTConstants.CONTENT_TYPE, RESTConstants.APP_XML_BULK);
      ret.put(RESTConstants.ACCEPT, RESTConstants.APP_XML);
      ret.put(RESTConstants.X_XSRF_TOKEN, _client.getXsrfTokenValue());
      return ret;
   }

   protected Response perform() {
      return this._client.httpPut(this.getUrl(), this.getDataBytes(), this.getHeaders(), ResourceAccessLevel.PROTECTED);
   }

   private byte[] getDataBytes() {
      StringBuilder builder = new StringBuilder("<Entities>");
      for (Map<String, String> values : getFields()) {
         builder.append("<Entity><Fields>");
         for (String key : values.keySet()) {
            builder.append(RestXmlUtils.fieldXml(key, values.get(key)));
         }
         builder.append("</Fields></Entity>");
      }
      return builder.append("</Entities>").toString().getBytes();
   }
}
