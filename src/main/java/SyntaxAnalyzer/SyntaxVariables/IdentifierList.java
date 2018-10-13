package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

import java.util.ArrayList;

// IdentifierList = identifier { "," identifier } .
public class IdentifierList extends SyntaxVariable {
    static final String type = "IdentifierList";
    private ArrayList<String> identifier;

    public IdentifierList() {
        super(type);
        identifier = new ArrayList<>();
    }

    public void addIdentifier(String identifier) throws Exception {
        if (identifier == null)
            throw new Exception("error in `Identifier`");
        this.identifier.add(identifier);
    }

    public StringBuilder toJSON() {
        return StringArrayToJSON(identifier);
    }
}
