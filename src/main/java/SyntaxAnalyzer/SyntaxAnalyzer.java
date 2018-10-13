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

    // Return Syntax tree. You can use `toJson` method to visualize
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
        if (pc == null)
            throw new Exception("Syntax grammar error. No `PackageClause` in `SourceFile`");
        sf.setPackageClause(pc);
        locPos = pos;

        if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals(";")) {
            locPos++;

            boolean flag = true;
            while (flag) {
                ImportDecl id = null;
                try {
                    id = importDecl(locPos);
                }
                catch (Exception e) {
                    flag = false;
                }
                finally {
                    if (id != null) {
                        locPos = pos;
                        sf.addImportDecl(id);

                        if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals(";"))
                            locPos++;
                        else
                            throw new Exception("Syntax grammar error. No `;` after `ImportDecl`");
                    }
                }
            }

            flag = true;
            while (flag) {
                TopLevelDecl tld = null;
                try {

                    tld = topLevelDecl(locPos);
                }
                catch (Exception e) {
                    flag = false;
                }
                finally {
                    if (tld != null) {
                        locPos = pos;
                        sf.addTopLevelDecl(tld);

                        if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals(";"))
                            locPos++;
                        else
                            throw new Exception("Syntax grammar error. No `;` after `TopLevelDecl`");
                    }
                }
            }

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
            if (pn == null)
                throw new Exception("Syntax grammar error. No `PackageName` in `PackageClause`");

            locPos = pos;
            pc.setPackageName(pn);

            pos = locPos;
            return pc;
        }

        throw new Exception("Syntax grammar error. No `package` in `PackageClause`");
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

    // ImportDecl grammar: ImportDecl = "import" ( ImportSpec | "(" { ImportSpec ";" } ")" ) .
    private ImportDecl importDecl(int locPos) throws Exception {
        ImportDecl id = new ImportDecl();

        if (tokens.get(locPos).getType().equals(TypeOfToken.Keyword) && tokens.get(locPos).getLexeme().equals("import")) {
            locPos++;

            if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals("(")) {
                locPos++;

                boolean flag = true;
                while(flag) {
                    ImportSpec is = null;
                    try {
                        is = importSpec(locPos);
                    }
                    catch (Exception e) {
                        flag = false;
                    }
                    finally {
                        if (is != null) {
                            locPos = pos;
                            id.addImportSpec(is);

                            if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals(";"))
                                locPos++;
                            else
                                throw new Exception("Syntax grammar error. No `;` after `ImportSpec`");
                        }
                    }
                }

                if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals(")")) {
                    locPos++;

                    pos = locPos;
                    return id;
                }
            }
            else {
                ImportSpec is = importSpec(locPos);
                if (is == null)
                    throw new Exception("Syntax grammar error. No `ImportSpec` after `import`");
                locPos = pos;
                id.addImportSpec(is);

                return id;
            }
        }

        throw new Exception("Syntax grammar error. No `import` in `ImportDecl`");
    }

    private TopLevelDecl topLevelDecl(int locPos) throws Exception {
        // TODO
        throw new Exception("error in TopLevelDecl");
    }

    // ImportSpec = [ "." | PackageName ] ImportPath .
    private ImportSpec importSpec(int locPos) throws Exception {
        ImportSpec is = new ImportSpec();

        if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals(".")) {
            locPos++;
        }
        else {
            PackageName pn = null;
            try {
                pn = packageName(locPos);
            }
            catch (Exception e) {}
            finally {
                if (pn != null) {
                    is.setPackageName(pn);
                    locPos = pos;
                }
            }
        }

        ImportPath ip = importPath(locPos);
        if (ip == null)
            throw new Exception("Syntax grammar error. No `ImportPath` in `ImportSpec`");
        locPos = pos;
        is.setImportPath(ip);

        return is;
    }

    // ImportPath = string_lit .
    public ImportPath importPath(int locPos) throws Exception {
        ImportPath ip = new ImportPath();

        if (tokens.get(locPos).getType().equals(TypeOfToken.StringLiteral)) {
            ip.setString_lit(tokens.get(locPos).getLexeme());

            locPos++;

            pos = locPos;
            return ip;
        }

        throw new Exception("Syntax grammar error. No `string_lit` in `ImportPath`");
    }
}
