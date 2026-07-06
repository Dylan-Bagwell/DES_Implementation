/*
 * Authors: Dylan Bagwell, Daniel Ferguson
 * Course: 3260 Data Security
 * Date Created: 02/05/2025
 * Last Modified: 06/06/2025
 *
 * Description: The DESInterface defines the methods that must be implemented by any DES variant.
 * This has been created to allow easy iteration inside the main class
 */

public interface DESInterface {
    void encryptDES(String plaintext, String inversePlaintext, String key, String inverseKey);
    String decryptDES(String ciphertext, String key);
    String getCipherTextP();
    String getInvCipherText();
    String getCipherPInvK();
    String getName();
    int getCompareRound1(int round);
    int getCompareRound2(int round);
}
