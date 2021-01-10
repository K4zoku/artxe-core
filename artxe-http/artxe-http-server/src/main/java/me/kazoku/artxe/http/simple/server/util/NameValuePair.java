package me.kazoku.artxe.http.simple.server.util;

public class NameValuePair {
  protected final String name;
  protected final String value;

  public NameValuePair(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public String getValue() {
    return value;
  }

  public String join(CharSequence delimiter) {
    return String.join(delimiter, name, value);
  }
}
