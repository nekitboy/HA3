package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

// ArrayLength = Expression .
public class ArrayLength extends SyntaxVariable {
    static final String type = "ArrayLength";

    Expression expression;

    public ArrayLength() {
        super(type);
    }

    public void setExpression(Expression expression) throws Exception {
        if (expression == null)
            throw new Exception("`Expression is null");
        this.expression = expression;
    }

    @Override
    public StringBuilder toJSON() {
        StringBuilder json = new StringBuilder();
        StringBuilder content = new StringBuilder();

        json.append("{\n");

        if (expression != null) {
            content.append("Expression: ");
            content.append(expression.toJSON());
        }
        content = tabulize(content);

        json.append(content);
        json.append("\n}");

        return json;
    }
}
