package com.opentext.automation.common.sdk.handler;

import com.opentext.automation.common.sdk.Logger;
import com.opentext.automation.common.SSEException;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import java.io.FileInputStream;
import java.io.InputStream;
import net.minidev.json.JSONArray;
import org.apache.commons.io.IOUtils;

public class JsonHandler {
   private Logger logger;

   public JsonHandler(Logger logger) {
      this.logger = logger;
   }

   public Object load(String path) {
      this.logger.log(String.format("Loading JSON file from: [%s]", path));

      try {
         InputStream is = new FileInputStream(path);
         String jsonTxt = IOUtils.toString((InputStream)is, "UTF-8");
         Object parsedJson = Configuration.defaultConfiguration().addOptions(Option.ALWAYS_RETURN_LIST).jsonProvider().parse(jsonTxt);
         return parsedJson;
      } catch (Throwable var5) {
         throw new SSEException(String.format("Failed to load JSON from: [%s]", path), var5);
      }
   }

   public String getValueFromJsonAsString(Object jsonObject, String pathToRead, boolean shouldGetSingleValueOnly) {
      String value = "";

      try {
         Object extractedObject;
         for(extractedObject = JsonPath.read(jsonObject, pathToRead); extractedObject instanceof JSONArray && shouldGetSingleValueOnly; extractedObject = ((JSONArray)extractedObject).get(0)) {
         }

         value = extractedObject.toString();
      } catch (Throwable var6) {
         this.logger.log(String.format("Failed to get the value of [%s] from the JSON file.\n\tError was: %s", pathToRead, var6.getMessage()));
      }

      return value;
   }
}
