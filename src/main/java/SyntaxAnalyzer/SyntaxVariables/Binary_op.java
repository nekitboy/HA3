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

    public void setOp(String op) throws Exception {
        if (op == null)
            throw new Exception("error in `Op`");
        this.op = op;
    }
    public void setRel_op(Rel_op rel_op) throws Exception {
        if (rel_op == null)
            throw new Exception("error in `Rel_op`");
        this.rel_op = rel_op;
    }
    public void setAdd_op(Add_op add_op) throws Exception {
        if (add_op == null)
            throw new Exception("error in `Add_op`");
        this.add_op = add_op;
    }
    public void setMul_op(Mul_op mul_op) throws Exception {
        if (mul_op == null)
            throw new Exception("error in `Mul_op`");
        this.mul_op = mul_op;
    }

    @Override
    public StringBuilder toJSON() {
        StringBuilder json = new StringBuilder();
        StringBuilder content = new StringBuilder();

        json.append("{\n");
        if (op != null) {
            content.append("Op: ");
            content.append(op);
        }
        if (rel_op != null) {
            content.append(",\n");
            content.append("Rel_op: ");
            content.append(rel_op.toJSON());
        }
        if (add_op != null) {
            content.append(",\n");
            content.append("Add_op: ");
            content.append(add_op.toJSON());
        }
        if (mul_op != null) {
            content.append(",\n");
            content.append("Mul_op: ");
            content.append(mul_op.toJSON());
        }

        content = tabulize(content);

        json.append(content);
        json.append("\n}");
        return json;
    }
}
