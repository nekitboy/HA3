package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

// binary_op  = "||" | "&&" | rel_op | add_op | mul_op .
public class Binary_op extends SyntaxVariable {
    static final String type = "Binary_op";
    private String op;
    private Rel_op rel_op;
    private Add_op add_op;
    private Mul_op mul_op;

    public Binary_op() {
        super(type);
    }
}
