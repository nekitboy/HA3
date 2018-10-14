package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

import java.util.ArrayList;

// PrimaryExpr: (Operand | Conversion | MethodExpr) { Selector | Index | Slice | TypeAssertion | Argument } .
public class PrimaryExpr extends SyntaxVariable {
    static final String type = "PrimaryExpr";
    private Operand operand;
    private Conversion conversion;
    private MethodExpr methodExpr;
    private ArrayList<SyntaxVariable> list;
    public PrimaryExpr() {
        super(type);
        list = new ArrayList<>();
    }

    public void setOperand(Operand operand) throws Exception {
        if (operand == null)
            throw new Exception("error in `Operand`");
        this.operand = operand;
    }

    public void setConversion(Conversion conversion) throws Exception {
        if (conversion == null)
            throw new Exception("error in `Conversion`");
        this.conversion = conversion;
    }

    public void setMethodExpr(MethodExpr methodExpr) throws Exception {
        if (methodExpr == null)
            throw new Exception("error in `MethodExpr`");
        this.methodExpr = methodExpr;
    }

    public void addToList(SyntaxVariable element) throws Exception {
        if (element == null)
            throw new Exception("error in `Selector | Index | Slice | TypeAssertion | Argument `");
        this.list.add(element);
    }

    @Override
    public StringBuilder toJSON() {
        StringBuilder json = new StringBuilder();
        StringBuilder content = new StringBuilder();

        json.append("{\n");
        if (operand != null) {
            content.append("Operand: ");
            content.append(operand.toJSON());
        }
        if (conversion != null) {
            content.append(",\n");
            content.append("Conversion: ");
            content.append(conversion.toJSON());
        }
        if (methodExpr != null) {
            content.append(",\n");
            content.append("MethodExpr: ");
            content.append(methodExpr.toJSON());
        }

        if (!list.isEmpty()){
            for (SyntaxVariable element:
                 list) {
                content.append(",\n" + element.getType());
                content.append(element.toJSON());
            }

        }
        content = tabulize(content);

        json.append(content);
        json.append("\n}");
        return json;
    }
}
