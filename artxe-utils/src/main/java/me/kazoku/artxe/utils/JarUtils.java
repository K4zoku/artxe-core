package me.kazoku.artxe.utils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

public class JarUtils {
    private JarUtils() {}

    public static File traceTheSource(@NotNull Class<?> context) {
        String rawName = context.getName();
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

        try {
            String fileName = URLDecoder.decode(uri.substring("jar:file:".length(), uri.indexOf('!')), Charset.defaultCharset().name());
            return new File(fileName);
        } catch (UnsupportedEncodingException e) {
            throw new InternalError("Default charset doesn't exist. Your VM is borked.");
        }
    }
    
    public static File traceTheSource() {
        return traceTheSource(ClassUtils.getCallerClass());
    }
    
}
