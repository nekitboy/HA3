package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

// MethodName   = identifier .
public class MethodName  extends SyntaxVariable{
    static final String type = "MethodName";

    private String identifier;

    public MethodName() {
        super(type);
    }

    public void setIdentifier(String identifier) throws Exception {
        if (identifier == null)
            throw new Exception("`identifier` is null in `MethodName`");
        this.identifier = identifier;
    }

    @Override
    public StringBuilder toJSON() {
        return new StringBuilder(identifier);
    }
}
