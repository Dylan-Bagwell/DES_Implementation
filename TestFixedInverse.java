public class TestFixedInverse {
    
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
    
    private static final int[] INVERSE_EXPANSION_TABLE = {
        2, 3, 4, 5, 6, 9, 10, 11, 12, 15, 16, 17, 18, 21, 22, 23, 
        24, 27, 28, 29, 30, 33, 34, 35, 36, 39, 40, 41, 42, 45, 46, 1
    };
    
    private static String expandRight(String right) {
        StringBuilder expandedRight = new StringBuilder();
        for (int i : EXPANSION_PERMUTATION) {
            expandedRight.append(right.charAt(i - 1));
        }
        return expandedRight.toString();
    }
    
    private static String inverseExpansion(String input48) {
        StringBuilder output32 = new StringBuilder();
        for (int pos : INVERSE_EXPANSION_TABLE) {
            output32.append(input48.charAt(pos - 1));
        }
        return output32.toString();
    }
    
    public static void main(String[] args) {
        String original32 = "10110101101011011011100011110000";
        String expanded48 = expandRight(original32);
        String recovered32 = inverseExpansion(expanded48);
        
        System.out.println("Original (32):  " + original32);
        System.out.println("Expanded (48):  " + expanded48);
        System.out.println("Recovered (32): " + recovered32);
        System.out.println("Match: " + original32.equals(recovered32));
        
        // Test with another string
        String test2 = "11111111000000001111111100000000";
        String expand2 = expandRight(test2);
        String recover2 = inverseExpansion(expand2);
        System.out.println("\nTest 2:");
        System.out.println("Original:  " + test2);
        System.out.println("Recovered: " + recover2);
        System.out.println("Match: " + test2.equals(recover2));
    }
}
