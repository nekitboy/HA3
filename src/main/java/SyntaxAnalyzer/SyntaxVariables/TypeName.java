package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

//TypeName  = identifier | QualifiedIdent .
public class TypeName extends SyntaxVariable {
    public static final String type = "TypeName";
    private String identifier;
    private QualifiedIdentifier qualifiedIdentifier;
    public TypeName() {
        super(type);
    }

    public void setIdentifier(String  identifier) throws Exception {
        if (identifier == null)
            throw new Exception("error in `Identifier`");
        this.identifier = identifier;
    }

    public void setQualifiedIdentifier(QualifiedIdentifier qualifiedIdentifier) throws Exception {
        if (qualifiedIdentifier == null)
            throw new Exception("error in `QualifiedIdentifier`");
        this.qualifiedIdentifier = qualifiedIdentifier;
    }

    @Override
    public StringBuilder toJSON() {
        StringBuilder json = new StringBuilder();
        StringBuilder content = new StringBuilder();

        json.append("{\n");
        if (identifier != null) {
            content.append("Identifier: ");
            content.append(identifier);
        }
        if (qualifiedIdentifier != null) {
            content.append(",\n");
            content.append("QualifiedIdentifier: ");
            content.append(qualifiedIdentifier.toJSON());
        }

        content = tabulize(content);

        json.append(content);
        json.append("\n}");
        return json;
    }
}
