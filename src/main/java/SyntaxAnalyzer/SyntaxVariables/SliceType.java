package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

// SliceType = "[" "]" ElementType .
public class SliceType extends SyntaxVariable {
    static final String type = "SliceType";

    private ElementType elementType;

    public SliceType() {
        super(type);
    }

    public void setElementType(ElementType elementType) throws Exception {
        if (elementType == null)
            throw new Exception("`ElementType` is null in `SliceType`");
        this.elementType = elementType;
    }

    @Override
    public StringBuilder toJSON() {
        StringBuilder json = new StringBuilder();
        StringBuilder content = new StringBuilder();
        json.append("{\n");
        content.append("ElementType: ");
        content.append(elementType.toJSON());
        json.append(tabulize(content));
        json.append("\n}");

        return json;
    }
}
