package me.kazoku.artxe.security;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hashing {

    public static String get(String algorithm, File file) throws NoSuchAlgorithmException {
        byte[] buffer = new byte[8192];
        int count;
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            while ((count = bis.read(buffer)) > 0) {
                digest.update(buffer, 0, count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] hash = digest.digest();
        StringBuilder hex = new StringBuilder();
        for (byte datum : hash) {
            hex.append(Integer.toString((datum & 0xff) + 0x100, 16).substring(1));
        }
        return hex.toString();
    }
}
