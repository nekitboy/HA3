package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

// TypeAssertion  = "." "(" Type ")" .
public class TypeAssertion extends SyntaxVariable {
    static final String type = "TypeAssertion";
    private Type type_;
    public TypeAssertion() {
        super(type);
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
        content.append("Type: ");
        content.append(type_.toJSON());

        content = tabulize(content);

        json.append(content);
        json.append("\n}");
        return json;
    }
}
