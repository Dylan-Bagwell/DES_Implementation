// Analysis of DES Expansion for creating proper inverse
public class AnalyzeExpansion {
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
    
    public static void main(String[] args) {
        System.out.println("Creating inverse expansion mapping...");
        
        // For each original position 1-32, find where it first appears in expansion
        int[] inverseMap = new int[32];
        
        for (int i = 0; i < 48; i++) {
            int originalPos = EXPANSION_PERMUTATION[i];
            int arrayIndex = originalPos - 1; // Convert to 0-based
            
            // If this is the first time we see this position, record it
            if (inverseMap[arrayIndex] == 0) {
                inverseMap[arrayIndex] = i + 1; // Convert to 1-based
            }
        }
        
        System.out.println("Inverse expansion array:");
        System.out.print("{ ");
        for (int i = 0; i < 32; i++) {
            System.out.print(inverseMap[i]);
            if (i < 31) System.out.print(", ");
        }
        System.out.println(" }");
    }
}