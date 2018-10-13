package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

// Expression = UnaryExpr | Expression binary_op Expression .
public class Expression extends SyntaxVariable {
    static final String type = "Expression";
    private UnaryExpr unaryExpr;
    private Expression expression;
    private Binary_op binary_op;

    public Expression() {
        super(type);
    }

    public void setUnaryExpr(UnaryExpr unaryExpr) throws Exception {
        if (unaryExpr == null)
            throw new Exception("error in `UnaryExpr`");
        this.unaryExpr = unaryExpr;
    }

    public void setExpression(Expression expression) throws Exception {
        if (expression == null)
            throw new Exception("error in `Expression`");
        this.expression = expression;
    }

    public void setBinary_op(Binary_op binary_op) throws Exception {
        if (binary_op == null)
            throw new Exception("error in `Binary_op`");
        this.binary_op = binary_op;
    }
}
