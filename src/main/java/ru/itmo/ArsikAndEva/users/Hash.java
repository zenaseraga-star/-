package ru.itmo.ArsikAndEva.users;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {
    public static String HashPass(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashPass = digest.digest(password.getBytes());
            StringBuilder builder = new StringBuilder();
            for (byte b : hashPass) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    builder.append(0);
                }
                builder.append(hex);
            }
            return builder.toString();
        }
        catch ( NoSuchAlgorithmException e){
            throw new RuntimeException("Алгоритм хеширования не найден", e);
        }
    }
}
