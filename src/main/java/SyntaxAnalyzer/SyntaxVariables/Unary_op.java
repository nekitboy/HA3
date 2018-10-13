package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

public class Unary_op extends SyntaxVariable {
    static final String type = "Unary_op";
    private String op;
    public Unary_op() {
        super(type);
    }
}
