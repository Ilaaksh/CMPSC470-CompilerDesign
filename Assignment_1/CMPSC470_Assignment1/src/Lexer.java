import java.io.IOException;
import java.util.*;
import java.lang.*;
public class Lexer
{
    Hashtable<String, String> symbolTable = new Hashtable<>();


    private static final char EOF      =  0;
    //need a retract method
    //need a

    private final Parser         yyparser;
    public int curr=0;// parent parser object
    private final java.io.Reader reader;   // input stream
    public int             lineno = 1;   // line number
    public int             column=0;   // column

    public int[] array2= new int[10];
    public int[] array3= new int[10];

    public int lex_begin=1;
    public int forward =0;

    public void buffer_fill() throws IOException {
        if (curr == 0) {
            int charss = 0;
            int index =0;
            while(charss!=-1 && index < array2.length){
                charss= reader.read();
                array2[index]=charss;
                index++;
            }

        }
        else{
            int charss = 0;
            int index =0;
            while(charss!=-1 && index < array3.length){
                charss= reader.read();
                array3[index]=charss;
                index++;
            }
        }
    }
    public void switchBuffer() throws IOException {
        if(curr==0){
            curr=1;
        }
        else {curr=0;}
        forward = 0;
        buffer_fill();
}
    public void InstallID(String InstallID){
        if (!symbolTable.containsKey(InstallID)) {

            symbolTable.put(InstallID, "ID");
        }
}
        public int getToken(String getToken){
        if (symbolTable.containsKey(getToken)){
            if(symbolTable.get(getToken).equals("PRINT")) return Parser.PRINT;
            if(symbolTable.get(getToken).equals("VAR"))return Parser.VAR;
            if(symbolTable.get(getToken).equals("FUNC"))return Parser.FUNC;
            if(symbolTable.get(getToken).equals("IF"))return Parser.IF;
            if(symbolTable.get(getToken).equals("ELSE")) return Parser.ELSE;
            if(symbolTable.get(getToken).equals("WHILE")) return Parser.WHILE;
            if(symbolTable.get(getToken).equals("VOID")) return Parser.VOID;
        }
        return Parser.ID;
    }
    public Lexer(java.io.Reader reader, Parser yyparser) throws Exception
    {

        this.reader   = reader;
        this.yyparser = yyparser;
        buffer_fill();

        symbolTable.put("print", "PRINT");
        symbolTable.put("var"," VAR");
        symbolTable.put("func", "FUNC");
        symbolTable.put("if"," IF");
        symbolTable.put("else", "ELSE");
        symbolTable.put("while"," WHILE");
        symbolTable.put("void", "VOID");
    }

    public char NextChar() throws Exception
    {
        if (curr==0) {
            int data = array2[forward];
            forward++;
            if (data == -1) {
                return EOF;
            }
            return (char) data;
        } else {
            int data = array3[forward];
            forward++;
            if (data == -1) {
                return EOF;
            }
            return (char) data;

        }
    }

    public void retratct(){
        forward--;

}
    public int Fail()
    {
        return -1;
    }

    // * If yylex reach to the end of file, return  0
    // * If there is a lexical error found, return -1
    // * If a proper lexeme is determined, return token <token-id, token-attribute> as follows:
    //   1. set token-attribute into yyparser.yylval
    //   2. return token-id defined in Parser
    //   token attribute can be lexeme, line number, colume, etc.


    public int yylex() throws Exception {
        StringBuilder nums = new StringBuilder();
        StringBuilder chars = new StringBuilder();
        int state = 0;


        while (true) {
            if(curr==0){
                if(forward==array2.length)
                {switchBuffer();}
            }
            else {
                if(forward==array3.length)
                {switchBuffer();}
            }
            char c;
            switch (state) {
                case 0:
                    c = NextChar();

                    if (c == '*') {

                        state = 2;
                        continue;
                    }
                    if (c == '+') {
                        state = 3;
                        continue;
                    }

                    if (c == '/') {
                        state = 5;
                        continue;
                    }
                    if (c == '=') {
                        state = 6;

                        continue;
                    }
                    if (c == '>') {
                        state = 7;
                        continue;
                    }
                    if (c == '<') {
                        state = 22;
                        continue;
                    }
                    if (c == '!') {
                        state = 8;
                        continue;
                    }
                    if (c == '-') {
                        state = 9;
                        continue;
                    }

                    if (c == '(') {
                        state = 11;
                        continue;
                    }
                    if (c == ')') {
                        state = 12;
                        continue;
                    }
                    if (c == ';') {
                        state = 13;
                        continue;
                    }
                    if (c == ',') {
                        state = 14;
                        continue;
                    }
                    if (c == '{') {
                        state = 16;
                        continue;
                    }
                    if (c == '}') {
                        state = 17;
                        continue;
                    }
                    if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                        state = 18;
                        chars.append(c);
                        continue;
                    }
                    if (c >= '0' && c <= '9') {
                        state = 15;
                        nums.append(c);
                        continue;
                    }
                    if (c=='\n'){
                        state=34;
                        continue;
                    }
                    if (c==' '||c=='\t'||c=='\r')
                    {
                        state=3400000;
                        continue;
                    }

                    if (c == EOF) {
                        state = 99;
                        continue;
                    } else {
                        return Fail();
                    }

                    // set token-attribute to yyparser.yylval
                case 2:
                    yyparser.yylval = new ParserVal((Object) "*");
                    return Parser.OP;
                case 3:
                    yyparser.yylval = new ParserVal((Object) "+");
                    return Parser.OP;
                case 5:
                    yyparser.yylval = new ParserVal((Object) "/");
                    return Parser.OP;
                case 6:
                    yyparser.yylval = new ParserVal((Object) "=");
                    return Parser.RELOP;
                case 7:
                    c=NextChar();
                    if (c=='=')
                    {
                        state=41;
                        continue;
                    }
                    yyparser.yylval = new ParserVal((Object) ">");   // set token-attribute to yyparser.yylval
                    return Parser.RELOP;
                case 41:
                    yyparser.yylval = new ParserVal((Object) ">=");   // set token-attribute to yyparser.yylval
                    return Parser.RELOP;

                case 34:
                    state=0;
                    lineno++;
                    continue;
                case 8:
                    c = NextChar();
                    if ((c == '=')) {
                        state = 32;
                        continue;
                    }
                    else {
                        return Fail();
                    }
                case 32:
                    yyparser.yylval = new ParserVal((Object) "!=");
                    return Parser.RELOP;
                case 9:
                    c = NextChar();
                    if (c == '>') {
                        state=30;
                        continue;
                    }
                    else {
                        retratct();
                    }
                    yyparser.yylval = new ParserVal((Object) "-");
                    return Parser.OP;
                case 30:
                    yyparser.yylval = new ParserVal((Object) "->");
                    return Parser.FUNCRET;
                case 22:
                    c = NextChar();
                    if (c == '-') {
                        state=31;
                        continue;
                    }
                    if(c=='='){
                        state=40;
                        continue;
                    }
                    else {
                        retratct();
                    }
                    yyparser.yylval = new ParserVal((Object) "<");
                    return Parser.OP;
                case 40:
                    yyparser.yylval = new ParserVal((Object) "<=");
                    return Parser.RELOP;
                case 31:
                    yyparser.yylval = new ParserVal((Object) "<-");
                    return Parser.ASSIGN;
                case 10:
                    c = NextChar();
                    if (c=='-') {
                        state = 9;
                        continue;
                    }
                    if (c=='<') {
                        state = 9;

                        continue;
                    }
                    if ((c == EOF)) {
                        state = 99;
                        continue;
                    } else {
                        retratct();
                    }
                    yyparser.yylval = new ParserVal(chars.toString());
                    return Parser.FUNCRET;
                case 11:
                    yyparser.yylval = new ParserVal((Object) "(");   // set token-attribute to yyparser.yylval
                    return Parser.LPAREN;
                case 12:
                    yyparser.yylval = new ParserVal((Object) ")");
                    return Parser.RPAREN;
                case 13:
                    yyparser.yylval = new ParserVal((Object) ";");   // set token-attribute to yyparser.yylval
                    return Parser.SEMI;
                case 14:
                    yyparser.yylval = new ParserVal((Object) ",");   // set token-attribute to yyparser.yylval
                    return Parser.COMMA;
                case 15:
                    c = NextChar();
                    if (c >= '0' && c <= '9') { nums.append(c); state = 15; continue; }
                    if (c == '.') { nums.append(c); state = 19; continue; }
                    else retratct();
                    yyparser.yylval = new ParserVal((Object) Integer.parseInt(nums.toString()));
                    return Parser.NUM;
                case 16:
                    yyparser.yylval = new ParserVal((Object) "{");   // set token-attribute to yyparser.yylval
                    return Parser.BEGIN;
                case 17:
                    yyparser.yylval = new ParserVal((Object) "}");   // set token-attribute to yyparser.yylval
                    return Parser.END;
                case 18:
                    c = NextChar();
                    if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                        state = 18;
                        chars.append(c);
                        continue;
                    }
                    if ((c >= '0' && c <= '9')) {
                        state = 18;
                        chars.append(c);
                        continue;
                    }
                    if ((c == '_')) {
                        state = 18;
                        chars.append(c);
                        continue;
                    } else {
                        retratct();
                    }
                    InstallID(chars.toString());
                    yyparser.yylval = new ParserVal(chars.toString());
                    return getToken(chars.toString());

                // set token-attribute to yyparser.yylval
                // return token-name
                case 19:
                    c= NextChar();
                    if(c>='0'&&c<='9')
                    {
                        state = 19;
                        nums.append(c);
                        continue;
                    }
                    if(c=='.')
                    {
                        return Fail();
                    } else retratct();
                    yyparser.yylval = new ParserVal((Object) Double.parseDouble(nums.toString()));
                    return Parser.NUM;
                case 20:
                    c=NextChar();
                    if(c>='0'&&c<='9'){
                        nums.append(c);
                        state=20;
                        continue;
                    }
                    if ((c == EOF)) {
                        state = 99;
                        continue;
                    } else {
                        retratct();
                    }
                    yyparser.yylval = new ParserVal((Object) Integer.parseInt(nums.toString()));
                    return Parser.NUM;
                case 99:
                    return EOF;
                case 3400000:
                    state=0;
                    continue;
            }


        }
    }}



