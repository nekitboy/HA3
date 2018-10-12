package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

// ImportSpec   = [ "." | PackageName ] ImportPath .
public class ImportSpec extends SyntaxVariable{
    public static final String type = "ImportSpec";

    private PackageName packageName;
    private ImportPath importPath;

    public ImportSpec() {
        super(type);
    }

    public void setPackageName(PackageName packageName) throws Exception {
        if (packageName == null)
            throw new Exception("error in `PackageName`");
        this.packageName = packageName;
    }

    public void setImportPath(ImportPath importPath) throws Exception {
        if (importPath == null)
            throw new Exception("error in `ImportPath`");
        this.importPath = importPath;
    }

    @Override
    public StringBuilder toJSON() {
        StringBuilder json = new StringBuilder();
        StringBuilder content = new StringBuilder();

        json.append("{\n");

        if (packageName != null) {
            content.append("PackageName: ");
            content.append(packageName.toJSON());
            content.append(",\n");
        }
        content.append("ImportPath: ");
        content.append(importPath.toJSON());

        content = tabulize(content);

        json.append(content);
        json.append("\n}");
        return json;
    }
}
