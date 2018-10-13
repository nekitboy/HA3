package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

//TypeLit = ArrayType | StructType | PointerType | FunctionType | InterfaceType | SliceType | MapType | ChannelType .
public class TypeLit extends SyntaxVariable {
    public static final String type = "TypeLit";
    private ArrayType arrayType;
    private StructType structType;
    private PointerType pointerType;
    private FunctionType functionType;
    private InterfaceType interfaceType;
    private SliceType sliceType;
    private MapType mapType;
    private ChannelType channelType;

    public TypeLit() {
        super(type);
    }

    public void setArrayType(ArrayType arrayType) throws Exception {
        if (arrayType == null)
            throw new Exception("error in `ArrayType`");
        this.arrayType = arrayType;
    }

    public void setStructType(StructType structType) throws Exception {
        if (structType == null)
            throw new Exception("error in `StructType`");
        this.structType = structType;
    }

    public void setPointerType(PointerType pointerType) throws Exception {
        if (pointerType == null)
            throw new Exception("error in `PointerType`");
        this.pointerType = pointerType;
    }

    public void setFunctionType(FunctionType functionType) throws Exception {
        if (functionType == null)
            throw new Exception("error in `FunctionType`");
        this.functionType = functionType;
    }

    public void setInterfaceType(InterfaceType interfaceType) throws Exception {
        if (interfaceType == null)
            throw new Exception("error in `InterfaceType`");
        this.interfaceType = interfaceType;
    }

    public void setSliceType(SliceType sliceType) throws Exception {
        if (sliceType == null)
            throw new Exception("error in `SliceType`");
        this.sliceType = sliceType;
    }

    public void setMapType(MapType mapType) throws Exception {
        if (mapType == null)
            throw new Exception("error in `MapType`");
        this.mapType = mapType;
    }

    public void setChannelType(ChannelType channelType) throws Exception {
        if (channelType == null)
            throw new Exception("error in `ChannelType`");
        this.channelType = channelType;
    }

    @Override
    public StringBuilder toJSON() {
        StringBuilder json = new StringBuilder();
        StringBuilder content = new StringBuilder();

        json.append("{\n");

        if (arrayType != null) {
            content.append("ArrayType: ");
            content.append(arrayType.toJSON());
        }
        else if (structType != null) {
            content.append("StructType: ");
            content.append(structType.toJSON());
        }
        else if (pointerType != null) {
            content.append("PointerType: ");
            content.append(pointerType.toJSON());
        }
        else if (functionType != null) {
            content.append("FunctionType: ");
            content.append(functionType.toJSON());
        }
        else if (interfaceType != null) {
            content.append("InterfaceType: ");
            content.append(interfaceType.toJSON());
        }
        else if (sliceType != null) {
            content.append("SliceType: ");
            content.append(sliceType.toJSON());
        }
        else if (mapType != null) {
            content.append("MapType: ");
            content.append(mapType.toJSON());
        }
        else if (channelType != null) {
            content.append("ChannelType: ");
            content.append(channelType.toJSON());
        }

        content = tabulize(content);
        json.append(content);
        json.append("\n}");

        return json;
    }
}
