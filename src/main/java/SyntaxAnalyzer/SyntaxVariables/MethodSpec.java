package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

// MethodSpec   = MethodName Signature | InterfaceTypeName .
public class MethodSpec extends SyntaxVariable{
    static final String type = "MethodSpec";

    private MethodName methodName;
    private Signature signature;
    private InterfaceTypeName interfaceTypeName;

    public MethodSpec() {
        super(type);
    }

    public void setMethodName(MethodName methodName) throws Exception {
        if (methodName == null)
            throw new Exception("`MethodName` is null in `MethodSpec`");
        this.methodName = methodName;
    }

    public void setSignature(Signature signature) throws Exception {
        if (signature == null)
            throw new Exception("`Signature` is null in `MethodSpec`");
        this.signature = signature;
    }

    public void setInterfaceTypeName(InterfaceTypeName interfaceTypeName) throws Exception {
        if (interfaceTypeName == null) {
            throw new Exception("`InterfaceTypeName` in null in `MethodSpec`");
        }
        this.interfaceTypeName = interfaceTypeName;
    }

    @Override
    public StringBuilder toJSON() {
        StringBuilder json = new StringBuilder();
        StringBuilder content = new StringBuilder();

        json.append("{\n");

        if (methodName != null) {
            content.append("MethodName: ");
            content.append(methodName.toJSON());
            content.append(",\nSignature: ");
            content.append(signature.toJSON());
        }
        else if (interfaceTypeName != null) {
            content.append("InterfaceTypeName: ");
            content.append(interfaceTypeName.toJSON());
        }
        json.append(tabulize(content));
        json.append("\n}");
        return json;
    }
}
