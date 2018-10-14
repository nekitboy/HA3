package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

// BaseType  = Type .
public class BaseType extends SyntaxVariable {
    static final String type = "BaseType";

    Type type_;

    public void setType_(Type type_) throws Exception {
        if (type_ == null)
            throw new Exception("`Type` is null in `ElementType`");
        this.type_ = type_;
    }

    public BaseType() {
        super(type);
    }

    @Override
    public StringBuilder toJSON() {
        StringBuilder json = new StringBuilder();
        StringBuilder content= new StringBuilder();

        json.append("{\n");
        content.append("Type: ");
        content.append(type_.toJSON());


        content = tabulize(content);

        json.append(content);
        json.append("\n}");

        return json;
    }
}
