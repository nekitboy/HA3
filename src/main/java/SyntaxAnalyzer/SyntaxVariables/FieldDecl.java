package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

// FieldDecl  = (IdentifierList Type | EmbeddedField) [ Tag ] .
public class FieldDecl extends SyntaxVariable{
    static final String type = "FieldDecl";

    private IdentifierList identifierList;
    private Type type_;
    private EmbeddedField embeddedField;
    private Tag tag;

    public void setIdentifierList(IdentifierList identifierList) throws Exception {
        if (identifierList == null)
            throw new Exception("`Identifier list` in null in `FieldDecl`");
        this.identifierList = identifierList;
    }

    public void setType_(Type type_) throws Exception {
        if (type_ == null)
            throw new Exception("`Type` is null in `FieldDecl`");
        this.type_ = type_;
    }

    public void setEmbeddedField(EmbeddedField embeddedField) throws Exception {
        if (embeddedField == null)
            throw new Exception("`EmbeddedField` is null in `FieldDecl`");
        this.embeddedField = embeddedField;
    }

    public void setTag(Tag tag) throws Exception {
        if (tag == null)
            throw new Exception("`Tag` is null in `FieldDecl`");
        this.tag = tag;
    }

    public FieldDecl() {
        super(type);
    }

    @Override
    public StringBuilder toJSON() {
        StringBuilder json = new StringBuilder();
        StringBuilder content = new StringBuilder();

        json.append("{\n");

        if (identifierList != null) {
            content.append("IdentifierList: ");
            content.append(identifierList.toJSON());

            content.append(",\nType: ");
            content.append(type_.toJSON());
        }
        else {
            content.append("EmbeddedField: ");
            content.append(embeddedField.toJSON());
        }

        if (tag != null) {
            content.append(",\nTag: ");
            content.append(tag.toJSON());
        }

        content = tabulize(content);

        json.append(content);
        json.append("\n}");

        return json;
    }
}
