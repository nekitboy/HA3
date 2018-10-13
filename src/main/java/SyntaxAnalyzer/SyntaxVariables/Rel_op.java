package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

public class Rel_op extends SyntaxVariable {
    static final String type = "Rel_op";
    private String op;
    public Rel_op() {
        super(type);
    }
}
