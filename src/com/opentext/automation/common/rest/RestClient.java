package com.opentext.automation.common.rest;

import com.opentext.automation.common.sdk.Client;
import com.opentext.automation.common.sdk.ResourceAccessLevel;
import com.opentext.automation.common.sdk.Response;
import com.opentext.automation.common.SSEException;
import com.opentext.automation.common.sdk.HttpRequestDecorator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class RestClient implements Client {
   private final String _serverUrl;
   protected Map<String, String> _cookies = new HashMap();
   private final String _restPrefix;
   private final String _webuiPrefix;
   private final String _username;
   private final String XSRF_TOKEN_VALUE;

   /**
    * Configure SSL context for the client.
    */
   static {
      // First create a trust manager that won't care.
      X509TrustManager trustManager = new X509TrustManager() {
         @Override
         public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            // Don't do anything.
         }
         @Override
         public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            // Don't do anything.
         }
         @Override
         public X509Certificate[] getAcceptedIssuers() {
            // Don't do anything.
            return null;
         }
      };
      // Now put the trust manager into an SSLContext.
      SSLContext sslcontext;
      try {
         sslcontext = SSLContext.getInstance("SSL");
         sslcontext.init(null, new TrustManager[] { trustManager }, null);
      } catch (KeyManagementException | NoSuchAlgorithmException e) {
         throw new SSEException(e);
      }

      HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());

      //Ignore hostname verify
      HttpsURLConnection.setDefaultHostnameVerifier(
              new HostnameVerifier(){
                 public boolean verify(String hostname, SSLSession sslSession) {
                    return true;
                 }
              }
      );
   }

   public RestClient(String url, String domain, String project, String username) {
      if (!url.endsWith("/")) {
         url = String.format("%s/", url);
      }

      this._serverUrl = url;
      this._username = username;
      this._restPrefix = this.getPrefixUrl("rest", String.format("domains/%s", domain), String.format("projects/%s", project));
      this._webuiPrefix = this.getPrefixUrl("webui/alm", domain, project);

      XSRF_TOKEN_VALUE = UUID.randomUUID().toString();
      _cookies.put(RESTConstants.XSRF_TOKEN, XSRF_TOKEN_VALUE);
   }

   public String getXsrfTokenValue() {
      return XSRF_TOKEN_VALUE;
   }

   public String build(String suffix) {
      return String.format("%1$s%2$s", this._serverUrl, suffix);
   }

   public String buildRestRequest(String suffix) {
      return String.format("%1$s/%2$s", this._restPrefix, suffix);
   }

   public String buildWebUIRequest(String suffix) {
      return String.format("%1$s/%2$s", this._webuiPrefix, suffix);
   }

   public Response httpGet(String url, String queryString, Map<String, String> headers, ResourceAccessLevel resourceAccessLevel) {
      Response ret = null;

      try {
         ret = this.doHttp(RESTConstants.GET, url, queryString, null, headers, resourceAccessLevel);
         return ret;
      } catch (Exception var7) {
         throw new SSEException(var7);
      }
   }

   public Response httpPost(String url, byte[] data, Map<String, String> headers, ResourceAccessLevel resourceAccessLevel) {
      Response ret = null;

      try {
         ret = this.doHttp(RESTConstants.POST, url, null, data, headers, resourceAccessLevel);
         return ret;
      } catch (Exception var7) {
         throw new SSEException(var7);
      }
   }

   public Response httpPut(String url, byte[] data, Map<String, String> headers, ResourceAccessLevel resourceAccessLevel) {
      Response ret = null;

      try {
         ret = this.doHttp(RESTConstants.PUT, url, null, data, headers, resourceAccessLevel);
         return ret;
      } catch (Exception var7) {
         throw new SSEException(var7);
      }
   }

   public String getServerUrl() {
      return this._serverUrl;
   }

   private String getPrefixUrl(String protocol, String domain, String project) {
      return String.format("%s%s/%s/%s", this._serverUrl, protocol, domain, project);
   }

   private Response doHttp(String type, String url, String queryString, byte[] data, Map<String, String> headers, ResourceAccessLevel resourceAccessLevel) {
      if (queryString != null && !queryString.isEmpty()) {
         url = url + "?" + queryString;
      }

      try {
         //System.out.println(type + " " + url);
         HttpURLConnection connection = (HttpURLConnection)openConnection(url);
         connection.setRequestMethod(type);
         Map<String, String> decoratedHeaders = new HashMap();
         if (headers != null) {
            decoratedHeaders.putAll(headers);
         }

         HttpRequestDecorator.decorateHeaderWithUserInfo(decoratedHeaders, this.getUsername(), resourceAccessLevel);
         this.prepareHttpRequest(connection, decoratedHeaders, data);
         connection.connect();
         Response ret = this.retrieveHtmlResponse(connection);
         //System.out.println(ret.getStatusCode() + " " + new String(ret.getData(), StandardCharsets.UTF_8));
         this.updateCookies(ret);
         return ret;
      } catch (Exception var10) {
         throw new SSEException(var10);
      }
   }

   private void prepareHttpRequest(HttpURLConnection connnection, Map<String, String> headers, byte[] bytes) {
      String cookies = getCookies();
      connnection.setRequestProperty(RESTConstants.COOKIE, cookies);
      this.setConnectionHeaders(connnection, headers);
      this.setConnectionData(connnection, bytes);

      /*System.out.println("Cookies:");
      System.out.println(cookies);
      System.out.println("Headers:");
      for (Map.Entry<String,String> entry : headers.entrySet()) {
         System.out.println(entry.getKey() + "=" + entry.getValue());
      }*/
   }

   private void setConnectionData(HttpURLConnection connnection, byte[] bytes) {
      if (bytes != null && bytes.length > 0) {
         connnection.setDoOutput(true);

         try {
            OutputStream out = connnection.getOutputStream();
            out.write(bytes);
            out.flush();
            out.close();
         } catch (Exception var4) {
            throw new SSEException(var4);
         }
      }

   }

   private void setConnectionHeaders(HttpURLConnection connnection, Map<String, String> headers) {
      if (headers != null) {
         Iterator headersIterator = headers.entrySet().iterator();

         while(headersIterator.hasNext()) {
            Entry<String, String> header = (Entry)headersIterator.next();
            connnection.setRequestProperty(header.getKey(), header.getValue());
         }
      }

   }

   private Response retrieveHtmlResponse(HttpURLConnection connection) {
      Response ret = new Response();

      try {
         ret.setStatusCode(connection.getResponseCode());
         ret.setHeaders(connection.getHeaderFields());
      } catch (Exception var9) {
         throw new SSEException(var9);
      }

      InputStream inputStream;
      try {
         inputStream = connection.getInputStream();
      } catch (Exception var8) {
         inputStream = connection.getErrorStream();
         ret.setFailure(var8);
      }

      ByteArrayOutputStream container = new ByteArrayOutputStream();
      byte[] buf = new byte[1024];

      try {
         int read;
         while((read = inputStream.read(buf, 0, 1024)) > 0) {
            container.write(buf, 0, read);
         }

         ret.setData(container.toByteArray());
         return ret;
      } catch (Exception var10) {
         throw new SSEException(var10);
      }
   }

   private void updateCookies(Response response) {
      Iterable<String> newCookies = response.getHeaders().get(RESTConstants.SET_COOKIE);
      if (newCookies != null) {
         for (String cookie : newCookies) {
            int equalIndex = cookie.indexOf('=');
            int semicolonIndex = cookie.indexOf(';');
            String cookieKey = cookie.substring(0, equalIndex);
            String cookieValue = cookie.substring(equalIndex + 1, semicolonIndex);
            _cookies.put(cookieKey, cookieValue);
         }
      }
   }

   private String getCookies() {
      StringBuilder ret = new StringBuilder();
      if (!_cookies.isEmpty()) {
         for (Entry<String, String> entry : _cookies.entrySet()) {
            ret.append(entry.getKey()).append("=").append(entry.getValue()).append(";");
         }
      }

      return ret.toString();
   }

   public String getUsername() {
      return this._username;
   }

   public static URLConnection openConnection(String urlString) throws IOException {
      URL url = new URL(urlString);
      return url.openConnection();
   }

   public static RestClient.ProxyInfo setProxyCfg(String host, String port, String userName, String password) {
      return new RestClient.ProxyInfo(host, port, userName, password);
   }

   public static RestClient.ProxyInfo setProxyCfg(String host, String port) {
      RestClient.ProxyInfo proxyInfo = new RestClient.ProxyInfo();
      proxyInfo._host = host;
      proxyInfo._port = port;
      return proxyInfo;
   }

   public static RestClient.ProxyInfo setProxyCfg(String address, String userName, String password) {
      RestClient.ProxyInfo proxyInfo = new RestClient.ProxyInfo();
      if (address != null) {
         String host = address;
         int index;
         if (address.endsWith("/")) {
            index = address.lastIndexOf(47);
            host = address.substring(0, index);
         }

         index = host.lastIndexOf(58);
         if (index > 0) {
            proxyInfo._host = host.substring(0, index);
            proxyInfo._port = host.substring(index + 1, host.length());
         } else {
            proxyInfo._host = host;
            proxyInfo._port = "80";
         }
      }

      proxyInfo._userName = userName;
      proxyInfo._password = password;
      return proxyInfo;
   }

   static {
      X509TrustManager trustManager = new X509TrustManager() {
         public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
         }

         public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
         }

         public X509Certificate[] getAcceptedIssuers() {
            return null;
         }
      };

      SSLContext sslcontext;
      try {
         sslcontext = SSLContext.getInstance("SSL");
         sslcontext.init((KeyManager[])null, new TrustManager[]{trustManager}, (SecureRandom)null);
      } catch (KeyManagementException var3) {
         throw new SSEException(var3);
      } catch (NoSuchAlgorithmException var4) {
         throw new SSEException(var4);
      }

      HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
      HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
         public boolean verify(String hostname, SSLSession sslSession) {
            return true;
         }
      });
   }

   static class ProxyInfo {
      String _host;
      String _port;
      String _userName;
      String _password;

      public ProxyInfo() {
      }

      public ProxyInfo(String host, String port, String userName, String password) {
         this._host = host;
         this._port = port;
         this._userName = userName;
         this._password = password;
      }
   }
}
