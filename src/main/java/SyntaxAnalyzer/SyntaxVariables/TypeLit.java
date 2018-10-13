package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

//TypeLit = ArrayType | StructType | PointerType | FunctionType | InterfaceType | SliceType | MapType | ChannelType .
public class TypeLit extends SyntaxVariable {
    public static final String type = "Type";
    public TypeLit() {
        super(type);
    }
}
