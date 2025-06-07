/*
 * Authors: Dylan Bagwell C3432837, Daniel Ferguson C3373690
 * Course: 3260 Data Security
 * Date Created: 30/0502025
 * Last Modified: 06/06/2025
 * 
 * Description: The Second implementation of the DES algortihm.
 */
public class DES1 implements DESInterface {

    private String name = "DES1"; // Name of the DES implementation
    private String plaintext = "";
    private String key = "";
    private String inversePlaintext = "";
    private String inverseKey = "";
    private String ciphertext;
    private String inverseCiphertext;
    private String pUnderKInv;
    private int[] compareRound1; // Array to store comparison results for each round
    private int[] compareRound2; // Array to store comparison results for each round

    // Constructor for DES0 class
    public DES1(String plaintext, String key, String inversePlaintext, String inverseKey) {
        this.plaintext = plaintext;
        this.key = key;
        this.inversePlaintext = inversePlaintext;
        this.inverseKey = inverseKey;
        this.compareRound1 = new int[17]; // Initialize comparison array for 16 rounds
        this.compareRound2 = new int[17];
    }

    public String getName() {
        return name;
    }

    public String getCipherPInvK() {
        return pUnderKInv;
    }
    public String getCipherTextP() {
        return ciphertext;
    }

    public String getInvCipherText() {
        return inverseCiphertext;
    }
    // round bit comparison
    public int getCompareRound1(int round) {
        return compareRound1[round];
    }

    public int getCompareRound2(int round){
        return compareRound2[round];
    }

    // Expansion Permutation (E Table) used at start and end of each function
    private static final int[] EXPANSION_PERMUTATION = {
            32, 1, 2, 3, 4, 5,
            4, 5, 6, 7, 8, 9,
            8, 9, 10, 11, 12, 13,
            12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29,
            28, 29, 30, 31, 32, 1
    };

    // Permutation table for the initial permutation(IP) start of encryption
    private static final int[] INITIAL_PERMUTATION = {
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7
    };

    // Permutation table for the final permutation(IP-1 Inverse)
    private static final int[] FINAL_PERMUTATION = {
            40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25
    };

    // S-boxes for the DES algorithm [row][column]
    private static final int[][] S1_BOX = {
            // s1
            { 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7 },
            { 0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8 },
            { 4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0 },
            { 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13 }
    };
    // s2
    private static final int[][] S2_BOX = {
            { 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10 },
            { 3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5 },
            { 0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15 },
            { 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 }
    };
    // s3
    private static final int[][] S3_BOX = {
            { 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8 },
            { 13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1 },
            { 13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7 },
            { 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12 }
    };
    // s4
    private static final int[][] S4_BOX = {
            { 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15 },
            { 13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9 },
            { 10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4 },
            { 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14 }
    };
    // s5
    private static final int[][] S5_BOX = {
            { 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9 },
            { 14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6 },
            { 4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14 },
            { 11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3 }
    };
    // s6
    private static final int[][] S6_BOX = {
            // s6
            { 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11 },
            { 10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8 },
            { 9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6 },
            { 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13 }
    };
    // s7
    private static final int[][] S7_BOX = {
            { 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1 },
            { 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2 },
            { 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2 },
            { 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12 }
    };
    // s8
    private static final int[][] S8_BOX = {
            { 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7 },
            { 1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2 },
            { 7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8 },
            { 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11 }
    };

    // Permuation Function(P) used at start and end of each function
    private static final int[] PERMUTATION = {
            16, 7, 20, 21,
            29, 12, 28, 17,
            1, 15, 23, 26,
            5, 18, 31, 10,
            2, 8, 24, 14,
            32, 27, 3, 9,
            19, 13, 30, 6,
            22, 11, 4, 25
    };

    // Permutation Choice 1 (PC-1) used to permute the key
    private static final int[] PC1 = {
            57, 49, 41, 33, 25, 17, 9,
            1, 58, 50, 42, 34, 26, 18,
            10, 2, 59, 51, 53, 35, 27,
            19, 11, 3, 60, 52, 44, 36,
            63, 55, 47, 39, 31, 23, 15,
            7, 62, 54, 46, 38, 30, 22,
            14, 6, 61, 53, 45, 37, 29,
            21, 13, 5, 28, 20, 12, 4
    };

    // The number of bits to shift left for each round
    private static final int[] LEFT_SHIFTS = { 1, 1, 2, 2, 2, 2, 1, 1, 2, 2, 2, 2, 1, 1, 2, 2 };

    // Permutation Choice 2 (PC-2) used to permute the key for each round
    private static final int[] PC2 = {
            14, 17, 11, 24, 1, 5, 3, 28,
            15, 6, 21, 10, 23, 19, 12, 4,
            26, 8, 16, 7, 27, 20, 13, 2,
            41, 52, 31, 37, 47, 55, 30, 40,
            51, 45, 33, 48, 44, 49, 39, 56,
            34, 53, 46, 42, 50, 36, 29, 32
    };

    /**
     * Encrypts the plaintext using the DES algorithm.
     * Is comparing the plaintext and inverse plaintext with a key to produce
     * ciphertext and round comparision.In one method for simplicity.
     * 
     * @param plaintext        The plaintext to be encrypted.
     * @param inversePlaintext The inverse plaintext (not used in this method).
     * @param key              The key to be used for encryption.
     */
    public void encryptDES(String plaintext, String inversePlaintext, String key,String inverseKey) {

        
        int round = 0;

        // round zero comparison 
        compareRound1[0] = compareRound(plaintext, inversePlaintext, round);
        
        
        
        // Initial Permutation
        String permPlain = permutation(plaintext, INITIAL_PERMUTATION);
        String permInvPlain = permutation(inversePlaintext, INITIAL_PERMUTATION);
        String plainKeyInv = permutation(plaintext, INITIAL_PERMUTATION);
        

        //String pc1Key = pc1Key(key, PC1);
        //String pc1KeyInv = pc1Key(inverseKey, PC1);
        // Split the perumutated plaintext into left and right halves
        String left = permPlain.substring(0, 32);
        String right = permPlain.substring(32, 64);

        String leftInv = permInvPlain.substring(0, 32);
        String rightInv = permInvPlain.substring(32, 64);

        String leftPlainInv = plainKeyInv.substring(0, 32);
        String rightPlainInv = plainKeyInv.substring(32, 64);


        // 16 rounds of DES
        for (int i = 0; i < 16; i++) {

            String expandedRight = expandRight(right, EXPANSION_PERMUTATION); // expansion of the right half
            //String xorResult = functionXOR(expandedRight, pc2Key(pc1Key, PC2));// XOR of expanded right and PC2 key
            String sBoxOutput = sBoxSubstitution(expandedRight);
            String pBoxOutput = endPermutaion(sBoxOutput, PERMUTATION);
            String newRight = functionXOR(left, pBoxOutput);
           

            String tempComp1 = left + right; // combine left and right halves for comparison P and K

            // Update left and right halves for the next round
            left = right; // left becomes the old right
            right = newRight; // right becomes the new right

            // Second round for comparison
            String expandedRightInv = expandRight(rightInv, EXPANSION_PERMUTATION); // expansion of the right half
            //String xorResultInv = functionXOR(expandedRightInv, pc2Key(pc1Key, PC2));// XOR of expanded right and PC2
            String sBoxOutputInv = sBoxSubstitution(expandedRightInv);
            String pBoxOutputInv = endPermutaion(sBoxOutputInv, PERMUTATION);
            String newRightInv = functionXOR(leftInv, pBoxOutputInv);

            

            String tempComp2 = leftInv + rightInv; // combine left and right halves for comparison P' and K

            // Update left and right halves for the next round
            leftInv = rightInv; // left becomes the old right
            rightInv = newRightInv; // right becomes the new right
           

            // plaintext with inverse key comparison
            String expandedRightIvKy = expandRight(rightPlainInv, EXPANSION_PERMUTATION); // expansion of the right half
            //String xorResultIvKy = functionXOR(expandedRightIvKy, pc2Key(pc1KeyInv, PC2));// XOR of expanded right and PC2
            String sBoxOutputIvKy = sBoxSubstitution(expandedRightIvKy);
            String pBoxOutputIvKy = endPermutaion(sBoxOutputIvKy, PERMUTATION);
            String newRightIvKy = functionXOR(leftPlainInv, pBoxOutputIvKy);

            

            String tempComp3 = leftPlainInv+ rightPlainInv; // plaintext with inverse key comparison
            
            // Update left and right halves for the next round
            leftPlainInv = rightPlainInv; // left becomes the old right
            rightPlainInv = newRightIvKy; // right becomes the new right

            //Comparisions to be stored and printed out later
            compareRound1[i + 1] = compareRound(tempComp1, tempComp2, round);//P and P' under K
            compareRound2[i + 1] = compareRound(tempComp1, tempComp3, round);//P under K and K'
            round++;
        }

        // Combine left and right halves
        String combinedHalves = right + left;
        String combinedHalvesInv = rightInv + leftInv;
        String combinedHalvesPlainInv = rightPlainInv + leftPlainInv;
        // Final permutation (IP-1 Inverse)
        this.ciphertext = permutation(combinedHalves, FINAL_PERMUTATION);
        this.inverseCiphertext = permutation(combinedHalvesInv, FINAL_PERMUTATION);
        this.pUnderKInv = permutation(combinedHalvesPlainInv, FINAL_PERMUTATION);
        
    }

    // is a permutation function that rearranges the input string based on the
    // provided permutation table (P)
    private static String permutation(String input, int[] permutation) {
        StringBuilder output = new StringBuilder();
        for (int i : permutation) {
            output.append(input.charAt(i - 1));
        }
        return output.toString();
    }

    // returns a key permuted using the PC1 table
    private static String pc1Key(String key, int[] pc1) {
        StringBuilder permutatedKey = new StringBuilder();

        // Apply pc1 permutation to the key
        for (int i : pc1) {
            permutatedKey.append(key.charAt(i - 1));
        }
        String pc1Key = permutatedKey.toString();

        // System.out.println("Key after PC1 permutation: " + pc1Key);

        key = pc1Key.toString();
        return key;
    }    

    // Get the shift length for the round then perform left shift on the key
    private static String leftShfit(int round, String key) {
        String keyshifted = "";
        int shiftLength;
        // make sure to stay in the range of 16 rounds
        if (round >= 16 || round < 0) {
            throw new IllegalArgumentException("Round must be between 1 and 16");
        } else {

            shiftLength = LEFT_SHIFTS[round];
            keyshifted = key.substring(shiftLength) + key.substring(0, shiftLength);
            // System.out.println("Key length after left shift:" + keyshifted.length());
        }
        return keyshifted;
    }

    // returns a key thats been shifted left and permuted using the PC2 table
    private static String pc2Key(String pc1Key, int[] pc2) {
        StringBuilder pc2Key = new StringBuilder();

        // Split the key into two halves then perform left shift
        String cKey = pc1Key.substring(0, 28);// 28 bits for C testing with 0 round for now
        // System.out.println("C Key: " + cKey.length());
        String dKey = pc1Key.substring(28, 56);// 28 bits for D
        // System.out.println("D Key: " + dKey.length());
        cKey = leftShfit(0, cKey);
        dKey = leftShfit(0, dKey);

        // Combine the halves
        String combined = cKey + dKey;

        // Permute combined key using pc2 array
        for (int i : pc2) {
            pc2Key.append(combined.charAt(i - 1));
        }

        // System.out.println("Key after PC2 permutation: " + pc2Key);
        // System.out.println("Key length after PC2 permutation: " + pc2Key.length());
        String key = pc2Key.toString();
        return key;
    }

    // takes right half and performs (E) expansion permutation
    private static String expandRight(String right, int[] expansionPermutation) {
        StringBuilder expandedRight = new StringBuilder();
        for (int i : expansionPermutation) {
            expandedRight.append(right.charAt(i - 1));
        }
        return expandedRight.toString();
    }

    // performs XOR operation
    private static String functionXOR(String side1, String side2) {
        String newRight = "";
        for (int i = 0; i < side1.length(); i++) {

            if (side1.charAt(i) == side2.charAt(i)) {
                newRight += "0"; // XOR 0 with 0 or 1 with 1
            } else {
                newRight += "1"; // XOR 0 with 1 or 1 with 0
            }
        }
        return newRight;
    }

    // performs S-box substitution and returns the output
    private String sBoxSubstitution(String xorResult) {

        String sboxOutput = "";

        // check if the XOR result is 48 bits long
        if (xorResult.length() != 48) {
            throw new IllegalArgumentException("XOR result must be 48 bits long");
        } else {
            for (int i = 0; i < 8; i++) {

                // take 6 bits at a time to put into S-box rounds until 48 bits are processed

                int sBoxValue = 0;
                String inputSBox = xorResult.substring(0, 6);
                // System.out.println("Input to S-box: " + inputSBox);

                String outsideBits = inputSBox.substring(0, 1) + inputSBox.substring(5, 6); // take the first and last
                                                                                            // bits

                // take the middle 4 bits
                String middleBits = inputSBox.substring(1, 5);

                // Convert outsideBits to row and middleBits to column
                int row = Integer.parseInt(outsideBits, 2); // Convert binary to decimal
                int column = Integer.parseInt(middleBits, 2); // Convert binary to decimal

                switch (i) {
                    case 0:
                        sBoxValue = S1_BOX[row][column];
                        break;
                    case 1:
                        sBoxValue = S2_BOX[row][column];
                        break;
                    case 2:
                        sBoxValue = S3_BOX[row][column];
                        break;
                    case 3:
                        sBoxValue = S4_BOX[row][column];
                        break;
                    case 4:
                        sBoxValue = S5_BOX[row][column];
                        break;
                    case 5:
                        sBoxValue = S6_BOX[row][column];
                        break;
                    case 6:
                        sBoxValue = S7_BOX[row][column];
                        break;
                    case 7:
                        sBoxValue = S8_BOX[row][column];
                        break;
                }
                // System.out.println("S-box value: " + sBoxValue);
                // remove the first 6 bits from the XOR result
                xorResult = xorResult.substring(6);
                // System.out.println(xorResult.length());

                // Convert back to binary
                sboxOutput += String.format("%4s", Integer.toBinaryString(sBoxValue)).replace(' ', '0');

            }
        }
        return sboxOutput;
    }

    // Performs the perumation (P) on the S-box output
    private String endPermutaion(String sBox, int[] permutationP) {
        StringBuilder permutedOutput = new StringBuilder();
        for (int i : permutationP) {
            permutedOutput.append(sBox.charAt(i - 1));
        }
        // System.out.println("S-box output after P permutation: " + permutedOutput + " Length: "
        // + permutedOutput.length());//should be 32 bits long
        return permutedOutput.toString();
    }

    private int compareRound(String one, String two, int round) {
        int count = 0;
        // Check if the two strings are of equal length
        if (one.length() != two.length()) {
            throw new IllegalArgumentException("Strings must be of equal length");
        }

        // Compare each character in the strings
        for (int i = 0; i < one.length(); i++) {
            if (one.charAt(i) != two.charAt(i)) {
                count++;
            }
        }
        
        return count; // Return the number of differing bits
    }

    /**
     * Decrypts the ciphertext using DES1 algorithm (no XOR with round key).
     * 
     * @param decryptText The ciphertext to be decrypted.
     * @param decryptKey The key to be used for decryption.
     * @return The decrypted plaintext.
     */
    public String decryptDES(String decryptText, String decryptKey) {
        String decrypted = "";

        String decrpt = permutation(decryptText, INITIAL_PERMUTATION);

        // Split the perumutated ciphertext into left and right halves
        String left = decrpt.substring(0, 32);
        String right = decrpt.substring(32, 64);

        for (int i = 15; i >= 0; i--) {
            String expandedRight = expandRight(right, EXPANSION_PERMUTATION); // expansion of the right half
            // DES1: Skip XOR with round key, but keep S-boxes and P-box
            String sBoxOutput = sBoxSubstitution(expandedRight);
            String pBoxOutput = endPermutaion(sBoxOutput, PERMUTATION);
            String newRight = functionXOR(left, pBoxOutput);

            // Update left and right halves for the next round
            left = right; // left becomes the old right
            right = newRight; // right becomes the new right
        }

        // Combine left and right halves
        String combinedHalves = right + left;
        // Final permutation (IP-1 Inverse)
        decrypted = permutation(combinedHalves, FINAL_PERMUTATION);
        return decrypted;
    }
}
