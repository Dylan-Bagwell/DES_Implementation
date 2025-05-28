/*
 * Authors: Dylan Bagwell C3432837, 
 * Course: 3260 Data Security
 * Date Created: 02/0502025
 * Last Modified: 24/05/2025 
 * 
 * Description: Main class file for the simulation of DES algorithm. This class will take the arguments
 * and run the program creating the processes to be used in the simulation for the 4 DES requirements.
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    private String plaintext = "";
    private String key = "";
    private String inversePlaintext = "";
    private String inverseKey = "";
    public static void main(String[] args) {

    // Reads 64-bit block and key from input.txt
    /*
     * Taking the arguments from the command line and reading the file to get
     * information on the processes that will be used in the simulation.
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

                        // Read the plaintext, inverse plaintext, key, and inverse key from the file based off Assignment spec
                        String plaintext = console.nextLine(); //P
                        String inversePlaintext = console.nextLine(); //P'
                        String key = console.nextLine();//K
                        String inverseKey = console.nextLine();//K'

                        //Full DES algorithm
                        DES0 des = new DES0(plaintext,inversePlaintext,key,inverseKey);
                        des.encryptDES(plaintext, inversePlaintext, key, inverseKey);
                    

                    }
                }

            }
        }
    }
}