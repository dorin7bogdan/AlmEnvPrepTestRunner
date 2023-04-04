package com.opentext.automation.common.rest;

import com.opentext.automation.common.rest.RestClient;
import com.opentext.automation.common.SSEException;
import com.opentext.automation.common.sdk.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestAuthenticator {
   public static final String IS_AUTHENTICATED = "rest/is-authenticated";
   public static String AUTHENTICATE_HEADER = "WWW-Authenticate";

   public boolean login(Client client, String username, String password, Logger logger) {
      boolean ret = true;
      String authenticationPoint = this.isAuthenticated(client, logger);
      if (authenticationPoint != null) {
         Response response = this.login(client, authenticationPoint, username, password);
         if (response.isOk()) {
            this.logLoggedInSuccessfully(username, client.getServerUrl(), logger);
         } else {
            logger.log(String.format("Login to ALM Server at %s failed. Status Code: %s", client.getServerUrl(), response.getStatusCode()));
            ret = false;
         }
      }

      return ret;
   }

   private Response login(Client client, String loginUrl, String username, String password) {
      byte[] credBytes = (username + ":" + password).getBytes();
      String credEncodedString = "Basic " + Base64Encoder.encode(credBytes);
      Map<String, String> headers = new HashMap();
      headers.put("Authorization", credEncodedString);
      return client.httpGet(loginUrl, null, headers, ResourceAccessLevel.PUBLIC);
   }

   public boolean logout(RestClient client, String username) {
      Response response = client.httpGet(client.build("authentication-point/logout"), null, null, ResourceAccessLevel.PUBLIC);
      return response.isOk();
   }

   public String isAuthenticated(Client client, Logger logger) {
      Response response = client.httpGet(client.build("rest/is-authenticated"), null, null, ResourceAccessLevel.PUBLIC);
      int responseCode = response.getStatusCode();
      String ret;
      if (responseCode == 200) {
         ret = null;
         this.logLoggedInSuccessfully(client.getUsername(), client.getServerUrl(), logger);
      } else {
         if (responseCode != 401) {
            try {
               throw response.getFailure();
            } catch (Throwable t) {
               throw new SSEException(t);
            }
         }

         String newUrl = ((String)((List)response.getHeaders().get(AUTHENTICATE_HEADER)).get(0)).split("=")[1];
         newUrl = newUrl.replace("\"", "");
         newUrl = newUrl + "/authenticate";
         ret = newUrl;
      }

      return ret;
   }

   private void logLoggedInSuccessfully(String username, String loginServerUrl, Logger logger) {
      logger.log(String.format("Logged in successfully to ALM Server %s using %s", loginServerUrl, username));
   }
}
