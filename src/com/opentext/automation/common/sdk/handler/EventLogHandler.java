package com.opentext.automation.common.sdk.handler;

import com.opentext.automation.common.XPathUtils;
import com.opentext.automation.common.sdk.Client;
import com.opentext.automation.common.sdk.Logger;
import com.opentext.automation.common.sdk.Response;
import com.opentext.automation.common.sdk.request.EventLogRequest;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EventLogHandler extends Handler {
   private String _timeslotId = "";
   private int _lastRead = -1;

   public EventLogHandler(Client client, String timeslotId) {
      super(client, timeslotId);
      this._timeslotId = timeslotId;
   }

   public boolean log(Logger logger) {
      boolean ret = false;
      Response eventLog = null;

      try {
         eventLog = this.getEventLog();
         String xml = eventLog.toString();
         List<Map<String, String>> entities = XPathUtils.toEntities(xml);
         Iterator var6 = entities.iterator();

         while(var6.hasNext()) {
            Map<String, String> currEntity = (Map)var6.next();
            if (this.isNew(currEntity)) {
               logger.log(String.format("%s:%s", currEntity.get("creation-time"), currEntity.get("description")));
            }
         }

         ret = true;
      } catch (Throwable var8) {
         logger.log(String.format("Failed to print Event Log: %s (run id: %s, reservation id: %s). Cause: %s", eventLog, this._runId, this._timeslotId, var8));
      }

      return ret;
   }

   private boolean isNew(Map<String, String> currEntity) {
      boolean ret = false;
      int currEvent = Integer.parseInt((String)currEntity.get("id"));
      if (currEvent > this._lastRead) {
         this._lastRead = currEvent;
         ret = true;
      }

      return ret;
   }

   private Response getEventLog() {
      return (new EventLogRequest(this._client, this._timeslotId)).execute();
   }
}
