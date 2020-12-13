package me.kazoku.artxe.http.server.entities;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class Query {
    private final Map<String, String> queryMap;
    private final String queryString;

    public Query(String query) {
        this.queryString = query;
        if (query.isEmpty()) {
            queryMap = Collections.emptyMap();
            return;
        }

        BiConsumer<Map<String, String>, String> action = (m, s) -> {
            int index;
            if ((index = s.indexOf('=')) > -1) {
                m.put(s.substring(0, index), s.substring(++index));
            } else m.put(s, "");
        };

        Map<String, String> queryMap = new LinkedHashMap<>();
        if (query.contains("&")) {
            Scanner queryScanner = new Scanner(query);
            queryScanner.useDelimiter("&");
            while (queryScanner.hasNext()) action.accept(queryMap, queryScanner.next());
        } else action.accept(queryMap, query);
        this.queryMap = Collections.unmodifiableMap(queryMap);
    }

    public Query(Map<String, String> queryMap) {
        this.queryMap = queryMap;
        this.queryString = queryMap.entrySet().stream()
                .map(entry -> urlEncode(entry.getKey()) + '=' + urlEncode(entry.getValue()))
                .collect(Collectors.joining("&"));
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
        return queryMap;
    }

    private static String urlEncode(String text) {
        try {
            return URLEncoder.encode(text, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            return text;
        }
    }
}
