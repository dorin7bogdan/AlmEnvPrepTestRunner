package com.opentext.automation.common.sdk.request;

import com.opentext.automation.common.sdk.Client;

public class EventLogRequest extends GetRequest {
   private final String _timeslotId;

   public EventLogRequest(Client client, String timeslotId) {
      super(client, timeslotId);
      this._timeslotId = timeslotId;
   }

   protected String getSuffix() {
      return String.format("event-log-reads?query={context[\"*Timeslot:%%20%s%%3B*\"]}&fields=id,event-type,creation-time,action,description", this._timeslotId);
   }
}
