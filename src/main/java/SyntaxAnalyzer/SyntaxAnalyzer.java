package SyntaxAnalyzer;
import LexicalAnalyzer.LexicalAnalyzer;
import LexicalAnalyzer.LexicalAnalyzer.TypeOfToken;
import SyntaxAnalyzer.SyntaxVariables.*;

import java.util.ArrayList;

public class SyntaxAnalyzer {

    private LexicalAnalyzer lex;
    private ArrayList<LexicalAnalyzer.Token> tokens;
    private int pos; // Global token position. We update local position to global after searching syntax variable

    public SyntaxAnalyzer(StringBuilder sourceCode) throws Exception {
        lex = new LexicalAnalyzer(sourceCode);
        tokens = lex.getAllTokens();

        pos = 0;
    }

    public SyntaxVariable getSyntaxTree() throws Exception {
        SourceFile sf = sourceFile(0);

        if (!tokens.get(pos).getType().equals(TypeOfToken.EOF))
            throw new Exception("Can't proceed all tokens");

        return sf;
    }

    // SourceFile grammar: SourceFile = PackageClause ";" { ImportDecl ";" } { TopLevelDecl ";" } .
    private SourceFile sourceFile(int locPos) throws Exception {
        SourceFile sf = new SourceFile();

        PackageClause pc = packageClause(locPos);
        sf.setPackageClause(pc);
        locPos = pos;

        if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals(";")) {
            locPos++;

            //TODO

            pos = locPos;
            return sf;
        }

        throw new Exception("Syntax grammar error. No `;` after `PackageClause`");
    }

    // PackageClause grammar: PackageClause = "package" PackageName .
    private PackageClause packageClause(int locPos) throws Exception {
        PackageClause pc = new PackageClause();

        if (tokens.get(locPos).getType().equals(TypeOfToken.Keyword) && tokens.get(locPos).getLexeme().equals("package")) {
            locPos++;
            PackageName pn = packageName(locPos);
            locPos = pos;
            pc.setPackageName(pn);
        }

        pos = locPos;
        return pc;
    }

    // PackageName grammar: PackageName = identifier .
    private PackageName packageName(int locPos) throws Exception {
        PackageName pn = new PackageName();

        if (tokens.get(locPos).getType().equals(TypeOfToken.Identifier)) {
            pn.setIdentifier(tokens.get(locPos).getLexeme());
            locPos++;

            pos = locPos;
            return pn;
        }

        throw new Exception("Syntax grammar error. No `Identifier` in `PackageName`");
    }
}
