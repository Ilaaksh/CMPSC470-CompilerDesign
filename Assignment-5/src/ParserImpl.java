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

    ParseTree.Program program____decllist(Object s1) throws Exception {
        ArrayList<ParseTree.FuncDecl> decllist = (ArrayList<ParseTree.FuncDecl> ) s1;

        boolean checkForMainFunc = false;

        for (ParseTree.FuncDecl funcdecl : decllist) {
            if (funcdecl.ident.equals("main") && funcdecl.params.size() == 0 && funcdecl.rettype.typename.equals("int")) {
                checkForMainFunc = true;
                break;
            }
        }

        
        if (!checkForMainFunc)
            throw new Exception("The program must have one main function that returns int type and has no parameters.");

        parsetree_program = new ParseTree.Program(decllist);
        return parsetree_program;
    }

    Object decl_list_____decl_decl (Object s1, Object s2) throws Exception {
        ArrayList<ParseTree.FuncDecl> decllist = (ArrayList<ParseTree.FuncDecl>) s1;
        ParseTree.FuncDecl decl = (ParseTree.FuncDecl) s2;
        decllist.add(decl);
        return decllist;
    }

    ArrayList<ParseTree.FuncDecl> decllist____eps() throws Exception {
        return new ArrayList<ParseTree.FuncDecl>();
    }

    ParseTree.FuncDecl decl(Object s1) throws Exception {
        return (ParseTree.FuncDecl) s1;
    }

    ParseTree.FuncDecl fundecl____FUNC_IDENT_LPAREN_params_RPAREN_FUNCRET_primtype_BEGIN_localdecls_10X_stmtlist_END(Object s1, Object s2, Object s4, Object s7, Object s9) throws Exception {
        Token func = (Token) s1;
        Token id = (Token) s2;
        ArrayList<ParseTree.Param> params = (ArrayList<ParseTree.Param>) s4;
        ParseTree.TypeSpec rettype = (ParseTree.TypeSpec) s7;
        ArrayList<ParseTree.LocalDecl> localdecls = (ArrayList<ParseTree.LocalDecl>) s9;

        ParseTreeInfo.FuncDeclInfo checkFuncName = (ParseTreeInfo.FuncDeclInfo) env.Get(id.lexeme);
        if (checkFuncName != null) {
            throw new Exception("[Error at " + func.lineno + ":" + func.column + "] " +
                    "The function " + id.lexeme + "() is already defined.");
        }

        ParseTreeInfo.FuncDeclInfo funcdeclinfo = new ParseTreeInfo.FuncDeclInfo();
        funcdeclinfo.id = id.lexeme;
        funcdeclinfo.ret_type = rettype.typename;
        funcdeclinfo.params = params;

       
        env.Put(id.lexeme, funcdeclinfo);

       
        currentId = id.lexeme;

        
        env = new Env(env);

      
        for (ParseTree.Param param : params) {
            if (env.Get(param.ident) != null) {
                throw new Exception("param error");
            }
            env.Put(param.ident, param.typespec.typename);
        }

       
        for (ParseTree.LocalDecl localdecl : localdecls) {
            
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

        
        boolean checkAtLeastOneReturnType = false;
        for (ParseTree.Stmt stmt : funcdecl.stmtlist) {
            if (stmt instanceof ParseTree.ReturnStmt || stmt.info.returened) {
                checkAtLeastOneReturnType = true;
                break;
            }
        }

        if (!checkAtLeastOneReturnType) {
            throw new Exception("[Error at " + end.lineno + ":" + end.column + "] " +
                    "The function " + funcdecl.ident + "() should return at least one " + funcdecl.rettype.typename + " value.");
        }

        
        env = env.prev;

        return funcdecl;
    }

    ArrayList<ParseTree.Param> params____paramlist(Object s1) throws Exception {
        ArrayList<ParseTree.Param> params = (ArrayList<ParseTree.Param>) s1;
        return new ArrayList<ParseTree.Param>(params);
    }

    ArrayList<ParseTree.Param> params____eps() throws Exception {
        return new ArrayList<ParseTree.Param>();
    }

    ArrayList<ParseTree.Param> paramlist____param_list_COMMA_param(Object s1, Object s3) throws Exception {
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

    ParseTree.Param param____VAR_type_spec_IDENT(Object s2, Object s3) throws Exception {
        Token id = (Token) s3;
        ParseTree.TypeSpec typespec = (ParseTree.TypeSpec) s2;
        return new ParseTree.Param(id.lexeme, typespec);
    }

    ParseTree.TypeSpec type_spec____prim_type(Object s1) throws Exception {
        return (ParseTree.TypeSpec) s1;
    }

    ParseTree.TypeSpec primtypeInt____INT() throws Exception {
        return new ParseTree.TypeSpec("int");
    }

    ParseTree.TypeSpec prim_type____BOOL() throws Exception {
        return new ParseTree.TypeSpec("bool");
    }

    ArrayList<ParseTree.LocalDecl> local_decls____local_decls_local_decl(Object s1, Object s2) throws Exception {
        ArrayList<ParseTree.LocalDecl> localdecls = (ArrayList<ParseTree.LocalDecl>) s1;
        ParseTree.LocalDecl localdecl = (ParseTree.LocalDecl) s2;
        localdecls.add(localdecl);

        return localdecls;
    }

    ArrayList<ParseTree.LocalDecl> localdecls____eps() throws Exception {
        return new ArrayList<ParseTree.LocalDecl>();
    }

    ParseTree.LocalDecl local_decl____VAR_type_spec_IDENT_SEMI(Object s1,Object s2, Object s3) {
        Token var = (Token) s1;
        ParseTree.TypeSpec typespec = (ParseTree.TypeSpec) s2;
        Token id = (Token) s3;

        ParseTree.LocalDecl localdecl = new ParseTree.LocalDecl(id.lexeme, typespec);
        localdecl.info.value_type = typespec.typename;
        localdecl.info.lineno = var.lineno;
        localdecl.info.column = var.column;
        return localdecl;
    }

    ArrayList<ParseTree.Stmt> stmt_list____stmt_list_stmt(Object s1, Object s2) throws Exception {
        ArrayList<ParseTree.Stmt> stmtlist = (ArrayList<ParseTree.Stmt>) s1;
        ParseTree.Stmt stmt = (ParseTree.Stmt) s2;
        stmtlist.add(stmt);
        return stmtlist;
    }

    ArrayList<ParseTree.Stmt> stmtlist____eps() throws Exception {
        return new ArrayList<ParseTree.Stmt>();
    }

    ParseTree.Stmt stmt____assign_stmt(Object s1) throws Exception {
        assert(s1 instanceof ParseTree.AssignStmt);
        return (ParseTree.AssignStmt) s1;
    }

    ParseTree.Stmt stmt____print_stmt(Object s1) throws Exception {
        assert(s1 instanceof ParseTree.PrintStmt);
        return (ParseTree.PrintStmt) s1;
    }

    ParseTree.Stmt stmt____return_stmt(Object s1) throws Exception {
        assert(s1 instanceof ParseTree.ReturnStmt);
        return (ParseTree.ReturnStmt) s1;
    }

    ParseTree.Stmt stmt____if_stmt(Object s1) throws Exception {
        assert(s1 instanceof ParseTree.IfStmt);
        return (ParseTree.IfStmt) s1;
    }

    ParseTree.Stmt stmt____while_stmt(Object s1) throws Exception {
        assert(s1 instanceof ParseTree.WhileStmt);
        return (ParseTree.WhileStmt) s1;
    }

    ParseTree.Stmt stmt____compound_stmt(Object s1) throws Exception {
        assert(s1 instanceof ParseTree.CompoundStmt);
        return (ParseTree.CompoundStmt) s1;
    }

    ParseTree.AssignStmt assign_stmt____IDENT_ASSIGN_expr_SEMI(Object s1, Object s2, Object s3) throws Exception {
        Token id     = (Token) s1;
        Token assign = (Token) s2;
        ParseTree.Expr expr   = (ParseTree.Expr) s3;

        Object id_type = env.Get(id.lexeme);

        
        if (id_type == null) {
            throw new Exception("[Error at " + id.lineno + ":" + id.column + "] " +
                    "Cannot use an undefined variable " + id.lexeme + ".");
        }

        String type = (String) id_type;

        
        if (!type.equals(expr.info.value_type)) {
            throw new Exception("[Error at " + assign.lineno + ":" + assign.column + "] " +
                    "Cannot assign " + expr.info.value_type + " value to " + type + " variable " + id.lexeme + ".");
        }

        ParseTree.AssignStmt stmt = new ParseTree.AssignStmt(id.lexeme, expr);
        stmt.ident_reladdr = 1;
        return stmt;
    }

    ParseTree.PrintStmt print_stmt_____PRINT_expr_SEMI(Object s2) throws Exception {
        ParseTree.Expr expr = (ParseTree.Expr) s2;
        ParseTree.PrintStmt stmt = new ParseTree.PrintStmt(expr);

        return stmt;
    }

    ParseTree.ReturnStmt return_stmt____RETURN_expr_SEMI(Object s2) throws Exception {
        ParseTree.Expr expr = (ParseTree.Expr) s2;
        ParseTree.ReturnStmt stmt = new ParseTree.ReturnStmt(expr);
        stmt.info.value_type = expr.info.value_type;

        ParseTreeInfo.FuncDeclInfo funcdeclinfo = (ParseTreeInfo.FuncDeclInfo) env.Get(currentId);

        
        if (!funcdeclinfo.ret_type.equals(stmt.info.value_type)) {
            throw new Exception("[Error at " + expr.info.lineno + ":" + expr.info.column + "] " +
                    "The type of returning value (" + stmt.info.value_type +  ") should match with the return type (" +  funcdeclinfo.ret_type + ") of the function " + funcdeclinfo.id + "().");
        }

        
        stmt.info.returened = true;
        return stmt;
    }

    ParseTree.IfStmt if_stmt____IF_LPAREN_expr_RPAREN_stmt_ELSE_stmt(Object s3, Object s5, Object s7) throws Exception {
        ParseTree.Expr cond = (ParseTree.Expr) s3;
        ParseTree.Stmt thenstmt = (ParseTree.Stmt) s5;
        ParseTree.Stmt elsestmt = (ParseTree.Stmt) s7;

        
        if (!cond.info.value_type.equals("bool")) {
            throw new Exception("[Error at " + cond.info.lineno + ":" + cond.info.column + "] " +
                    "Use bool value to check the condition in if statement.");
        }

        ParseTree.IfStmt stmt = new ParseTree.IfStmt(cond, thenstmt, elsestmt);
        if (thenstmt.info.returened || elsestmt.info.returened) {
            stmt.info.returened = true;
        }
        return stmt;
    }

    ParseTree.WhileStmt while_stmt____WHILE_LPAREN_expr_RPAREN_stmt(Object s3, Object s5) throws Exception {
        ParseTree.Expr cond = (ParseTree.Expr) s3;
        ParseTree.Stmt whilestmt = (ParseTree.Stmt) s5;

        
        if (!cond.info.value_type.equals("bool")) {
            throw new Exception("[Error at " + cond.info.lineno + ":" + cond.info.column + "] " +
                    "Use bool value to check the condition in while statement.");
        }

        ParseTree.WhileStmt stmt = new ParseTree.WhileStmt(cond, whilestmt);
        if (stmt.info.returened) {
            whilestmt.info.returened = true;
        }
        return stmt;
    }

    ParseTree.CompoundStmt compound_stmt____BEGIN_local_decls(Object s2) throws Exception {
        ArrayList<ParseTree.LocalDecl> localdecls = (ArrayList<ParseTree.LocalDecl>) s2;

        env = new Env(env);

        for (ParseTree.LocalDecl localdecl : localdecls) {
            
            if (env.GetCurrentScope(localdecl.ident) != null) {
                throw new Exception("localdecl current scope error");
            }
            env.Put(localdecl.ident, localdecl.typespec.typename);
        }

        return null;
    }

    ParseTree.CompoundStmt compoundstmt____stmtlist_END(Object s2, Object s3) throws Exception {
        ArrayList<ParseTree.LocalDecl> localdecls = (ArrayList<ParseTree.LocalDecl>) s2;
        ArrayList<ParseTree.Stmt> stmtlist = (ArrayList<ParseTree.Stmt>) s3;


        env = env.prev;

        ParseTree.CompoundStmt compoundstmt = new ParseTree.CompoundStmt(localdecls, stmtlist);
        for (ParseTree.Stmt stmt : stmtlist) {
            if (stmt instanceof ParseTree.ReturnStmt) {
                compoundstmt.info.returened = true;
                break;
            }
        }
        return compoundstmt;
    }

    ArrayList<ParseTree.Arg> args____arg_list(Object s1) throws Exception {
        ArrayList<ParseTree.Arg> arglist = (ArrayList<ParseTree.Arg>) s1;
        return arglist;
    }

    ArrayList<ParseTree.Arg> args____eps() throws Exception {
        return new ArrayList<ParseTree.Arg>();
    }

    ArrayList<ParseTree.Arg> arg_list____arg_list_COMMA_expr(Object s1, Object s3) throws Exception {
        ArrayList<ParseTree.Arg> arglist = (ArrayList<ParseTree.Arg>) s1;
        ParseTree.Expr expr = (ParseTree.Expr) s3;
        arglist.add(new ParseTree.Arg(expr));
        return arglist;
    }

    ArrayList<ParseTree.Arg> arg_list____expr(Object s1) throws Exception {
        ArrayList<ParseTree.Arg> arglist = new ArrayList<>();
        ParseTree.Expr expr = (ParseTree.Expr) s1;
        arglist.add(new ParseTree.Arg(expr));
        return arglist;
    }

    ParseTree.ExprAdd expr____expr_ADD_expr(Object s1, Object s2, Object s3) throws Exception {
        ParseTree.Expr expr1 = (ParseTree.Expr) s1;
        Token oper = (Token) s2;
        ParseTree.Expr expr2 = (ParseTree.Expr) s3;

        
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

    ParseTree.ExprIdent expr____ident(Object s1) throws Exception {
        Token id = (Token) s1;

        
        Object id_type = env.Get(id.lexeme);

        
        if (id_type != null) {

           
            if (id_type instanceof ParseTreeInfo.FuncDeclInfo) {
                throw new Exception("[Error at " + id.lineno + ":" + id.column + "] " +
                        "Cannot use the function " + id.lexeme + "() as a variable.");
            }
        }
        else {
            
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

    ParseTree.ExprIntLit expr_______int_lit(Object s1) throws Exception {
        Token token = (Token) s1;
        int value = Integer.parseInt(token.lexeme);
        ParseTree.ExprIntLit lit = new ParseTree.ExprIntLit(value);

        lit.info.value_type = "int";
        lit.info.lineno = token.lineno;
        lit.info.column = token.column;
        return lit;
    }

    ParseTree.ExprBoolLit expr_______bool_lit(Object s1) throws Exception {
        
        Token token = (Token) s1;
        boolean value = Boolean.parseBoolean(token.lexeme);
        ParseTree.ExprBoolLit lit = new ParseTree.ExprBoolLit(value);

        lit.info.value_type = "bool";
        lit.info.lineno = token.lineno;
        lit.info.column = token.column;
        return lit;
    }

    ParseTree.ExprCall expr____CALL_IDENT_LPAREN_args_RPAREN(Object s1, Object s2, Object s4) throws Exception {
        Token call = (Token) s1;
        Token id = (Token) s2;
        ArrayList<ParseTree.Arg> args = (ArrayList<ParseTree.Arg>) s4;

        Object id_type = env.Get(id.lexeme);

        
        if (env.Get(id.lexeme) != null && env.Get(id.lexeme) instanceof String) {
            throw new Exception("[Error at " + call.lineno + ":" + call.column + "] " +
                    "Cannot use a variable " + id.lexeme + " as a function.");
        }

        ParseTreeInfo.FuncDeclInfo funcdeclinfo = (ParseTreeInfo.FuncDeclInfo) id_type;

        
        if (funcdeclinfo == null) {
            throw new Exception("[Error at " + call.lineno + ":" + call.column + "] " +
                    "Cannot use an undefined function " + id.lexeme + "().");
        }

        
        if (funcdeclinfo.params.size() != args.size()) {
            throw new Exception("[Error at " + call.lineno + ":" + call.column + "] " +
                    "Cannot pass the incorrect number of arguments to " + funcdeclinfo.id + "().");
        }

       
        for (int i = 0; i < funcdeclinfo.params.size(); i++) {
            ParseTree.Param param = funcdeclinfo.params.get(i);
            ParseTree.Arg arg = args.get(i);

            
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
        expr.info.value_type = funcdeclinfo.ret_type;
        expr.info.lineno = id.lineno;
        expr.info.column = id.column;
        return expr;
    }
}
