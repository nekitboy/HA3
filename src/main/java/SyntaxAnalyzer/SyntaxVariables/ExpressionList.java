package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

import java.util.ArrayList;

// ExpressionList = Expression { "," Expression } .
public class ExpressionList extends SyntaxVariable {
    static final String type = "ExpressionList";
    private ArrayList<Expression> expression;

    public ExpressionList() {
        super(type);
    }

    public void addExpression(Expression expression) throws Exception {
        if (expression == null)
            throw new Exception("error in `Expression`");
        this.expression.add(expression);
    }

    public StringBuilder toJSON() {
        StringBuilder json = new StringBuilder();

        json.append("{\n");

        StringBuilder content = new StringBuilder();

        if (!expression.isEmpty()) {
            content.append("Expression: ");
            content.append(ArrayToJSON((ArrayList<SyntaxVariable>) (ArrayList<?>) expression));
        }
        content = tabulize(content);

        json.append(content);
        json.append("\n}");

        return json;
    }
}
