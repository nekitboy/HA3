package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

// UnaryExpr  = PrimaryExpr | unary_op UnaryExpr .
public class UnaryExpr extends SyntaxVariable {
    static final String type = "UnaryExpr";
    private PrimaryExpr primaryExpr;
    private Unary_op unary_op;
    private UnaryExpr unaryExpr;
    public UnaryExpr() {
        super(type);
    }

    public void setUnaryExpr(UnaryExpr unaryExpr) throws Exception {
        if (unaryExpr == null)
            throw new Exception("error in `UnaryExpr`");
        this.unaryExpr = unaryExpr;
    }

    public void setPrimaryExpr(PrimaryExpr primaryExpr) throws Exception {
        if (primaryExpr == null)
            throw new Exception("error in `PrimaryExpr`");
        this.primaryExpr = primaryExpr;
    }

    public void setUnary_op(Unary_op unary_op) throws Exception {
        if (unary_op == null)
            throw new Exception("error in `Unary_op`");
        this.unary_op = unary_op;
    }

    @Override
    public StringBuilder toJSON() {
        StringBuilder json = new StringBuilder();
        StringBuilder content = new StringBuilder();

        json.append("{\n");
        if (primaryExpr != null) {
            content.append("PrimaryExpr: ");
            content.append(primaryExpr.toJSON());
        }
        if (unary_op != null) {
            content.append(",\n");
            content.append("Unnary_op: ");
            content.append(unary_op.toJSON());

            content.append(",\n");
            content.append("UnaryExpr: ");
            content.append(unaryExpr.toJSON());
        }

        content = tabulize(content);

        json.append(content);
        json.append("\n}");
        return json;
    }
}
