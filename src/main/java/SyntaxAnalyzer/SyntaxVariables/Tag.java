package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

// Tag  = string_lit .
public class Tag extends SyntaxVariable {
    static final String type = "Tag";
    
    private String string_lit;
    
    public Tag() {
        super(type);
    }

    public void setString_lit(String string_lit) throws Exception {
        if (string_lit == null)
            throw new Exception("`string_lit` in null");
        this.string_lit = string_lit;
    }

    @Override
    public StringBuilder toJSON() {
        return new StringBuilder(string_lit);
    }
}
