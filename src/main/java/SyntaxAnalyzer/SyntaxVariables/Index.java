package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

// Index  = "[" Expression "]" .
public class Index extends SyntaxVariable {
    static final String type = "Index";
    private Expression expression;
    public Index() {
        super(type);
    }

    public void setExpression(Expression  expression) throws Exception {
        if (expression == null)
            throw new Exception("error in `Expression`");
        this.expression = expression;
    }

    @Override
    public StringBuilder toJSON() {
        StringBuilder json = new StringBuilder();
        StringBuilder content = new StringBuilder();

        json.append("{\n");
        content.append("Expression: ");
        content.append(expression.toJSON());

        content = tabulize(content);

        json.append(content);
        json.append("\n}");
        return json;
    }
}
