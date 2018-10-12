package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

// PackageName   =  identifier .
public class PackageName extends SyntaxVariable {
    static private final String type = "PackageName";
    private String identifier;

    public PackageName() {
        super(type);
    }

    public void setIdentifier(String identifier) throws Exception {
        if (identifier == null)
            throw new Exception("error in `Identifier`");
        this.identifier = identifier;
    }

    @Override
    public StringBuilder toJSON() {
        StringBuilder json = new StringBuilder(identifier);

        return json;
    }
}
