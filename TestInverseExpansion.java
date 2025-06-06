public class TestInverseExpansion {
    
    // Original expansion permutation
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
    
    // Current implementation
    private static String inverseExpansion_Current(String input48) {
        StringBuilder output32 = new StringBuilder();
        for (int i = 0; i < 48; i += 3) {
            if (i + 1 < 48) {
                output32.append(input48.charAt(i + 1));
            }
        }
        while (output32.length() < 32) {
            output32.append(input48.charAt((output32.length() * 3) % 48));
        }
        return output32.toString().substring(0, 32);
    }
    
    // Expansion function
    private static String expandRight(String right) {
        StringBuilder expandedRight = new StringBuilder();
        for (int i : EXPANSION_PERMUTATION) {
            expandedRight.append(right.charAt(i - 1));
        }
        return expandedRight.toString();
    }
    
    public static void main(String[] args) {
        // Test if expansion followed by inverse expansion gives back original
        String original32 = "10110101101011011011100011110000";
        String expanded48 = expandRight(original32);
        String recovered32 = inverseExpansion_Current(expanded48);
        
        System.out.println("Original (32):  " + original32);
        System.out.println("Expanded (48):  " + expanded48);
        System.out.println("Recovered (32): " + recovered32);
        System.out.println("Match: " + original32.equals(recovered32));
    }
}
