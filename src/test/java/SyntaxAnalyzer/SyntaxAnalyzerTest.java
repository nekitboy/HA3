package SyntaxAnalyzer;

import static org.junit.jupiter.api.Assertions.*;

class SyntaxAnalyzerTest {

    @org.junit.jupiter.api.Test
    void getSyntaxTree1() throws Exception {
        StringBuilder code = new StringBuilder("package main;");
        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(code);
        String ans = syntaxAnalyzer.getSyntaxTree().toJSON().toString();
        String expectedAns = "{\n" +
                "  PackageClause: {\n" +
                "    PackageName: main\n" +
                "  }\n" +
                "}";
        assertEquals(expectedAns, ans);
    }

    @org.junit.jupiter.api.Test
    void getSyntaxTree2() throws Exception {
        StringBuilder code = new StringBuilder("packae main");
        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(code);

        assertThrows(Exception.class, () -> syntaxAnalyzer.getSyntaxTree());
    }

    @org.junit.jupiter.api.Test
    void getSyntaxTree3() throws Exception {
        StringBuilder code = new StringBuilder("package main;\n" +
                "\n" +
                "import (\n" +
                "        \"bufio\";\n" +
                "    \"fmt\";\n" +
                "    \"io/ioutil\";\n" +
                "    \"os\";\n" +
                ");");
        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(code);
        String ans = syntaxAnalyzer.getSyntaxTree().toJSON().toString();
        String expectedAns = "{\n" +
                "  PackageClause: {\n" +
                "    PackageName: main\n" +
                "  },\n" +
                "  ImportDecl: {\n" +
                "    ImportSpec: [\n" +
                "      {\n" +
                "        ImportPath: \"bufio\"\n" +
                "      },\n" +
                "      {\n" +
                "        ImportPath: \"fmt\"\n" +
                "      },\n" +
                "      {\n" +
                "        ImportPath: \"io/ioutil\"\n" +
                "      },\n" +
                "      {\n" +
                "        ImportPath: \"os\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        assertEquals(expectedAns, ans);
    }

    @org.junit.jupiter.api.Test
    void getSyntaxTree4() throws Exception {
        StringBuilder code = new StringBuilder("package main\n" +
                "import (identifier \"bufio\";)");
        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(code);
        String ans = syntaxAnalyzer.getSyntaxTree().toJSON().toString();
        String expectedAns = "{\n" +
                "  PackageClause: {\n" +
                "    PackageName: main\n" +
                "  },\n" +
                "  ImportDecl: {\n" +
                "    ImportSpec: {\n" +
                "      PackageName: identifier,\n" +
                "      ImportPath: \"bufio\"\n" +
                "    }\n" +
                "  }\n" +
                "}";
        assertEquals(expectedAns, ans);
    }

    @org.junit.jupiter.api.Test
    void getSyntaxTree5() throws Exception {
        StringBuilder code = new StringBuilder("package main;\n" +
                "const a, b, c, d");
        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(code);
        String ans = syntaxAnalyzer.getSyntaxTree().toJSON().toString();
        String expectedAns = "{\n" +
                "  PackageClause: {\n" +
                "    PackageName: main\n" +
                "  },\n" +
                "  TopLevelDecl: {\n" +
                "    Declaration: {\n" +
                "      ConstDecl: {\n" +
                "        ConstSpec: {\n" +
                "          IdentifierList: [\n" +
                "            a,\n" +
                "            b,\n" +
                "            c,\n" +
                "            d\n" +
                "          ]\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
        assertEquals(expectedAns, ans);
    }

}