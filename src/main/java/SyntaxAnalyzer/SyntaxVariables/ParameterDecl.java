package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

// ParameterDecl  = [ IdentifierList ] [ "..." ] Type .
public class ParameterDecl extends SyntaxVariable{
    static final String type = "ParameterDecl";

    private IdentifierList identifierList;
    private Type type_;

    public ParameterDecl() {
        super(type);
    }

    public void setIdentifierList(IdentifierList identifierList) throws Exception {
        if (identifierList == null)
            throw new Exception("`IdentifierList` in null in `ParameterDecl`");
        this.identifierList = identifierList;
    }

    public void setType_(Type type_) throws Exception {
        if (type_ == null)
            throw new Exception("`Type` in null in `ParameterDecl`");
        this.type_ = type_;
    }

    @Override
    public StringBuilder toJSON() {
        StringBuilder json = new StringBuilder();
        StringBuilder content = new StringBuilder();

        json.append("{\n");

        if (identifierList != null) {
            content.append("IdentifierList: ");
            content.append(identifierList.toJSON());
            content.append(",\n");
        }
        content.append("Type: ");
        content.append(type_.toJSON());

        json.append(tabulize(content));
        json.append("\n}");

        return json;
    }
}
