package com.ksondzyk;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;


public class CipherMy {

    private static final String myKey = "EnctyptionKey";

    private static Key setKey(String myKey)
    {
        MessageDigest sha ;
        try {
            byte[] key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1"); //типу хеш-функції
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            return new SecretKeySpec(key, "AES");//This class specifies a secret key in a provider-independent fashion. It can be used to construct a SecretKey from a byte array
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String encode(final String message) {


        Key secretKey= setKey(myKey);

        try {

            Cipher cipher = Cipher.getInstance("AES");

                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                byte[] cipherText = cipher.doFinal(message.getBytes("UTF-8"));
                return Base64.getEncoder().encodeToString(cipherText);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return null;

    }

    public static String decode(final String message) {

        Key secretKey= setKey(myKey);

        try {

            Cipher cipher = Cipher.getInstance("AES");

                cipher.init(Cipher.DECRYPT_MODE, secretKey);
                byte[] cipherText = cipher.doFinal(Base64.getDecoder().decode(message));
                return new String(cipherText);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}

