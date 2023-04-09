//Done by Krina Patel and Ilaaksh Mishra
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;


public class Parser {
    public static final int ENDMARKER = 0;
    public static final int LEXERROR = 1;
    public static final int CALL = 2;
    public static final int RETURN = 3;
    public static final int VAR= 4;
    public static final int IF= 5;
    public static final int ELSE= 31;
    public static final int WHILE= 6;
    public static final int PRINT= 7;
    public static final int SIZEOF= 8;
    public static final int ELEMOF= 32;
    public static final int LBRACKET= 9;
    public static final int RBRACKET= 30;
    public static final int BOOL= 10;
    public static final int INT = 11;
    public static final int PTR = 12;
    public static final int BEGIN = 13;
    public static final int END = 14;
    public static final int LPAREN = 15;
    public static final int RPAREN = 16;
    public static final int ASSIGN = 17;
    public static final int EXPROP = 18;
    public static final int TERMOP = 19;
    public static final int SEMI = 20;
    public static final int INT_LIT = 21;
    public static final int IDENT = 22;
    public static final int FUNCRET = 23;
    public static final int FUNC = 24;
    public static final int NEW = 25;
    public static final int RELOP = 26;
    public static final int COMMA = 27;
    public static final int DOT = 28;
    public static final int BOOL_LIT = 29;
    public class Token {
        public int type;
        public ParserVal attr;

        public Token(int type, ParserVal attr) {
            this.type = type;
            this.attr = attr;

        }
    }

    public ParserVal yylval;
    Token _token;
    Lexer _lexer;
    Compiler _compiler;
    public ParseTree.Program _parsetree;
    public String _errormsg;

    public Parser(java.io.Reader r, Compiler compiler) throws Exception {
        _compiler = compiler;
        _parsetree = null;
        _errormsg = null;
        _lexer = new Lexer(r, this);
        _token = null;                  // _token is initially null
        Advance();                          // make _token to point the first token by calling Advance()
        map.put(2, "call");
        map.put(3, "return");
        map.put(4, "var");
        map.put(5, "if");
        map.put(31, "else");
        map.put(6, "while");
        map.put(7, "print");
        map.put(8, "sizeof");
        map.put(32, "elemof");
        map.put(9, "[");
        map.put(30, "]");
        map.put(10, "bool");
        map.put(11, "int");
        map.put(12, "ptr");
        map.put(13, "{");
        map.put(14, "}");
        map.put(15, "(");
        map.put(16, ")");
        map.put(17, "<-");
        map.put(18, "exprop");
        map.put(19, "termop");
        map.put(20, ";");
        map.put(21, "int_lit");
        map.put(22, "ident");
        map.put(23, "->");
        map.put(24, "func");
        map.put(25, "new");
        map.put(26, "relop");
        map.put(27, ",");
        map.put(28, ".");
        map.put(29,"bool_lit");
    }
    public HashMap<Integer,String> map = new HashMap<>();

    public void Advance() throws Exception {
        int token_type = _lexer.yylex();                                    // get next/first token from lexer
        if (token_type == 0) _token = new Token(ENDMARKER, null);     // if  0 => token is endmarker
        else if (token_type == -1) _token = new Token(LEXERROR, yylval);   // if -1 => there is a lex error
        else _token = new Token(token_type, yylval);   // otherwise, set up _token
    }

    public ParserVal Match(int token_type) throws Exception {
        boolean match = (token_type == _token.type);
        ParserVal lexeme = yylval;


        // throw exception (indicating parsing error in this assignment)
        if (!match){
            throw new Exception(map.get(token_type) + " is expected instead of " + map.get(_token.type) + " " + (_lexer.lineno+1) + ":" + (_lexer.column+1) + ".");

        }
        if (_token.type != ENDMARKER)    // if token is not endmarker,
            Advance();


        return lexeme;
    }

    public int yyparse() throws Exception {
        try {
            _parsetree = program();
            return 0;
        } catch (Exception e) {
            _errormsg = e.getMessage();

            return -1;
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    //      program -> decl_list
    //    decl_list -> decl_list'
    //   decl_list' -> fun_decl decl_list'  |  eps
    //     fun_decl -> FUNC IDENT LPAREN params RPAREN FUNCRET prim_type BEGIN local_decls stmt_list END
    //    prim_type -> INT
    //       params -> eps
    //  local_decls -> local_decls'
    // local_decls' -> eps
    //    stmt_list -> stmt_list'
    //   stmt_list' -> eps
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    public ParseTree.Program program() throws Exception {
        switch (_token.type) {
            case FUNC:
            case ENDMARKER:
                List<ParseTree.FuncDecl> funcs = decl_list();
                return new ParseTree.Program(funcs);
        }
        throw new Exception("Incorrect program declaration at"+" " + (_lexer.lineno+1) + ":" + (_lexer.column+1) + ".");
    }

    public List<ParseTree.FuncDecl> decl_list() throws Exception {
        switch (_token.type) {
            case FUNC:
            case ENDMARKER:
                return decl_list_();
        }
        throw new Exception("Incorrect declaration list at"+" " +(_lexer.lineno+1) + ":" + (_lexer.column+1) + ".");
    }

    public List<ParseTree.FuncDecl> decl_list_() throws Exception {
        switch (_token.type) {
            case FUNC:
                ParseTree.FuncDecl v1 = fun_decl();
                List<ParseTree.FuncDecl> v2 = decl_list_();
                v2.add(0, v1);
                return v2;
            case ENDMARKER:
                return new ArrayList<>();
        }
        throw new Exception("Incorrect declaration list at"+" " + (_lexer.lineno+1) + ":" + (_lexer.column+1) + ".");
    }

    public ParseTree.FuncDecl fun_decl() throws Exception {
        switch (_token.type) {
            case FUNC:
                Match(FUNC);
                ParserVal v02 = Match(IDENT);
                Match(LPAREN);
                List<ParseTree.Param> v04 = params();
                Match(RPAREN);
                Match(FUNCRET);
                ParseTree.PrimType v07 = prim_type();
                Match(BEGIN);
                List<ParseTree.LocalDecl> v09 = local_decls();
                List<ParseTree.Stmt> v10 = stmt_list();
                Match(END);
                return new ParseTree.FuncDecl(v02.obj.toString(), v07, v04, v09, v10);
        }
        throw new Exception("Incorrect fuction declaration at"+" " + (_lexer.lineno+1) + ":" + (_lexer.column+1) + ".");
    }

    public ParseTree.PrimType prim_type() throws Exception {
        switch (_token.type) {
            case INT:
                Match(INT);
                return new ParseTree.PrimTypeInt();
            case BOOL:
                Match(BOOL);
                return new ParseTree.PrimTypeBool();

        }
        throw new Exception("Incorrect Prim type at"+" " + (_lexer.lineno+1) + ":" + (_lexer.column+1) + ".");
    }

    public List<ParseTree.Param> params() throws Exception {

        switch (_token.type) {
            case RPAREN:
                return new ArrayList<>();
            case INT:
                List<ParseTree.Param> func1 = param_list();
                return func1;
            case BOOL:
                List<ParseTree.Param> func2 = param_list();
                return func2;
        }
        throw new Exception("Incorrect paramameters at"+" " + (_lexer.lineno+1) + ":" + (_lexer.column+1) + ".");
    }
    public ParseTree.Param param() throws Exception {

        switch (_token.type) {
            case INT:
            case BOOL:
                ParseTree.TypeSpec ty = type_spec();
                ParserVal iden =Match(IDENT);
                return new ParseTree.Param(iden.obj.toString(), ty);
        }
        throw new Exception("Incorrect parameter declaration at"+" " + (_lexer.lineno+1) + ":" + (_lexer.column+1) + ".");
    }
    public ParseTree.LocalDecl local_decl() throws Exception {
        switch (_token.type) {
            case VAR:
                Match(VAR);
                ParseTree.TypeSpec ts = type_spec();
                ParserVal iden = Match(IDENT);
                Match(SEMI);
            return new ParseTree.LocalDecl(iden.obj.toString(), ts);
        }
        throw new Exception("Incorrect declaration of a local variable at"+" " + (_lexer.lineno+1) + ":" + (_lexer.column+1)+ ".");
    }

    public List<ParseTree.LocalDecl> local_decls() throws Exception {

        switch (_token.type) {
            case FUNC:
            case IDENT:
            case BEGIN:
            case VAR:
            case PRINT:
            case RETURN:
            case IF:
            case WHILE:
            case END:
            case ENDMARKER:
                return local_decls_();
        }
        throw new Exception("Incorrect declaration of a local variable at"+" " + (_lexer.lineno+1) + ":" + (_lexer.column+1) + ".");
    }

    public List<ParseTree.LocalDecl> local_decls_() throws Exception {
        switch (_token.type) {
            case END:
            case IDENT:
            case BEGIN:
            case ENDMARKER:
            case PRINT:
            case RETURN:
            case IF:
            case WHILE:
                return new ArrayList<>();
            case VAR:
                 ParseTree.LocalDecl ld = local_decl();
                 List<ParseTree.LocalDecl> localDecls = local_decls_();
                 localDecls.add(0, ld);
                 return localDecls;

        }
        throw new Exception("Incorrect local declarations at"+" " + (_lexer.lineno+1) + ":" + (_lexer.column+1) + ".");
    }

    public List<ParseTree.Stmt> stmt_list() throws Exception {
        switch (_token.type) {
            case IDENT:
            case BEGIN:
            case END:
            case PRINT:
            case RETURN:
            case IF:
            case WHILE:
                return stmt_list_();
        }
        throw new Exception("Incorrect statement at"+" " + (_lexer.lineno+1) + ":" + (_lexer.column+1) + ".");
    }
    public ParseTree.Stmt stmt() throws Exception {
        switch (_token.type) {
            case IDENT:
                return assign_stmt();
            case BEGIN:
                return compund_stmt();
            case PRINT:
                return print_stmt();
            case RETURN:
                return return_stmt();
            case IF:
                return if_stmt();
            case WHILE:
                return while_stmt();
        }
        throw new Exception("Incorrect statement at"+" " + (_lexer.lineno+1) + ":" + (_lexer.column+1) + ".");
    }

    public List<ParseTree.Stmt> stmt_list_() throws Exception {

        switch (_token.type) {
            case IDENT:
            case BEGIN:
            case PRINT:
            case RETURN:
            case IF:
            case WHILE:
                ParseTree.Stmt stmt = stmt();
                List<ParseTree.Stmt> param2 = stmt_list_();
                param2.add(0, stmt);
                return param2;
            case END:
                return new ArrayList<>();
        }
        throw new Exception("Incorrect statement at"+" " + (_lexer.lineno+1) + ":" + (_lexer.column+1) + ".");
    }

    public ParseTree.TypeSpec type_spec() throws Exception {
        switch (_token.type) {
            case INT:
            case BOOL:
                ParseTree.PrimType pt = prim_type();
                return new ParseTree.TypeSpec(pt,type_spec_());
        }
        throw new Exception("Incorrect type specifcation at"+" " + (_lexer.lineno+1) + ":" + (_lexer.column+1) + ".");
    }

    public ParseTree.TypeSpec_ type_spec_() throws Exception {

        switch (_token.type) {
            case IDENT:
                return new ParseTree.TypeSpec_Value();
            case LBRACKET:
                ParserVal v1 =Match(LBRACKET);
                ParserVal v2 =Match(RBRACKET);
                return new ParseTree.TypeSpec_Array();
        }
        throw new Exception("Incorrect type specifcation at"+" " + (_lexer.lineno+1) + ":" + (_lexer.column+1) + ".");
    }

    public List<ParseTree.Param> param_list() throws Exception {

        switch (_token.type) {
            case INT:
            case BOOL:
                ParseTree.Param param = param();
                List<ParseTree.Param> param2 = param_list_();
                param2.add(0, param);
                return param2;

        }
        throw new Exception("Incorrect parameter format of a function"+" " + (_lexer.lineno+1) + ":" + (_lexer.column+1) + ".");
    }

    public List<ParseTree.Param> param_list_() throws Exception {

        switch (_token.type) {
            case RPAREN:{
                return new ArrayList<>();
            }
            case COMMA:{
                ParserVal v1 = Match(COMMA);
                ParseTree.Param v2  = param();
                List<ParseTree.Param> v3 = param_list_();
                v3.add(0,v2);
                return v3;
            }
        }
        throw new Exception("Incorrect parameter format of a function"+" " + (_lexer.lineno+1) + ":" + (_lexer.column+1) + ".");
    }

    public ParseTree.StmtAssign assign_stmt() throws Exception {

        switch (_token.type) {
            case IDENT:
                ParserVal v0  = Match(IDENT);
                Match(ASSIGN);
                ParseTree.Expr v1 = expr();
                Match(SEMI);
                return new ParseTree.StmtAssign(v0.obj.toString(),v1);
        }
        throw new Exception("Incorrect assign statement at"+" " + (_lexer.lineno+1) + ":" + (_lexer.column+1) + ".");
    }

    public ParseTree.StmtPrint print_stmt() throws Exception {

        switch (_token.type) {
            case PRINT:
                Match(PRINT);
                ParseTree.Expr v2 =expr();
                Match(SEMI);
                return new ParseTree.StmtPrint(v2);

        }
        throw new Exception("Incorrect print statement at"+" " + (_lexer.lineno+1) + ":" + (_lexer.column+1) + ".");
    }

    public ParseTree.StmtReturn return_stmt() throws Exception {

        switch (_token.type) {
            case RETURN:
                Match(RETURN);
                ParseTree.Expr v2 =expr();
                Match(SEMI);
                return new ParseTree.StmtReturn(v2);
        }
        throw new Exception("Incorrect return statement at"+" " + (_lexer.lineno+1) + ":" + (_lexer.column+1) + ".");
    }

    public ParseTree.StmtIf if_stmt() throws Exception {

        switch (_token.type) {
            case IF:
                Match(IF);
                Match(LPAREN);
                ParseTree.Expr v07 = expr();
                Match(RPAREN);
                ParseTree.Stmt v08 = stmt();
                Match(ELSE);
                ParseTree.Stmt v11 = stmt();
                return new ParseTree.StmtIf(v07 ,v08,v11);
        }

        throw new Exception("Incorrect if statement at"+" " + (_lexer.lineno+1) + ":" + (_lexer.column+1) + ".");
    }
    public ParseTree.StmtWhile while_stmt() throws Exception {
        switch (_token.type) {
            case WHILE:
                Match(WHILE);
                Match(LPAREN);
                ParseTree.Expr expr = expr();
                Match(RPAREN);
                ParseTree.Stmt stmt = stmt();
                return new ParseTree.StmtWhile(expr,stmt);
        }
        throw new Exception("Incorrect while statement at"+" " + (_lexer.lineno+1) + ":" + (_lexer.column+1) + ".");
    }
    public ParseTree.StmtCompound compund_stmt() throws Exception {
        switch (_token.type) {
            case BEGIN:
                Match(BEGIN);
                List<ParseTree.LocalDecl> v1 = local_decls();
                List<ParseTree.Stmt> v2= stmt_list();
                Match(END);
                return new ParseTree.StmtCompound(v1,v2);
        }
        throw new Exception("Incorrect compound statement at"+" " + (_lexer.lineno+1) + ":" + (_lexer.column+1) + ".");
    }
    public List<ParseTree.Arg> args() throws Exception {
        switch (_token.type) {
            case IDENT:
            case LPAREN:
            case INT_LIT:
            case BOOL_LIT:
            case CALL:
            case NEW:
            case ELEMOF:
            case SIZEOF:
                return arg_list();
            case RPAREN:
                return new ArrayList<>();
        }
        throw new Exception("Incorrect arugument at"+" " + (_lexer.lineno+1) + ":" + (_lexer.column+1) + ".");
    }
    public List<ParseTree.Arg> arg_list() throws Exception {
        switch (_token.type) {
            case IDENT:
            case LPAREN:
            case INT_LIT:
            case BOOL_LIT:
            case CALL:
            case NEW:
            case ELEMOF:
            case SIZEOF:
                ParseTree.Expr v1 = expr();
                List<ParseTree.Arg> v2 = arg_list_();
                v2.add(0, new ParseTree.Arg(v1));
                return v2;

        }
        throw new Exception("Incorrect argument format at"+" " + (_lexer.lineno+1) + ":" + (_lexer.column+1)+ ".");
    }
    public List<ParseTree.Arg> arg_list_() throws Exception {
        switch (_token.type) {
            case RPAREN:
                return new ArrayList<>();
            case COMMA:
                Match(COMMA);
                ParseTree.Expr expr = expr();
                List<ParseTree.Arg> v3= arg_list_();
                v3.add(0, new ParseTree.Arg(expr));
                return v3;
        }
        throw new Exception("Incorrect argument format at"+" " + (_lexer.lineno+1) + ":" + (_lexer.column+1)+ ".");
    }
    public ParseTree.Expr expr() throws Exception {
        switch (_token.type) {
                case IDENT:
                case LPAREN:
                case INT_LIT:
                case BOOL_LIT:
                case CALL:
                case NEW:
                case ELEMOF:
                case SIZEOF:
                    ParseTree.Term tm = term();
                    ParseTree.Expr_ exp_= expr_();
                    return new ParseTree.Expr(tm,exp_);
            }

        throw new Exception("Incorrect expression at"+" " + (_lexer.lineno+1) + ":" + (_lexer.column+1) + ".");
    }
    public ParseTree.Expr_ expr_() throws Exception {
        switch (_token.type) {
            case RPAREN:
            case COMMA:
            case RBRACKET:
            case SEMI:
                return null;
            case EXPROP:
                ParserVal v1 =Match(EXPROP);
                ParseTree.Term ter = term();
                ParseTree.Expr_ exp_= expr_();
                return new ParseTree.Expr_(v1.obj.toString(),ter,exp_);
            case RELOP:
                ParserVal v2 =Match(RELOP);
                ParseTree.Term term = term();
                ParseTree.Expr_ expr_= expr_();
                return new ParseTree.Expr_(v2.obj.toString(),term,expr_);


        }

        throw new Exception("Incorrect expression at"+" " + (_lexer.lineno+1) + ":" + (_lexer.column+1) + ".");
    }
    public ParseTree.Term term() throws Exception {
        switch (_token.type) {
            case IDENT:
            case LPAREN:
            case INT_LIT:
            case BOOL_LIT:
            case CALL:
            case NEW:
            case ELEMOF:
            case SIZEOF:
                ParseTree.Factor fact = factor();
                ParseTree.Term_ trm_ = term_();
            return new ParseTree.Term(fact,trm_);

        }
        throw new Exception("Incorrect expression at"+" " +(_lexer.lineno+1) + ":" + (_lexer.column+1) + ".");
    }
    public ParseTree.Term_ term_() throws Exception {
        switch (_token.type) {
            case RPAREN:
            case COMMA:
            case RBRACKET:
            case SEMI:
            case EXPROP:
            case RELOP:
                return null;//Eps
            case TERMOP:
                ParserVal v2=Match(TERMOP);
                ParseTree.Factor fact = factor();
                ParseTree.Term_ term = term_();
                return new ParseTree.Term_(v2.obj.toString(),fact,term);
        }
        throw new Exception("Incorrect expression at"+" " + (_lexer.lineno+1) + ":" + (_lexer.column+1) + ".");
    }
    public ParseTree.Factor factor() throws Exception {
        switch (_token.type) {
            case IDENT:
                ParserVal v02=Match(IDENT);
                return new ParseTree.FactorIdent(v02.obj.toString());
            case LPAREN:
                Match(LPAREN);
                ParseTree.Expr expr = expr();
                Match(RPAREN);
                return new ParseTree.FactorParen(expr);
            case INT_LIT:
                ParserVal v05=Match(INT_LIT);
                String xyz = v05.obj.toString();
                int xyzi = Integer.parseInt(xyz);
                return new ParseTree.FactorIntLit(xyzi);
            case BOOL_LIT:
                ParserVal v06=Match(BOOL_LIT);
                String v8 = v06.obj.toString();
                boolean v9= Boolean.parseBoolean(v8);
                return new ParseTree.FactorBoolLit(v9);
            case CALL:
                Match(CALL);
                ParserVal v01=Match(IDENT);
                Match(LPAREN);
                List<ParseTree.Arg> arg = args();
                Match(RPAREN);
                return new ParseTree.FactorCall(v01.obj.toString(),arg);
            case NEW:
                Match(NEW);
                ParseTree.PrimType pt = prim_type();
                Match(LBRACKET);
                ParseTree.Expr exr = expr();
                Match(RBRACKET);
                return new ParseTree.FactorNew(pt,exr);
            case ELEMOF:
                Match(ELEMOF);
                ParserVal ident = Match(IDENT);
                Match(LBRACKET);
                ParseTree.Expr er = expr();
                Match(RBRACKET);
                return new ParseTree.FactorElemof(ident.obj.toString(), er);

            case SIZEOF:
                Match(SIZEOF);
                ParserVal v03=Match(IDENT);
                return new ParseTree.FactorSizeof(v03.obj.toString());
        }
        throw new Exception("Incorrect expression at"+" " + (_lexer.lineno+1) + ":" + (_lexer.column+1) + ".");
    }


}
