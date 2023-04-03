package com.opentext.automation.common.sdk;

import com.opentext.automation.common.StringUtils;
import com.opentext.automation.common.XPathUtils;

public class RunResponse {
   private String _successStatus;
   private String _runId;

   public void initialize(Response response) {
      String xml = response.toString();
      this._successStatus = XPathUtils.getAttributeValue(xml, "SuccessStaus");
      this._runId = this.parseRunId(XPathUtils.getAttributeValue(xml, "info"));
   }

   protected String parseRunId(String runIdResponse) {
      String ret = runIdResponse;
      if (StringUtils.isNullOrEmpty(runIdResponse)) {
         ret = "No Run ID";
      }

      return ret;
   }

   public String getRunId() {
      return this._runId;
   }

   public boolean isSucceeded() {
      return "1".equals(this._successStatus);
   }
}
