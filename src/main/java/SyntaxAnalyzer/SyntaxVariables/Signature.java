package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

// Signature  = Parameters [ Result ] .
public class Signature extends SyntaxVariable {
    static final String type = "Signature";

    private Parameters parameters;
    private Result result;

    public Signature() {
        super(type);
    }

    public void setParameters(Parameters parameters) throws Exception {
        if (parameters == null)
            throw new Exception("`Parameters` in null in `Signature`");
        this.parameters = parameters;
    }

    public void setResult(Result result) throws Exception {
        if (result == null)
            throw new Exception("`Result` in null in `Signature`");
        this.result = result;
    }

    @Override
    public StringBuilder toJSON() {
        StringBuilder json = new StringBuilder();
        StringBuilder content = new StringBuilder();

        json.append("{\n");
        content.append("Parameters: ");
        content.append(parameters.toJSON());

        if (result != null) {
            content.append(",\nResult: ");
            content.append(result.toJSON());
        }

        json.append(tabulize(content));
        json.append("\n}");

        return json;
    }
}
