package com.opentext.automation.common.sdk;

import java.util.List;
import java.util.Map;

public class Response {
    private Map<String, List<String>> _headers;
    private byte[] _data;
    private Throwable _failure;
    private int _statusCode;

    public Response() {
        this(null, null, null, -1);
    }

    public Response(Exception failure) {
        this(null, null, failure, -1);
    }

    public Response(Map<String, List<String>> headers, byte[] data, Exception failure, int statusCode) {
        this._statusCode = -1;
        this._headers = headers;
        this._data = data;
        this._failure = failure;
        this._statusCode = statusCode;
    }

    public Map<String, List<String>> getHeaders() {
        return this._headers;
    }

    public void setHeaders(Map<String, List<String>> responseHeaders) {
        this._headers = responseHeaders;
    }

    public byte[] getData() {
        return this._data;
    }

    public void setData(byte[] data) {
        this._data = data;
    }

    public Throwable getFailure() {
        return this._failure;
    }

    public void setFailure(Throwable cause) {
        this._failure = cause;
    }

    public int getStatusCode() {
        return this._statusCode;
    }

    public void setStatusCode(int statusCode) {
        this._statusCode = statusCode;
    }

    public boolean isOk() {
        return this.getFailure() == null && (this.getStatusCode() == 200 || this.getStatusCode() == 201 || this.getStatusCode() == 202);
    }

    public String toString() {
        return new String(this._data);
    }
}
