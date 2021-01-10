package me.kazoku.artxe.utils;

import com.google.gson.JsonObject;

import java.io.*;
import java.net.*;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class SimpleWebUtils {

  private static final int DEFAULT_ATTEMPT = 3;

  public static String sendPost(String url, JsonObject json) {
    return sendPost(url, json, UserAgent.DEFAULT, DEFAULT_ATTEMPT);
  }

  public static String sendPost(String url, JsonObject json, String userAgent, int attempt) {
    while (attempt > 0) {
      try {
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        String query = json.toString();

        HttpURLConnection connection = createConnection("POST", url, userAgent);
        connection.addRequestProperty("content-type", "application/json");
        DataOutputStream postWriter = new DataOutputStream(connection.getOutputStream());
        postWriter.writeBytes(query);
        postWriter.flush();

        return readResponse(connection.getInputStream());
      } catch (SocketTimeoutException e) {
        Logger.getGlobal().log(Level.SEVERE, "Timed out, might be the requested server went down");
        Logger.getGlobal().log(Level.SEVERE, "Check by yourself: " + url);
        Logger.getGlobal().log(Level.WARNING, "Attempting to try again...");
        attempt--;
      } catch (IOException e) {
        Logger.getGlobal().log(Level.SEVERE, "An error occurred ", e);
        return "";
      }
    }
    throw new TooManyAttempt();
  }

  public static String sendPost(String url, Map<String, String> parameters) {
    return sendPost(url, parameters, UserAgent.DEFAULT, DEFAULT_ATTEMPT);
  }

  public static String sendPost(String url, Map<String, String> parameters, String userAgent, int attempt) {
    while (attempt > 0) {
      try {
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        String query = mapToQuery(parameters);

        HttpURLConnection connection = createConnection("POST", url, userAgent);

        DataOutputStream postWriter = new DataOutputStream(connection.getOutputStream());
        postWriter.writeBytes(query);
        postWriter.flush();

        return readResponse(connection.getInputStream());
      } catch (SocketTimeoutException e) {
        Logger.getGlobal().log(Level.SEVERE, "Timed out, might be the requested server went down");
        Logger.getGlobal().log(Level.SEVERE, "Check by yourself: " + url);
        Logger.getGlobal().log(Level.WARNING, "Attempting to try again...");
        attempt--;
      } catch (IOException e) {
        Logger.getGlobal().log(Level.SEVERE, "An error occurred ", e);
        return "";
      }
    }
    throw new TooManyAttempt();
  }

  public static String sendRequest(String url) {
    return sendGet(url, Collections.emptyMap());
  }

  public static String sendGet(String url, Map<String, String> parameters) {
    return sendGet(url, parameters, UserAgent.DEFAULT, DEFAULT_ATTEMPT);
  }

  public static String sendGet(String url, Map<String, String> parameters, String userAgent, int attempt) {
    while (attempt > 0) {
      try {
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        return readResponse(createConnection("GET", url + (parameters.isEmpty() ? "" : (url.contains("?") ? "&" : "?") + mapToQuery(parameters)), userAgent).getInputStream());
      } catch (SocketTimeoutException e) {
        Logger.getGlobal().log(Level.SEVERE, "Timed out, might be the requested server went down");
        Logger.getGlobal().log(Level.SEVERE, "Check by yourself: " + url);
        Logger.getGlobal().log(Level.WARNING, "Attempting to try again...");
        attempt--;
      } catch (IOException e) {
        Logger.getGlobal().log(Level.SEVERE, "An error occurred ", e);
        return "";
      }
    }
    throw new TooManyAttempt();
  }

  private static HttpURLConnection createConnection(String type, String url, String userAgent) throws IOException {
    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
    connection.setRequestProperty("User-Agent", userAgent.isEmpty() ? UserAgent.DEFAULT : userAgent);
    connection.setRequestMethod(type);
    connection.setDoInput(true);
    connection.setDoOutput(true);
    return connection;
  }

  private static String readResponse(InputStream response) throws IOException {
    BufferedReader in = new BufferedReader(new InputStreamReader(response));
    String line;
    StringBuilder responseText = new StringBuilder();
    while ((line = in.readLine()) != null) {
      responseText.append(line);
    }
    return responseText.toString();
  }

  private static String mapToQuery(Map<String, String> map) throws UnsupportedEncodingException {
    StringBuilder queryBuilder = new StringBuilder();

    int i = 0;
    for (Map.Entry<String, String> entry : map.entrySet()) {
      queryBuilder.append(URLEncoder.encode(entry.getKey(), "UTF-8"))
          .append("=")
          .append(URLEncoder.encode(entry.getValue(), "UTF-8"));
      if (i >= 0 && i < map.size() - 1) queryBuilder.append("&");
      i++;
    }
    return queryBuilder.toString();
  }

  public static class TooManyAttempt extends Error {
    public TooManyAttempt(String msg) {
      super(msg);
    }

    public TooManyAttempt() {
      this("");
    }
  }

  public static class UserAgent {
    public static final String DEFAULT = "Mozilla/5.0";
    public static final String CHROMIUM_LINUX = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/534.30 (KHTML, like Gecko) Ubuntu/11.04 Chromium/12.0.742.112 Chrome/12.0.742.112 Safari/534.30";
    public static final String CHROMIUM_WINDOWS = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.75 Safari/537.36";
    public static final String SAFARI_MAC = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.80 Safari/537.36";

    private UserAgent() {
    }
  }
}
