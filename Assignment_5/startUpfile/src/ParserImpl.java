import java.util.*;

@SuppressWarnings("unchecked")
public class ParserImpl
{
    public static Boolean _debug = true;
    void Debug(String message)
    {
        if(_debug)
            System.out.println(message);
    }

    // This is for chained symbol table.
    // This includes the global scope only at this moment.
    Env env = new Env(null);
    String currentId;

    // this stores the root of parse tree, which will be used to print parse tree and run the parse tree
    ParseTree.Program parsetree_program = null;

    //program
    ParseTree.Program program____decllist (Object s1) throws Exception {
        // 1. check if decllist has main function having no parameters and returns int type
        // 2. assign the root, whose type is ParseTree.Program, to parsetree_program
        ArrayList<ParseTree.FuncDecl> decllist = (ArrayList<ParseTree.FuncDecl> ) s1;

        boolean checkForMainFunc = false;

        for (ParseTree.FuncDecl funcdecl : decllist) {
            if (funcdecl.ident.equals("main") && funcdecl.params.size() == 0 && funcdecl.rettype.typename.equals("int")) {
                checkForMainFunc = true;
                break;
            }
        }

        
        // test_01_main_fail1
        if (!checkForMainFunc)
            throw new Exception("The program must have one main function that returns int type and has no parameters.");

        parsetree_program = new ParseTree.Program(decllist);
        return parsetree_program;
    }

    //decl_list
    Object decllist____decllist_decl(Object s1, Object s2) throws Exception {
        ArrayList<ParseTree.FuncDecl> decllist = (ArrayList<ParseTree.FuncDecl>) s1;
        ParseTree.FuncDecl decl = (ParseTree.FuncDecl) s2;
        decllist.add(decl);
        return decllist;
    }

    ArrayList<ParseTree.FuncDecl> decllist____eps() throws Exception {
        return new ArrayList<ParseTree.FuncDecl>();
    }

    //decl
    ParseTree.FuncDecl  decl____fundecl(Object s1) throws Exception {
        return (ParseTree.FuncDecl) s1;
    }

    //fun_decl
    ParseTree.FuncDecl fundecl____FUNC_IDENT_LPAREN_params_RPAREN_FUNCRET_primtype_BEGIN_localdecls_10X_stmtlist_END(Object s1, Object s2, Object s4, Object s7, Object s9) throws Exception {
        Token func = (Token) s1;
        Token id = (Token) s2;
        ArrayList<ParseTree.Param> params = (ArrayList<ParseTree.Param>) s4;
        ParseTree.TypeSpec rettype = (ParseTree.TypeSpec) s7;
        ArrayList<ParseTree.LocalDecl> localdecls = (ArrayList<ParseTree.LocalDecl>) s9;

        ParseTreeInfo.FuncDeclInfo checkFuncName = (ParseTreeInfo.FuncDeclInfo) env.Get(id.lexeme);

        
        // test_05_func1_fail2
        if (checkFuncName != null) {
            throw new Exception("[Error at " + func.lineno + ":" + func.column + "] " +
                    "The function " + id.lexeme + "() is already defined.");
        }

        ParseTreeInfo.FuncDeclInfo funcdeclinfo = new ParseTreeInfo.FuncDeclInfo();
        funcdeclinfo.id = id.lexeme;
        funcdeclinfo.returntype = rettype.typename;
        funcdeclinfo.params = params;

        // global-scope now has funcdecl info, (id, returntype, params)
        // global-scope holds all info for all functions
        env.Put(id.lexeme, funcdeclinfo);

        // Update current Id
        currentId = id.lexeme;

        // push new env to store params and localdecls
        env = new Env(env);

        // add parameters into top-local scope of env
        // ex: ( var bool a , var int  b  , var int  c )
        // {a=bool, b=int, c=int}
        for (ParseTree.Param param : params) {
            if (env.Get(param.ident) != null) {
                throw new Exception("param error");
            }
            env.Put(param.ident, param.typespec.typename);
        }

        // add localdecls into top-local scope of env
        // ex: var int x;
        // {x=int}
        for (ParseTree.LocalDecl localdecl : localdecls) {
            // check if localdecl.ident is already in env
            // i.e. was a param when passsed in
            // test_10_scope1_fail1 and test_10_scope1_fail2
            if (env.GetCurrentScope(localdecl.ident) != null) {
                throw new Exception("[Error at " + localdecl.info.lineno + ":" + localdecl.info.column + "] " +
                        "The identifier " + localdecl.ident + " is already defined.");
            }
            env.Put(localdecl.ident, localdecl.typespec.typename);
        }

        return null;
    }

    ParseTree.FuncDecl fundecl____FUNC_IDENT_LPAREN_params_RPAREN_FUNCRET_primtype_BEGIN_localdecls_X10_stmtlist_END(Object s2, Object s4, Object s7, Object s9, Object s11, Object s12) throws Exception {
        Token id = (Token) s2;
        ArrayList<ParseTree.Param> params = (ArrayList<ParseTree.Param>) s4;
        ParseTree.TypeSpec rettype = (ParseTree.TypeSpec) s7;
        ArrayList<ParseTree.LocalDecl> localdecls = (ArrayList<ParseTree.LocalDecl>) s9;
        ArrayList<ParseTree.Stmt> stmtlist = (ArrayList<ParseTree.Stmt>) s11;
        Token end = (Token) s12;

        ParseTree.FuncDecl funcdecl = new ParseTree.FuncDecl(id.lexeme, rettype, params, localdecls, stmtlist);

        // check for at least one return type
        // will also check if, while, and compound stmt returns with stmt.info.hasReturn check
        // test_02_expr1_fail2 and test_03_expr2_fail2 and test_05_func1_fail1 and test_07_func3_fail1
        boolean checkAtLeastOneReturnType = false;
        for (ParseTree.Stmt stmt : funcdecl.stmtlist) {
            if (stmt instanceof ParseTree.ReturnStmt || stmt.info.hasReturn) {
                checkAtLeastOneReturnType = true;
                break;
            }
        }

        if (!checkAtLeastOneReturnType) {
            throw new Exception("[Error at " + end.lineno + ":" + end.column + "] " +
                    "The function " + funcdecl.ident + "() should return at least one " + funcdecl.rettype.typename + " value.");
        }

        // Pop env when we are done using current funcdecl
        env = env.prev;

        return funcdecl;
    }

    //params
    ArrayList<ParseTree.Param> params____paramlist(Object s1) throws Exception {
        ArrayList<ParseTree.Param> params = (ArrayList<ParseTree.Param>) s1;
        return new ArrayList<ParseTree.Param>(params);
    }

    ArrayList<ParseTree.Param> params____eps() throws Exception {
        return new ArrayList<ParseTree.Param>();
    }


    //param_list
    ArrayList<ParseTree.Param> paramlist____paramlist_COMMA_param(Object s1, Object s3) throws Exception {
        ArrayList<ParseTree.Param> existingParams = (ArrayList<ParseTree.Param>) s1;
        ParseTree.Param newParam = (ParseTree.Param) s3;
        existingParams.add(newParam);
        return existingParams;
    }

    ArrayList<ParseTree.Param> paramlist____param(Object s1) throws Exception {
        ParseTree.Param param = (ParseTree.Param) s1;
        ArrayList<ParseTree.Param> paramlist = new ArrayList<>();
        paramlist.add(param);
        return paramlist;
    }

    //param
    ParseTree.Param param____VAR_typespec_IDENT(Object s1,Object s2, Object s3) throws Exception {
        Token id = (Token) s3;
        ParseTree.TypeSpec typespec = (ParseTree.TypeSpec) s2;
        return new ParseTree.Param(id.lexeme, typespec);
    }

    //typespec
    ParseTree.TypeSpec typespec____primtype(Object s1) throws Exception {
        return (ParseTree.TypeSpec) s1;
    }

    //INT
    ParseTree.TypeSpec primtype____INT() throws Exception {
        return new ParseTree.TypeSpec("int");
    }

    //Bool
    ParseTree.TypeSpec primtype____BOOL() throws Exception {
        return new ParseTree.TypeSpec("bool");
    }

    //localdecls
    ArrayList<ParseTree.LocalDecl> localdecls____localdecls_localdecl(Object s1, Object s2) throws Exception {
        ArrayList<ParseTree.LocalDecl> localdecls = (ArrayList<ParseTree.LocalDecl>) s1;
        ParseTree.LocalDecl localdecl = (ParseTree.LocalDecl) s2;
        localdecls.add(localdecl);

        return localdecls;
    }

    ArrayList<ParseTree.LocalDecl> localdecls____eps() throws Exception {
        return new ArrayList<ParseTree.LocalDecl>();
    }

    //local_decl
    ParseTree.LocalDecl localdecl____VAR_typespec_IDENT_SEMI(Object s1,Object s2, Object s3) {
        Token var = (Token) s1;
        ParseTree.TypeSpec typespec = (ParseTree.TypeSpec) s2;
        Token id = (Token) s3;

        ParseTree.LocalDecl localdecl = new ParseTree.LocalDecl(id.lexeme, typespec);
        localdecl.info.value_type = typespec.typename;
        localdecl.info.lineno = var.lineno;
        localdecl.info.column = var.column;
        return localdecl;
    }

    //stmt_list
    ArrayList<ParseTree.Stmt> stmtlist____stmtlist_stmt(Object s1, Object s2) throws Exception {
        ArrayList<ParseTree.Stmt> stmtlist = (ArrayList<ParseTree.Stmt>) s1;
        ParseTree.Stmt stmt = (ParseTree.Stmt) s2;
        stmtlist.add(stmt);
        return stmtlist;
    }

    ArrayList<ParseTree.Stmt> stmtlist____eps() throws Exception {
        return new ArrayList<ParseTree.Stmt>();
    }

    //stmt
    ParseTree.Stmt stmt____assignstmt(Object s1) throws Exception {
        assert(s1 instanceof ParseTree.AssignStmt);
        return (ParseTree.AssignStmt) s1;
    }

    ParseTree.Stmt stmt____printstmt(Object s1) throws Exception {
        assert(s1 instanceof ParseTree.PrintStmt);
        return (ParseTree.PrintStmt) s1;
    }

    ParseTree.Stmt stmt____returnstmt(Object s1) throws Exception {
        assert(s1 instanceof ParseTree.ReturnStmt);
        return (ParseTree.ReturnStmt) s1;
    }

    ParseTree.Stmt stmt____ifstmt(Object s1) throws Exception {
        assert(s1 instanceof ParseTree.IfStmt);
        return (ParseTree.IfStmt) s1;
    }

    ParseTree.Stmt stmt____whilestmt(Object s1) throws Exception {
        assert(s1 instanceof ParseTree.WhileStmt);
        return (ParseTree.WhileStmt) s1;
    }

    ParseTree.Stmt stmt____compoundstmt(Object s1) throws Exception {
        assert(s1 instanceof ParseTree.CompoundStmt);
        return (ParseTree.CompoundStmt) s1;
    }

    //assignstmt
    ParseTree.AssignStmt assignstmt____IDENT_ASSIGN_expr_SEMI(Object s1, Object s2, Object s3) throws Exception {
        Token id     = (Token) s1;
        Token assign = (Token) s2;
        ParseTree.Expr expr   = (ParseTree.Expr) s3;

        Object id_type = env.Get(id.lexeme);

        // check id_type was found in scope
        // test_10_scope1_fail3
        if (id_type == null) {
            throw new Exception("[Error at " + id.lineno + ":" + id.column + "] " +
                    "Cannot use an undefined variable " + id.lexeme + ".");
        }

        String type = (String) id_type;

        // check for assignstmt type mismatch
        // test_04_stmt_fail1 and test_04_stmt_fail4
        if (!type.equals(expr.info.value_type)) {
            throw new Exception("[Error at " + assign.lineno + ":" + assign.column + "] " +
                    "Cannot assign " + expr.info.value_type + " value to " + type + " variable " + id.lexeme + ".");
        }

        ParseTree.AssignStmt stmt = new ParseTree.AssignStmt(id.lexeme, expr);
        stmt.ident_reladdr = 1;
        return stmt;
    }

    //printstmt
    ParseTree.PrintStmt printnstmt____PRINT_expr_SEMI(Object s2) throws Exception {
        ParseTree.Expr expr = (ParseTree.Expr) s2;
        ParseTree.PrintStmt stmt = new ParseTree.PrintStmt(expr);

        return stmt;
    }

    //returnstmt

    ParseTree.ReturnStmt returnstmt____RETURN_expr_SEMI(Object s2) throws Exception {
        ParseTree.Expr expr = (ParseTree.Expr) s2;
        ParseTree.ReturnStmt stmt = new ParseTree.ReturnStmt(expr);
        stmt.info.value_type = expr.info.value_type;

        ParseTreeInfo.FuncDeclInfo funcdeclinfo = (ParseTreeInfo.FuncDeclInfo) env.Get(currentId);

        // check return type is equal to expected return type of function
        // test_01_main_fail2
        if (!funcdeclinfo.returntype.equals(stmt.info.value_type)) {
            throw new Exception("[Error at " + expr.info.lineno + ":" + expr.info.column + "] " +
                    "The type of returning value (" + stmt.info.value_type +  ") should match with the return type (" +  funcdeclinfo.returntype + ") of the function " + funcdeclinfo.id + "().");
        }

        // send return back up, for edge cases like ifstmt, whilestmt, compoundstmt
        stmt.info.hasReturn = true;
        return stmt;
    }

    //if_stmt
    ParseTree.IfStmt ifstmt____IF_LPAREN_expr_RPAREN_stmt_ELSE_stmt(Object s3, Object s5, Object s7) throws Exception {
        ParseTree.Expr cond = (ParseTree.Expr) s3;
        ParseTree.Stmt thenstmt = (ParseTree.Stmt) s5;
        ParseTree.Stmt elsestmt = (ParseTree.Stmt) s7;

        // check cond for bool in ifstmt
        // test_04_stmt_fail3
        if (!cond.info.value_type.equals("bool")) {
            throw new Exception("[Error at " + cond.info.lineno + ":" + cond.info.column + "] " +
                    "Use bool value to check the condition in if statement.");
        }

        ParseTree.IfStmt stmt = new ParseTree.IfStmt(cond, thenstmt, elsestmt);
        if (thenstmt.info.hasReturn || elsestmt.info.hasReturn) {
            stmt.info.hasReturn = true;
        }
        return stmt;
    }

    //while_stmt

    ParseTree.WhileStmt whilestmt____WHILE_LPAREN_expr_RPAREN_stmt(Object s3, Object s5) throws Exception {
        ParseTree.Expr cond = (ParseTree.Expr) s3;
        ParseTree.Stmt whilestmt = (ParseTree.Stmt) s5;

        // check cond for bool in whilestmt
        // test_04_stmt_fail2
        if (!cond.info.value_type.equals("bool")) {
            throw new Exception("[Error at " + cond.info.lineno + ":" + cond.info.column + "] " +
                    "Use bool value to check the condition in while statement.");
        }

        ParseTree.WhileStmt stmt = new ParseTree.WhileStmt(cond, whilestmt);
        if (stmt.info.hasReturn) {
            whilestmt.info.hasReturn = true;
        }
        return stmt;
    }

    //compound_stmt
    ParseTree.CompoundStmt compoundstmt____BEGIN_localdecls(Object s2) throws Exception {
        ArrayList<ParseTree.LocalDecl> localdecls = (ArrayList<ParseTree.LocalDecl>) s2;

        // Push new env
        env = new Env(env);

        for (ParseTree.LocalDecl localdecl : localdecls) {
            // only worry about current scope
            if (env.GetCurrentScope(localdecl.ident) != null) {
                throw new Exception("localdecl current scope error");
            }
            env.Put(localdecl.ident, localdecl.typespec.typename);
        }

        return null;
    }

    //compound_stmt----object are diff
    ParseTree.CompoundStmt compoundstmt____stmtlist_END(Object s2, Object s3) throws Exception {
        ArrayList<ParseTree.LocalDecl> localdecls = (ArrayList<ParseTree.LocalDecl>) s2;
        ArrayList<ParseTree.Stmt> stmtlist = (ArrayList<ParseTree.Stmt>) s3;

        // Pop env when done using compound
        env = env.prev;

        ParseTree.CompoundStmt compoundstmt = new ParseTree.CompoundStmt(localdecls, stmtlist);
        for (ParseTree.Stmt stmt : stmtlist) {
            if (stmt instanceof ParseTree.ReturnStmt) {
                compoundstmt.info.hasReturn = true;
                break;
            }
        }
        return compoundstmt;
    }

    //args 
    ArrayList<ParseTree.Arg> args____arglist(Object s1) throws Exception {
        ArrayList<ParseTree.Arg> arglist = (ArrayList<ParseTree.Arg>) s1;
        return arglist;
    }

    ArrayList<ParseTree.Arg> args____eps() throws Exception {
        return new ArrayList<ParseTree.Arg>();
    }

    //arg_list
    ArrayList<ParseTree.Arg> arglist____arglist_COMMA_expr(Object s1, Object s3) throws Exception {
        ArrayList<ParseTree.Arg> arglist = (ArrayList<ParseTree.Arg>) s1;
        ParseTree.Expr expr = (ParseTree.Expr) s3;
        arglist.add(new ParseTree.Arg(expr));
        return arglist;
    }

    ArrayList<ParseTree.Arg> arglist____expr(Object s1) throws Exception {
        ArrayList<ParseTree.Arg> arglist = new ArrayList<>();
        ParseTree.Expr expr = (ParseTree.Expr) s1;
        arglist.add(new ParseTree.Arg(expr));
        return arglist;
    }

    //expr
    ParseTree.ExprAdd expr____expr_ADD_expr(Object s1, Object s2, Object s3) throws Exception {
        ParseTree.Expr expr1 = (ParseTree.Expr) s1;
        Token oper = (Token) s2;
        ParseTree.Expr expr2 = (ParseTree.Expr) s3;

        // check for mismatch typing in add
        // test_02_expr1_fail3
        if (!expr1.info.value_type.equals(expr2.info.value_type)) {
            throw new Exception("[Error at " + oper.lineno + ":" + oper.column + "] " +
                                "Cannot perform " + expr1.info.value_type + " " + oper.lexeme + " " + expr2.info.value_type + ".");
        }

        ParseTree.ExprAdd expr = new ParseTree.ExprAdd(expr1, expr2);
        expr.info.value_type = expr1.info.value_type;
        expr.info.lineno = expr1.info.lineno;
        expr.info.column = expr1.info.column;
        return expr;
    }

    ParseTree.ExprSub expr____expr_SUB_expr(Object s1, Object s2, Object s3) throws Exception {
        ParseTree.Expr expr1 = (ParseTree.Expr) s1;
        Token oper = (Token) s2;
        ParseTree.Expr expr2 = (ParseTree.Expr) s3;

        // check for mismatch typing in sub
        if (!expr1.info.value_type.equals(expr2.info.value_type)) {
            throw new Exception("[Error at " + oper.lineno + ":" + oper.column + "] " +
                    "Cannot perform " + expr1.info.value_type + " " + oper.lexeme + " " + expr2.info.value_type + ".");
        }

        ParseTree.ExprSub expr = new ParseTree.ExprSub(expr1, expr2);
        expr.info.value_type = expr1.info.value_type;
        expr.info.lineno = expr1.info.lineno;
        expr.info.column = expr1.info.column;
        return expr;
    }

    ParseTree.ExprMul expr____expr_MUL_expr(Object s1, Object s2, Object s3) throws Exception {
        ParseTree.Expr expr1 = (ParseTree.Expr) s1;
        Token oper = (Token) s2;
        ParseTree.Expr expr2 = (ParseTree.Expr) s3;

        // check for mismatch typing in mul
        // test_02_expr1_fail1
        if (!expr1.info.value_type.equals(expr2.info.value_type)) {
            throw new Exception("[Error at " + oper.lineno + ":" + oper.column + "] " +
                    "Cannot perform " + expr1.info.value_type + " " + oper.lexeme + " " + expr2.info.value_type + ".");
        }

        ParseTree.ExprMul expr = new ParseTree.ExprMul(expr1, expr2);
        expr.info.value_type = expr1.info.value_type;
        expr.info.lineno = expr1.info.lineno;
        expr.info.column = expr1.info.column;
        return expr;
    }

    ParseTree.ExprDiv expr____expr_DIV_expr(Object s1, Object s2, Object s3) throws Exception {
        ParseTree.Expr expr1 = (ParseTree.Expr) s1;
        Token oper = (Token) s2;
        ParseTree.Expr expr2 = (ParseTree.Expr) s3;

        // check for mismatch typing in div
        if (!expr1.info.value_type.equals(expr2.info.value_type)) {
            throw new Exception("[Error at " + oper.lineno + ":" + oper.column + "] " +
                    "Cannot perform " + expr1.info.value_type + " " + oper.lexeme + " " + expr2.info.value_type + ".");
        }

        ParseTree.ExprDiv expr = new ParseTree.ExprDiv(expr1, expr2);
        expr.info.value_type = expr1.info.value_type;
        expr.info.lineno = expr1.info.lineno;
        expr.info.column = expr1.info.column;
        return expr;
    }

    ParseTree.ExprMod expr____expr_MOD_expr(Object s1, Object s2, Object s3) throws Exception {
        ParseTree.Expr expr1 = (ParseTree.Expr) s1;
        Token oper = (Token) s2;
        ParseTree.Expr expr2 = (ParseTree.Expr) s3;

        // check for mismatch typing in mod
        if (!expr1.info.value_type.equals(expr2.info.value_type)) {
            throw new Exception("[Error at " + oper.lineno + ":" + oper.column + "] " +
                    "Cannot perform " + expr1.info.value_type + " " + oper.lexeme + " " + expr2.info.value_type + ".");
        }

        ParseTree.ExprMod expr = new ParseTree.ExprMod(expr1, expr2);
        expr.info.value_type = expr1.info.value_type;
        expr.info.lineno = expr1.info.lineno;
        expr.info.column = expr1.info.column;
        return expr;
    }

    ParseTree.ExprEq expr____expr_EQ_expr(Object s1, Object s2, Object s3) throws Exception {
        ParseTree.Expr expr1 = (ParseTree.Expr) s1;
        Token oper = (Token) s2;
        ParseTree.Expr expr2 = (ParseTree.Expr) s3;

        // check for mismatch typing in eq
        if (!expr1.info.value_type.equals(expr2.info.value_type)) {
            throw new Exception("[Error at " + oper.lineno + ":" + oper.column + "] " +
                    "Cannot perform " + expr1.info.value_type + " " + oper.lexeme + " " + expr2.info.value_type + ".");
        }

        ParseTree.ExprEq expr = new ParseTree.ExprEq(expr1, expr2);
        expr.info.value_type = "bool";
        expr.info.lineno = expr1.info.lineno;
        expr.info.column = expr1.info.column;
        return expr;
    }

    ParseTree.ExprNe expr____expr_NE_expr(Object s1, Object s2, Object s3) throws Exception {
        ParseTree.Expr expr1 = (ParseTree.Expr) s1;
        Token oper = (Token) s2;
        ParseTree.Expr expr2 = (ParseTree.Expr) s3;

        // check for mismatch typing in ne
        // test_03_expr2_fail4
        if (!expr1.info.value_type.equals(expr2.info.value_type)) {
            throw new Exception("[Error at " + oper.lineno + ":" + oper.column + "] " +
                    "Cannot perform " + expr1.info.value_type + " " + oper.lexeme + " " + expr2.info.value_type + ".");
        }

        ParseTree.ExprNe expr = new ParseTree.ExprNe(expr1, expr2);
        expr.info.value_type = "bool";
        expr.info.lineno = expr1.info.lineno;
        expr.info.column = expr1.info.column;
        return expr;
    }

    ParseTree.ExprLe expr____expr_LE_expr(Object s1, Object s2, Object s3) throws Exception {
        ParseTree.Expr expr1 = (ParseTree.Expr) s1;
        Token oper = (Token) s2;
        ParseTree.Expr expr2 = (ParseTree.Expr) s3;

        // check for mismatch typing in le
        // test_02_expr1_fail4
        if (!expr1.info.value_type.equals("int") || !expr2.info.value_type.equals("int")) {
            throw new Exception("[Error at " + oper.lineno + ":" + oper.column + "] " +
                    "Cannot perform " + expr1.info.value_type + " " + oper.lexeme + " " + expr2.info.value_type + ".");
        }

        ParseTree.ExprLe expr = new ParseTree.ExprLe(expr1, expr2);
        expr.info.value_type = "bool";
        expr.info.lineno = expr1.info.lineno;
        expr.info.column = expr1.info.column;
        return expr;
    }

    ParseTree.ExprLt expr____expr_LT_expr(Object s1, Object s2, Object s3) throws Exception {
        ParseTree.Expr expr1 = (ParseTree.Expr) s1;
        Token oper = (Token) s2;
        ParseTree.Expr expr2 = (ParseTree.Expr) s3;

        // check for mismatch typing in lt
        // test_03_expr2_fail3
        if (!expr1.info.value_type.equals("int") || !expr2.info.value_type.equals("int")) {
            throw new Exception("[Error at " + oper.lineno + ":" + oper.column + "] " +
                    "Cannot perform " + expr1.info.value_type + " " + oper.lexeme + " " + expr2.info.value_type + ".");
        }

        ParseTree.ExprLt expr = new ParseTree.ExprLt(expr1, expr2);
        expr.info.value_type = "bool";
        expr.info.lineno = expr1.info.lineno;
        expr.info.column = expr1.info.column;
        return expr;
    }

    ParseTree.ExprGe expr____expr_GE_expr(Object s1, Object s2, Object s3) throws Exception {
        ParseTree.Expr expr1 = (ParseTree.Expr) s1;
        Token oper = (Token) s2;
        ParseTree.Expr expr2 = (ParseTree.Expr) s3;

        // check for mismatch typing in ge
        if (!expr1.info.value_type.equals("int") || !expr2.info.value_type.equals("int")) {
            throw new Exception("[Error at " + oper.lineno + ":" + oper.column + "] " +
                    "Cannot perform " + expr1.info.value_type + " " + oper.lexeme + " " + expr2.info.value_type + ".");
        }

        ParseTree.ExprGe expr = new ParseTree.ExprGe(expr1, expr2);
        expr.info.value_type = "bool";
        expr.info.lineno = expr1.info.lineno;
        expr.info.column = expr1.info.column;
        return expr;
    }

    ParseTree.ExprGt expr____expr_GT_expr(Object s1, Object s2, Object s3) throws Exception {
        ParseTree.Expr expr1 = (ParseTree.Expr) s1;
        Token oper = (Token) s2;
        ParseTree.Expr expr2 = (ParseTree.Expr) s3;

        // check for mismatch typing in gt
        if (!expr1.info.value_type.equals("int") || !expr2.info.value_type.equals("int")) {
            throw new Exception("[Error at " + oper.lineno + ":" + oper.column + "] " +
                    "Cannot perform " + expr1.info.value_type + " " + oper.lexeme + " " + expr2.info.value_type + ".");
        }

        ParseTree.ExprGt expr = new ParseTree.ExprGt(expr1, expr2);
        expr.info.value_type = "bool";
        expr.info.lineno = expr1.info.lineno;
        expr.info.column = expr1.info.column;
        return expr;
    }

    ParseTree.ExprAnd expr____expr_AND_expr(Object s1, Object s2, Object s3) throws Exception {
        ParseTree.Expr expr1 = (ParseTree.Expr) s1;
        Token oper = (Token) s2;
        ParseTree.Expr expr2 = (ParseTree.Expr) s3;

        // check for mismatch typing in and
        // test_02_expr1_fail5
        if (!expr1.info.value_type.equals("bool") || !expr2.info.value_type.equals("bool")) {
            throw new Exception("[Error at " + oper.lineno + ":" + oper.column + "] " +
                    "Cannot perform " + expr1.info.value_type + " " + oper.lexeme + " " + expr2.info.value_type + ".");
        }

        ParseTree.ExprAnd expr = new ParseTree.ExprAnd(expr1, expr2);
        expr.info.value_type = "bool";
        expr.info.lineno = expr1.info.lineno;
        expr.info.column = expr1.info.column;
        return expr;
    }

    ParseTree.ExprOr expr____expr_OR_expr(Object s1, Object s2, Object s3) throws Exception {
        ParseTree.Expr expr1 = (ParseTree.Expr) s1;
        Token oper = (Token) s2;
        ParseTree.Expr expr2 = (ParseTree.Expr) s3;

        // check for mismatch typing in or
        if (!expr1.info.value_type.equals(expr2.info.value_type)) {
            throw new Exception("[Error at " + oper.lineno + ":" + oper.column + "] " +
                    "Cannot perform " + expr1.info.value_type + " " + oper.lexeme + " " + expr2.info.value_type + ".");
        }

        ParseTree.ExprOr expr = new ParseTree.ExprOr(expr1, expr2);
        expr.info.value_type = "bool";
        expr.info.lineno = expr1.info.lineno;
        expr.info.column = expr1.info.column;
        return expr;
    }

    ParseTree.ExprNot expr____NOT_expr(Object s1, Object s2) throws Exception {
        Token oper = (Token) s1;
        ParseTree.Expr expr1 = (ParseTree.Expr) s2;

        // check for mismatch typing in not
        // test_03_expr2_fail1
        if (!expr1.info.value_type.equals("bool")) {
            throw new Exception("[Error at " + oper.lineno + ":" + oper.column + "] " +
                    "Cannot perform " + oper.lexeme + " " + expr1.info.value_type + ".");
        }

        ParseTree.ExprNot expr = new ParseTree.ExprNot(expr1);
        expr.info.value_type = "bool";
        expr.info.lineno = expr1.info.lineno;
        expr.info.column = expr1.info.column;
        return expr;
    }

    ParseTree.ExprParen expr____LPAREN_expr_RPAREN(Object s1, Object s2, Object s3) throws Exception {
        Token lparen = (Token) s1;
        ParseTree.Expr expr = (ParseTree.Expr) s2;
        Token rparen = (Token) s3;

        ParseTree.ExprParen parenexpr = new ParseTree.ExprParen(expr);
        parenexpr.info.value_type = expr.info.value_type;
        parenexpr.info.lineno = expr.info.lineno;
        parenexpr.info.column = expr.info.column;

        return parenexpr;
    }

    ParseTree.ExprIdent expr____IDENT(Object s1) throws Exception {
        Token id = (Token) s1;

        // ParseTreeInfo.FuncDeclInfo if in global-scope
        // String if in any other scope
        Object id_type = env.Get(id.lexeme);

        // check if id.lexeme found
        if (id_type != null) {

            // check if id.lexeme is a function
            // test_06_func2_fail5
            if (id_type instanceof ParseTreeInfo.FuncDeclInfo) {
                throw new Exception("[Error at " + id.lineno + ":" + id.column + "] " +
                        "Cannot use the function " + id.lexeme + "() as a variable.");
            }
        }
        else {
            // variable not found
            // test_02_expr1_fail6
            throw new Exception("[Error at " + id.lineno + ":" + id.column + "] " +
                    "Cannot use an undefined variable " + id.lexeme + ".");
        }

        ParseTree.ExprIdent expr = new ParseTree.ExprIdent(id.lexeme);
        expr.info.value_type = (String) id_type;
        expr.info.lineno = id.lineno;
        expr.info.column = id.column;
        expr.reladdr = 1;
        return expr;
    }

    ParseTree.ExprIntLit expr____INT_LIT(Object s1) throws Exception {
        Token token = (Token) s1;
        int value = Integer.parseInt(token.lexeme);
        ParseTree.ExprIntLit exprintlit = new ParseTree.ExprIntLit(value);

        exprintlit.info.value_type = "int";
        exprintlit.info.lineno = token.lineno;
        exprintlit.info.column = token.column;
        return exprintlit;
    }

    ParseTree.ExprBoolLit expr____BOOL_LIT(Object s1) throws Exception {
        // TODO
        // 1. create and return node that has bool type
        Token token = (Token) s1;
        boolean value = Boolean.parseBoolean(token.lexeme);
        ParseTree.ExprBoolLit exprboollit = new ParseTree.ExprBoolLit(value);

        exprboollit.info.value_type = "bool";
        exprboollit.info.lineno = token.lineno;
        exprboollit.info.column = token.column;
        return exprboollit;
    }

    ParseTree.ExprCall expr____CALL_IDENT_LPAREN_args_RPAREN(Object s1, Object s2, Object s4) throws Exception {
        Token call = (Token) s1;
        Token id = (Token) s2;
        ArrayList<ParseTree.Arg> args = (ArrayList<ParseTree.Arg>) s4;
        

        Object id_type = env.Get(id.lexeme);

        // check if func id in local-scope first as variable
        // test_06_func2_fail4
        if (env.Get(id.lexeme) != null && env.Get(id.lexeme) instanceof String) {
            throw new Exception("[Error at " + call.lineno + ":" + call.column + "] " +
                    "Cannot use a variable " + id.lexeme + " as a function.");
        }

        ParseTreeInfo.FuncDeclInfo funcdeclinfo = (ParseTreeInfo.FuncDeclInfo) id_type;

        // check if func id exists in global-scope as function
        // test_06_func2_fail3
        if (funcdeclinfo == null) {
            throw new Exception("[Error at " + call.lineno + ":" + call.column + "] " +
                    "Cannot use an undefined function " + id.lexeme + "().");
        }

        // check matching params/args size
        // test_06_func2_fail2
        if (funcdeclinfo.params.size() != args.size()) {
            throw new Exception("[Error at " + call.lineno + ":" + call.column + "] " +
                    "Cannot pass the incorrect number of arguments to " + funcdeclinfo.id + "().");
        }

        // checking matching types for params/args
        for (int i = 0; i < funcdeclinfo.params.size(); i++) {
            ParseTree.Param param = funcdeclinfo.params.get(i);
            ParseTree.Arg arg = args.get(i);

            // test_06_func2_fail1 and test_09_advanced3_fail1
            if (!param.typespec.typename.equals(arg.expr.info.value_type)) {
                switch (i) {
                    case 0:
                            throw new Exception("[Error at " + arg.expr.info.lineno + ":" + arg.expr.info.column + "] " +
                                    "The " + (i + 1) + "st argument of the function " + funcdeclinfo.id + "() should be " + param.typespec.typename + " type.");
                    case 1:
                            throw new Exception("[Error at " + arg.expr.info.lineno + ":" + arg.expr.info.column + "] " +
                                    "The " + (i + 1) + "nd argument of the function " + funcdeclinfo.id + "() should be " + param.typespec.typename + " type.");
                    case 2:
                            throw new Exception("[Error at " + arg.expr.info.lineno + ":" + arg.expr.info.column + "] " +
                                    "The " + (i + 1) + "rd argument of the function " + funcdeclinfo.id + "() should be " + param.typespec.typename + " type.");
                    default:
                            throw new Exception("[Error at " + arg.expr.info.lineno + ":" + arg.expr.info.column + "] " +
                                    "The " + (i + 1) + "th argument of the function " + funcdeclinfo.id + "() should be " + param.typespec.typename + " type.");
                }

            }
        }

        ParseTree.ExprCall expr = new ParseTree.ExprCall(id.lexeme, args);
        expr.info.value_type = funcdeclinfo.returntype;
        expr.info.lineno = id.lineno;
        expr.info.column = id.column;
        return expr;
    }
}