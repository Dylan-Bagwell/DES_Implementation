public class TestDecryption {
    public static void main(String[] args) {
        String plaintext = "1011010110101101101110001111000010101011001011110000111100001101";
        String key = "1100110001111001101010110010101011110000111101010101010101010101";
        String dummy = "0000000000000000000000000000000000000000000000000000000000000000";
        
        // Test DES0 (should work)
        System.out.println("Testing DES0 (Standard DES):");
        DES0 des0_encrypt = new DES0(plaintext, dummy, key, dummy);
        des0_encrypt.encryptDES(plaintext, dummy, key, dummy);
        String ciphertext0 = des0_encrypt.getCipherTextP();
        System.out.println("Plaintext:  " + plaintext);
        System.out.println("Ciphertext: " + ciphertext0);
        
        // Test DES2 (should work with fixed inverse expansion)
        System.out.println("\nTesting DES2 (Inverse Expansion):");
        DES2 des2_encrypt = new DES2(plaintext, dummy, key, dummy);
        des2_encrypt.encryptDES(plaintext, dummy, key, dummy);
        String ciphertext2 = des2_encrypt.getCipherTextP();
        System.out.println("Plaintext:  " + plaintext);
        System.out.println("Ciphertext: " + ciphertext2);
        
        // Test DES3 (should work)
        System.out.println("\nTesting DES3 (No P-box):");
        DES3 des3_encrypt = new DES3(plaintext, dummy, key, dummy);
        des3_encrypt.encryptDES(plaintext, dummy, key, dummy);
        String ciphertext3 = des3_encrypt.getCipherTextP();
        System.out.println("Plaintext:  " + plaintext);
        System.out.println("Ciphertext: " + ciphertext3);
        
        // Note: To fully test decryption, we'd need to implement the decryption algorithm
        // For now, this shows that encryption works without errors
    }
}
