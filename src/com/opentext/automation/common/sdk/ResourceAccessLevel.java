package com.opentext.automation.common.sdk;

import com.opentext.automation.common.rest.RESTConstants;

public enum ResourceAccessLevel {
    PUBLIC(null),
    PROTECTED(RESTConstants.PtaL),
    PRIVATE(RESTConstants.PvaL);

    private String _headerName;

    private ResourceAccessLevel(String headerName) {
        this._headerName = headerName;
    }

    public String getUserHeaderName() {
        return this._headerName;
    }
}
