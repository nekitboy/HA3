package SyntaxAnalyzer.SyntaxVariables;

import SyntaxAnalyzer.SyntaxVariable;

// KeyType  = Type .
public class KeyType extends SyntaxVariable{
    static final String type = "KeyType";

    private Type type_;

    public KeyType() {
        super(type);
    }

    public void setType_(Type type_) throws Exception {
        if (type_ == null)
            throw new Exception("`Type` is null in `KeyType`");
        this.type_ = type_;
    }

    @Override
    public StringBuilder toJSON() {
        StringBuilder json = new StringBuilder();
        StringBuilder content = new StringBuilder();
        json.append("{\n");
        content.append("Type: ");
        content.append(type_.toJSON());
        json.append(tabulize(content));
        json.append("\n}");
        return json;
    }
}
