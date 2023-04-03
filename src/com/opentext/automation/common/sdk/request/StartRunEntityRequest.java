package com.opentext.automation.common.sdk.request;

import com.opentext.automation.common.Pair;
import com.opentext.automation.common.StringUtils;
import com.opentext.automation.common.sdk.Client;
//import com.ot.automation.common.model.CdaDetails;
import java.util.ArrayList;
import java.util.List;

public class StartRunEntityRequest extends PostRequest {
   private final String _duration;
   private final String _suffix;
   private final String _environmentConfigurationId;
   //private final CdaDetails _cdaDetails;

   public StartRunEntityRequest(Client client, String suffix, String runId, String duration, String postRunAction, String environmentConfigurationId/*, CdaDetails cdaDetails*/) {
      super(client, runId);
      this._duration = duration;
      this._suffix = suffix;
      this._environmentConfigurationId = environmentConfigurationId;
      //this._cdaDetails = cdaDetails;
   }

   protected List<Pair<String, String>> getDataFields() {
      List<Pair<String, String>> ret = new ArrayList();
      ret.add(new Pair("duration", this._duration));
      ret.add(new Pair("vudsMode", "false"));
      ret.add(new Pair("reservationId", "-1"));
      if (!StringUtils.isNullOrEmpty(this._environmentConfigurationId)) {
         ret.add(new Pair("valueSetId", this._environmentConfigurationId));
         //if (this._cdaDetails != null) {
            //ret.add(new Pair("topologyAction", this._cdaDetails.getTopologyAction()));
            //ret.add(new Pair("realizedTopologyName", this._cdaDetails.getDeployedEnvironmentName()));
         //}
      }

      return ret;
   }

   protected String getSuffix() {
      return this._suffix;
   }
}
