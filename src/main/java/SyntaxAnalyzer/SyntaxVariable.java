package SyntaxAnalyzer;

import java.util.ArrayList;
import java.util.Scanner;

public abstract class SyntaxVariable {
    private String type;
    private static final int TAB_SPACE = 2;

    public SyntaxVariable(String type) {
        this.type = type;
    }

    public StringBuilder toJSON() {
        StringBuilder json = new StringBuilder(type);

        return json;
    }

    public String getType() {
        return type;
    }

    protected static StringBuilder tabulize(StringBuilder s) {
        StringBuilder tabulized = new StringBuilder();
        Scanner sc = new Scanner(s.toString());
        while(sc.hasNextLine()) {
            for (int i=0; i<TAB_SPACE; i++)
                tabulized.append(" ");
            tabulized.append(sc.nextLine());
            if(sc.hasNextLine())
                tabulized.append('\n');
        }
        return tabulized;
    }

    protected static StringBuilder ArrayToJSON(ArrayList <SyntaxVariable> list) {
        StringBuilder str = new StringBuilder();
        if (!list.isEmpty()) {
            /* str
            type:
             */
            str.append(list.get(0).getType() + ": ");

            if (list.size()>1) {
                /* str
                type: [

                 */
                str.append("[\n");
            }

            StringBuilder substr = new StringBuilder();
            /* substr
            ...,
            ...,
            ...
            */
            for (int i = 0; i<list.size(); i++) {
                substr.append(list.get(i).toJSON());
                if (i+1 < list.size())
                    substr.append(",\n");
            }


            if (list.size()>1) {
                /* substr
                    ...,
                    ...,
                    ...
                */
                substr = tabulize(substr);
            }

            /* str
            type: substr
             */
            str.append(substr);

            if (list.size()>1) {
                /* str
                type: [
                    substr
                 ]
                 */
                str.append("\n]");
            }
        }

        return str;
    }
}
