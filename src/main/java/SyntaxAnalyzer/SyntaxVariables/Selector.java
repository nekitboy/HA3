package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

// Selector = "." identifier .
public class Selector extends SyntaxVariable {
    static final String type = "Selector";
    private String identifier;
    public Selector() {
        super(type);
    }

    public void setIdentifier(String  identifier) throws Exception {
        if (identifier == null)
            throw new Exception("error in `Identifier`");
        this.identifier = identifier;
    }

    @Override
    public StringBuilder toJSON() {
        return new StringBuilder(identifier);
    }
}
