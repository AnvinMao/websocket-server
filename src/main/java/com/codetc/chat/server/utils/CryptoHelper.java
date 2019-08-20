package com.codetc.chat.server.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Created by anvin 2019-08-20
 */
public final class CryptoHelper {

    public static byte[] md5(String text) {
        return hash("MD5", text.getBytes(StandardCharsets.UTF_8));
    }

    private static byte[] hash(String algorithm, byte[] bytes) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            return messageDigest.digest(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new SecurityException(algorithm + " is not supported but it is required for this service.", e);
        }
    }

    public static String bytesToHexString(byte[] data) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : data) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }

    public static String decryptWithAes(String key, String data) {
        try {
            String hashKey = bytesToHexString(md5(key));
            String[] parts = data.split(":");

            IvParameterSpec iv = new IvParameterSpec(Base64.getUrlDecoder().decode(parts[1]));
            SecretKeySpec keySpec = new SecretKeySpec(hashKey.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);

            byte[] decodedEncryptedData = Base64.getUrlDecoder().decode(parts[0]);
            byte[] original = cipher.doFinal(decodedEncryptedData);

            return new String(original, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new SecurityException("Failed to decrypt.", e);
        }
    }
}
