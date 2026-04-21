package commons;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PasswordUtils {

    private static final Logger LOGGER = Logger.getLogger(PasswordUtils.class.getName());

    
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.log(Level.SEVERE, "Algoritmo SHA-256 non disponibile", e);
            throw new RuntimeException("Errore di hashing", e);
        }
    }

    
    public static boolean verifyPassword(String password, String storedHash) {
        return hashPassword(password).equals(storedHash);
    }

}
