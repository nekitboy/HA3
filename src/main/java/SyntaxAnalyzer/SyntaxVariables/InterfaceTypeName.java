package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

// InterfaceTypeName  = TypeName .
public class InterfaceTypeName extends SyntaxVariable{
    static final String type = "InterfaceTypeName";

    private TypeName typeName;

    public InterfaceTypeName() {
        super(type);
    }

    public void setTypeName(TypeName typeName) throws Exception {
        if (typeName == null)
            throw new Exception("`TypeName` is null in `InterfaceTypeName`");
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
