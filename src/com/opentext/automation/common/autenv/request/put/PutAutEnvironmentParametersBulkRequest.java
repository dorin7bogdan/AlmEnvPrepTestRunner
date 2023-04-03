package com.opentext.automation.common.autenv.request.put;

import com.opentext.automation.common.sdk.Client;
import com.opentext.automation.common.sdk.request.GeneralPutBulkRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PutAutEnvironmentParametersBulkRequest extends GeneralPutBulkRequest {
   private Collection<AUTEnvironmnentParameter> parameters;

   public PutAutEnvironmentParametersBulkRequest(Client client, Collection<AUTEnvironmnentParameter> parameters) {
      super(client);
      this.parameters = parameters;
   }

   protected List<Map<String, String>> getFields() {
      List<Map<String, String>> fieldsToUpdate = new ArrayList();
      Iterator var2 = this.parameters.iterator();

      while(var2.hasNext()) {
         AUTEnvironmnentParameter autEnvironmnentParameter = (AUTEnvironmnentParameter)var2.next();
         Map<String, String> mapOfValues = new HashMap();
         mapOfValues.put("id", autEnvironmnentParameter.getId());
         mapOfValues.put("value", autEnvironmnentParameter.getValue());
         fieldsToUpdate.add(mapOfValues);
      }

      return fieldsToUpdate;
   }

   protected String getSuffix() {
      return "aut-environment-parameter-values";
   }
}
