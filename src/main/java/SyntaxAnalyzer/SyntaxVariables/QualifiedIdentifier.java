package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

// QualifiedIdent = PackageName "." identifier .
public class QualifiedIdentifier extends SyntaxVariable {
    public static final String type = "QualifiedIdentifier";
    private PackageName packageName;
    private String identifier;
    public QualifiedIdentifier() {
        super(type);
    }

    public void setIdentifier(String  identifier) throws Exception {
        if (identifier == null)
            throw new Exception("error in `Identifier`");
        this.identifier = identifier;
    }

    public void setPackageName(PackageName packageName) throws Exception {
        if (packageName == null)
            throw new Exception("error in `PackageName`");
        this.packageName = packageName;
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
        if (packageName != null) {
            content.append(",\n");
            content.append("PackageName: ");
            content.append(packageName.toJSON());
        }

        content = tabulize(content);

        json.append(content);
        json.append("\n}");
        return json;
    }
}
