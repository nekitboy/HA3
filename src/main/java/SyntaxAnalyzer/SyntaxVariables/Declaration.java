package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;
// Declaration   = ConstDecl | TypeDecl | VarDecl .
public class Declaration extends SyntaxVariable {
    static private final String type = "Declaration";
    private ConstDecl constDecl;
    private TypeDecl typeDecl;
    private VarDecl varDecl;

    public Declaration() {
        super(type);
    }

    public void setConstDecl(ConstDecl constDecl) throws Exception {
        if (constDecl == null)
            throw new Exception("error in `ConstDecl`");
        this.constDecl = constDecl;
    }

    public void setTypeDecl(TypeDecl typeDecl) throws Exception {
        if (typeDecl == null)
            throw new Exception("error in `FunctionDecl`");
        this.typeDecl = typeDecl;
    }

    public void setVarDecl(VarDecl varDecl) throws Exception {
        if (varDecl == null)
            throw new Exception("error in `PackageClause`");
        this.varDecl = varDecl;
    }

    @Override
    public StringBuilder toJSON() {
        StringBuilder json = new StringBuilder();
        StringBuilder content = new StringBuilder();

        json.append("{\n");
        if (constDecl != null) {
            content.append("ConstDecl: ");
            content.append(constDecl.toJSON());
        }
        if (typeDecl != null) {
            content.append(",\n");
            content.append("TypeDecl: ");
            content.append(typeDecl.toJSON());
        }
        if (varDecl != null) {
            content.append(",\n");
            content.append("VarDecl: ");
            content.append(varDecl.toJSON());
        }

        content = tabulize(content);

        json.append(content);
        json.append("\n}");
        return json;
    }
}
