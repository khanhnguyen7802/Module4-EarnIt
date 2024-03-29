package nl.utwente.di.first.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;


/**
 * The SHA-256 algorithm generates an almost-unique, fixed-size 256-bit (32-byte) hash.
 * This is a one-way function, so the result cannot be decrypted back to the original value.
 * salt+password OR password+salt are all valid
 */
public class Security {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        String passwordToHash = "company10";
        byte[] byteSalt = getSalt();
        String stringSalt = toHex(byteSalt);
        String testHashedPassword = "eebf325d66b84653940b17a1f6825679e862a4ab857d82bb85501359cdfdce01";

        System.out.println("Convert from byte to Hex(String): " + stringSalt);

        String securePassword = saltSHA256(passwordToHash, byteSalt);
        System.out.println(securePassword);
        System.out.println("Compare equal: " + securePassword.equals(testHashedPassword));

    }


    public static String saltSHA256(String passwordToHash, byte[] salt) {
        String finalPassword = "";
    
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Passing the salt to the digest for the computation
            md.update(salt);

            // Add password bytes to digest
            byte[] bytes = md.digest(passwordToHash.getBytes());

            finalPassword = toHex(bytes);

        } catch (NoSuchAlgorithmException e) {
            System.err.println("Algorithm does not exist");
        }
        return finalPassword;
    }


    // Add salt
    public static byte[] getSalt() throws NoSuchAlgorithmException {
        // generate random salt
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG"); // “SHA1PRNG” pseudo-random number generator algorithm
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt); // fill in 16-byte array

        return salt;

    }

    public static String toHex(byte[] bytes) {
        HexFormat hexFormat = HexFormat.of();
    
        return hexFormat.formatHex(bytes);
    }
    
    public static byte[] toByteArray(String str) {
        HexFormat hexFormat = HexFormat.of();
    
        return hexFormat.parseHex(str);
    }
}
