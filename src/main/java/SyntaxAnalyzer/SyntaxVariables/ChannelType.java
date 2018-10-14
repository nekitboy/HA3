package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

//ChannelType = ( "chan" | "chan" "<-" | "<-" "chan" ) ElementType .
public class ChannelType extends SyntaxVariable {
    static final String type = "ChannelType";

    private ElementType elementType;

    public ChannelType() {
        super(type);
    }

    public void setElementType(ElementType elementType) throws Exception {
        if (elementType == null)
            throw new Exception("`ElementType` is null in `ChannelType`");
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
