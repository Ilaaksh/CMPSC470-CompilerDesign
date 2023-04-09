public class Parser
{
    public static final int PRINT       = 10; // "print"
    public static final int FUNC        = 11; // "func"
    public static final int VAR         = 12; // "var"
    public static final int VOID        = 13; // "void"
    public static final int BOOL        = 14; // "bool"
    public static final int INT         = 15; // "int"
    public static final int FLOAT       = 16; // "float"
    public static final int STRUCT      = 17; // "struct"
    public static final int SIZE        = 18; // "size"
    public static final int NEW         = 19; // "new"
    public static final int IF          = 20; // "if"
    public static final int ELSE        = 22; // "else"
    public static final int BEGIN       = 23; // "{"
    public static final int END         = 24; // "}"
    public static final int WHILE       = 25; // "while"
    public static final int RETURN      = 26; // "return"
    public static final int BREAK       = 27; // "break"
    public static final int CONTINUE    = 28; // "continue"
    public static final int LPAREN      = 29; // "("
    public static final int RPAREN      = 30; // ")"
    public static final int LBRACKET    = 31; // "["
    public static final int RBRACKET    = 32; // "]"
    public static final int SEMI        = 33; // ";"
    public static final int COMMA       = 34; // ","
    public static final int DOT         = 35; // "."
    public static final int ADDR        = 36; // "&"
    public static final int ASSIGN      = 38; // "<-"
    public static final int FUNCRET     = 39; // "->"
    public static final int OP          = 40; // "+", "-", "*", "/", "and", "or", "not"
    public static final int RELOP       = 41; // "=", "!=", "<", ">", "<=", ">="
    public static final int BOOL_LIT    = 42; // "true", "false"
    public static final int INT_LIT     = 43; // {int}
    public static final int FLOAT_LIT   = 44; // {float}
    public static final int IDENT       = 45; // {identifier}

    public Parser(java.io.Reader r, Compiler compiler) throws Exception
    {
        this.compiler = compiler;
        this.lexer    = new Lexer(r, this);
    }

    Lexer            lexer;
    Compiler         compiler;
    public ParserVal yylval;

    public int yyparse() throws java.io.IOException
    {
        while ( true )
        {
            int token = lexer.yylex();
            String tokenName = "";
            Object attr = yylval.obj;
            if (token ==23){
                tokenName= lexer.yytext();
                System.out.print("<"+"BEGIN"+", "+lexer.lineno+":"+ lexer.column+">");
            }

            if (token ==24){
                tokenName= lexer.yytext();
                System.out.print("<"+"END"+", "+lexer.lineno+":"+ lexer.column+">");
            }
            if (token ==39){
                tokenName= lexer.yytext();
                System.out.print("<"+"FUNCRET"+", "+lexer.lineno+":"+ lexer.column+">");
            }
            if (token ==12){
                tokenName= lexer.yytext();
                System.out.print("<"+"VAR"+", "+lexer.lineno+":"+ lexer.column+">");
            }
            if (token ==27){

                System.out.print("<"+"BREAK"+", "+lexer.lineno+":"+ lexer.column+">");
            }
            if (token ==26){
                tokenName= lexer.yytext();
                System.out.print("<"+"RETURN"+", "+lexer.lineno+":"+ lexer.column+">");
            }
            if (token ==13){
                tokenName= lexer.yytext();
                System.out.print("<"+"VOID"+", "+lexer.lineno+":"+ lexer.column+">");
            }
            if (token ==25){
                tokenName= lexer.yytext();
                System.out.print("<"+"WHILE"+", "+lexer.lineno+":"+ lexer.column+">");
            }
            if (token ==34){
                tokenName= lexer.yytext();
                System.out.print("<"+"COMMA"+", "+lexer.lineno+":"+ lexer.column+">");
            }
            if (token ==14){
                tokenName= lexer.yytext();
                System.out.print("<"+"BOOL"+", "+lexer.lineno+":"+ lexer.column+">");
            }
            if (token ==33){
                tokenName= lexer.yytext();
                System.out.print("<"+"SEMI"+", "+lexer.lineno+":"+ lexer.column+">");
            }
            if (token ==22){
                tokenName= lexer.yytext();
                System.out.print("<"+"ELSE"+", "+lexer.lineno+":"+ lexer.column+">");
            }
            if (token ==36){
                tokenName= lexer.yytext();
                System.out.print("<"+"ADDR"+", "+lexer.lineno+":"+ lexer.column+">");
            }
            if (token ==30){
                tokenName= lexer.yytext();
                System.out.print("<"+"RPAREN"+", "+lexer.lineno+":"+ lexer.column+">");
            }
            if (token ==35){
                tokenName= lexer.yytext();
                System.out.print("<"+"DOT"+", "+lexer.lineno+":"+ lexer.column+">");
            }
            if (token ==38){
                tokenName= lexer.yytext();
                System.out.print("<"+"ASSIGN"+", "+lexer.lineno+":"+ lexer.column+">");
            }
            if (token ==28){
                tokenName= lexer.yytext();
                System.out.print("<"+"CONTINUE"+", "+lexer.lineno+":"+ lexer.column+">");
            }
            if (token ==29){
                tokenName= lexer.yytext();
                System.out.print("<"+"LPAREN"+", "+lexer.lineno+":"+ lexer.column+">");
            }
            if (token ==10){
                tokenName= lexer.yytext();
                System.out.print("<"+"PRINT"+", "+lexer.lineno+":"+ lexer.column+">");
            }
            if (token ==11){
                tokenName= lexer.yytext();
                System.out.print("<"+"FUNC"+", "+lexer.lineno+":"+lexer.column+">");
            }
            if (token ==19){
                tokenName= lexer.yytext();
                System.out.print("<"+"NEW"+", "+lexer.lineno+":"+lexer.column+">");
            }
            if (token ==15){
                tokenName= lexer.yytext();
                System.out.print("<"+"INT"+", "+lexer.lineno+":"+ lexer.column+">");
            }
            if (token ==17){
                tokenName= lexer.yytext();
                System.out.print("<"+"STRUCT"+", "+lexer.lineno+":"+ lexer.column+">");
            }
            if (token ==31){
                tokenName= lexer.yytext();
                System.out.print("<"+"LBRACKET"+", "+lexer.lineno+":"+ lexer.column+">");
            }
            if (token ==32){
                tokenName= lexer.yytext();
                System.out.print("<"+"RBRACKET"+", "+lexer.lineno+":"+ lexer.column+">");
            }
            if (token ==18){
                tokenName= lexer.yytext();
                System.out.print("<"+"SIZE"+", "+lexer.lineno+":"+ lexer.column+">");
            }
            if (token ==20){
                tokenName= lexer.yytext();
                System.out.print("<"+"IF"+", "+lexer.lineno+":"+ lexer.column+">");
            }
            if (token ==43){
                tokenName= lexer.yytext();
                System.out.print("<"+"INT_VALUE"+", "+attr+", "+lexer.lineno+":"+ lexer.column+">");
            }
            if (token==40)
            {
                System.out.print("<"+"OP"+","+"attr"+":\""+attr+"\","+lexer.lineno+":"+ lexer.column+">");
            }
            if (token==41)
            {
                System.out.print("<"+"RELOP"+","+"attr"+":\""+attr+"\","+lexer.lineno+":"+ lexer.column+">");
            }
            if (token==44)
            {
                System.out.print("<"+"FLOAT_VALUE"+","+attr+", "+lexer.lineno+":"+lexer.column+">");
            }
            if (token==16)
            {
                System.out.print("<"+"FLOAT"+", "+lexer.lineno+":"+lexer.column+">");
            }
            if (token==45)
            {
                System.out.print("<"+"ID"+",attr:sym-id:"+lexer.symbolTable.get(attr.toString())+", "+lexer.lineno+":"+lexer.column+">");
            }
            if (token==42)
            {
                System.out.print("<"+"BOOL_VALUE"+","+attr+", "+lexer.lineno+":"+lexer.column+">");
            }
            if(token == 0)
            {
                // EOF is reached
                return 0;
            }
            if(token == -1)
            {
                // error
                return -1;
            }
            lexer.column+= yylval.obj.toString().length();
        }
    }
}
