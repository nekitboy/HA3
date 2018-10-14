package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

// Arguments = "(" [ ( ExpressionList | Type [ "," ExpressionList ] ) [ "..." ] [ "," ] ] ")" .
public class Arguments extends SyntaxVariable {
    static final String type = "Arguments";
    private ExpressionList expressionList;
    private Type type_;
    public Arguments() {
        super(type);
    }

    public void setExpressionList(ExpressionList expressionList) throws Exception {
        if (expressionList == null)
            throw new Exception("error in `ExpressionList`");
        this.expressionList = expressionList;
    }

    public void setType(Type type_) throws Exception {
        if (type_ == null)
            throw new Exception("error in `Type`");
        this.type_ = type_;
    }

    @Override
    public StringBuilder toJSON() {
        StringBuilder json = new StringBuilder();
        StringBuilder content = new StringBuilder();

        json.append("{\n");
        content.append("ExpressionList: ");
        content.append(expressionList.toJSON());

        if (type_ != null) {
            content.append("Type: ");
            content.append(type_.toJSON());
            content.append(",\n");
        }
        content = tabulize(content);

        json.append(content);
        json.append("\n}");
        return json;
    }
}
