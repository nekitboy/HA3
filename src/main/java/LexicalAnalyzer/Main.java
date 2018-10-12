package LexicalAnalyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by rakZeee on 15.09.2018.
 * This is main class of the program.
 * It uses LexicalAnalyzer and tokens one by one
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(new File("in.txt"));
        StringBuilder file  = new StringBuilder();
        // read - line by line and add it to the string builder
        while (sc.hasNextLine()){
            file.append(sc.nextLine());
            file.append("\n");
        }
        if (file.length() > 0) { file.deleteCharAt(file.length()-1); }
        LexicalAnalyzer lex = new LexicalAnalyzer(file);
        LexicalAnalyzer.Token token = null;
        Writer wr = new Writer("out.txt");
        // if meet EOF - finish tokenizing
        while (token == null || token.type != LexicalAnalyzer.TypeOfToken.EOF){
            // get next token
            token = lex.nextToken();
            if (token != null && token.type != LexicalAnalyzer.TypeOfToken.WhiteSpace) {
                wr.writeString(token.toString() + "\n");
            }
            if(token == null)
                break;
        }
    }
}
