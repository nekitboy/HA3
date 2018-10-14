package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

// MapType  = "map" "[" KeyType "]" ElementType .
public class MapType extends SyntaxVariable {
    static final String type = "MapType";

    private KeyType keyType;
    private ElementType elementType;

    public MapType() {
        super(type);
    }

    public void setKeyType(KeyType keyType) throws Exception {
        if (keyType == null)
            throw new Exception("`KeyType` is null in `MapType`");
        this.keyType = keyType;
    }

    public void setElementType(ElementType elementType) throws Exception {
        if (elementType == null)
            throw new Exception("`ElementType` is null in `MapType`");
        this.elementType = elementType;
    }

    @Override
    public StringBuilder toJSON() {
        StringBuilder json = new StringBuilder();
        StringBuilder content = new StringBuilder();

        json.append("{\n");
        content.append("KeyType: ");
        content.append(keyType.toJSON());
        content.append(",\nElementType: ");
        content.append(elementType.toJSON());

        json.append(tabulize(content));
        json.append("\n}");

        return json;
    }
}
