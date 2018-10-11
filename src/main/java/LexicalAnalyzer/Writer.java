package LexicalAnalyzer;

import java.io.FileWriter;
import java.io.IOException;

/**
 * This class describing a Writer,
 * that writes string to the file
 */
public class Writer {
    private FileWriter wr;

    public Writer(String fileName) {
        try {
            this.wr = new FileWriter(fileName);
        } catch (IOException var3) {
            var3.printStackTrace();
        }

    }

    public void writeString(String s) {
        try {
            this.wr.write(s);
            this.wr.flush();
        } catch (IOException var3) {
            var3.printStackTrace();
        }

    }
}
