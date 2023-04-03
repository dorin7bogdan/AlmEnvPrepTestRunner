package com.opentext.automation.common.sdk.request;

import com.opentext.automation.common.sdk.Client;

public class PollTimeslotRequest extends GetRequest {
   private final String _timeslotId;

   public PollTimeslotRequest(Client client, String timeslotId) {
      super(client, timeslotId);
      this._timeslotId = timeslotId;
   }

   protected String getSuffix() {
      return String.format("reservations/%s", this._timeslotId);
   }
}
