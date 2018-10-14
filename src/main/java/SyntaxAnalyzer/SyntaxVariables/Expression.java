package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

// Expression = UnaryExpr | UnaryExpr binary_op Expression .
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

    @Override
    public StringBuilder toJSON() {
        StringBuilder json = new StringBuilder();
        StringBuilder content = new StringBuilder();

        json.append("{\n");
        content.append("UnaryExpr: ");
        content.append(unaryExpr.toJSON());
        if (binary_op != null) {
            content.append(",\n");
            content.append("Binary_op: ");
            content.append(binary_op.toJSON());

            content.append(",\n");
            content.append("Expression: ");
            content.append(expression.toJSON());
        }

        content = tabulize(content);

        json.append(content);
        json.append("\n}");
        return json;
    }
}
