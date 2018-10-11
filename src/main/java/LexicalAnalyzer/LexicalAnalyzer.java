package LexicalAnalyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rakZeee on 15.09.2018.
 * This is heart of the program - lexcial tokenizer
 * It can take input file and give tokens one by one (using nextToken() method)
 */
public class LexicalAnalyzer {
    // Tokens
    public enum TypeOfToken {
        Identifier, Keyword, Punctuation, Operator, Literal, Number, Comment, WhiteSpace, EOF
    };

    public class Token {
        TypeOfToken type;
        String lexeme;

        public Token(TypeOfToken type, String lexeme) {
            this.type = type;
            this.lexeme = lexeme;
        }

        @Override
        public String toString() {
            return type.toString() + ": " + lexeme;
        }
    }

    // position of current char in the file
    private int pos;
    // length of input file
    private int len;
    private StringBuilder line;
    // regexp for identifier, decimal digit, hexadecimal number, white spaces (including \n)
    private String identRegexp = "[\\p{L}_][\\p{L}\\p{Nd}_]*";
    private String decimalRegexp = "\\d";
    private String hexDecimalDigitRegexp = "[\\da-fA-F]+";
    private String spaceRegexp = "\\s+";
    // Array  of all keywords in Golang
    private ArrayList<String> keywords = new ArrayList<String>(Arrays.asList("break", "default", "func", "interface",
            "select", "case", "defer", "goto", "go", "map", "struct", "chan", "else", "package", "switch",
            "const", "fallthrough", "if", "range", "type", "continue", "for", "import", "return", "var"));
    // Array of all punctuation symbols in Golang
    private ArrayList<String> punctuation = new ArrayList<String>(Arrays.asList(",", ";", "...", ".", ":", ")", "(",
            "{", "}", "[", "]"));
    // Array of all operators in Golang
    private ArrayList<String> operators = new ArrayList<String>(Arrays.asList( "<<=", "++", ":=", "%=", ">>=", ">>", "--",
            "&^=", "&^", "+=", "&=", "&&", "==", "!=","<<", "/=", "<=", "-=", "|=", "||", ">=", "*=", "^=", "<-", "=", "%",
            "+", "&",  "-", "|", "<", "*", "^", ">", "/", "!"));

    /**
     * Constrictor of the class
     * sets current position to 0
     * sets input file length
     * @param line - input file
     */
    public LexicalAnalyzer(StringBuilder line) {
        this.pos = 0;
        this.len = line.length();
        this.line = line;
    }

    // Gets next token
    public Token nextToken() throws Exception {
        // If reached end of the file - return EOF

        if (pos >= len) return new Token(TypeOfToken.EOF, "");

        // Else - try parsing each token
        Token result = null;
        result = parseSpaces();
        if (result != null) return result;

        result = parseComment();
        if (result != null) return result;

        result = parseKeywords();
        if (result != null) return result;

        result = parseIdentifier();
        if (result != null) return result;

        result = parseLiteral();
        if (result != null) return result;

        result = parseOperator();
        if (result != null) return result;

        result = parsePunctuation();
        if (result != null) return result;

        throw new Exception("Syntax error");
    }

    // Parse number token
    private Token parseNumber(){
        int locPos = pos;
        // If hexadecimal
        if (locPos+1 < len && line.charAt(locPos) == '0' && Character.toLowerCase(line.charAt(locPos+1)) == 'x'){
            String str = String.valueOf(line.charAt(locPos)) + String.valueOf(line.charAt(locPos + 1));

            locPos = locPos + 2;
            Token numb = parseWithRegexp(hexDecimalDigitRegexp, TypeOfToken.Number, locPos);
            if (numb == null)
                return null;
            return new Token(numb.type, str + numb.lexeme);
        }else{
            boolean flag = false;
            StringBuilder str = new StringBuilder();
            // skip all decimals
            while (locPos < len && checkWithRegExp(String.valueOf(line.charAt(locPos)), decimalRegexp)){
                str.append(line.charAt(locPos));
                locPos++;
                flag = true;
            }
            // if meet '.' - skip all decimal numbers after it
            // TODO check if we can obtain `number.enumber` (1.e2) or not
            if (locPos < len && line.charAt(locPos) == '.'){
                str.append(line.charAt(locPos));
                locPos++;
                while (locPos < len && checkWithRegExp(String.valueOf(line.charAt(locPos)), decimalRegexp)){
                    str.append(line.charAt(locPos));
                    locPos++;
                    flag = true;
                }
            }
            if (flag) {
                // if got any numbers before - try passing exponential part
                if (locPos < len && Character.toLowerCase(line.charAt(locPos)) == 'e'){
                    str.append(line.charAt(locPos));
                    locPos++;
                    // try parse '+' or '-'
                    if (locPos < len && (line.charAt(locPos) == '+' || line.charAt(locPos) == '-')){
                        str.append(line.charAt(locPos));
                        locPos++;
                    }
                    // skip decimal numbers
                    while (locPos < len && checkWithRegExp(String.valueOf(line.charAt(locPos)), decimalRegexp)){
                        str.append(line.charAt(locPos));
                        locPos++;
                    }
                }
                // imaginary
                if (locPos < len && line.charAt(locPos) == 'i') {
                    str.append(line.charAt(locPos));
                    locPos++;
                }
                pos = locPos;
                // if success - return token
                return new Token(TypeOfToken.Number, str.toString());
            }else{
                // if didn't find - return null
                return null;
            }
        }
    }

    // Try parse punctuation characters
    private Token parsePunctuation(){
        return parseInList(punctuation, TypeOfToken.Punctuation);
    }
    // Try parse operators
    private Token parseOperator(){
        return parseInList(operators, TypeOfToken.Operator);
    }
    // Try parse identifiers
    private Token parseIdentifier(){
        return parseWithRegexp(identRegexp, TypeOfToken.Identifier, pos);
    }
    // Try parse keywords
    private Token parseKeywords(){
        return parseInList(keywords, TypeOfToken.Keyword);
    }
    // Try parse whitespaces
    private Token parseSpaces(){
        return parseWithRegexp(spaceRegexp, TypeOfToken.WhiteSpace, pos);
    }
    // Try parse literals
    private Token parseLiteral(){
        int locPos = pos;
        if (locPos < len && (Character.isDigit(line.charAt(locPos)) || line.charAt(locPos) == '.')){
            // if digit or '.' - try parse number
            return parseNumber();
        }else
            // else string/char literals
        if (locPos < len && line.charAt(locPos) == '"'){
            StringBuilder str = new StringBuilder();
            str.append(line.charAt(locPos));
            locPos++;
            while (true){
                // skip symbols until we find string closing symbol
                if (locPos<len && line.charAt(locPos) == '"' && line.charAt(locPos-1) != '\\') {
                    str.append(line.charAt(locPos));
                    locPos++;
                    pos = locPos;
                    return new Token(TypeOfToken.Literal, str.toString());
                }
                // TODO check this logical expression. Is it real to end String literal without ending quote?
                if (locPos<len && line.charAt(locPos) == '\n')
                {
                    str.append(line.charAt(locPos));
                    locPos++;
                    pos = locPos;
                    return new Token(TypeOfToken.Literal, str.toString());
                }
                str.append(locPos);
                locPos++;
            }
        }else if (locPos<len && line.charAt(locPos) == '\''){
            StringBuilder str = new StringBuilder();
            str.append(line.charAt(locPos));
            locPos++;
            // skip until we reach char closing symbol
            while (true){
                if (locPos<len && line.charAt(locPos) == '\'') {
                    str.append(line.charAt(locPos));
                    locPos++;
                    pos = locPos;
                    return new Token(TypeOfToken.Literal, str.toString());
                }
                // TODO check this logical expression. Is it real to end String literal without ending quote?
                if (locPos<len && line.charAt(locPos) == '\n')
                {
                    str.append(line.charAt(locPos));
                    locPos++;
                    pos = locPos;
                    return new Token(TypeOfToken.Literal, str.toString());
                }
                str.append(line.charAt(locPos));
                locPos++;
            }
        }else if (locPos<len && line.charAt(locPos) == '`'){
            StringBuilder str = new StringBuilder();
            str.append(line.charAt(locPos));
            locPos++;
            // skip characters until we find closing symbol
            while (true){
                if (locPos<len && line.charAt(locPos) == '`') {
                    str.append(line.charAt(locPos));
                    locPos++;
                    pos = locPos;
                    return new Token(TypeOfToken.Literal, str.toString());
                }
                str.append(line.charAt(locPos));
                locPos++;
            }
        }
        return null;
    }
    // Try parse comment
    private Token parseComment(){
        int locPos = pos;
        StringBuilder str = new StringBuilder();
        if (locPos+1 < len){
            if (line.charAt(locPos) == '/') {
                str.append(line.charAt(locPos));
                if (line.charAt(locPos + 1) == '/') {
                    // If meet line comment
                    str.append(line.charAt(locPos+1));
                    locPos = locPos + 2;

                    // Skip characters until end of the line character reached
                    while (true) {
                        if (locPos >= len){
                            pos = locPos;
                            return new Token(TypeOfToken.Comment, str.toString());
                        }
                        if (locPos < len && line.charAt(locPos) == '\n'){
                            //locPos++;
                            pos = locPos;
                            return new Token(TypeOfToken.Comment, str.toString());
                        }
                        str.append(line.charAt(locPos));
                        locPos++;
                    }
                }else if (line.charAt(locPos + 1) == '*'){
                    // if multiline comment
                    str.append(line.charAt(locPos));
                    locPos = locPos + 2;
                    // Skip until meet EOF or multiline comment closing symbols
                    while (true) {
                        if (locPos >= len){
                            pos = locPos;
                            return new Token(TypeOfToken.Comment, str.toString());
                        }
                        if (line.charAt(locPos) == '*' && line.charAt(locPos+1) == '/'){
                            str.append(line.charAt(locPos));
                            str.append(line.charAt(locPos+1));
                            locPos = locPos + 2;
                            pos = locPos;
                            return new Token(TypeOfToken.Comment, str.toString());
                        }
                        str.append(line.charAt(locPos));
                        locPos++;
                    }
                }else{
                    return null;
                }
            }else{
                return null;
            }
        }
        return null;
    }

    // Parse using regexp pattern and return certain token
    private Token parseWithRegexp(String pattern, TypeOfToken token, int position){
        int locPos = position;
        StringBuilder str = new StringBuilder();
        boolean found = false;
        while (locPos < len) {
            // Each time append to string builder and check if string matches to pattern
            str.append(line.charAt(locPos));
            // match with pattern
            if (checkWithRegExp(str.toString(), pattern)) {
                locPos++;
                found = true;
            } else {
                break;
            }
        }
        // If found at least one symbol that matched to the pattern - return token
        if (found) { pos = locPos; return new Token(token, str.deleteCharAt(str.length()-1).toString()); }
        // Else return null
        else return null;
    }

    // Parse using some ArrayList
    private Token parseInList(ArrayList<String> list, TypeOfToken token){
        int locPos = pos;
        // Iterate in list elements
        for (String element : list){
            // If we have enough characters in file - try match with current element
            if (locPos+element.length() <= len && line.substring(locPos, locPos+element.length()).equals(element)){
                // Check char after keyword to be sure keyword not an identifier
                if (token != TypeOfToken.Keyword || locPos+element.length() == len || (
                        line.charAt(locPos+element.length())!='_' &&
                        !Character.isDigit(line.charAt(locPos+element.length())) &&
                        !Character.isLetter(line.charAt(locPos+element.length())))) {
                    locPos = locPos + element.length();
                    pos = locPos;
                    return new Token(token, element);
                }
            }
        }
        // If no element matched - return null
        return null;
    }

    // Method that matches string 's' with some regexp pattern
    private static boolean checkWithRegExp(String s, String pattern){
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(s);
        return m.matches();
    }
}
