package me.kazoku.artxe.http.server.entities;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

public class SimpleResponse {
    private final PrintWriter writer;
    private final List<String> headers;
    private final List<String> fields;
    private final List<String> content;

    public SimpleResponse(OutputStream responseStream) {
        writer = new PrintWriter(responseStream);
        headers = new LinkedList<>();
        fields = new LinkedList<>();
        content = new LinkedList<>();
        header("Server", "NKD/1.0-SNAPSHOT (JVM)");
        header("X-Powered-By", "Java/" + System.getProperty("java.version"));
        header("Date", rfc7231(new Date()));
    }

    public void header(HeaderField header) {
        if (!fields.contains(header.getName())) {
            fields.add(header.getName());
            headers.add(header.toString());
        } else throw new UnsupportedOperationException("Header is already exists");
    }

    public void header(String name, String content) {
        header(new HeaderField(name, content));
    }

    public void header(String header) {
        header(new HeaderField(header));
    }

    public void println(String line) {
        content.add(line);
    }

    public void flush() {
        writer.print("HTTP/1.1 200\r\n");
        writer.print(String.join("\r\n", headers));
        writer.print("\r\n\r\n");
        writer.print(String.join("\r\n", content));
        writer.flush();
    }

    public void close() {
        writer.close();
    }

    public static String rfc7231(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(date);
    }
}
