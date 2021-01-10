package me.kazoku.artxe.security;

import me.kazoku.artxe.security.exception.CryptError;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

public class BiCrypt {
  private static Cipher cipher;

  static {
    try {
      cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
      cipher = null;
      e.printStackTrace();
    }
  }

  private final String key;
  private final String iv;

  public BiCrypt(String key, String iv) {
    this.key = key;
    this.iv = iv;
  }

  public BiCrypt() {
    Random random = new Random();
    this.key = Long.toHexString(random.nextLong());
    this.iv = Long.toHexString(random.nextLong());
  }

  private static byte[] hash(String algorithm, String text) {
    try {
      MessageDigest md = MessageDigest.getInstance(algorithm);
      return md.digest(text.getBytes());
    } catch (NoSuchAlgorithmException e) {
      throw new CryptError(e.getMessage());
    }
  }

  private static String hexBinary(byte[] val) {
    StringBuilder sb = new StringBuilder();
    for (byte b : val) sb.append(String.format("%02x", b));
    return sb.toString();
  }

  public String encrypt(String text) {
    try {
      cipher.init(Cipher.ENCRYPT_MODE, makeKey(), makeIv());
      return
          Base64.getEncoder().encodeToString(
              cipher.doFinal(text.getBytes())
          );
    } catch (InvalidKeyException | InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException e) {
      throw new CryptError(e.getMessage());
    }
  }

  public String decrypt(String text) {
    try {
      cipher.init(Cipher.DECRYPT_MODE, makeKey(), makeIv());
      return new String(cipher.doFinal(Base64.getDecoder().decode(text)));
    } catch (InvalidKeyException | InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException e) {
      throw new CryptError(e.getMessage());
    }
  }

  private Key makeKey() {
    byte[] keyBytes = hash("MD5", this.key);
    keyBytes = Arrays.copyOfRange(keyBytes, 0, 16);
    return new SecretKeySpec(hexBinary(keyBytes).toLowerCase().getBytes(), "AES");
  }

  private AlgorithmParameterSpec makeIv() {
    byte[] ivBytes = hash("SHA-256", this.iv);
    ivBytes = Arrays.copyOfRange(ivBytes, 0, 8);
    return new IvParameterSpec(hexBinary(ivBytes).toLowerCase().getBytes());
  }
}
