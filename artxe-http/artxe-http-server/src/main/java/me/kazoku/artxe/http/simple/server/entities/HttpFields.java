package me.kazoku.artxe.http.simple.server.entities;

import me.kazoku.artxe.http.simple.server.util.NameValuePair;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class HttpFields {
  private final Map<String, HttpField> fieldMap;

  public HttpFields() {
    this.fieldMap = new LinkedHashMap<>();
  }

  private void addField(HttpField field) {
    this.fieldMap.put(field.getName(), field);
  }

  public void addField(String name, String content) {
    this.fieldMap.put(name, HttpField.newInstance(name, content));
  }

  public void addField(String raw) {
    addField(HttpField.parse(raw));
  }

  public void removeField(String name) {
    fieldMap.remove(name);
  }

  public boolean containsField(String name) {
    return fieldMap.containsKey(name);
  }

  public String getFieldContent(String name) {
    return Optional.ofNullable(fieldMap.get(name)).map(HttpField::getValue).orElse("");
  }

  @Override
  public String toString() {
    return fieldMap.values().stream().map(HttpField::toString).collect(Collectors.joining("\r\n"));
  }

  private static class HttpField extends NameValuePair {
    private HttpField(String name, String value) {
      super(name, value);
    }

    private static HttpField newInstance(String name, String value) {
      return new HttpField(name, value);
    }

    private static HttpField parse(String raw) {
      int index;
      String name, content;
      if ((index = raw.indexOf(':')) != -1) {
        name = raw.substring(0, index);
        content = raw.substring(index + 1).trim();
      } else {
        name = raw;
        content = "";
      }
      return new HttpField(name, content);
    }

    @Override
    public String toString() {
      return join(": ");
    }
  }
}
