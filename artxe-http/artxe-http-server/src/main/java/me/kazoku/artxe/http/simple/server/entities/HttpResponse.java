package me.kazoku.artxe.http.simple.server.entities;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

public class HttpResponse {
  private final PrintWriter writer;
  private final HttpFields headers;
  private final List<String> content;

  public HttpResponse(OutputStream responseStream) {
    writer = new PrintWriter(responseStream);
    headers = new HttpFields();
    content = new LinkedList<>();
    header("Server", "NKD/1.0-SNAPSHOT (JVM)");
    header("X-Powered-By", "Java/" + System.getProperty("java.version"));
    header("Date", rfc7231(Calendar.getInstance().getTime()));
  }

  public static String rfc7231(Date date) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    return dateFormat.format(date);
  }

  public void header(String name, String content) {
    headers.addField(name, content);
  }

  public void header(String header) {
    headers.addField(header);
  }

  public void println(String line) {
    content.add(line);
  }

  public void response() {
    writer.print("HTTP/1.1 200\r\n");
    writer.print(headers.toString());
    writer.print("\r\n\r\n");
    writer.print(String.join("\r\n", content));
    writer.flush();
    writer.close();
  }
}
