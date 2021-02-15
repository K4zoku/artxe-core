package me.kazoku.artxe.security;

public class BaseN {
  public static String bytesToHex(byte[] bytes) {
    StringBuilder hex = new StringBuilder();
    for (byte b : bytes) hex.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
    return hex.toString();
  }

  public static String hexPadded(int n, int padding) {
    return String.format("%0" + padding + "x", n);
  }

  public static String binPadded(int n, int padding) {
    String rawBin = Integer.toBinaryString(n);
    return n >= 0 && rawBin.length() < padding
        ? Integer.toBinaryString((1 << padding) | (n & ((1 << padding) - 1))).substring(1)
        : Integer.toBinaryString(n);
  }
}
