package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

public class Mul_op extends SyntaxVariable {
    static final String type = "Mul_op";
    private String op;
    public Mul_op() {
        super(type);
    }

    public void setOp(String op) throws Exception {
        if (op == null)
            throw new Exception("error in `Op`");
        this.op = op;
    }

    @Override
    public StringBuilder toJSON() {
        return new StringBuilder(op);
    }
}
