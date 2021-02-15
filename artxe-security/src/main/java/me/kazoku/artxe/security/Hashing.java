package me.kazoku.artxe.security;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static me.kazoku.artxe.security.BaseN.bytesToHex;

public class Hashing {

  public static byte[] getRawHash(String algorithm, File file, byte[] salt) throws IOException {
    final MessageDigest digest = getDigest(algorithm);
    if (salt != null) digest.update(salt);

    byte[] buffer = new byte[0x2000];
    int count;
    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
    while ((count = bis.read(buffer)) > 0) digest.update(buffer, 0, count);

    return digest.digest();
  }

  public static byte[] getRawHash(String algorithm, String text, byte[] salt) {
    final MessageDigest digest = getDigest(algorithm);
    if (salt != null) digest.update(salt);
    return digest.digest(text.getBytes());
  }

  public static String getHash(String algorithm, File file) throws IOException {
    return bytesToHex(getRawHash(algorithm, file, null));
  }

  public static String getHash(String algorithm, String text) {
    return bytesToHex(getRawHash(algorithm, text, null));
  }

  public static String getHash(String algorithm, File file, byte[] salt) throws IOException {
    return bytesToHex(getRawHash(algorithm, file, salt));
  }

  public static String getHash(String algorithm, String text, byte[] salt) {
    return bytesToHex(getRawHash(algorithm, text, salt));
  }


  private static MessageDigest getDigest(String algorithm) {
    try {
      return MessageDigest.getInstance(algorithm);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalArgumentException("No such algorithm: " + algorithm);
    }
  }


}
