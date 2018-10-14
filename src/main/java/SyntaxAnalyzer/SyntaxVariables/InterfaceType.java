package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

import java.util.ArrayList;

// InterfaceType  = "interface" "{" { MethodSpec ";" } "}" .
public class InterfaceType extends SyntaxVariable {
    static final String type = "InterfaceType";

    private ArrayList<MethodSpec> methodSpec;

    public InterfaceType() {
        super(type);
        methodSpec = new ArrayList<>();
    }

    public void addMethodSpec(MethodSpec methodSpec) throws Exception {
        if (methodSpec == null)
            throw new Exception("`MethodSpec` in null in `InterfaceType`");
        this.methodSpec.add(methodSpec);
    }

    @Override
    public StringBuilder toJSON() {
        StringBuilder json = new StringBuilder();
        StringBuilder content = new StringBuilder();

        json.append("{\n");

        if (methodSpec.size() > 0) {
            content.append("MethodSpec: ");
            content.append(ArrayToJSON((ArrayList<SyntaxVariable>) (ArrayList <?>) methodSpec));
        }

        json.append(tabulize(content));
        json.append("\n}");

        return json;
    }
}
