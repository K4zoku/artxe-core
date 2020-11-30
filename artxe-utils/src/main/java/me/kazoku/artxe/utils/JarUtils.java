package me.kazoku.artxe.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

public class JarUtils {
    private JarUtils() {}

    public static File traceTheSource(Class<?> context) throws ClassNotFoundException {
        if (context == null) context = Class.forName(Thread.currentThread().getStackTrace()[2].getClassName());
        String rawName = context.getName();
        /* rawName is something like package.name.ContainingClass$ClassName. We need to turn this into ContainingClass$ClassName.class. */
        String classFileName = String.format("%s.class",
                rawName.contains(".")
                        ? rawName.substring(rawName.lastIndexOf('.') + 1)
                        : rawName
        );
        String uri = context.getResource(classFileName).toString();
        if (uri.startsWith("file:"))
            throw new IllegalStateException("This class has been loaded from a directory and not from a jar file.");
        if (!uri.startsWith("jar:file:")) {
            String protocol = uri.contains(":")
                    ? uri.substring(0, uri.indexOf(":"))
                    : "(unknown)";
            throw new IllegalStateException(String.format("This class has been loaded remotely via the %s protocol. Only loading from a jar on the local file system is supported.", protocol));
        }

        //As far as I know, the if statement below can't ever trigger, so it's more of a sanity check thing.
        if (!uri.contains("!"))
            throw new IllegalStateException("You appear to have loaded this class from a local jar file, but I can't make sense of the URL!");

        try {
            String fileName = URLDecoder.decode(uri.substring("jar:file:".length(), uri.indexOf('!')), Charset.defaultCharset().name());
            return new File(fileName);
        } catch (UnsupportedEncodingException e) {
            throw new InternalError("Default charset doesn't exist. Your VM is borked.");
        }
    }
}
