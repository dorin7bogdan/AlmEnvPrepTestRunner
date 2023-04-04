package com.opentext.automation.common.autenv.request.post;

import com.opentext.automation.common.Pair;
import com.opentext.automation.common.sdk.Client;
import com.opentext.automation.common.sdk.request.GeneralPostRequest;

import java.util.ArrayList;
import java.util.List;

public class CreateAutEnvConfRequest extends GeneralPostRequest {
   private final String autEnvironmentId;
   private final String name;

   public CreateAutEnvConfRequest(Client client, String autEnvironmentId, String name) {
      super(client);
      this.autEnvironmentId = autEnvironmentId;
      this.name = name;
   }

   protected String getSuffix() {
      return "aut-environment-configurations";
   }

   protected List<Pair<String, String>> getDataFields() {
      List<Pair<String, String>> ret = new ArrayList();
      ret.add(new Pair("app-param-set-id", this.autEnvironmentId));
      ret.add(new Pair("name", this.name));
      return ret;
   }
}
