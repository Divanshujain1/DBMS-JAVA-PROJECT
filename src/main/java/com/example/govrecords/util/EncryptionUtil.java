package com.example.govrecords.util;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Arrays;

public class EncryptionUtil {
    private static final String ALGO = "AES/GCM/NoPadding";
    private static final int IV_SIZE = 12; // 96 bits recommended for GCM
    private static final int TAG_BIT_LENGTH = 128;

    private static SecretKeySpec getKey() {
        String b64 = System.getenv("ENCRYPTION_KEY_BASE64");
        if (b64 == null) throw new IllegalStateException("ENCRYPTION_KEY_BASE64 not set");
        byte[] keyBytes = Base64.getDecoder().decode(b64);
        if (keyBytes.length != 32) throw new IllegalStateException("Key must be 32 bytes (base64 of 32 bytes)");
        return new SecretKeySpec(keyBytes, "AES");
    }

    public static String encrypt(String plain) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGO);
        byte[] iv = new byte[IV_SIZE];
        new SecureRandom().nextBytes(iv);
        GCMParameterSpec spec = new GCMParameterSpec(TAG_BIT_LENGTH, iv);
        SecretKeySpec key = getKey();
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        byte[] cipherText = cipher.doFinal(plain.getBytes(StandardCharsets.UTF_8));
        byte[] out = new byte[iv.length + cipherText.length];
        System.arraycopy(iv, 0, out, 0, iv.length);
        System.arraycopy(cipherText, 0, out, iv.length, cipherText.length);
        return Base64.getEncoder().encodeToString(out);
    }

    public static String decrypt(String combinedBase64) throws Exception {
        byte[] combined = Base64.getDecoder().decode(combinedBase64);
        byte[] iv = Arrays.copyOfRange(combined, 0, IV_SIZE);
        byte[] cipherText = Arrays.copyOfRange(combined, IV_SIZE, combined.length);
        Cipher cipher = Cipher.getInstance(ALGO);
        GCMParameterSpec spec = new GCMParameterSpec(TAG_BIT_LENGTH, iv);
        SecretKeySpec key = getKey();
        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        byte[] plain = cipher.doFinal(cipherText);
        return new String(plain, StandardCharsets.UTF_8);
    }

    // helper to mask last 4
    public static String mask(String decrypted, int showLast) {
        if (decrypted == null) return null;
        int len = decrypted.length();
        if (len <= showLast) return "****";
        String last = decrypted.substring(len - showLast);
        return "****" + last;
    }
}
