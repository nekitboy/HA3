package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

import java.util.ArrayList;

// ConstSpec      = IdentifierList [ [ Type ] "=" ExpressionList ].
public class ConstSpec extends SyntaxVariable {
    static private final String type = "ConstSpec";
    private IdentifierList identifierList;
    private ExpressionList expressionList;
    private Type type_;
    public ConstSpec() {
        super(type);
    }

    public void setIdentifierList(IdentifierList identifierList) throws Exception {
        if (identifierList == null)
            throw new Exception("error in `IdentifierList`");
        this.identifierList = identifierList;
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

        content.append("IdentifierList: ");
        content.append(identifierList.toJSON());


        if (type_ != null) {
            content.append(",\n");
            content.append("Type: ");
            content.append(type_.toJSON());
        }

        if (expressionList != null) {
            content.append(",\n");
            content.append("ExpressionList: ");
            content.append(expressionList.toJSON());
        }
        content = tabulize(content);

        json.append(content);
        json.append("\n}");
        return json;
    }
}
