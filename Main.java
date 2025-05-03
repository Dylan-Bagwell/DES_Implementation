/*
 * Author: Your Name(s)
 * Date: 02/0502025
 * Last Modified: 02/05/2025 
 * Description: 
 */
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        // Reads 64-bit block and key from input.txt
        File file = new File("input.txt");
        
        try (Scanner scanner = new Scanner(file)) {
            ArrayList<String> lines = new ArrayList<>();
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
            // Process the lines as needed
            for (String line : lines) {
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}