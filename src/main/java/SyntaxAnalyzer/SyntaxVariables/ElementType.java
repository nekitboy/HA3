package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

// ElementType = Type .
public class ElementType extends SyntaxVariable {
    static final String type = "ElementType";

    Type type_;

    public void setType_(Type type_) throws Exception {
        if (type_ == null)
            throw new Exception("`Type` is null in `ElementType`");
        this.type_ = type_;
    }

    public ElementType() {
        super(type);
    }

    @Override
    public StringBuilder toJSON() {
        StringBuilder json = new StringBuilder();
        StringBuilder content= new StringBuilder();

        json.append("{\n");
        if (type_ != null) {
            content.append("Type: ");
            content.append(type_.toJSON());
        }

        content = tabulize(content);

        json.append(content);
        json.append("\n}");

        return json;
    }
}
