package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

import java.util.ArrayList;

// ParameterList  = ParameterDecl { "," ParameterDecl } .
public class ParameterList extends SyntaxVariable {
    static final String type = "ParameterList";

    private ArrayList<ParameterDecl> parameterDecl;

    public ParameterList() {
        super(type);
        parameterDecl = new ArrayList<>();
    }

    public void addParameterDecl(ParameterDecl parameterDecl) throws Exception {
        if (parameterDecl == null)
            throw new Exception("`ParameterDecl` in null in `ParameterList`");
        this.parameterDecl.add(parameterDecl);
    }

    @Override
    public StringBuilder toJSON() {
        StringBuilder json = new StringBuilder();
        StringBuilder content = new StringBuilder();

        json.append("{\n");

        content.append("ParameterList: ");
        content.append(ArrayToJSON((ArrayList<SyntaxVariable>) (ArrayList<?>) parameterDecl));

        json.append(tabulize(content));
        json.append("\n}");

        return json;
    }
}
