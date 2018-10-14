package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

// rel_op     = "==" | "!=" | "<" | "<=" | ">" | ">=" .
public class Rel_op extends SyntaxVariable {
    static final String type = "Rel_op";
    private String op;
    public Rel_op() {
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
