import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello");
    }

    /**
     * Reads GO source code from the file
     * @param path: filepath
     * @return StringBuilder - which represents the file
     * @throws FileNotFoundException - if the file from `path` doesn't exists
     */
    StringBuilder readSourceCode(String path) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("in.txt"));
        StringBuilder file  = new StringBuilder();
        // read - line by line and add it to the string builder
        while (sc.hasNextLine()){
            file.append(sc.nextLine());
            file.append("\n");
        }
        if (file.length() > 0) { file.deleteCharAt(file.length()-1); }

        return file;
    }
}
