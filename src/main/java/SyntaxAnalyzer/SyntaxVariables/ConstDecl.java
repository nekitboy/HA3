package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;
import com.sun.tools.internal.jxc.ap.Const;

import java.util.ArrayList;
// ConstDecl      = "const" ( ConstSpec | "(" { ConstSpec ";" } ")" ).
public class ConstDecl extends SyntaxVariable {
    static private final String type = "ConstDecl";
    private ArrayList<ConstSpec> constSpec;

    public ConstDecl() {
        super(type);
        constSpec = new ArrayList<>();
    }

    public void addConstSpec(ConstSpec constSpec) throws Exception {
        if (constSpec == null)
            throw new Exception("error in `ConstSpec`");
        this.constSpec.add(constSpec);
    }

    @Override
    public StringBuilder toJSON() {
        StringBuilder json = new StringBuilder();

        json.append("{\n");

        StringBuilder content = new StringBuilder();

        if (!constSpec.isEmpty()) {
            content.append("ConstSpec: ");
            content.append(ArrayToJSON((ArrayList<SyntaxVariable>) (ArrayList<?>) constSpec));
        }
        content = tabulize(content);

        json.append(content);
        json.append("\n}");

        return json;
    }
}
