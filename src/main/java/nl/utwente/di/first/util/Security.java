package nl.utwente.di.first.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


/**
 * The SHA-256 algorithm generates an almost-unique, fixed-size 256-bit (32-byte) hash.
 * This is a one-way function, so the result cannot be decrypted back to the original value.
 * salt+password OR password+salt are all valid
 */
public class Security {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        String passwordToHash = "password";
        String saltString = "";
        byte[] salt = getSalt();
        for(byte b:salt) {
            saltString+= b+ " ";

        }
        System.out.println(saltString.trim());
        String securePassword = saltSHA256(passwordToHash, salt);
        System.out.println(securePassword);

    }


    private static String saltSHA256(String passwordToHash, byte[] salt) {
        String finalPassword = "";

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Passing the salt to the digest for the computation
            md.update(salt);

            // Add password bytes to digest
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(String.format("%02x", bytes[i]));
            }

            finalPassword = sb.toString();

        } catch (NoSuchAlgorithmException e) {
            System.err.println("Algorithm does not exist");
        }
        return finalPassword;
    }


    // Add salt
    private static byte[] getSalt() throws NoSuchAlgorithmException {
        // generate random salt
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG"); // “SHA1PRNG” pseudo-random number generator algorithm
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt); // fill in 16-byte array

        return salt;
    }
}
