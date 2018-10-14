package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

// PointerType = "*" BaseType .
public class PointerType extends SyntaxVariable {
    static final String type = "PointerType";

    private BaseType baseType;

    public PointerType() {
        super(type);
    }

    public void setBaseType(BaseType baseType) throws Exception {
        if (baseType == null)
            throw new Exception("`BaseType` is null");
        this.baseType = baseType;
    }

    @Override
    public StringBuilder toJSON() {
        StringBuilder json = new StringBuilder();
        StringBuilder content = new StringBuilder();
        json.append("{\n");
        content.append("BaseType: ");
        content.append(baseType.toJSON());
        json.append(tabulize(content));
        json.append("\n}");

        return json;
    }
}
