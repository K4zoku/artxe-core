package me.kazoku.artxe.http.server.util;

import me.kazoku.artxe.http.server.entities.Query;

import java.util.LinkedHashMap;
import java.util.Map;

public class QueryBuilder {
    private final Map<String, String> fields;

    public QueryBuilder() {
        fields = new LinkedHashMap<>();
    }

    public QueryBuilder addField(String name, String value) {
        fields.put(name, value);
        return this;
    }

    public Query create() {
        return new Query(fields);
    }

}
