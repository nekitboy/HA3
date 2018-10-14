package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

// Parameters  = "(" [ ParameterList [ "," ] ] ")" .
public class Parameters extends SyntaxVariable {
    static final String type = "Parameters";

    private ParameterList parameterList;

    public Parameters() {
        super(type);
    }

    public void setParameterList(ParameterList parameterList) throws Exception {
        if (parameterList == null)
            throw new Exception("`PaeameterList is null in `Parameters");
        this.parameterList = parameterList;
    }

    @Override
    public StringBuilder toJSON() {
        StringBuilder json = new StringBuilder();
        StringBuilder content = new StringBuilder();

        json.append("{\n");
        content.append("ParameterList: ");
        content.append(parameterList.toJSON());

        json.append(tabulize(content));
        json.append("\n}");

        return json;
    }
}
