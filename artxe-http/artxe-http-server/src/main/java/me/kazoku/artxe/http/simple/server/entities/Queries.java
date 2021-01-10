package me.kazoku.artxe.http.simple.server.entities;

import me.kazoku.artxe.http.simple.server.util.NameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class Queries {
  private final Map<String, String> queryMap;
  private final String queryString;

  private Queries(String queryString, Map<String, String> queryMap) {
    this.queryMap = queryMap;
    this.queryString = queryString;
  }

  public static Queries fromMap(Map<String, String> queryMap) {
    String queryString = queryMap.entrySet().stream()
        .map(Query::newInstance)
        .map(Query::toString)
        .collect(Collectors.joining("&"));
    return new Queries(queryString, queryMap);
  }


  public static Queries fromString(String rawQueries) {
    if (rawQueries.isEmpty()) {
      return new Queries("", Collections.emptyMap());
    }

    List<Query> queries = new LinkedList<>();
    if (rawQueries.contains("&")) {
      Scanner queryScanner = new Scanner(rawQueries);
      queryScanner.useDelimiter("&");
      while (queryScanner.hasNext()) queries.add(Query.parse(queryScanner.next()));
    } else queries.add(Query.parse(rawQueries));
    Map<String, String> queryMap = queries.stream()
        .collect(Collectors.toMap(Query::getName, Query::getValue));
    queryMap = Collections.unmodifiableMap(queryMap);
    return new Queries(rawQueries, queryMap);
  }

  private static String urlEncode(String text) {
    try {
      return URLEncoder.encode(text, StandardCharsets.UTF_8.name());
    } catch (UnsupportedEncodingException e) {
      return text;
    }
  }

  public boolean containField(String name) {
    return queryMap.containsKey(name);
  }

  public String getField(String name) {
    return queryMap.getOrDefault(name, null);
  }

  public String toString() {
    return queryString;
  }

  public Map<String, String> toMap() {
    return Collections.unmodifiableMap(queryMap);
  }

  private static class Query extends NameValuePair {

    private Query(String name, String value) {
      super(name, value);
    }

    private static Query newInstance(Map.Entry<String, String> entry) {
      return newInstance(entry.getKey(), entry.getValue());
    }

    private static Query newInstance(String name, String value) {
      return new Query(urlEncode(name), urlEncode(value));
    }

    private static Query parse(String raw) {
      int index;
      String name, content;
      if ((index = raw.indexOf('=')) != -1) {
        name = raw.substring(0, index);
        content = raw.substring(index + 1).trim();
      } else {
        name = raw;
        content = "";
      }
      return new Query(name, content);
    }

    @Override
    public String toString() {
      return join("=");
    }
  }
}
