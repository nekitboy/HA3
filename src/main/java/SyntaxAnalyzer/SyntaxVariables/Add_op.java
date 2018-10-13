package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

public class Add_op extends SyntaxVariable {
    static final String type = "Add_op";
    private String op;
    public Add_op() {
        super(type);
    }
}
