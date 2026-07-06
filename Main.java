/*
 * Authors: Dylan Bagwell, Daniel Ferguson 
 * Course: 3260 Data Security
 * Date Created: 02/05/2025
 * Last Modified: 06/06/2025 
 * 
 * Description: Main class file for the simulation of DES algorithm. This class will take the arguments
 * and run the program creating the processes to be used in the simulation for the 4 DES requirements.
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    // input vars
    private String plaintext = "";
    private String inversePlaintext = "";
    private String key = "";
    private String inverseKey = "";
    
    // default vars
    private final String DEFAULT_PLAINTEXT = "1010101010101010101010101010101010101010101010101010101010101010";
    private final String DEFAULT_INV_PLAINTEXT = "1010101010101010101010101010101010101010101010101010101010101011";
    private final String DEFAULT_KEY = "0110011001100110011001100110011001100110011001100110011001100110";
    private final String DEFAULT_INV_KEY = "1110011001100110011001100110011001100110011001100110011001100110";

    // DES interface, used to create the 4 variants of DES as an array for iteration
    private DESInterface[] desVariants = new DESInterface[4];

    private String[] ciphertexts = new String[4];
    private String[] decryptedTexts = new String[4];
    
    private double runTime = 0;
    
    // output files
    private String printTable1 = "";
    private String printTable2 = "";
    private final String encryptionfile = "AvalancheResultsOutput.txt";
    private final String decryptFileName = "VerificationResultsOutput.txt";
    
    public static void main(String[] args) {
        Main main = new Main();
        try {
            main.run(args);  // input arg
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }

    // method to create DES variants, this allows for easy iteration
    private DESInterface createDESVariant(int index) {
        return switch (index) {
            case 0 -> new DES0(plaintext, inversePlaintext, key, inverseKey);
            case 1 -> new DES1(plaintext, inversePlaintext, key, inverseKey);
            case 2 -> new DES2(plaintext, inversePlaintext, key, inverseKey);
            case 3 -> new DES3(plaintext, inversePlaintext, key, inverseKey);
            default -> throw new IllegalArgumentException("DON'T CHANGE ME");
        };
}

    public void run(String[] args) throws FileNotFoundException {
        // import input values
        readInputFile(args);

        // timer for avalanche effect analysis
        Long start = System.currentTimeMillis();

        // Uses the DESInterface to interate over the 4 variants of DES
        // and run the encryption and decryption processes
        for (int i = 0; i < 4; i++) {desVariants[i] = createDESVariant(i);}
        for (int i = 0; i < 4; i++) {desVariants[i].encryptDES(plaintext, inversePlaintext, key, inverseKey);}
        for (int i = 0; i < 4; i++) {ciphertexts[i] = desVariants[i].getCipherTextP();}
        for (int i = 0; i < 4; i++) {decryptedTexts[i] = desVariants[i].decryptDES(ciphertexts[i], key);}

        // time taken
        Long end = System.currentTimeMillis();
        runTime = (end - start) / 1000.0;

        // report results
        generateEncryptionOutput();
        generateDecryptionOutput();
        
        System.out.println("Results written to " + encryptionfile + " and " + decryptFileName);
    }


    private void readInputFile(String[] args) throws FileNotFoundException {
        for (String arg : args) {
            File file = new File(arg);
            Scanner console = new Scanner(file);
            if (file.exists()) {
                try (console) {
                    while (console.hasNext() && file.getName().equals("TestDataInput.txt")) {
                        // Read the plaintext, inverse plaintext, key, and inverse key from the file
                        // based off Assignment spec
                        this.plaintext = console.nextLine(); // P
                        this.inversePlaintext = console.nextLine(); // P'
                        this.key = console.nextLine();// K
                        this.inverseKey = console.nextLine();// K'
                    }
                }
            }
        }
        validateInputs(); // ensure inputs are valid or set defaults
    }

    // used by readInputFile to set defaults if file reading fails
    private void validateInputs() {
        // Use defaults if file reading failed
        if (plaintext.isEmpty()) {
            plaintext = DEFAULT_PLAINTEXT;
            inversePlaintext = DEFAULT_INV_PLAINTEXT;
            key = DEFAULT_KEY;
            inverseKey = DEFAULT_INV_KEY;
            System.out.println("Using default inputs (file not found)");
        } else {
            System.out.println("loaded inputs from file");
        }
    }


    private void generateEncryptionOutput() {
        createOutputFile(encryptionfile); // create or clear the output file
        
        // assignment header
        writeToFile("Avalanche Demonstration\n", encryptionfile);
        writeToFile("Plaintext P: " + plaintext + "\n", encryptionfile);
        writeToFile("Plaintext P': " + inversePlaintext + "\n", encryptionfile);
        writeToFile("Key K: " + key + "\n", encryptionfile);
        writeToFile("Key K': " + inverseKey + "\n", encryptionfile);
        writeToFile("Total running: " + runTime + "(seconds)\n\n", encryptionfile);
        
        // P and P' under K comparison
        writeToFile("P and P' under K\n", encryptionfile);
        writeToFile("Ciphertext C: " + ciphertexts[0] + "\n", encryptionfile);  // DES0 result
        writeToFile("Ciphertext C': " + desVariants[0].getInvCipherText() + "\n", encryptionfile);
        writeToFile("Round    " + desVariants[0].getName() +"    " +desVariants[1].getName()+"    " +desVariants[2].getName()+"    " +desVariants[3].getName()+"\n",encryptionfile);
        
        for (int j = 0; j < 17; j++) {
           printTable1 += String.format("%5d    %3d    %3d    %3d    %3d\n", j, desVariants[0].getCompareRound1(j), desVariants[1].getCompareRound1(j), desVariants[2].getCompareRound1(j), desVariants[3].getCompareRound1(j));
        }
        writeToFile(printTable1, encryptionfile);
        
        // P under K and K' comparison
        writeToFile("\nP under K and K'\n", encryptionfile);
        writeToFile("Ciphertext C: " + ciphertexts[0] + "\n", encryptionfile);  // DES0 result
        writeToFile("Ciphertext C': " + desVariants[0].getCipherPInvK() + "\n", encryptionfile);
        writeToFile("Round    " + desVariants[0].getName() +"    " +desVariants[1].getName()+"    " +desVariants[2].getName()+"    " +desVariants[3].getName()+"\n",encryptionfile);
        
        for (int j = 0; j < 17; j++) {
            printTable2 += String.format("%5d    %3d    %3d    %3d    %3d\n", j, desVariants[0].getCompareRound2(j), desVariants[1].getCompareRound2(j), desVariants[2].getCompareRound2(j), desVariants[3].getCompareRound2(j));
        }
        writeToFile(printTable2, encryptionfile);
    }

    // VerificationResultsOutput file generation
    private void generateDecryptionOutput() {
        createOutputFile(decryptFileName);
        
        writeToFile("DECRYPTION\n", decryptFileName);
        writeToFile("Ciphertext C: " + ciphertexts[0] + "\n", decryptFileName);  // Show DES0's ciphertext as reference
        writeToFile("Key K: " + key + "\n", decryptFileName);
        writeToFile("DES0 Plaintext: " + decryptedTexts[0] + "\n", decryptFileName);
        writeToFile("DES1 Plaintext: " + decryptedTexts[1] + "\n", decryptFileName);
        writeToFile("DES2 Plaintext: " + decryptedTexts[2] + "\n", decryptFileName);
        writeToFile("DES3 Plaintext: " + decryptedTexts[3] + "\n", decryptFileName);
    }


    public void writeToFile(String output, String fileName) {
        try {
            FileWriter write = new FileWriter(fileName, true);
            write.write(output);
            write.close();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }


public void createOutputFile(String fileName) {
    try {
        File outputFile = new File(fileName);
        if (outputFile.createNewFile()) {
            System.out.println(fileName + " file created: " + outputFile.getName());
        } else {
            FileWriter writer = new FileWriter(outputFile, false); // overwrite the file
            writer.write("");
            writer.close();
            System.out.println(fileName + " file cleared.");
        }
    } catch (Exception e) {
        System.out.println(fileName + " file was not created: " + e.getMessage());
    }
}
}
