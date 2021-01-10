package me.kazoku.artxe.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

public class PlaceholderCache extends Cache<String, Pattern> {

  protected final Map<String, Pattern> patternCache;

  protected final char[] wrapper;

  public PlaceholderCache(char[] wrapper) {
    if (wrapper.length != 2) throw new IllegalArgumentException("Wrapper must be 2 char");
    this.patternCache = new HashMap<>();
    this.wrapper = wrapper;
  }

  public PlaceholderCache(char wrapper) {
    this(new char[]{wrapper, wrapper});
  }

  public PlaceholderCache() {
    this(new char[]{123, 125});
  }

  public String apply(String text, String placeholder, String replacement) {
    return get(placeholder).matcher(text).replaceAll(replacement);
  }

  public String apply(String text, Map<String, String> batch) {
    AtomicReference<String> result = new AtomicReference<>(text);
    batch.forEach((placeholder, replacement) -> result.getAndUpdate(txt -> apply(txt, placeholder, replacement)));
    return result.get();
  }

  protected final Pattern create(String placeholder) {
    return Pattern.compile(String.format("(?iu)[%s]%s[%s]", wrapper[0], Pattern.quote(placeholder), wrapper[1]));
  }

}
