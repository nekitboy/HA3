package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

import java.util.ArrayList;

// StructType  = "struct" "{" { FieldDecl ";" } "}" .
public class StructType extends SyntaxVariable {
    static final String type = "StructType";

    ArrayList <FieldDecl> fieldDecl;

    public void addFieldDecl(FieldDecl fieldDecl) throws Exception {
        if (fieldDecl == null)
            throw new Exception("`FieldDecl` is null in `StructType`");
        this.fieldDecl.add(fieldDecl);
    }

    public StructType() {
        super(type);
    }

    @Override
    public StringBuilder toJSON() {
        StringBuilder json = new StringBuilder();
        StringBuilder content = new StringBuilder();

        json.append("{\n");

        if (!fieldDecl.isEmpty()) {
            content.append("FieldDecl: ");
            content.append(ArrayToJSON((ArrayList<SyntaxVariable>) (ArrayList<?>) fieldDecl));
        }

        json.append(tabulize(content));
        json.append("\n}");
        return json;
    }
}
