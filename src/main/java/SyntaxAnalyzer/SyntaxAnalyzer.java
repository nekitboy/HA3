package SyntaxAnalyzer;
import LexicalAnalyzer.LexicalAnalyzer;
import LexicalAnalyzer.LexicalAnalyzer.TypeOfToken;
import SyntaxAnalyzer.SyntaxVariables.*;
import com.sun.deploy.pings.Pings;
import com.sun.xml.internal.bind.v2.TODO;

import java.io.FileReader;
import java.lang.reflect.Method;
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
        return null;
    }

    private MethodDecl methodDecl(int locPos) throws Exception{
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

    // ChannelType = ( "chan" | "chan" "<-" | "<-" "chan" ) ElementType .
    private ChannelType channelType(int locPos) throws Exception {
        ChannelType ct = new ChannelType();

        boolean flag = true;
        try {
            if (tokens.get(locPos).getType().equals(TypeOfToken.Keyword) && tokens.get(locPos).getLexeme().equals("chan")) {
                locPos++;

                if (tokens.get(locPos).getType().equals(TypeOfToken.Operator) && tokens.get(locPos).getLexeme().equals("<-")) {
                    locPos++;
                    flag = false;
                }
            }
            else
                throw new Exception("Syntax grammar error. No `chan` in `ChannelType`");
        } catch (Exception e) {flag = true;}

        if (flag) {
            try {
                if (tokens.get(locPos).getType().equals(TypeOfToken.Operator) && tokens.get(locPos).getLexeme().equals("<-")) {
                    locPos++;

                    if (tokens.get(locPos).getType().equals(TypeOfToken.Keyword) && tokens.get(locPos).getLexeme().equals("<-")) {
                        locPos++;
                        flag = false;
                    }
                    else
                        throw new Exception("Syntax grammar error. No `chan` after `<-` in `ChannelType");
                }
                else
                    throw new Exception("Syntax grammar error. No `<-` in `ChannelType`");
            } catch (Exception e) {flag = true;}
        }

        if (!flag) {
            ElementType et = null;
            et = elementType(locPos);

            if (et != null) {
                locPos = pos;
                ct.setElementType(et);
                return ct;
            }
            throw new Exception("Syntax grammar error. No `ElementType` in `ChannelType`");
        }
        throw new Exception("Syntax grammar error. No `chan` nor `<- chan` nor `chan <-` in `ChannelType`");
    }

    // FunctionType   = "func" Signature .
    private FunctionType functionType(int locPos) throws Exception {
        FunctionType ft = new FunctionType();

        if (tokens.get(locPos).getType().equals(TypeOfToken.Keyword) && tokens.get(locPos).getLexeme().equals("func")) {
            locPos++;

            Signature s = null;
            s = signature(locPos);

            if (s != null) {
                locPos = pos;
                ft.setSignature(s);

                return ft;
            }
            throw new Exception("Syntax grammar error. No `Signature` in `FunctionType`");
        }
        throw new Exception("Syntax grammar error. No `func` in `FunctionType`");
    }

    // Signature  = Parameters [ Result ] .
    private Signature signature(int locPos) throws Exception {
        Signature s = new Signature();

        Parameters p = null;
        p = parameters(locPos);

        if (p != null) {
            locPos = pos;
            s.setParameters(p);

            try {
                Result r = null;
                r = result(locPos);

                if (r != null) {
                    locPos = pos;
                    s.setResult(r);
                }
                else
                    throw new Exception("Syntax grammar error. No `Result` in `Signature`");
            }
            catch (Exception e) {}

            pos = locPos;
            return s;
        }
        throw new Exception("Syntax grammar error. No `Parameters` in `Signature");
    }

    //Result    = Parameters | Type .
    private Result result(int locPos) throws Exception {
        Result r = new Result();

        try {
            Parameters p = null;
            p = parameters(locPos);

            if (p != null) {
                locPos = pos;
                r.setParameters(p);

                return r;
            }
            else
                throw new Exception("Syntax grammar error. No `Parameters` in `Result`");
        } catch (Exception e) {}

        Type t = null;
        t = type(locPos);

        if (t != null) {
            locPos = pos;
            r.setType_(t);

            return r;
        }
        throw new Exception("Syntax grammar error. No `Type` in `Result`");
    }

    // Parameters  = "(" [ ParameterList [ "," ] ] ")" .
    private Parameters parameters(int locPos) throws Exception {
        Parameters p = new Parameters();

        if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals("(")) {
            locPos++;

            ParameterList pl = null;
            try {
                pl = parameterList(locPos);
                if (pl != null) {
                    locPos = pos;
                    p.setParameterList(pl);

                    if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals(",")) {
                        locPos++;
                    }
                }
                else
                    throw new Exception("Syntax grammar error. No `ParameterList` in `Parameters`");
            }
            catch (Exception e) {}

            if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals(")")) {
                locPos++;

                pos = locPos;
                return p;
            }
            throw new Exception("Syntax grammar error. No `)` in `Parameters`");
        }
        throw new Exception("Syntax grammar error. No `(` in `Parameters`");
    }

    // ParameterList  = ParameterDecl { "," ParameterDecl } .
    private ParameterList parameterList(int locPos) throws Exception {
        ParameterList pl = new ParameterList();

        ParameterDecl pd = null;
        pd = parameterDecl(locPos);

        if (pd != null) {
            pl.addParameterDecl(pd);
            locPos = pos;

            boolean flag = true;
            while (flag) {
                pd = null;
                try {
                    pd = parameterDecl(locPos);
                    if (pd != null) {
                        locPos = pos;
                        pl.addParameterDecl(pd);
                    }
                    else
                        throw new Exception("Syntax grammar error. No `ParameterDecl`");
                }
                catch (Exception e) {
                    flag = false;
                }
            }

            pos = locPos;
            return pl;
        }
        throw new Exception("Syntax grammar error. No `ParameterDecl` in `ParameterList`");
    }

    // ParameterDecl  = [ IdentifierList ] [ "..." ] Type .
    private ParameterDecl parameterDecl(int locPos) throws Exception {
        ParameterDecl pd = new ParameterDecl();

        try {
            IdentifierList il = null;
            il = identifierList(locPos);
            if (il != null) {
                locPos = pos;
                pd.setIdentifierList(il);
            }
        } catch (Exception e) {}

        try {
            if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals("...")) {
               locPos++;
            }
        }
        catch (Exception e) {}

        Type t = null;
        t = type(locPos);
        if (t != null) {
            locPos = pos;
            pd.setType_(t);

            return pd;
        }

        throw new Exception("Syntax grammar error. No `Type` in `ParameterDecl`");
    }

    // InterfaceType  = "interface" "{" { MethodSpec ";" } "}" .
    private InterfaceType interfaceType(int locPos) throws Exception {
        InterfaceType it = new InterfaceType();

        if (tokens.get(locPos).getType().equals(TypeOfToken.Keyword) && tokens.get(locPos).equals("interface")) {
            locPos++;

            if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals("{")) {
                locPos++;

                boolean flag = true;
                while(flag) {
                    MethodSpec ms = null;
                    try {
                        ms = methodSpec(locPos);
                        if (ms != null) {
                            locPos = pos;
                            it.addMethodSpec(ms);

                            if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals(";")) {
                                locPos++;
                            }
                            else
                                throw new Exception("Syntax grammar error. No `;` after `MethodSpec` in `InterfaceDecl");
                        }
                        else
                            throw new Exception("Syntax grammar error. No MethodSpec` in `InterfaceType`");
                    }
                    catch (Exception e) {
                        flag = false;
                    }
                }

                if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals("}")) {
                    locPos++;
                    pos = locPos;
                    return it;
                }
            }
            throw new Exception("Suntax grammar error. No `{` after `interface` in `InterfaceType`");
        }
        throw new Exception("Syntax grammar error. No `interface` in `InterfaceType`");
    }

    // MethodSpec   = MethodName Signature | InterfaceTypeName .
    private MethodSpec methodSpec(int locPos) throws Exception {
        MethodSpec ms = new MethodSpec();

        int tempLoc = locPos;
        try {
            MethodName mn = null;
            mn = methodName(tempLoc);

            if (mn != null) {
                tempLoc = pos;

                Signature s = null;
                s = signature(tempLoc);

                if (s != null) {
                    tempLoc = pos;
                    ms.setMethodName(mn);
                    ms.setSignature(s);

                    return ms;
                }
                throw new Exception("Syntax grammar error. No `Signature` after `MethodName` in `MethodSpec`");
            }
            throw new Exception("Syntax grammar error. No `MethodName` in `MethodSpec`");

        } catch (Exception e) {}

        InterfaceTypeName itn = null;
        itn = interfaceTypeName(locPos);

        if (itn != null) {
            locPos = pos;

            ms.setInterfaceTypeName(itn);
            return ms;
        }
        throw new Exception("Syntax grammar error. No `InterfaceTypeName` in `MethodSpec`");
    }

    // InterfaceTypeName  = TypeName .
    private InterfaceTypeName interfaceTypeName(int locPos) throws Exception {
        InterfaceTypeName itn = new InterfaceTypeName();

        TypeName tn = null;
        tn = typeName(locPos);

        if (tn != null) {
            locPos = pos;
            itn.setTypeName(tn);
            return itn;
        }
        throw new Exception("Syntax grammar error. No `TypeName` in `InterfaceTypeName`");
    }

    // MethodName   = identifier .
    private MethodName methodName(int locPos) throws Exception {
        MethodName mn = new MethodName();

        if (tokens.get(locPos).getType().equals(TypeOfToken.Identifier)) {
            mn.setIdentifier(tokens.get(locPos).getLexeme());
            locPos++;

            pos = locPos;
            return mn;
        }
        throw new Exception("Syntax grammar error. No `identifier` in `MethodName`");
    }

    // SliceType = "[" "]" ElementType .
    private SliceType sliceType(int locPos) throws Exception {
        SliceType st = new SliceType();

        if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals("[")) {
            locPos++;

            if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals("]")) {
                locPos++;

                ElementType et = null;
                et = elementType(locPos);

                if (et != null) {
                    locPos = pos;
                    st.setElementType(et);
                    return st;
                }
                throw new Exception("Syntax grammar error. No `ElementType` in `SliceType`");
            }
            throw new Exception("Syntax grammar error. No `]` after `[` in `SliceType`");
        }
        throw new Exception("Syntax grammar error. No `[` in `SliceType`");
    }

    // MapType  = "map" "[" KeyType "]" ElementType .
    private MapType mapType(int locPos) throws Exception {
        MapType mt = new MapType();

        if (tokens.get(locPos).getType().equals(TypeOfToken.Keyword) && tokens.get(locPos).getLexeme().equals("map")) {
            locPos++;

            if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals("[")) {
                locPos++;

                KeyType kt = null;
                kt = keyType(locPos);

                if (kt != null) {
                    locPos = pos;
                    mt.setKeyType(kt);

                    if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals("]")) {
                        locPos++;

                        ElementType et = null;
                        et = elementType(locPos);
                        if (et != null) {
                            locPos = pos;
                            mt.setElementType(et);
                            return mt;
                        }
                        throw new Exception("Syntax grammar error. No `ElementType` in `MapType`");
                    }
                    throw new Exception("Syntax grammar error. No `]` after `[` `KryType` in `MapType`");
                }
                throw new Exception("Syntax grammar error. No `KeyType` in `MapType`");
            }
            throw new Exception("Syntax grammar error. No `[` after `map` in `MapType`");
        }
        throw new Exception("Syntax grammar error. No `map` in `MapType`");
    }

    // KeyType  = Type .
    private KeyType keyType(int locPos) throws Exception {
        KeyType kt = new KeyType();

        Type t = null;
        t = type(locPos);
        if (t != null) {
            locPos = pos;
            kt.setType_(t);
            return kt;
        }
        throw new Exception("Syntax grammar error. No `Type` in `KeyType`");
    }

    // PointerType = "*" BaseType .
    private PointerType pointerType(int locPos) throws Exception {
        PointerType pt = new PointerType();

        if (tokens.get(locPos).getType().equals(TypeOfToken.Operator) && tokens.get(locPos).getLexeme().equals("*")) {
            locPos++;

            BaseType bs = null;
            bs = baseType(locPos);

            if (bs != null) {
                locPos = pos;
                pt.setBaseType(bs);

                return pt;
            }
            throw new Exception("Syntax grammar error. No `BaseType` after `*` in `PointerType`");
        }
        throw new Exception("Syntax grammar error. No `*` in `PointerType`");
    }

    // BaseType  = Type .
    private BaseType baseType(int locPos) throws Exception {
        BaseType bt = new BaseType();

        Type t = type(locPos);
        if (t != null) {
            locPos = pos;
            bt.setType_(t);
            return bt;
        }
        throw new Exception("Syntax gramar error. No `Type` in `BaseType`");
    }

    // StructType  = "struct" "{" { FieldDecl ";" } "}" .
    private StructType structType(int locPos) throws Exception {
        StructType st = new StructType();

        if (tokens.get(locPos).getType().equals(TypeOfToken.Keyword) && tokens.get(locPos).getLexeme().equals("struct")) {
            locPos++;

            if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals("{")) {
                locPos++;

                boolean flag = true;
                while (flag) {
                    FieldDecl fd = null;
                    try {
                        fd = fieldDecl(locPos);
                    }
                    catch (Exception e) {
                        flag = false;
                    }
                    finally {
                        if (fd != null) {
                            locPos = pos;
                            st.addFieldDecl(fd);

                            if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals(";")) {
                                locPos++;
                            }
                            else
                                throw new Exception("Syntax grammar error. No `;` after `FieldDecl` in `StructType`");
                        }
                    }
                }

                if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals("}")) {
                    locPos++;

                    pos = locPos;
                    return st;
                }
                throw new Exception("Syntax grammar error. No `}` after `{` { `FieldDecl` } in `StructType`");
            }
            throw new Exception("Syntax grammar error. No `{` after `struct` in `StructType`");
        }
        throw new Exception("Syntax grammar error. No `struct` in `StructType");
    }

    // FieldDecl  = (IdentifierList Type | EmbeddedField) [ Tag ] .
    private FieldDecl fieldDecl(int locPos) throws Exception {
        FieldDecl fd = new FieldDecl();

        boolean flag = true;
        IdentifierList il = null;
        try {
            il = identifierList(locPos);
        } catch (Exception e) {}
        finally {
            if (il != null) {
                flag = false;
                locPos = pos;
                fd.setIdentifierList(il);

                Type t = null;
                t = type(locPos);
                if (t != null) {
                    locPos = pos;
                    fd.setType_(t);
                }
                else {
                    throw new Exception("Syntax grammar error. No `Type` after `IdentifierList` in `FieldDecl`");
                }
            }
            else {
                throw new Exception("Syntax grammar error. No `IdentifierList` in `FieldDecl`");
            }
        }

        if (flag) {
            EmbeddedField ef = null;
            ef = embeddedField(locPos);

            if (ef != null) {
                locPos = pos;
                fd.setEmbeddedField(ef);
            }
            else {
                throw new Exception("Syntax grammar error. No `EmbeddedField` in `FieldDecl`");
            }
        }

        try {
            Tag t = null;
            t = tag(locPos);

            if (t != null) {
                fd.setTag(t);
            }
        }
        catch (Exception e) {}

        pos = locPos;
        return fd;
    }

    // Tag  = string_lit .
    private Tag tag(int locPos) throws Exception {
        Tag t = new Tag();

        if (tokens.get(locPos).getType().equals(TypeOfToken.StringLiteral)) {
            t.setString_lit(tokens.get(locPos).getLexeme());
            locPos++;

            pos = locPos;
            return t;
        }
        throw new Exception("Syntax grammar error. No `string_lit` in `Tag`");
    }

    // EmbeddedField = [ "*" ] TypeName .
    private EmbeddedField embeddedField(int locPos) throws Exception {
        EmbeddedField ef = new EmbeddedField();

        try {
            if (tokens.get(locPos).getType().equals(TypeOfToken.Operator) && tokens.get(locPos).getLexeme().equals("*")) {
                locPos++;
            }
            else
                throw new Exception("Syntax grammar error. No `*` in `EmbeddedField`");
        } catch (Exception e) {}

        TypeName tn = null;
        tn = typeName(locPos);

        if (tn != null) {
            locPos = pos;
            ef.setTypeName(tn);

            return ef;
        }
        throw new Exception("Syntax grammar error. No `TypeName` in `EmbeddedField`");
    }

    // ArrayType   = "[" ArrayLength "]" ElementType .
    private ArrayType arrayType(int locPos) throws Exception {
        ArrayType at = new ArrayType();

        if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals("[")) {
            locPos++;

            ArrayLength al = arrayLength(locPos);
            if (al != null) {
                locPos = pos;
                at.setArrayLength(al);

                if (tokens.get(locPos).getType().equals(TypeOfToken.Punctuation) && tokens.get(locPos).getLexeme().equals("]")) {
                    locPos++;

                    ElementType et = elementType(locPos);
                    if (et != null) {
                        at.setElementType(et);
                        locPos = pos;

                        return at;
                    }
                    throw new Exception("Syntax grammar error. No `ElementType` in `ArrayType`");
                }
                throw new Exception("Syntax grammar error. No `]` after `[` `ArrayLength` in `ArrayType`");
            }
            throw new Exception("Syntax grammar error. No `ArrayLength` after `[` in `ArrayType`");
        }
        throw new Exception("Syntax grammar error. No `[` in `ArrayType`");
    }

    // ElementType = Type .
    private ElementType elementType(int locPos) throws Exception {
        ElementType et = new ElementType();

        Type t = type(locPos);
        if (t != null) {
            locPos = pos;
            et.setType_(t);
            return et;
        }
        throw new Exception("Syntax gramar error. No `Type` in `ElementType`");
    }

    // ArrayLength = Expression .
    private ArrayLength arrayLength(int locPos) throws Exception {
        ArrayLength al = new ArrayLength();

        Expression e = expression(locPos);
        if (e != null) {
            locPos = pos;
            al.setExpression(e);

            return al;
        }
        throw new Exception("Syntax grammar error. No `Expression` in `ArrayLength`");
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

    // Expression = UnaryExpr | Expression binary_op Expression .
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
                return ex;
            }
        }

        try{
            Expression exp = null;
            exp = expression(locPos);
            if (exp != null){
                ex.setExpression(exp);
                locPos = pos;

                Binary_op bo = null;
                bo = binary_op(locPos);
                if (bo != null) {
                    ex.setBinary_op(bo);
                    locPos = pos;

                    exp = null;
                    exp = expression(locPos);
                    if (exp != null){
                        ex.setExpression(exp);
                        locPos = pos;
                        return ex;
                    }
                    throw new Exception("Syntax grammar error. No `Expression` after `Expression binary_op` in `Expression`");
                }
                throw new Exception("Syntax grammar error. No `binary_op` in `Expression binary_op Expression` in `Expression`");
            }
            throw new Exception("Syntax grammar error. No `Expression` before `binary_op Expression` in `Expression`");
        }catch (Exception e){
        }
        throw new Exception("Syntax grammar error. No `UnaryExpr` nor `Expression binary_op Expression` in `Expression`");
    }

    // binary_op  = "||" | "&&" | rel_op | add_op | mul_op .
    private Binary_op binary_op(int locPos) throws Exception{
        return null;
    }

    // UnaryExpr  = PrimaryExpr | unary_op UnaryExpr .
    private UnaryExpr unaryExpr(int locPos) throws Exception {
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
