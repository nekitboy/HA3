package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

import java.util.ArrayList;

// SourceFile = PackageClause ";" { ImportDecl ";" } { TopLevelDecl ";" }

public class SourceFile extends SyntaxVariable{
    static private final String type = "SourceFile";

    private PackageClause packageClause;
    private ArrayList <ImportDecl> importDecl;
    private ArrayList <TopLevelDecl> topLevelDecl;

    public SourceFile() {
        super(type);
        importDecl = new ArrayList<>();
        topLevelDecl = new ArrayList<>();
    }

    public void setPackageClause(PackageClause packageClause) throws Exception {
        if (packageClause == null)
            throw new Exception("error in `PackageClause`");
        this.packageClause = packageClause;
    }

    public void addImportDecl(ImportDecl importDecl) throws Exception {
        if (importDecl == null) {
            throw new Exception("error in `ImportDecl`");
        }
        this.importDecl.add(importDecl);
    }

    public void addTopLevelDecl (TopLevelDecl topLevelDecl) throws Exception {
        if (topLevelDecl == null) {
            throw new Exception("error in `TopLevelDecl`");
        }
        this.topLevelDecl.add(topLevelDecl);
    }

    @Override
    public StringBuilder toJSON() {
        StringBuilder json = new StringBuilder();
        StringBuilder content = new StringBuilder();

        /* json
        {

         */
        json.append("{\n");

        /* content
        PackageClause: ...
         */
        content.append("PackageClause: ");
        content.append(packageClause.toJSON());

        if (!importDecl.isEmpty()) {
            /* content
            PackageClause: ... ,
            ImportDecl:
             */
            content.append(",\nImportDecl: ");

            /* content
            PackageClause: ... ,
            ImportDecl: [...]
             */
            content.append(ArrayToJSON((ArrayList<SyntaxVariable>) (ArrayList<?>) importDecl));
        }

        if (!topLevelDecl.isEmpty()) {
            content.append(",\nTopLevelDecl: ");
            content.append(ArrayToJSON((ArrayList<SyntaxVariable>) (ArrayList<?>) topLevelDecl));
        }

        json.append(tabulize(content));
        json.append("\n}");
        return json;
    }
}
