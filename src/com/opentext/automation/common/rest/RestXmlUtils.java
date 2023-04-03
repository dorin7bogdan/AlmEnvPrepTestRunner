package com.opentext.automation.common.rest;

import java.util.HashMap;
import java.util.Map;

public class RestXmlUtils {
   public static String fieldXml(String field, String value) {
      return String.format("<Field Name=\"%s\"><Value>%s</Value></Field>", field, value);
   }

   public static Map<String, String> getAppXmlHeaders() {
      Map<String, String> ret = new HashMap();
      ret.put(RESTConstants.CONTENT_TYPE, RESTConstants.APP_XML);
      ret.put(RESTConstants.ACCEPT, RESTConstants.APP_XML);
      return ret;
   }
}
