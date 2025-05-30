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
    private double runTime = 0; // will be used to track runtime of Avalanche effect
    private String printTable1 = "";
    private String printTable2 = "";

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
                    while (console.hasNext()) {

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
        // Full DES algorithm

        // Start timer for avalanche effect analysis
        Long start = System.currentTimeMillis();

        DES0 des0 = new DES0(plaintext, inversePlaintext, key, inverseKey);
        DES1 des1 = new DES1(plaintext, inversePlaintext,key, inverseKey);
        des0.encryptDES(plaintext, inversePlaintext, key,inverseKey);
        des1.encryptDES(plaintext,inversePlaintext, key, inverseKey);
        
        
        Long end = System.currentTimeMillis();
        
        runTime = (int) (end - start)/1000; // Calculate the total running time in seconds
        // creates output file
        createOutputFile();

        // write to output file
        writeToFile("Avalanche Demonstration\n");
        writeToFile("Plaintext P: " + plaintext + "\n");
        writeToFile("Plaintext P': " + inversePlaintext + "\n");
        writeToFile("Key K: " + key + "\n");
        writeToFile("Key K: " + inverseKey + "\n");
        writeToFile("Total running: " + runTime + "(seconds)\n\n");
        writeToFile("P and P' under K\n");
        writeToFile("Ciphertext C: " + des0.getCipherTextP() + "\n");
        writeToFile("Ciphertext C': " + des0.getInvCipherText() + "\n");
        writeToFile("Round    " + des0.getName() +"    " +des1.getName()+"\n");// will add in all the names of the DES1.. etc
        for (int j = 0; j < 17; j++) {
           printTable1 += String.format("%5d    %3d    %3d\n", j, des0.getCompareRound1(j), des1.getCompareRound1(j));
        }
        writeToFile(printTable1);
        writeToFile("\nP under K and K'\n");
        writeToFile("Ciphertext C: " + des0.getCipherTextP() + "\n");
        writeToFile("Ciphertext C':" + des0.getCipherPInvK() + "\n");
         writeToFile("Round    " + des0.getName() +"    " +des1.getName()+"\n");// will add in all the names of the DES1.. etc
        for (int j = 0; j < 17; j++) {
            printTable2 += String.format("%5d    %3d    %3d\n", j, des0.getCompareRound2(j), des1.getCompareRound2(j));
        }
        writeToFile(printTable2);
        
        
    }

    /**
     * Method to write the output to a file.
     * Takes strings that need to be written to the output file
     * Made it general so it can be used for any output
     * 
     * @param output The output string to be written to the file.
     * @throws IOException
     */
    public void writeToFile(String output) {
        try {
            // Create a FileWriter object to write to the file in append mode
            FileWriter write = new FileWriter("EncryptionOutput.txt", true);

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
}