package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

// Type = TypeName | TypeLit | "(" Type ")" .
public class Type extends SyntaxVariable {
    public static final String type = "Type";
    private TypeName typeName;
    private TypeLit typeLit;
    private Type type_;
    public Type() {
        super(type);
    }

    public void setTypeName(TypeName typeName) throws Exception {
        if (typeName == null)
            throw new Exception("error in `TypeName`");
        this.typeName = typeName;
    }

    public void setTypeLit(TypeLit typeLit) throws Exception {
        if (typeLit == null)
            throw new Exception("error in `TypeLit`");
        this.typeLit = typeLit;
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
        if (typeName != null) {
            content.append("TypeName: ");
            content.append(typeName.toJSON());
        }
        if (typeLit != null) {
            content.append(",\n");
            content.append("TypeLit: ");
            content.append(typeLit.toJSON());
        }
        if (type_ != null) {
            content.append(",\n");
            content.append("Type: ");
            content.append(type_.toJSON());
        }

        content = tabulize(content);

        json.append(content);
        json.append("\n}");
        return json;
    }
}
