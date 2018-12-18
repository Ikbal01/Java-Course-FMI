package bg.sofia.uni.fmi.mjt.git.generator;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashGenerator {
    public static String hexDigest(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        byte[] bytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        return convertBytesToHex(bytes);
    }

    private static String convertBytesToHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte current : bytes) {
            hex.append(String.format("%02x", current));
        }

        return hex.toString();
    }
}
