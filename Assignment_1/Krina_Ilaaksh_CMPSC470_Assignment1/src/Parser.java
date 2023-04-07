public class Parser
{
    public static final int OP         = 10;    // "+", "-", "*", "/"
    public static final int RELOP      = 11;    // "<", ">", "=", "!=", "<=", ">="
    public static final int TYPEOF     = 12;    // "::"
    public static final int ASSIGN     = 13;    // "<-"
    public static final int LPAREN     = 14;    // "("
    public static final int RPAREN     = 15;    // ")"
    public static final int SEMI       = 16;    // ";"
    public static final int COMMA      = 17;    // ","
    public static final int FUNCRET    = 18;    // "->"
    public static final int NUM        = 19;    // number
    public static final int ID         = 20;    // identifier // Ask for stating this(not declared dont know how to)
    public static final int BEGIN      = 21;    // "{"
    public static final int END        = 22;    // "}"
    public static final int INT        = 23;    // "int"
    public static final int PRINT      = 24;    // "print"
    public static final int VAR        = 25;    // "var" // problem when u run these terms ERROR in output(Var and void)
    public static final int FUNC       = 26;    // "func"
    public static final int IF         = 27;    // "if"
    public static final int ELSE       = 29;    // "else"
    public static final int WHILE      = 30;    // "while"
    public static final int VOID       = 31;    // "void"
    public static final int SPACE      = 32;    // ' '

    Compiler         compiler;
    Lexer            lexer;     // lexer.yylex() returns token-name
    public ParserVal yylval;    // yylval contains token-attribute

    public Parser(java.io.Reader r, Compiler compiler) throws Exception
    {
        this.compiler = compiler;
        this.lexer    = new Lexer(r, this);
    }

    public int yyparse() throws Exception
    {
        while ( true )
        {
            int token = lexer.yylex();  // get next token-name
            Object attr = yylval.obj;   // get      token-attribute
            String tokenname = "OP";

            if (token==10)
            {
                tokenname = "OP";
            }
            if (token==11)
            {
                tokenname = "RELOP";
            }

            if (token==12)
            {
                tokenname = "TYPEOF";
            }
            if (token==13)
            {
                tokenname = "ASSIGN";
            }
            if (token==14)
            {
                tokenname = "LPAREN";
            }
            if (token==15)
            {
                tokenname = "RPAREN";
            }
            if(token == 16)
            {
                tokenname = "SEMI";
            }

            if (token==17)
            {
                tokenname = "COMMA";
            }

            if (token==18)
            {
                tokenname = "FUNCRET";
            }
            if (token==19)
            {
                tokenname = "NUM";
            }
            if (token==20)
            {
                attr = yylval.sval;
                tokenname = "ID";
            }
            if (token==21)
            {
                tokenname = "BEGIN";
            }
            if(token == 22)
            {
                tokenname = "END";
            }
            if (token==23)
            {
                tokenname = "INT";
            }
            if (token==24)
            {
                attr = yylval.sval;
                tokenname = "PRINT";
            }
            if (token==25)
            {
                attr = yylval.sval;
                tokenname = "VAR";
            }
            if(token == 26)
            {
                attr = yylval.sval;
                tokenname = "FUNC";
            }
            if(token == 27)
            {
                attr = yylval.sval;
                tokenname = "IF";
            }
            if (token==29)
            {
                attr = yylval.sval;
                tokenname = "ELSE";
            }
            if (token==30)
            {
                attr = yylval.sval;
                tokenname = "WHILE";
            }
            if (token==31)
            {
                attr = yylval.sval;
                tokenname = "VOID";
            }
            if (token==32)
            {
                tokenname = "SPACE";
            }
            if(token == 0)
            {
                // EOF is reached
                System.out.println("Success!");
                return 0;
            }
            if(token == -1)
            {
                // lexical error is found
                System.out.println("Error! There is a lexical error at " + lexer.lineno + ":" + lexer.column + ".");
                return -1;
            }

            System.out.println("<" + tokenname + ", token-attr:\"" + attr + "\", " + lexer.lineno + ":" + lexer.column + ">");
        }
    }
}
