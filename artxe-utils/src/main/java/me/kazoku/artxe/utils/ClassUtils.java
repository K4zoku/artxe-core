package me.kazoku.artxe.utils;

public class ClassUtils {
    public static Class<?> getCallerClass() {
        try {
            return Class.forName(Thread.currentThread().getStackTrace()[2].getClassName());
        } catch (ClassNotFoundException e) {
            throw new InternalError();
        }
    }
}
