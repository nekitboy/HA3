package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

// PackageClause  =  "package" PackageName .
public class PackageClause extends SyntaxVariable {
    static private final String type = "PackageClause";

    private PackageName packageName;

    public PackageClause() {
        super(type);
    }

    public void setPackageName(PackageName packageName) throws Exception {
        if (packageName == null)
            throw new Exception("Syntax grammar error in `PackageName`");
        this.packageName = packageName;
    }

    @Override
    public StringBuilder toJSON() {
        StringBuilder json = new StringBuilder();

        /* json
        {

         */
        json.append("{\n");

        /* content
        PackageName: ...
         */
        StringBuilder content = new StringBuilder();
        content.append("PackageName: ");
        content.append(packageName.toJSON());

        /* content
            PackageName: ...
         */
        content = tabulize(content);

        /* json
        {
            PackageName: ...
         */
        json.append(content);

        /* json
        {
            PackageName: ...
        }
         */
        json.append("\n}");
        return json;
    }
}
