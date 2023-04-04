package com.opentext.automation.common.autenv.request.get;

import com.opentext.automation.common.sdk.Client;
import com.opentext.automation.common.sdk.request.GeneralGetRequest;

public class GetAutEnvFoldersByIdRequest extends GeneralGetRequest {
   private final String folderId;

   public GetAutEnvFoldersByIdRequest(Client client, String folderId) {
      super(client);
      this.folderId = folderId;
   }

   protected String getSuffix() {
      return "aut-environment-parameter-folders";
   }

   protected String getQueryString() {
      return String.format("query={id[%s]}&page-size=2000", this.folderId);
   }
}
