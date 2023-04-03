package com.opentext.automation.common.sdk.handler;

//import com.ot.automation.common.model.CdaDetails;
//import com.ot.automation.common.sdk.ALMRunReportUrlBuilder;
//import com.ot.automation.common.sdk.Args;
import com.opentext.automation.common.sdk.Client;
import com.opentext.automation.common.sdk.Response;
import com.opentext.automation.common.sdk.RunResponse;
import com.opentext.automation.common.sdk.request.StartRunEntityRequest;
import com.opentext.automation.common.sdk.request.StopEntityRequest;

public abstract class RunHandler extends Handler {
   protected abstract String getStartSuffix();

   public abstract String getNameSuffix();

   public RunHandler(Client client, String entityId) {
      super(client, entityId);
   }

   public Response start(String duration, String postRunAction, String environmentConfigurationId/*, CdaDetails cdaDetails*/) {
      return (new StartRunEntityRequest(this._client, this.getStartSuffix(), this._entityId, duration, postRunAction, environmentConfigurationId/*, cdaDetails*/)).execute();
   }

   public Response stop() {
      return (new StopEntityRequest(this._client, this._runId)).execute();
   }

//   public String getReportUrl(Args args) {
//      return (new ALMRunReportUrlBuilder()).build(this._client, this._client.getServerUrl(), args.getDomain(), args.getProject(), this._runId);
//   }

   public RunResponse getRunResponse(Response response) {
      RunResponse ret = new RunResponse();
      ret.initialize(response);
      return ret;
   }
}
