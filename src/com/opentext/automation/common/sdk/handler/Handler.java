package com.opentext.automation.common.sdk.handler;

import com.opentext.automation.common.sdk.Client;

public abstract class Handler {
   protected final Client _client;
   protected final String _entityId;
   protected String _runId;
   protected String _timeslotId;

   public Handler(Client client, String entityId) {
      this._runId = "";
      this._timeslotId = "";
      this._client = client;
      this._entityId = entityId;
   }

   public Handler(Client client, String entityId, String runId) {
      this(client, entityId);
      this._runId = runId;
   }

   public String getRunId() {
      return this._runId;
   }

   public String getEntityId() {
      return this._entityId;
   }

   public void setRunId(String runId) {
      this._runId = runId;
   }

   public String getTimeslotId() {
      return this._timeslotId;
   }

   public void setTimeslotId(String timeslotId) {
      this._timeslotId = timeslotId;
   }
}
