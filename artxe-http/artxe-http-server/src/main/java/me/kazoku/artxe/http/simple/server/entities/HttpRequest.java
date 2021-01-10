package me.kazoku.artxe.http.simple.server.entities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.Scanner;

/**
 * @see <a href="https://www.w3.org/Protocols/rfc2616/rfc2616-sec5.html">Request</a>
 */
public class HttpRequest {
  public final Map<String, String> _GET;
  public final Map<String, String> _POST;
  private final String method;
  private final URI uri;
  private final String httpVersion;
  private final HttpFields headerFields;

  public HttpRequest(InputStream requestStream) {
    // init default
    String method = "GET";
    URI uri = URI.create("/");
    String httpVersion = "HTTP/1.1";
    Map<String, String> _GET = Collections.emptyMap();
    Map<String, String> _POST = Collections.emptyMap();
    headerFields = new HttpFields();

    try {
      BufferedReader request = new BufferedReader(new InputStreamReader(requestStream));

      // @see <a href="https://www.w3.org/Protocols/rfc2616/rfc2616-sec5.html#sec5.1">request-line</a>
      Scanner requestLine = new Scanner(request.readLine());
      method = requestLine.next();
      String uriString = requestLine.next();
      uri = URI.create(uriString);
      httpVersion = requestLine.next();

      // @see <a href="https://en.wikipedia.org/wiki/List_of_HTTP_header_fields">header fields</a>
      String rawHeader;
      while ((rawHeader = request.readLine()) != null && !rawHeader.isEmpty())
        headerFields.addField(rawHeader);

      // GET data
      int index;
      if ((index = uriString.indexOf('?')) != -1) _GET = Queries.fromString(uriString.substring(index + 1)).toMap();

      // POST data
      if (method.equalsIgnoreCase("POST")) {
        StringBuilder payload = new StringBuilder();
        while (request.ready()) payload.append((char) request.read());
        if (getHeaders().getFieldContent("content-type").equalsIgnoreCase("application/x-www-form-urlencoded"))
          _POST = Queries.fromString(payload.toString()).toMap();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    this.method = method;
    this.uri = uri;
    this.httpVersion = httpVersion;
    this._GET = Collections.unmodifiableMap(_GET);
    this._POST = Collections.unmodifiableMap(_POST);
  }

  public String getMethod() {
    return method;
  }

  public URI getUri() {
    return uri;
  }

  public String getHttpVersion() {
    return httpVersion;
  }

  public HttpFields getHeaders() {
    return headerFields;
  }

}
