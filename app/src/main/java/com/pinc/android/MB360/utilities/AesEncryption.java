package com.pinc.android.MB360.utilities;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

import java.security.SecureRandom;
import java.util.Arrays;

public class AesEncryption {
    private final byte[] key;
    private final byte[] iv;

    public AesEncryption(byte[] key, byte[] iv) {
        this.key = key;
        this.iv = iv;
    }

    public String encrypt(String plaintext) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());
        String encryptedBase64 = Base64.encodeToString(encryptedBytes, Base64.DEFAULT);

        return encryptedBase64;
    }

    public String decrypt(String ciphertext) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

        byte[] encryptedBytes = Base64.decode(ciphertext, Base64.DEFAULT);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        String decryptedString = new String(decryptedBytes);

        return decryptedString;
    }


    public String encryptWithSalt(String plaintext) throws Exception {
        // Generate a random salt value
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);

        // Concatenate salt and plaintext
        byte[] saltedPlaintext = new byte[salt.length + plaintext.getBytes().length];
        System.arraycopy(salt, 0, saltedPlaintext, 0, salt.length);
        System.arraycopy(plaintext.getBytes(), 0, saltedPlaintext, salt.length, plaintext.getBytes().length);

        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

        byte[] encryptedBytes = cipher.doFinal(saltedPlaintext);
        String encryptedBase64 = Base64.encodeToString(encryptedBytes, Base64.DEFAULT);

        return encryptedBase64;
    }

    public String decryptWithSalt(String ciphertext) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

        byte[] encryptedBytes = Base64.decode(ciphertext, Base64.DEFAULT);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        // Extract the salt value from the decrypted bytes
        byte[] salt = Arrays.copyOfRange(decryptedBytes, 0, 16);

        // Remove the salt from the plaintext
        byte[] plaintextBytes = Arrays.copyOfRange(decryptedBytes, 16, decryptedBytes.length);
        String decryptedString = new String(plaintextBytes);

        return decryptedString;
    }


}

