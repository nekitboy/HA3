package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

// ImportPath   = string_lit .
public class ImportPath extends SyntaxVariable{
    static final String type = "ImportPath";

    private String string_lit;

    public ImportPath() {
        super(type);
    }

    public void setString_lit(String string_lit) throws Exception {
        if (string_lit == null)
            throw new Exception("error in `string_lit`");
        this.string_lit = string_lit;
    }

    @Override
    public StringBuilder toJSON() {

        return new StringBuilder(string_lit);
    }
}
