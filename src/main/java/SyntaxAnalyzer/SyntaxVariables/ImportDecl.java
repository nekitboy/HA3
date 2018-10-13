package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

import java.util.ArrayList;

// ImportDecl = "import" ( ImportSpec | "(" { ImportSpec ";" } ")" ) .
public class ImportDecl extends SyntaxVariable {
    private static final String type = "ImportDecl";

    private ArrayList <ImportSpec> importSpec;

    public ImportDecl() {
        super(type);
        importSpec = new ArrayList<>();
    }

    public void addImportSpec(ImportSpec importSpec) throws Exception {
        if (importSpec == null)
            throw new Exception("error in `ImportSpec`");
        this.importSpec.add(importSpec);
    }

    @Override
    public StringBuilder toJSON() {
        StringBuilder json = new StringBuilder();

        json.append("{\n");

        StringBuilder content = new StringBuilder();

        if (!importSpec.isEmpty()) {
            content.append("ImportSpec: ");
            content.append(ArrayToJSON((ArrayList<SyntaxVariable>) (ArrayList<?>) importSpec));
        }
        content = tabulize(content);

        json.append(content);
        json.append("\n}");

        return json;
    }
}
