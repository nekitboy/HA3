package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

import java.util.ArrayList;

// TopLevelDecl  = Declaration | FunctionDecl | MethodDecl .
public class TopLevelDecl extends SyntaxVariable {
    static private final String type = "TopLevelDecl";

    private Declaration declaration;
    private FunctionDecl functionDecl;
    private MethodDecl methodDecl;

    public TopLevelDecl() {
        super(type);
    }

    public void setDeclaration(Declaration declaration) throws Exception {
        if (declaration == null)
            throw new Exception("error in `Declaration`");
        this.declaration = declaration;
    }

    public void setFunctionDecl(FunctionDecl functionDecl) throws Exception {
        if (functionDecl == null)
            throw new Exception("error in `FunctionDecl`");
        this.functionDecl = functionDecl;
    }

    public void setMethodDecl(MethodDecl methodDecl) throws Exception {
        if (methodDecl == null)
            throw new Exception("error in `PackageClause`");
        this.methodDecl = methodDecl;
    }

    @Override
    public StringBuilder toJSON() {
        StringBuilder json = new StringBuilder();
        StringBuilder content = new StringBuilder();

        json.append("{\n");
        if (declaration != null) {
            content.append("Declaration: ");
            content.append(declaration.toJSON());
        }
        else if (functionDecl != null) {
            content.append("FuncDecl: ");
            content.append(functionDecl.toJSON());
        }
        else if (methodDecl != null) {
            content.append("MethodDecl: ");
            content.append(methodDecl.toJSON());
        }

        content = tabulize(content);

        json.append(content);
        json.append("\n}");
        return json;
    }

}
