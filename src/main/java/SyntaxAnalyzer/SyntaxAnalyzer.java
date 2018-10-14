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
                else
                    throw new Exception("Syntax grammar error. No `)` after `(` { `ImportSpec` }");
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

    // TopLevelDecl  = Declaration | FunctionDecl | MethodDecl
    private TopLevelDecl topLevelDecl(int locPos) throws Exception {
        TopLevelDecl tld = new TopLevelDecl();

        Declaration dec = null;
        try {
            dec = declaration(locPos);
        } catch (Exception e) {
        } finally {
            if (dec != null) {
                locPos = pos;
                tld.setDeclaration(dec);
                return tld;
            }
        }

        FunctionDecl funDec = null;
        try {
            funDec = functionDecl(locPos);
        } catch (Exception e) {
        } finally {
            if (funDec != null) {
                locPos = pos;
                tld.setFunctionDecl(funDec);
                return tld;
            }
        }
        MethodDecl metDec = null;
        try {
            metDec = methodDecl(locPos);
        } catch (Exception e) {
        } finally {
            if (metDec != null) {
                locPos = pos;
                tld.setMethodDecl(metDec);
                return tld;
            }
        }

        throw new Exception("Syntax grammar error. No `Declaration` nor `FunctionDecl` nor `MethodDecl`");
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
    private ImportPath importPath(int locPos) throws Exception {
        ImportPath ip = new ImportPath();

        if (tokens.get(locPos).getType().equals(TypeOfToken.StringLiteral)) {
            ip.setString_lit(tokens.get(locPos).getLexeme());

            locPos++;

            pos = locPos;
            return ip;
        }

        throw new Exception("Syntax grammar error. No `string_lit` in `ImportPath`");
    }

    // Declaration   = ConstDecl | TypeDecl | VarDecl .
    private Declaration declaration(int locPos) throws Exception {
        Declaration dec = new Declaration();

        ConstDecl constDecl = null;
        try {
            constDecl = constDecl(locPos);
        } catch (Exception e) {
        } finally {
            if (constDecl != null) {
                locPos = pos;
                dec.setConstDecl(constDecl);
                return dec;
            }
        }

        TypeDecl typeDecl = null;
        try {
            typeDecl = typeDecl(locPos);
        } catch (Exception e) {
        } finally {
            if (typeDecl != null) {
                locPos = pos;
                dec.setTypeDecl(typeDecl);
                return dec;
            }
        }

        VarDecl varDecl = null;
        try {
            varDecl = varDecl(locPos);
        } catch (Exception e) {
        } finally {
            if (varDecl != null) {
                locPos = pos;
                dec.setVarDecl(varDecl);
                return dec;
            }
        }

        throw new Exception("Syntax grammar error. No `ConstDecl` nor `TypeDecl` nor `VarDecl`");
    }

    private FunctionDecl functionDecl(int locPos) throws Exception {
        //TODO
        return null;
    }

    private MethodDecl methodDecl(int locPos) throws Exception{
        // TODO
        return null;
    }

    // ConstDecl = "const" ( ConstSpec | "(" { ConstSpec ";" } ")" ) .
    private ConstDecl constDecl(int locPos) throws Exception{
        ConstDecl cd = new ConstDecl();

        if (tokens.get(locPos).getType().equals(TypeOfToken.Keyword) && tokens.get(locPos).getLexeme().equals("const")) {
            locPos++;

            if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals("(")) {
                locPos++;
                ConstSpec cs = null;
                boolean flag = true;
                while (flag) {
                    try {
                        cs = constSpec(locPos);
                    } catch (Exception e) {
                        flag = false;
                    } finally {
                        if (cs != null) {
                            locPos = pos;
                            cd.addConstSpec(cs);

                            if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals(";"))
                                locPos++;
                            else
                                throw new Exception("Syntax grammar error. No `;` after `ConstSpec`");
                        }
                    }
                }

                if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals(")")) {
                    locPos++;

                    pos = locPos;
                    return cd;
                } else
                    throw new Exception("Syntax grammar error. No `)` after `(` { `ConstSpec` }");
            } else {
                ConstSpec cs = constSpec(locPos);
                if (cs == null)
                    throw new Exception("Syntax grammar error. No `ConstSpec` after `import`");
                locPos = pos;
                cd.addConstSpec(cs);

                return cd;
            }
        }

        throw new Exception("Syntax grammar error. No `const` in `ConstDecl`");
    }

    // ConstSpec = IdentifierList [ [ Type ] "=" ExpressionList ] .
    private ConstSpec constSpec(int locPos) throws Exception{
        ConstSpec cs = new ConstSpec();

        IdentifierList il = identifierList(locPos);
        if (il == null)
            throw new Exception("Syntax grammar error. No `PackageClause` in `SourceFile`");
        cs.setIdentifierList(il);
        locPos = pos;

        try {
            Type t = null;
            try {
                t = type(locPos);
            }
            catch (Exception e) {}
            finally {
                if (t != null) {
                    locPos = pos;
                    cs.setType(t);
                }
            }

            if (tokens.get(locPos).getType().equals(TypeOfToken.Operator) && tokens.get(locPos).getLexeme().equals("=")) {
                locPos++;

                ExpressionList el = expressionList(locPos);
                if (el == null)
                    throw new Exception("Syntax grammar error. No `ExpressionList` in `ConstSpec`");
                cs.setExpressionList(el);
                locPos = pos;
            }
            else
                throw new Exception("Syntax grammar error. No `=` in `ConstSpec`");


        }
        catch(Exception e) {}

        pos = locPos;
        return cs;
    }

    // Type = TypeName | TypeLit | "(" Type ")" .
    private Type type(int locPos) throws Exception {
        Type t = new Type();

        TypeName tn = null;
        try {
            tn = typeName(locPos);
        } catch (Exception e) {
        } finally {
            if (tn != null) {
                locPos = pos;
                t.setTypeName(tn);
                return t;
            }
        }

        TypeLit tl = null;
        try {
            tl = typeLit(locPos);
        } catch (Exception e) {
        } finally {
            if (tl != null) {
                locPos = pos;
                t.setTypeLit(tl);
                return t;
            }
        }

        Type t_ = null;
        try {
            if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals("(")) {
                locPos++;

                t_ = type(locPos);

            }else{
                throw new Exception("Syntax grammar error. No `(` in `Type`");
            }
        } catch (Exception e) {
        } finally {
            if (t_ != null) {
                locPos = pos;

                if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals(")")) {
                    locPos++;
                    t.setType(t_);
                    pos=locPos;
                    return t;
                }
                throw new Exception("Syntax grammar error. No `)` after `(` `Type` ");
            }
        }

        throw new Exception("Syntax grammar error. No `Declaration` nor `FunctionDecl` nor `MethodDecl`");
    }

    //TypeName  = identifier | QualifiedIdent .
    private TypeName typeName(int locPos) throws Exception {
        TypeName tn = new TypeName();

        String i = null;
        try {
            if (tokens.get(locPos).getType().equals(TypeOfToken.Identifier)){
                i = tokens.get(locPos).getLexeme();
                locPos++;
                pos=locPos;
            }
        } catch (Exception e) {
        } finally {
            if (i != null) {
                locPos = pos;
                tn.setIdentifier(i);
                return tn;
            }
        }

        QualifiedIdentifier qi = null;
        try {
            qi = qualifiedIdentifier(locPos);
        } catch (Exception e) {
        } finally {
            if (qi != null) {
                locPos = pos;
                tn.setQualifiedIdentifier(qi);
                return tn;
            }
        }
        throw new Exception("Syntax grammar error. No `Identifier` nor `QualifiedIdentifier` in `TypeName`");
    }

    // QualifiedIdent = PackageName "." identifier .
    private QualifiedIdentifier qualifiedIdentifier(int locPos) throws Exception {
        QualifiedIdentifier qa = new QualifiedIdentifier();
        PackageName pn = null;
        pn = packageName(locPos);
        if (pn != null){
            qa.setPackageName(pn);
            locPos = pos;
            if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals(".")) {
                locPos++;

                if (tokens.get(locPos).getType().equals(TypeOfToken.Identifier)){
                    qa.setIdentifier(tokens.get(locPos).getLexeme());
                    locPos++;

                    pos = locPos;
                    return qa;
                }
                throw new Exception("Syntax grammar error. No `Identifier` after `.` in `QualifiedIdentifier`");
            }
            throw new Exception("Syntax grammar error. No `.` after `PackageName` in `QualifiedIdentifier`");
        }
        throw new Exception("Syntax grammar error. No `PackageName` in `QualifiedIdentifier`");
    }

    // TypeLit   = ArrayType | StructType | PointerType | FunctionType | InterfaceType | SliceType | MapType | ChannelType .
    private TypeLit typeLit(int locPos) throws Exception {
        TypeLit tl = new TypeLit();

        try {
            ArrayType at = null;
            at = arrayType(locPos);
            if (at != null) {
                locPos = pos;
                // TODO: not pos = locPos ???
                tl.setArrayType(at);
                return tl;
            }
        }
        catch (Exception e) {}

        try {
            StructType st = null;
            st = structType(locPos);
            if (st != null) {
                locPos = pos;
                tl.setStructType(st);
                return tl;
            }
        }
        catch (Exception e) {}

        try {
            PointerType pt = null;
            pt = pointerType(locPos);
            if (pt != null) {
                locPos = pos;
                tl.setPointerType(pt);
                return tl;
            }
        }
        catch (Exception e) {}

        try {
            FunctionType ft = null;
            ft = functionType(locPos);
            if (ft != null) {
                locPos = pos;
                tl.setFunctionType(ft);
                return tl;
            }
        }
        catch (Exception e) {}

        try {
            InterfaceType it = null;
            it = interfaceType(locPos);
            if (it != null) {
                locPos = pos;
                tl.setInterfaceType(it);
                return tl;
            }
        }
        catch (Exception e) {}

        try {
            SliceType st = null;
            st = sliceType(locPos);
            if (st != null) {
                locPos = pos;
                tl.setSliceType(st);
                return tl;
            }
        }
        catch (Exception e) {}

        try {
            MapType mt = null;
            mt = mapType(locPos);
            if (mt != null) {
                locPos = pos;
                tl.setMapType(mt);
                return tl;
            }
        }
        catch (Exception e) {}

        try {
            ChannelType ct = null;
            ct = channelType(locPos);
            if (ct != null) {
                locPos = pos;
                tl.setChannelType(ct);
                return tl;
            }
        }
        catch (Exception e) {}

        throw new Exception("Syntax grammar error. No `ArrayType` nor `StructType` nor `PointerType` nor `FunctionType` nor `InterfaceType` nor `SliceType` nor `MapType` nor `ChannelTyp` in `TypeLit`");
    }

    private ChannelType channelType(int locPos) {
        // TODO
        return null;
    }

    private FunctionType functionType(int locPos) {
        //TODO
        return null;
    }

    private InterfaceType interfaceType(int locPos) {
        //TODO
        return null;
    }

    private SliceType sliceType(int locPos) {
        //TODO
        return null;
    }

    private MapType mapType(int locPos) {
        //TODO
        return null;
    }

    private PointerType pointerType(int locPos) {
        // TODO
        return null;
    }

    private StructType structType(int locPos) {
        // TODO
        return null;
    }

    private ArrayType arrayType(int locPos) {
        // TODO
        return null;
    }

    // ExpressionList = Expression { "," Expression } .
    private ExpressionList expressionList(int locPos) throws Exception{
        ExpressionList el = new ExpressionList();

        Expression ex = null;
        ex = expression(locPos);
        if (ex != null){
            locPos = pos;
            el.addExpression(ex);

            boolean flag = true;
            while (flag) {

                try {
                    if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals(",")) {
                        locPos++;

                        ex = null;
                        ex = expression(locPos);
                        if (ex != null) {
                            el.addExpression(ex);
                            locPos = pos;
                        }
                        else
                            throw new Exception("Syntax grammar error. No `Expression` in `ExpressionList`");
                    }
                    else
                        throw new Exception("Syntax grammar error. No `,` in `IdentifierList`");
                } catch (Exception e) {
                    flag = false;
                }
            }

            pos = locPos;
            return el;
        }
        throw new Exception("Syntax grammar error. No `Expression` in `ExpressionList`");
    }

    // Expression = UnaryExpr | UnaryExpr binary_op Expression .
    private Expression expression(int locPos) throws Exception{
        Expression ex = new Expression();
        UnaryExpr unaryExpr = null;
        try {
            unaryExpr = unaryExpr(locPos);
        } catch (Exception e) {
        } finally {
            if (unaryExpr != null) {
                locPos = pos;
                ex.setUnaryExpr(unaryExpr);

                Binary_op bo = null;
                bo = binary_op(locPos);
                if (bo != null) {
                    ex.setBinary_op(bo);
                    locPos = pos;

                    Expression exp = null;
                    exp = expression(locPos);
                    if (exp != null){
                        ex.setExpression(exp);
                        pos = locPos;
                        return ex;
                    }
                    throw new Exception("Syntax grammar error. No `Expression` after `UnaryExpr binary_op` in `Expression`");
                }
                pos = locPos;
                return ex;
            }
            throw new Exception("Syntax grammar error. No `UnaryExpr` nor `UnaryExpr binary_op Expression` in `Expression`");
        }
    }

    // binary_op  = "||" | "&&" | rel_op | add_op | mul_op .
    private Binary_op binary_op(int locPos) throws Exception{
        Binary_op bo = new Binary_op();

        try {
            String op = null;
            if (tokens.get(locPos).getType().equals(TypeOfToken.Operator) && tokens.get(locPos).getLexeme().equals("||")){
                op = tokens.get(locPos).getLexeme();
                locPos++;
                pos = locPos;
                bo.setOp(op);
                return bo;
            }
        }
        catch (Exception e) {}

        try {
            String op = null;
            if (tokens.get(locPos).getType().equals(TypeOfToken.Operator) && tokens.get(locPos).getLexeme().equals("&&")){
                op = tokens.get(locPos).getLexeme();
                locPos++;
                pos = locPos;
                bo.setOp(op);
                return bo;
            }
        }
        catch (Exception e) {}

        try {
            Rel_op ro = null;
            ro = rel_op(locPos);
            if (ro != null) {
                locPos = pos;
                bo.setRel_op(ro);
                return bo;
            }
        }
        catch (Exception e) {}

        try {
            Add_op ao = null;
            ao = add_op(locPos);
            if (ao != null) {
                locPos = pos;
                bo.setAdd_op(ao);
                return bo;
            }
        }
        catch (Exception e) {}

        try {
            Mul_op mo = null;
            mo = mul_op(locPos);
            if (mo != null) {
                locPos = pos;
                bo.setMul_op(mo);
                return bo;
            }
        }
        catch (Exception e) {}

        throw new Exception("Syntax grammar error. No \"||\" nor \"&&\" nor `Rel_op` nor `Add_op` nor `Mul_op` in `Binary_op`");
    }

    // mul_op     = "*" | "/" | "%" | "<<" | ">>" | "&" | "&^" .
    private Mul_op mul_op(int locPos) throws Exception {
        Mul_op mo = new Mul_op();

        try {
            String op = null;
            if (tokens.get(locPos).getType().equals(TypeOfToken.Operator) && tokens.get(locPos).getLexeme().equals("*")){
                op = tokens.get(locPos).getLexeme();
                locPos++;
                pos = locPos;
                mo.setOp(op);
                return mo;
            }
        }
        catch (Exception e) {}

        try {
            String op = null;
            if (tokens.get(locPos).getType().equals(TypeOfToken.Operator) && tokens.get(locPos).getLexeme().equals("/")){
                op = tokens.get(locPos).getLexeme();
                locPos++;
                pos = locPos;
                mo.setOp(op);
                return mo;
            }
        }
        catch (Exception e) {}

        try {
            String op = null;
            if (tokens.get(locPos).getType().equals(TypeOfToken.Operator) && tokens.get(locPos).getLexeme().equals("%")){
                op = tokens.get(locPos).getLexeme();
                locPos++;
                pos = locPos;
                mo.setOp(op);
                return mo;
            }
        }
        catch (Exception e) {}

        try {
            String op = null;
            if (tokens.get(locPos).getType().equals(TypeOfToken.Operator) && tokens.get(locPos).getLexeme().equals("<<")){
                op = tokens.get(locPos).getLexeme();
                locPos++;
                pos = locPos;
                mo.setOp(op);
                return mo;
            }
        }
        catch (Exception e) {}

        try {
            String op = null;
            if (tokens.get(locPos).getType().equals(TypeOfToken.Operator) && tokens.get(locPos).getLexeme().equals(">>")){
                op = tokens.get(locPos).getLexeme();
                locPos++;
                pos = locPos;
                mo.setOp(op);
                return mo;
            }
        }
        catch (Exception e) {}

        try {
            String op = null;
            if (tokens.get(locPos).getType().equals(TypeOfToken.Operator) && tokens.get(locPos).getLexeme().equals("&")){
                op = tokens.get(locPos).getLexeme();
                locPos++;
                pos = locPos;
                mo.setOp(op);
                return mo;
            }
        }
        catch (Exception e) {}

        try {
            String op = null;
            if (tokens.get(locPos).getType().equals(TypeOfToken.Operator) && tokens.get(locPos).getLexeme().equals("&^")){
                op = tokens.get(locPos).getLexeme();
                locPos++;
                pos = locPos;
                mo.setOp(op);
                return mo;
            }
        }
        catch (Exception e) {}

        throw new Exception("Syntax grammar error. No \"*\" | \"/\" | \"%\" | \"<<\" | \">>\" | \"&\" | \"&^\" in `Mul_op`");
    }

    // add_op     = "+" | "-" | "|" | "^" .
    private Add_op add_op(int locPos) throws Exception {
        Add_op ao = new Add_op();

        try {
            String op = null;
            if (tokens.get(locPos).getType().equals(TypeOfToken.Operator) && tokens.get(locPos).getLexeme().equals("+")){
                op = tokens.get(locPos).getLexeme();
                locPos++;
                pos = locPos;
                ao.setOp(op);
                return ao;
            }
        }
        catch (Exception e) {}

        try {
            String op = null;
            if (tokens.get(locPos).getType().equals(TypeOfToken.Operator) && tokens.get(locPos).getLexeme().equals("-")){
                op = tokens.get(locPos).getLexeme();
                locPos++;
                pos = locPos;
                ao.setOp(op);
                return ao;
            }
        }
        catch (Exception e) {}

        try {
            String op = null;
            if (tokens.get(locPos).getType().equals(TypeOfToken.Operator) && tokens.get(locPos).getLexeme().equals("|")){
                op = tokens.get(locPos).getLexeme();
                locPos++;
                pos = locPos;
                ao.setOp(op);
                return ao;
            }
        }
        catch (Exception e) {}

        try {
            String op = null;
            if (tokens.get(locPos).getType().equals(TypeOfToken.Operator) && tokens.get(locPos).getLexeme().equals("^")){
                op = tokens.get(locPos).getLexeme();
                locPos++;
                pos = locPos;
                ao.setOp(op);
                return ao;
            }
        }
        catch (Exception e) {}

        throw new Exception("Syntax grammar error. No \"+\" | \"-\" | \"|\" | \"^\" in `Add_op`");
    }

    // rel_op     = "==" | "!=" | "<" | "<=" | ">" | ">=" .
    private Rel_op rel_op(int locPos) throws Exception {
        Rel_op bo = new Rel_op();

        try {
            String op = null;
            if (tokens.get(locPos).getType().equals(TypeOfToken.Operator) && tokens.get(locPos).getLexeme().equals("==")){
                op = tokens.get(locPos).getLexeme();
                locPos++;
                pos = locPos;
                bo.setOp(op);
                return bo;
            }
        }
        catch (Exception e) {}

        try {
            String op = null;
            if (tokens.get(locPos).getType().equals(TypeOfToken.Operator) && tokens.get(locPos).getLexeme().equals("!=")){
                op = tokens.get(locPos).getLexeme();
                locPos++;
                pos = locPos;
                bo.setOp(op);
                return bo;
            }
        }
        catch (Exception e) {}

        try {
            String op = null;
            if (tokens.get(locPos).getType().equals(TypeOfToken.Operator) && tokens.get(locPos).getLexeme().equals("<")){
                op = tokens.get(locPos).getLexeme();
                locPos++;
                pos = locPos;
                bo.setOp(op);
                return bo;
            }
        }
        catch (Exception e) {}

        try {
            String op = null;
            if (tokens.get(locPos).getType().equals(TypeOfToken.Operator) && tokens.get(locPos).getLexeme().equals("<=")){
                op = tokens.get(locPos).getLexeme();
                locPos++;
                pos = locPos;
                bo.setOp(op);
                return bo;
            }
        }
        catch (Exception e) {}

        try {
            String op = null;
            if (tokens.get(locPos).getType().equals(TypeOfToken.Operator) && tokens.get(locPos).getLexeme().equals(">")){
                op = tokens.get(locPos).getLexeme();
                locPos++;
                pos = locPos;
                bo.setOp(op);
                return bo;
            }
        }
        catch (Exception e) {}

        try {
            String op = null;
            if (tokens.get(locPos).getType().equals(TypeOfToken.Operator) && tokens.get(locPos).getLexeme().equals(">=")){
                op = tokens.get(locPos).getLexeme();
                locPos++;
                pos = locPos;
                bo.setOp(op);
                return bo;
            }
        }
        catch (Exception e) {}

        throw new Exception("Syntax grammar error. No \"*\" | \"/\" | \"%\" | \"<<\" | \">>\" | \"&\" | \"&^\" in `Rel_op`");
    }

    // UnaryExpr  = PrimaryExpr | unary_op UnaryExpr .
    private UnaryExpr unaryExpr(int locPos) throws Exception {
        UnaryExpr uef = new UnaryExpr();
        PrimaryExpr pe = null;
        try{
            pe = primaryExpr(locPos);
        }catch (Exception e){
        }finally {
            if (pe != null){
                uef.setPrimaryExpr(pe);
                pos = locPos;
                return uef;
            }
        }

        Unary_op uo = null;
        try{
            uo = unary_op(locPos);
        }catch (Exception e){
        }finally {
            if (uo != null){
                uef.setUnary_op(uo);
                locPos = pos;

                UnaryExpr ue = null;
                ue = unaryExpr(locPos);
                if (ue != null){
                    uef.setUnaryExpr(ue);
                    pos = locPos;
                    return uef;
                }
                throw new Exception("Syntax grammar error. No `UnaryExpr` after `unary_op` in `unary_op UnaryExpr`");
            }
        }
        throw new Exception("Syntax grammar error. No `PrimaryExpr` nor `unary_op UnaryExpr` in `UnaryExpr`");
    }

    // unary_op   = "+" | "-" | "!" | "^" | "*" | "&" | "<-" .
    private Unary_op unary_op(int locPos) throws Exception{
        Unary_op uo = new Unary_op();
        if (tokens.get(locPos).getType().equals(TypeOfToken.Operator)){
            try {
                String op = null;
                if (tokens.get(locPos).getLexeme().equals("+")){
                    op = tokens.get(locPos).getLexeme();
                    locPos++;
                    pos = locPos;
                    uo.setOp(op);
                    return uo;
                }
            }
            catch (Exception e) {}

            try {
                String op = null;
                if (tokens.get(locPos).getLexeme().equals("-")){
                    op = tokens.get(locPos).getLexeme();
                    locPos++;
                    pos = locPos;
                    uo.setOp(op);
                    return uo;
                }
            }
            catch (Exception e) {}

            try {
                String op = null;
                if (tokens.get(locPos).getLexeme().equals("!")){
                    op = tokens.get(locPos).getLexeme();
                    locPos++;
                    pos = locPos;
                    uo.setOp(op);
                    return uo;
                }
            }
            catch (Exception e) {}

            try {
                String op = null;
                if (tokens.get(locPos).getLexeme().equals("^")){
                    op = tokens.get(locPos).getLexeme();
                    locPos++;
                    pos = locPos;
                    uo.setOp(op);
                    return uo;
                }
            }
            catch (Exception e) {}

            try {
                String op = null;
                if (tokens.get(locPos).getLexeme().equals("*")){
                    op = tokens.get(locPos).getLexeme();
                    locPos++;
                    pos = locPos;
                    uo.setOp(op);
                    return uo;
                }
            }
            catch (Exception e) {}

            try {
                String op = null;
                if (tokens.get(locPos).getLexeme().equals("&")){
                    op = tokens.get(locPos).getLexeme();
                    locPos++;
                    pos = locPos;
                    uo.setOp(op);
                    return uo;
                }
            }
            catch (Exception e) {}

            try {
                String op = null;
                if (tokens.get(locPos).getLexeme().equals("<-")){
                    op = tokens.get(locPos).getLexeme();
                    locPos++;
                    pos = locPos;
                    uo.setOp(op);
                    return uo;
                }
            }
            catch (Exception e) {}
            throw new Exception("Syntax grammar error. No \"+\" | \"-\" | \"!\" | \"^\" | \"*\" | \"&\" | \"<-\" in `Unary_op`");

        }
        throw new Exception("Syntax grammar error. No `Operator token` in `Unary_op`");
    }

    // PrimaryExpr: (Operand | Conversion | MethodExpr) { Selector | Index | Slice | TypeAssertion | Argument } .
    private PrimaryExpr primaryExpr(int locPos) throws Exception{
        PrimaryExpr pe = new PrimaryExpr();

        Operand op = null;
        try {
            op = operand(locPos);
        } catch (Exception e) {
        } finally {
            if (op != null) {
                locPos = pos;
                pe.setOperand(op);
            }else{
                Conversion c = null;
                try {
                    c = conversion(locPos);
                } catch (Exception e) {
                } finally {
                    if (c != null) {
                        locPos = pos;
                        pe.setConversion(c);
                    }else{
                        MethodExpr me = null;
                        try {
                            me = methodExpr(locPos);
                        } catch (Exception e) {
                        } finally {
                            if (me != null) {
                                locPos = pos;
                                pe.setMethodExpr(me);
                            }else {
                                throw new Exception("Syntax grammar error. No `Operand` nor `Conversion` nor `MethodExpr` in `PrimaryExpr`");
                            }
                        }
                    }
                }
            }
        }
        
        boolean flag = true;
        while (flag) {
            flag = false;

            try {
                Selector s = null;
                s = selector(locPos);
                if (s != null) {
                    locPos = pos;
                    pe.addToList(s);
                    flag = true;
                    continue;
                }
            }
            catch (Exception e) {}

            try {
                Index i = null;
                i = index(locPos);
                if (i != null) {
                    locPos = pos;
                    pe.addToList(i);
                    flag = true;
                    continue;
                }
            }
            catch (Exception e) {}

            try {
                Slice s = null;
                s = slice(locPos);
                if (s != null) {
                    locPos = pos;
                    pe.addToList(s);
                    flag = true;
                    continue;
                }
            }
            catch (Exception e) {}

            try {
                TypeAssertion ta = null;
                ta = typeAssertion(locPos);
                if (ta != null) {
                    locPos = pos;
                    pe.addToList(ta);
                    flag = true;
                    continue;
                }
            }
            catch (Exception e) {}

            try {
                Arguments a = null;
                a = arguments(locPos);
                if (a != null) {
                    locPos = pos;
                    pe.addToList(a);
                    flag = true;
                    continue;
                }
            }
            catch (Exception e) {}
        }
        pos = locPos;
        return pe;
    }

    // Arguments = "(" [ ( ExpressionList | Type [ "," ExpressionList ] ) [ "..." ] [ "," ] ] ")" .
    private Arguments arguments(int locPos) throws Exception {
        //TODO
        return null;
    }

    // TypeAssertion  = "." "(" Type ")" .
    private TypeAssertion typeAssertion(int locPos) throws Exception{
        TypeAssertion ta = new TypeAssertion();
        if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals(".")) {
            locPos++;
            if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals("(")){
                locPos++;
                Type t = null;
                t = type(locPos);
                if (t != null) {
                    locPos = pos;
                    ta.setType(t);
                    if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals(")")) {
                        locPos++;
                        pos=locPos;
                        return ta;
                    }
                    throw new Exception("Syntax grammar error. No `)` after `Type` in `TypeAssertion`");
                }
                throw new Exception("Syntax grammar error. No `Type` after `.` `(` in `TypeAssertion`");

            }
            throw new Exception("Syntax grammar error. No `(` after `.` in `TypeAssertion`");
        }
        throw new Exception("Syntax grammar error. No `.` before `(` in `TypeAssertion`");
    }

    // Slice = "[" [ Expression ] ":" [ Expression ] "]" | [" [ Expression ] ":" Expression ":" Expression "]" .
    private Slice slice(int locPos) throws Exception{
        //TODO: check
        Slice s = new Slice();
        if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals("[")) {
            try {
                Expression e = null;
                e = expression(locPos);
                if (e != null) {
                    s.addExpression(e);
                    locPos = pos;
                }
            } catch (Exception e) {
            }
            if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals(":")) {
                locPos++;
                try {
                    Expression e = null;
                    e = expression(locPos);
                    s.addExpression(e);
                    locPos = pos;
                    if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals(":")) {
                        locPos++;
                        e = expression(locPos);
                        s.addExpression(e);
                        locPos = pos;
                        return s;
                    }
                } catch (Exception e) {
                }
                try {
                    Expression e = null;
                    e = expression(locPos);
                    if (e != null) {
                        s.addExpression(e);
                        locPos = pos;
                    }
                } catch (Exception e) {
                }
                if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals("]")) {
                    locPos++;
                    pos = locPos;
                    return s;
                }
                throw new Exception("Syntax grammar error. No `]` in the end in `Slice`");
            }
            throw new Exception("Syntax grammar error. No `:` after \"[\" `Expression` in `Slice`");
        }
        throw new Exception("Syntax grammar error. No `[` before `Expression` \":\" in `Slice`");
    }

    // Index  = "[" Expression "]" .
    private Index index(int locPos) throws Exception {
        Index i = new Index();
        if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals("[")) {
            locPos++;
            Expression e = null;
            e = expression(locPos);
            if (e != null) {
                locPos = pos;
                i.setExpression(e);
                if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals("]")){
                    locPos++;
                    pos = locPos;
                    return i;
                }
                throw new Exception("Syntax grammar error. No `]` after `Expression` in `Index`");
            }

            throw new Exception("Syntax grammar error. No `identifier` after `[` in `Index`");
        }
        throw new Exception("Syntax grammar error. No `[` before `Expression` in `Index`");
    }

    // Selector = "." identifier .
    private Selector selector(int locPos) throws Exception {
        Selector s = new Selector();
        if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals(".")) {
            locPos++;
            String i = null;
            if (tokens.get(locPos).getType().equals(TypeOfToken.Identifier)){
                i = tokens.get(locPos).getLexeme();
                s.setIdentifier(i);
                locPos++;

                pos = locPos;
                return s;
            }
            throw new Exception("Syntax grammar error. No `identifier` after `.` in `Selector`");
        }
        throw new Exception("Syntax grammar error. No `.` before identifier in `Selector`");
    }

    private MethodExpr methodExpr(int locPos) throws Exception{
        // TODO
        return null;
    }

    private Conversion conversion(int locPos) throws Exception{
        // TODO
        return null;
    }

    private Operand operand(int locPos) throws Exception {
        // TODO
        return null;
    }

    // IdentifierList = identifier { "," identifier } .
    private IdentifierList identifierList(int locPos) throws Exception{
        IdentifierList il = new IdentifierList();

        if (tokens.get(locPos).getType().equals(TypeOfToken.Identifier)) {
            il.addIdentifier(tokens.get(locPos).getLexeme());
            locPos++;

            boolean flag = true;
            while (flag) {
                try {
                    if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals(",")) {
                        locPos++;

                        if (tokens.get(locPos).getType().equals(TypeOfToken.Identifier)) {
                            il.addIdentifier(tokens.get(locPos).getLexeme());
                            locPos++;
                        }
                        else
                            throw new Exception("Syntax grammar error. No `Identifier` in `IdentifierList`");
                    }
                    else
                        throw new Exception("Syntax grammar error. No `,` in `IdentifierList`");
                } catch (Exception e) {
                    flag = false;
                }
            }

            pos = locPos;
            return il;
        }
        throw new Exception("Syntax grammar error. No `Identifier` in `IdentifierList`");
    }

    private TypeDecl typeDecl(int locPos) throws Exception{
        // TODO
        return null;
    }

    private VarDecl varDecl(int locPos) throws Exception{
        // TODO
        return null;
    }
}
