package me.kazoku.artxe.utils;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public final class SimpleWebUtils {

  private static final int DEFAULT_ATTEMPT = 3;

  public static String sendPost(String url, Map<String, String> parameters) {
    return sendPost(url, parameters, UserAgent.DEFAULT, DEFAULT_ATTEMPT, Logger.getAnonymousLogger());
  }

  public static String sendPost(String url, Map<String, String> parameters, String userAgent, int attempt, Logger logger) {
    while (attempt-- > 0) {
      try {
        String query = mapToQuery(parameters);

        HttpURLConnection connection = createConnection("POST", url, userAgent);

        DataOutputStream postWriter = new DataOutputStream(connection.getOutputStream());
        postWriter.writeBytes(query);
        postWriter.flush();

        return readResponse(connection.getInputStream());
      } catch (SocketTimeoutException e) {
        logger.log(Level.SEVERE, "Timed out, might be the requested server went down");
        logger.log(Level.SEVERE, "Check by yourself: " + url);
        logger.log(Level.WARNING, "Attempting to try again...");
      } catch (IOException e) {
        throw new IllegalStateException(e);
      }
    }
    throw new TooManyAttempt();
  }

  public static String sendRequest(String url) {
    return sendGet(url, Collections.emptyMap());
  }

  public static String sendGet(String url, Map<String, String> parameters) {
    return sendGet(url, parameters, UserAgent.DEFAULT, DEFAULT_ATTEMPT, Logger.getAnonymousLogger());
  }

  public static String sendGet(String url, Map<String, String> parameters, String userAgent, int attempt, Logger logger) {
    while (attempt-- > 0) {
      try (InputStream connection = createConnection("GET", url + (parameters.isEmpty() ? "" : (url.contains("?") ? "&" : "?") + mapToQuery(parameters)), userAgent).getInputStream()) {
        return readResponse(connection);
      } catch (SocketTimeoutException e) {
        logger.log(Level.SEVERE, "Timed out, might be the requested server went down");
        logger.log(Level.SEVERE, "Check by yourself: " + url);
        logger.log(Level.WARNING, "Attempting to try again...");
      } catch (IOException e) {
        throw new IllegalStateException(e);
      }
    }
    throw new TooManyAttempt();
  }

  private static HttpURLConnection createConnection(String type, String url, String userAgent) throws IOException {
    CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
    connection.setRequestProperty("User-Agent", userAgent.isEmpty() ? UserAgent.DEFAULT : userAgent);
    connection.setRequestMethod(type);
    connection.setDoInput(true);
    connection.setDoOutput(true);
    return connection;
  }

  private static String readResponse(InputStream response) throws IOException {
    return new BufferedReader(new InputStreamReader(response)).lines().collect(Collectors.joining("\n"));
  }

  private static String mapToQuery(Map<String, String> map) {
    return map.entrySet().stream()
        .map(query -> urlEncode(query.getKey()) + '=' + urlEncode(query.getValue()))
        .collect(Collectors.joining("&"));
  }

  public static String urlEncode(String s) {
    try {
      return URLEncoder.encode(s, StandardCharsets.UTF_8.name());
    } catch (UnsupportedEncodingException e) {
      return s;
    }
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
    public static final String DEFAULT = "Artxe/1.0";
    public static final String CHROMIUM_LINUX = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/534.30 (KHTML, like Gecko) Ubuntu/11.04 Chromium/12.0.742.112 Chrome/12.0.742.112 Safari/534.30";
    public static final String CHROMIUM_WINDOWS = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.75 Safari/537.36";
    public static final String SAFARI_MAC = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.80 Safari/537.36";

    private UserAgent() {
    }
  }
}
