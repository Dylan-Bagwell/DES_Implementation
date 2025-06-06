public class QuickDecryptTest {
    public static void main(String[] args) {
        String ciphertext = "1011111011110010101110111000011010011100011101000110011010110011";
        String key = "1100110001111001101010110010101011110000111101010101010101010101";
        
        DES0 des0 = new DES0("", "", "", "");
        String decrypted = des0.decryptDES(ciphertext, key);
        System.out.println("Decrypted by DES3: " + decrypted);
        
        // This should match the original plaintext from EncryptInput.txt
        String originalPlaintext = "1011010110101101101110001111000010101011001011110000111100001101";
        System.out.println("Original plaintext: " + originalPlaintext);
        System.out.println("Match: " + decrypted.equals(originalPlaintext));
    }
}
