/*
 * Authors: Dylan Bagwell C3432837, Daniel Ferguson C3373690
 * Course: 3260 Data Security
 * Date Created: 02/0502025
 * Last Modified: 30/05/2025 
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
    private String plaintext = "";
    private String key = "";
    private String inversePlaintext = "";
    private String inverseKey = "";
    private String DecryptText = "";
    private String DecryptKey = "";
    private double runTime = 0; // will be used to track runtime of Avalanche effect
    private String printTable1 = "";
    private String printTable2 = "";
    private String encryptionfile = "EncryptionOutput.txt";
    private String decryptFileName = "DecryptionOutput.txt";
    public static void main(String[] args) {

        /*
         * Taking the arguments from the command line and reading the file to get
         * information on the processes that will be used in the simulation.
         * Reads 64-bit block and key from input.txt
         */
        Main main = new Main();
        try {
            main.run(args);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }

    public void run(String[] args) throws FileNotFoundException {

        for (int i = 0; i < args.length; i++) {
            File file = new File(args[i]);
            Scanner console = new Scanner(file);
            if (file.exists()) {
                try (console) {
                    while (console.hasNext() && file.getName().equals("EncryptInput.txt")) {

                        // Read the plaintext, inverse plaintext, key, and inverse key from the file
                        // based off Assignment spec
                        this.plaintext = console.nextLine(); // P
                        this.inversePlaintext = console.nextLine(); // P'
                        this.key = console.nextLine();// K
                        this.inverseKey = console.nextLine();// K'

                    }
                    while (console.hasNext() && file.getName().equals("DecryptionInput.txt")) {

                        // Read the ciphertext and key for decryption
                        this.DecryptText = console.nextLine(); // C
                        this.DecryptKey = console.nextLine(); // K

                    }
                }

            }
        }
        // Full DES algorithm

        // Start timer for avalanche effect analysis
        Long start = System.currentTimeMillis();

        DES0 des0 = new DES0(plaintext, inversePlaintext, key, inverseKey);
        DES1 des1 = new DES1(plaintext, inversePlaintext,key, inverseKey);
        DES2 des2 = new DES2(plaintext, inversePlaintext,key, inverseKey);
        DES3 des3 = new DES3(plaintext, inversePlaintext,key, inverseKey);
        des0.encryptDES(plaintext, inversePlaintext, key,inverseKey);
        des1.encryptDES(plaintext,inversePlaintext, key, inverseKey);
        des2.encryptDES(plaintext,inversePlaintext, key, inverseKey);
        des3.encryptDES(plaintext,inversePlaintext, key, inverseKey);
        
        
        Long end = System.currentTimeMillis();
        
        runTime = (int) (end - start)/1000; // Calculate the total running time in seconds
        // creates output file
        createOutputFile();
        createDecryptOutputFile();
        // write to output file
        writeToFile("Avalanche Demonstration\n", encryptionfile);
        writeToFile("Plaintext P: " + plaintext + "\n", encryptionfile);
        writeToFile("Plaintext P': " + inversePlaintext + "\n", encryptionfile);
        writeToFile("Key K: " + key + "\n", encryptionfile);
        writeToFile("Key K': " + inverseKey + "\n", encryptionfile);
        writeToFile("Total running: " + runTime + "(seconds)\n\n", encryptionfile);
        writeToFile("P and P' under K\n", encryptionfile);
        writeToFile("Ciphertext C: " + des0.getCipherTextP() + "\n", encryptionfile);
        writeToFile("Ciphertext C': " + des0.getInvCipherText() + "\n", encryptionfile);
        writeToFile("Round    " + des0.getName() +"    " +des1.getName()+"    " +des2.getName()+"    " +des3.getName()+"\n",encryptionfile);
        for (int j = 0; j < 17; j++) {
           printTable1 += String.format("%5d    %3d    %3d    %3d    %3d\n", j, des0.getCompareRound1(j), des1.getCompareRound1(j), des2.getCompareRound1(j), des3.getCompareRound1(j));
        }
        writeToFile(printTable1, encryptionfile);
        writeToFile("\nP under K and K'\n", encryptionfile);
        writeToFile("Ciphertext C: " + des0.getCipherTextP() + "\n", encryptionfile);
        writeToFile("Ciphertext C':" + des0.getCipherPInvK() + "\n", encryptionfile);
         writeToFile("Round    " + des0.getName() +"    " +des1.getName()+"    " +des2.getName()+"    " +des3.getName()+"\n",encryptionfile);
        for (int j = 0; j < 17; j++) {
            printTable2 += String.format("%5d    %3d    %3d    %3d    %3d\n", j, des0.getCompareRound2(j), des1.getCompareRound2(j), des2.getCompareRound2(j), des3.getCompareRound2(j));
        }
        writeToFile(printTable2, encryptionfile);
        
        //Decryption File
        writeToFile("DECRYPTION\n", decryptFileName);
        writeToFile("Ciphertext C: " + DecryptText+"\n", decryptFileName);
        writeToFile("Key K: " + DecryptKey+"\n",decryptFileName);
        writeToFile("DES0 Plaintext: " + des0.decryptDES(DecryptText, DecryptKey)+"\n", decryptFileName);
        writeToFile("DES1 Plaintext: " + des1.decryptDES(DecryptText, DecryptKey)+"\n", decryptFileName);
        writeToFile("DES2 Plaintext: " + des2.decryptDES(DecryptText, DecryptKey)+"\n", decryptFileName);
        writeToFile("DES3 Plaintext: " + des3.decryptDES(DecryptText, DecryptKey)+"\n", decryptFileName);
    }

    /**
     * Method to write the output to a file.
     * Takes strings that need to be written to the output file
     * Made it general so it can be used for any output
     * 
     * @param output The output string to be written to the file.
     * @throws IOException
     */
    public void writeToFile(String output,String fileName) {
        try {
            // Create a FileWriter object to write to the file in append mode
            FileWriter write = new FileWriter(fileName, true);

            // Write the output string to the file
            write.write(output);
            write.close();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public void createOutputFile() {
        // create the output file if it doesnt exist
        try {
            File outputFile = new File("EncryptionOutput.txt");
            if (outputFile.createNewFile()) {
                System.out.println("Output file created: " + outputFile.getName());
            } else {
                System.out.println("Output file already exists.");
            }
        } catch (Exception e) {
            System.out.println("EncryptionOutput file was not created: " + e.getMessage());
        }
    }

    public void createDecryptOutputFile() {
        // create the output file if it doesnt exist
        try {
            File outputFile = new File("DecryptionOutput.txt");
            if (outputFile.createNewFile()) {
                System.out.println("DecryptionOutput file created: " + outputFile.getName());
            } else {
                System.out.println("DecryptionOutput file already exists.");
            }
        } catch (Exception e) {
            System.out.println("DecryptionOutput file was not created: " + e.getMessage());
        }
    }
}