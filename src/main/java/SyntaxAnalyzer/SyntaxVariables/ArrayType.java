package SyntaxAnalyzer.SyntaxVariables;
import SyntaxAnalyzer.SyntaxVariable;


// ArrayType   = "[" ArrayLength "]" ElementType .
public class ArrayType extends SyntaxVariable {
    static private final String type = "ArrayType";

    private ArrayLength arrayLength;
    private ElementType elementType;

    public ArrayType() {
        super(type);
    }

    public void setArrayLength(ArrayLength arrayLength) throws Exception {
        if (arrayLength == null)
            throw new Exception("`ArrayLength` is nul");
        this.arrayLength = arrayLength;
    }

    public void setElementType(ElementType elementType) throws Exception {
        if (elementType == null)
            throw new Exception("`ElementType` is null");
        this.elementType = elementType;
    }

    @Override
    public StringBuilder toJSON() {
        StringBuilder json = new StringBuilder();
        StringBuilder content = new StringBuilder();

        json.append("{\n");
        if (arrayLength != null) {
            content.append("ArrayLength: ");
            content.append(arrayLength.toJSON());
            content.append(",\n");
        }
        if (elementType != null) {
            content.append("ElementType: ");
            content.append(elementType.toJSON());
        }

        content = tabulize(content);

        json.append(content);
        json.append("\n}");
        return json;
    }
}
