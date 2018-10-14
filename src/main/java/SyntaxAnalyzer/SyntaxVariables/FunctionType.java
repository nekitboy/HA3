package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

// FunctionType   = "func" Signature .
public class FunctionType  extends SyntaxVariable {
    static final String type = "FunctionType";

    private Signature signature;

    public FunctionType() {
        super(type);
    }

    public void setSignature(Signature signature) throws Exception {
        if (signature == null)
            throw new Exception("`Signature` is null in `FunctionType`");
        this.signature = signature;
    }

    @Override
    public StringBuilder toJSON() {
        StringBuilder json = new StringBuilder();
        StringBuilder content = new StringBuilder();
        json.append("{\n");
        content.append("Signature: ");
        content.append(signature.toJSON());
        json.append(tabulize(content));
        json.append("\n}");

        return json;
    }
}
