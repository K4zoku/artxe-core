package me.kazoku.artxe.http.server.entities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.*;

/**
 * @see <a href="https://www.w3.org/Protocols/rfc2616/rfc2616-sec5.html">Request</a>
 * */
public class ClientRequest {
    private final String method;
    private final URI uri;
    private final String httpVersion;
    private final List<HeaderField> headerFields;
    public final Map<String, String> $_GET;
    public final Map<String, String> $_POST;

    public ClientRequest(InputStream requestStream) {
        // init default
        String method = "GET";
        URI uri = URI.create("/");
        String httpVersion = "HTTP/1.1";
        List<HeaderField> headers = Collections.emptyList();
        Map<String, String> $_GET = Collections.emptyMap();
        Map<String, String> $_POST = Collections.emptyMap();

        try {
            BufferedReader request = new BufferedReader(new InputStreamReader(requestStream));

            // @see <a href="https://www.w3.org/Protocols/rfc2616/rfc2616-sec5.html#sec5.1">request-line</a>
            Scanner requestLine = new Scanner(request.readLine());
            method = requestLine.next();
            String uriString = requestLine.next();
            uri = URI.create(uriString);
            httpVersion = requestLine.next();

            // @see <a href="https://en.wikipedia.org/wiki/List_of_HTTP_header_fields">header fields</a>
            headers = new ArrayList<>();
            String rawHeader;
            while ((rawHeader = request.readLine()) != null && !rawHeader.isEmpty())
                headers.add(new HeaderField(rawHeader));

            // GET data
            int index;
            if ((index = uriString.indexOf('?')) != -1) $_GET = new Query(uriString.substring(++index)).toMap();

            // POST data
            if (method.equalsIgnoreCase("POST")) {
                StringBuilder payload = new StringBuilder();
                while (request.ready()) payload.append((char) request.read());
                if (getHeader("content-type").getContent().equalsIgnoreCase("application/x-www-form-urlencoded"))
                    $_POST = new Query(payload.toString()).toMap();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.method = method;
        this.uri = uri;
        this.httpVersion = httpVersion;
        this.headerFields = Collections.unmodifiableList(headers);
        this.$_GET = Collections.unmodifiableMap($_GET);
        this.$_POST = Collections.unmodifiableMap($_POST);
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

    public HeaderField getHeader(String field) {
        return headerFields.stream()
                .filter(f -> f.getName().equalsIgnoreCase(field))
                .findFirst()
                .orElse(null);
    }

}
