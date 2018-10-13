package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

// UnaryExpr  = PrimaryExpr | unary_op UnaryExpr .
public class UnaryExpr extends SyntaxVariable {
    static final String type = "UnaryExpr";
    public UnaryExpr() {
        super(type);
    }
}
