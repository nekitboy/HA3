package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

// Result    = Parameters | Type .
public class Result extends SyntaxVariable{
    static final String type = "Result";

    private Parameters parameters;
    private Type type_;

    public Result() {
        super(type);
    }

    public void setParameters(Parameters parameters) throws Exception {
        if (parameters == null)
            throw new Exception("`Parameters` is null in `Result`");
        this.parameters = parameters;
    }

    public void setType_(Type type_) throws Exception {
        if (type_ == null)
            throw new Exception("`Type` is null in `Result`");
        this.type_ = type_;
    }

    @Override
    public StringBuilder toJSON() {
        StringBuilder json = new StringBuilder();
        StringBuilder content = new StringBuilder();
        json.append("{\n");
        if (parameters != null) {
            content.append("Parameters: ");
            content.append(parameters.toJSON());
        }
        else if (type_ != null) {
            content.append("Type: ");
            content.append(type_.toJSON());
        }
        json.append(tabulize(content));
        json.append("\n}");

        return json;
    }
}
