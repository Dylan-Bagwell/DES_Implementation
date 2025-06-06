public class TestRoundTripDecryption {
    public static void main(String[] args) {
        String plaintext = "1011010110101101101110001111000010101011001011110000111100001101";
        String key = "1100110001111001101010110010101011110000111101010101010101010101";
        String dummy = "0000000000000000000000000000000000000000000000000000000000000000";
        
        System.out.println("=== Round-Trip Encryption/Decryption Test ===");
        System.out.println("Original Plaintext: " + plaintext);
        System.out.println("Key:               " + key);
        System.out.println();
        
        // Test DES0
        System.out.println("DES0 (Standard DES):");
        DES0 des0 = new DES0(plaintext, dummy, key, dummy);
        des0.encryptDES(plaintext, dummy, key, dummy);
        String cipher0 = des0.getCipherTextP();
        String decrypted0 = des0.decryptDES(cipher0, key);
        System.out.println("  Ciphertext:    " + cipher0);
        System.out.println("  Decrypted:     " + decrypted0);
        System.out.println("  Round-trip OK: " + plaintext.equals(decrypted0));
        System.out.println();
        
        // Test DES1
        System.out.println("DES1 (No XOR with round key):");
        DES1 des1 = new DES1(plaintext, dummy, key, dummy);
        des1.encryptDES(plaintext, dummy, key, dummy);
        String cipher1 = des1.getCipherTextP();
        String decrypted1 = des1.decryptDES(cipher1, key);
        System.out.println("  Ciphertext:    " + cipher1);
        System.out.println("  Decrypted:     " + decrypted1);
        System.out.println("  Round-trip OK: " + plaintext.equals(decrypted1));
        System.out.println();
        
        // Test DES2
        System.out.println("DES2 (Inverse expansion instead of S-boxes):");
        DES2 des2 = new DES2(plaintext, dummy, key, dummy);
        des2.encryptDES(plaintext, dummy, key, dummy);
        String cipher2 = des2.getCipherTextP();
        String decrypted2 = des2.decryptDES(cipher2, key);
        System.out.println("  Ciphertext:    " + cipher2);
        System.out.println("  Decrypted:     " + decrypted2);
        System.out.println("  Round-trip OK: " + plaintext.equals(decrypted2));
        System.out.println();
        
        // Test DES3
        System.out.println("DES3 (No P-box):");
        DES3 des3 = new DES3(plaintext, dummy, key, dummy);
        des3.encryptDES(plaintext, dummy, key, dummy);
        String cipher3 = des3.getCipherTextP();
        String decrypted3 = des3.decryptDES(cipher3, key);
        System.out.println("  Ciphertext:    " + cipher3);
        System.out.println("  Decrypted:     " + decrypted3);
        System.out.println("  Round-trip OK: " + plaintext.equals(decrypted3));
        System.out.println();
        
        // Summary
        boolean allPassed = plaintext.equals(decrypted0) && 
                           plaintext.equals(decrypted1) && 
                           plaintext.equals(decrypted2) && 
                           plaintext.equals(decrypted3);
        System.out.println("=== TEST SUMMARY ===");
        System.out.println("All variants encrypt/decrypt correctly: " + allPassed);
    }
}
