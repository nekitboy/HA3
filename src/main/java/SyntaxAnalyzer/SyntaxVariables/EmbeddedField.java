package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

// EmbeddedField = [ "*" ] TypeName .
public class EmbeddedField extends SyntaxVariable{
    static final String type = "EmbeddedField";

    private TypeName typeName;

    public EmbeddedField() {
        super(type);
    }

    public void setTypeName(TypeName typeName) throws Exception {
        if (typeName == null)
            throw new Exception("`TypeName` is null in `EmbeddedField`");
        this.typeName = typeName;
    }

    @Override
    public StringBuilder toJSON() {
        StringBuilder json = new StringBuilder();
        StringBuilder content = new StringBuilder();

        json.append("{\n");

        content.append("TypeName: ");
        content.append(typeName.toJSON());

        json.append(tabulize(content));

        json.append("\n}");

        return json;
    }
}
