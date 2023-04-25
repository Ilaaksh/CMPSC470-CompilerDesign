import java.util.*;
import java.util.HashMap;

@SuppressWarnings("unchecked")
public class ParserImpl
{

    public String ret_type = "";

    public static Boolean _debug = true;
    void Debug(String message)
    {
        if(_debug)
            System.out.println(message);
    }

    // This is for chained symbol table.
    // This includes the global scope only at this moment.
    Env env = new Env(null);
    // this stores the root of parse tree, which will be used to print parse tree and run the parse tree
    ParseTree.Program parsetree_program = null;


    ////////////////////////////////////////////////////// Code Start ////////////////////////////////////////////////////////

    Object program____decllist(Object s1) throws Exception
    {
        // 1. check if decllist has main function having no parameters and returns int type
        // 2. assign the root, whose type is ParseTree.Program, to parsetree_program
        ArrayList<ParseTree.FuncDecl> decllist = (ArrayList<ParseTree.FuncDecl>)s1;
        parsetree_program = new ParseTree.Program(decllist);

        if( env.Get("main") == null){
            throw new Exception("The program must have one main function that returns int type and has no parameters.");
        }
        return parsetree_program;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    Object decllist____decllist_decl(Object s1, Object s2) throws Exception
    {
        ArrayList<ParseTree.FuncDecl> decllist = (ArrayList<ParseTree.FuncDecl>)s1;
        ParseTree.FuncDecl                decl = (ParseTree.FuncDecl           )s2;

        decllist.add(decl);
        return decllist;
    }
    Object decllist____eps() throws Exception
    {
        return new ArrayList<ParseTree.FuncDecl>();
    }
    Object decl____fundecl(Object s1) throws Exception
    {
        return s1;
    }

    Object primtype____INT() throws Exception
    {
        ParseTree.TypeSpec typespec = new ParseTree.TypeSpec("int");
        return typespec;
    }
    Object primtype____BOOL() throws Exception{

        ParseTree.TypeSpec typespec = new ParseTree.TypeSpec("bool");
        return typespec;
    }


    Object typespec____primtype(Object s1)
    {
        ParseTree.TypeSpec primtype = (ParseTree.TypeSpec)s1;
        return primtype;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    Object fundecl____FUNC_IDENT_LPAREN_params_RPAREN_FUNCRET_primtype_BEGIN_localdecls_10X_stmtlist_END(Object s2, Object s4, Object s7, Object s9) throws Exception
    {
        Token                            id         = (Token                           )s2;
        ArrayList<ParseTree.Param>       params     = (ArrayList<ParseTree.Param>      )s4;
        ParseTree.TypeSpec               rettype    = (ParseTree.TypeSpec              )s7;
        ArrayList<ParseTree.LocalDecl>   localdecls = (ArrayList<ParseTree.LocalDecl>  )s9;
        env.Put(id.lexeme, rettype);
        ParseTree.FuncDecl funcdecl= new ParseTree.FuncDecl(id.lexeme, rettype, params, localdecls, null);
        ret_type = id.lexeme;
        funcdecl.info.value_type=rettype.typename;
        return funcdecl;
    }
    Object fundecl____FUNC_IDENT_LPAREN_params_RPAREN_FUNCRET_primtype_BEGIN_localdecls_X10_stmtlist_END(Object s2, Object s4, Object s7, Object s9, Object s11, Object s12) throws Exception
    {
        // 1. check if this function has at least one return type
        // 2. etc.
        // 3. create and return funcdecl node
        Token                            id         = (Token                           )s2;
        ArrayList<ParseTree.Param>       params     = (ArrayList<ParseTree.Param>      )s4;
        ParseTree.TypeSpec               rettype    = (ParseTree.TypeSpec              )s7;
        ArrayList<ParseTree.LocalDecl>   localdecls = (ArrayList<ParseTree.LocalDecl>  )s9;
        ArrayList<ParseTree.Stmt>        stmtlist   = (ArrayList<ParseTree.Stmt>       )s11;
        Token                            end        = (Token                           )s12;
        ParseTree.FuncDecl funcdecl = new ParseTree.FuncDecl(id.lexeme, rettype, params, localdecls, stmtlist);
        funcdecl.info.value_type=rettype.typename;
        return funcdecl;
    }


    //params
    Object params____paramlist(Object s1) throws Exception
    {
        return s1;
    }

    Object params____eps() throws Exception
    {
        return new ArrayList<ParseTree.Param>();
    }

    Object paramlist____paramlist_COMMA_param(Object s1, Object s2, Object s3) throws Exception{

        ArrayList<ParseTree.Param> param_list = (ArrayList<ParseTree.Param>)s1;
        ParseTree.Param            param    = (ParseTree.Param           )s2;
        param_list.add(param);
        return param_list;
    }

    Object paramlist____param(Object s1) throws Exception{
        ParseTree.Param              param = (ParseTree.Param)s1;
        return param;
    }

    Object param____VAR_typespec_IDENT(Object s1, Object s2, Object s3) throws Exception{
        Token          id     = (Token         )s3;
        Token          typeof = (Token         )s2;
        ParseTree.TypeSpec type_spec   = (ParseTree.TypeSpec) s1;
        return new ParseTree.Param(id.lexeme, type_spec);
    }

    Object stmtlist____stmtlist_stmt(Object s1, Object s2) throws Exception
    {
        ArrayList<ParseTree.Stmt> stmtlist = (ArrayList<ParseTree.Stmt>)s1;
        ParseTree.Stmt            stmt     = (ParseTree.Stmt           )s2;
        stmtlist.add(stmt);
        return stmtlist;
    }
    Object stmtlist____eps() throws Exception
    {
        return new ArrayList<ParseTree.Stmt>();
    }

    Object stmt____assignstmt  (Object s1) throws Exception
    {
        assert(s1 instanceof ParseTree.AssignStmt);
        return s1;
    }
    Object stmt____printstmt  (Object s1) throws Exception
    {
        assert(s1 instanceof ParseTree.PrintStmt);
        return s1;
    }
    Object stmt____returnstmt  (Object s1) throws Exception
    {
        assert(s1 instanceof ParseTree.ReturnStmt);
        return s1;
    }
    Object stmt____ifstmt  (Object s1) throws Exception
    {
        assert(s1 instanceof ParseTree.IfStmt);
        return s1;
    }
    Object stmt____whilestmt  (Object s1) throws Exception
    {
        assert(s1 instanceof ParseTree.WhileStmt);
        return s1;
    }
    Object stmt____compoundstmt  (Object s1) throws Exception
    {
        assert(s1 instanceof ParseTree.CompoundStmt);
        return s1;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    Object assignstmt____IDENT_ASSIGN_expr_SEMI(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if ident.value_type matches with expr.value_type
        // 2. etc.
        // e. create and return node
        Token          id     = (Token         )s1;
        Token          assign = (Token         )s2;
        ParseTree.Expr expr   = (ParseTree.Expr)s3;
        Object id_type = env.Get(id.lexeme);
        {   
            if (id_type==null){throw new Exception("IDENT does not exist");}
            // check if expr.type matches with id_type
            if(id_type.equals("int") && expr.info.value_type=="int")
                {
                } // ok
            else if (id_type.equals("bool") && expr.info.value_type=="bool")
            {
            }    
            else
            {
                throw new Exception("semantic error  in assign statement");
            }
        }
        ParseTree.AssignStmt stmt = new ParseTree.AssignStmt(id.lexeme, expr);
        stmt.info.stmt = "assingment";
        stmt.ident_reladdr = 1;
        return stmt;
    }
    Object printnstmt____PRINT_expr_SEMI(Object s2) throws Exception
    {
        // 1. check if expr.value_type matches with the current function return type
        // 2. etc.
        // 3. create and return node
        ParseTree.Expr expr = (ParseTree.Expr)s2;
        return new ParseTree.PrintStmt(expr);
    }

    Object returnstmt____RETURN_expr_SEMI(Object s2) throws Exception
    {
        // 1. check if expr.value_type matches with the current function return type
        // 2. etc.
        // 3. create and return node

        ParseTree.Expr expr = (ParseTree.Expr)s2;
        if(!ret_type.equals(expr.info.value_type)){
            throw new Exception("[Error at "+expr.info+":"+expr.info+"] The type of returning value ("+expr.info.value_type+") should match with the return type ("+(((ParseTree.TypeSpec)env.Get(ret_type)).typename)+") of the function main().");
        }

        return new ParseTree.ReturnStmt(expr);
    }

    Object ifstmt____IF_LPAREN_expr_RPAREN_stmt_ELSE_stmt(Object s1,Object s2,Object s3) throws Exception{

        return (ParseTree.IfStmt) s1;
    }

    Object whilestmt____WHILE_LPAREN_expr_RPAREN_stmt(Object s1, Object s2) throws Exception{

        return (ParseTree.WhileStmt)s1;
    }

    Object compoundstmt____BEGIN_localdecls(Object s2) throws Exception{

        return s2;
    }
    Object compoundstmt____stmtlist_END(Object s2, Object s4) throws Exception{

        return s2;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    Object localdecls____localdecls_localdecl(Object s1, Object s2)
    {
        ArrayList<ParseTree.LocalDecl> localdecls = (ArrayList<ParseTree.LocalDecl>)s1;
        ParseTree.LocalDecl            localdecl  = (ParseTree.LocalDecl           )s2;
        localdecls.add(localdecl);
        return localdecls;
    }
    Object localdecls____eps() throws Exception
    {
        return new ArrayList<ParseTree.LocalDecl>();
    }
    Object localdecl____VAR_typespec_IDENT_SEMI(Object s2, Object s3)
    {
        ParseTree.TypeSpec typespec = (ParseTree.TypeSpec)s2;
        Token              id       = (Token             )s3;
        ParseTree.LocalDecl localdecl = new ParseTree.LocalDecl(id.lexeme, typespec);
        localdecl.reladdr = 1;
        return localdecl;
    }

    Object args____arglist(Object s1) throws Exception{
        return new ArrayList<ParseTree.Arg>();

    }



//  ///////////////////////////////////////////////////////////---------------------------------------------------------------------------
    Object args____eps() throws Exception
    {
        return new ArrayList<ParseTree.Arg>();
    }

    Object arglist____arglist_COMMA_expr(Object s1, Object s2) throws Exception{

        return (ArrayList<ParseTree.Arg>) s1;
    }

    Object arglist____expr(Object s1) throws Exception{

        return (ArrayList<ParseTree.Arg>) s1;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    Object expr____expr_ADD_expr(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if expr1.value_type matches with the expr2.value_type
        // 2. etc.
        // 3. create and return node that has value_type
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          oper  = (Token         )s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;
        ParseTree.ExprAdd add = new ParseTree.ExprAdd(expr1, expr2);
        if(!expr1.info.value_type.equals("bool")|| expr2.info.value_type.equals("int")){
            throw new Exception("expr1.value_type doesnt matches with the expr2.value_type ADD");
        }
        add.info.value_type = ("bool");
        return add;
    }
    Object expr____expr_SUB_expr(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if expr1.value_type matches with the expr2.value_type
        // 2. etc.
        // 3. create and return node that has value_type
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          oper  = (Token         )s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;
        // check if expr1.type matches with expr2.type
        ParseTree.ExprSub sub = new ParseTree.ExprSub(expr1, expr2);
        if(!expr1.info.value_type.equals("bool")|| expr2.info.value_type.equals("int")){
            throw new Exception("expr1.value_type doesnt matches with the expr2.value_type SUB");
        }
        sub.info.value_type = ("bool");
        return sub;
    }
    Object expr____expr_MUL_expr(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if expr1.value_type matches with the expr2.value_type
        // 2. etc.
        // 3. create and return node that has value_type
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          oper  = (Token         )s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;
        // check if expr1.type matches with expr2.type
        ParseTree.ExprMul mul = new ParseTree.ExprMul(expr1, expr2);
        if(!expr1.info.value_type.equals("bool")|| expr2.info.value_type.equals("int")){
            throw new Exception("expr1.value_type doesnt matches with the expr2.value_type MUL");
        }
        mul.info.value_type = ("bool");
        return mul;
    }
    Object expr____expr_DIV_expr(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if expr1.value_type matches with the expr2.value_type
        // 2. etc.
        // 3. create and return node that has value_type
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          oper  = (Token         )s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;
        // check if expr1.type matches with expr2.type
        ParseTree.ExprDiv div = new ParseTree.ExprDiv(expr1, expr2);
        if(!expr1.info.value_type.equals("bool")|| expr2.info.value_type.equals("int")){
            throw new Exception("expr1.value_type doesnt matches with the expr2.value_type DIV");
        }
        div.info.value_type = ("bool");
        return div;
    }
    Object expr____expr_MOD_expr(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if expr1.value_type matches with the expr2.value_type
        // 2. etc.
        // 3. create and return node that has value_type
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          oper  = (Token         )s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;
        // check if expr1.type matches with expr2.type
        ParseTree.ExprMod mod = new ParseTree.ExprMod(expr1, expr2);
        if(!expr1.info.value_type.equals("bool")|| expr2.info.value_type.equals("int")){
            throw new Exception("expr1.value_type doesnt matches with the expr2.value_type MOD");
        }
        mod.info.value_type = ("bool");
        return mod;
    }
    
    Object expr____expr_EQ_expr(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if expr1.value_type matches with the expr2.value_type
        // 2. etc.
        // 3. create and return node that has value_type
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          oper  = (Token         )s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;
        // check if expr1.type matches with expr2.type
        ParseTree.ExprEq eq = new ParseTree.ExprEq(expr1, expr2);
        if(!expr1.info.value_type.equals("bool")|| expr2.info.value_type.equals("int")){
            throw new Exception("expr1.value_type doesnt matches with the expr2.value_type EQ");
        }
        eq.info.value_type = ("bool");
        return eq;
    }
    Object expr____expr_NE_expr(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if expr1.value_type matches with the expr2.value_type
        // 2. etc.
        // 3. create and return node that has value_type
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          oper  = (Token         )s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;
        // check if expr1.type matches with expr2.type
        ParseTree.ExprNe ne = new ParseTree.ExprNe(expr1, expr2);
        if(!expr1.info.value_type.equals("bool")|| expr2.info.value_type.equals("int")){
            throw new Exception("expr1.value_type doesnt matches with the expr2.value_type NE");
        }
        ne.info.value_type = ("bool");
        return ne;
    }
    Object expr____expr_LE_expr(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if expr1.value_type matches with the expr2.value_type
        // 2. etc.
        // 3. create and return node that has value_type
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          oper  = (Token         )s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;
        // check if expr1.type matches with expr2.type
        ParseTree.ExprLe le = new ParseTree.ExprLe(expr1, expr2);
        if(!expr1.info.value_type.equals("bool")|| expr2.info.value_type.equals("int")){
            throw new Exception("expr1.value_type doesnt matches with the expr2.value_type LE");
        }
        le.info.value_type = ("bool");
        return le;
    }
    Object expr____expr_LT_expr(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if expr1.value_type matches with the expr2.value_type
        // 2. etc.
        // 3. create and return node that has value_type
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          oper  = (Token         )s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;
        // check if expr1.type matches with expr2.type
        ParseTree.ExprLt lt = new ParseTree.ExprLt(expr1, expr2);
        if(!expr1.info.value_type.equals("bool")|| expr2.info.value_type.equals("int")){
            throw new Exception("expr1.value_type doesnt matches with the expr2.value_type LT");
        }
        lt.info.value_type = ("bool");
        return lt;
    }
    Object expr____expr_GE_expr(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if expr1.value_type matches with the expr2.value_type
        // 2. etc.
        // 3. create and return node that has value_type
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          oper  = (Token         )s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;
        // check if expr1.type matches with expr2.type
        ParseTree.ExprGe ge = new ParseTree.ExprGe(expr1, expr2);
        if(!expr1.info.value_type.equals("bool")|| expr2.info.value_type.equals("int")){
            throw new Exception("expr1.value_type doesnt matches with the expr2.value_type GE");
        }
        ge.info.value_type = ("bool");
        return ge;
    }
    Object expr____expr_GT_expr(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if expr1.value_type matches with the expr2.value_type
        // 2. etc.
        // 3. create and return node that has value_type
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          oper  = (Token         )s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;
        // check if expr1.type matches with expr2.type
        ParseTree.ExprGt gt = new ParseTree.ExprGt(expr1, expr2);
        if(!expr1.info.value_type.equals("bool")|| expr2.info.value_type.equals("int")){
            throw new Exception("expr1.value_type doesnt matches with the expr2.value_type GT");
        }
        gt.info.value_type = ("bool");
        return gt;
    }
    Object expr____expr_AND_expr(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if expr1.value_type matches with the expr2.value_type
        // 2. etc.
        // 3. create and return node that has value_type
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          oper  = (Token         )s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;
        // check if expr1.type matches with expr2.type
        ParseTree.ExprAnd and = new ParseTree.ExprAnd(expr1, expr2);
        if(!expr1.info.value_type.equals("bool")|| expr2.info.value_type.equals("int")){
            throw new Exception("expr1.value_type doesnt matches with the expr2.value_type AND");
        }
        and.info.value_type = ("bool");
        return and;
    }
    Object expr____expr_OR_expr(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if expr1.value_type matches with the expr2.value_type
        // 2. etc.
        // 3. create and return node that has value_type
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          oper  = (Token         )s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;
        // check if expr1.type matches with expr2.type
        ParseTree.ExprOr or = new ParseTree.ExprOr(expr1, expr2);
        if(!expr1.info.value_type.equals("bool")|| expr2.info.value_type.equals("int")){
            throw new Exception("expr1.value_type doesnt matches with the expr2.value_type OR");
        }
        or.info.value_type = ("bool");

        return or;
    }
    Object expr____NOT_expr (Object s1, Object s2) throws Exception
    {
        // 1. check if expr1.value_type matches with the expr2.value_type
        // 2. etc.
        // 3. create and return node that has value_type
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          oper  = (Token         )s2;
        // check if expr1.type matches with expr2.type
        ParseTree.ExprNot not = new ParseTree.ExprNot(expr1);
        if (!expr1.info.value_type.equals("bool")){
            throw new Exception("expr1.value_type doesnt matches with the expr2.value_type NOT");
        }
        not.info.value_type = ("bool");
        return not;
    }
    Object expr____LPAREN_expr_RPAREN(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. create and return node whose value_type is the same to the expr.value_type
        Token          lparen = (Token         )s1;
        ParseTree.Expr expr   = (ParseTree.Expr)s2;
        Token          rparen = (Token         )s3;
        ParseTree.ExprParen paren = new ParseTree.ExprParen(expr);
        paren.info.value_type = expr.info.value_type;
        paren.info.value = expr.info.value;
        return new ParseTree.ExprParen(expr);
    }
    Object expr____IDENT(Object s1) throws Exception
    {
        // 1. check if id.lexeme can be found in chained symbol tables
        // 2. check if it is variable type
        // 3. etc.
        // 4. create and return node that has the value_type of the id.lexeme
        Token id = (Token)s1;
        
        if (env.Get(id.lexeme) == null){ throw new Exception("IDENT not found");}
        ParseTree.ExprIdent ident =  new ParseTree.ExprIdent(id.lexeme);
        ident.info.value_type = env.Get(id.lexeme).toString();
        ident.reladdr = 1;
        return ident;
    }
    //TODO
    Object expr____CALL_IDENT_LPAREN_args_RPAREN(Object s2, Object s4) throws Exception
    {
        // 1. check if id.lexeme can be found in chained symbol tables
        // 2. check if it is function type
        // 3. check if the number and types of env(id.lexeme).params match with those of args
        // 4. etc.
        // 5. create and return node that has the value_type of env(id.lexeme).return_type
        Token                    id   = (Token                   )s2;
        ArrayList<ParseTree.Arg> args = (ArrayList<ParseTree.Arg>)s4;
        Object func_attr = env.Get(id.lexeme);
        if (id.lexeme==null){ throw new Exception("Symbol not found");}
        {
            // check if argument types match with function param types
            if(env.Get(id.lexeme).equals("func()->int")
                && (args.size() == 0)
                )
            {

            } // ok
            else
            {
                throw new Exception("semantic error");
            }
        }
        return new ParseTree.ExprCall(id.lexeme, args);
    }
    Object expr____INT_LIT(Object s1) throws Exception
    {
        // 1. create and return node that has int type
        Token token = (Token)s1;
        int value = Integer.parseInt(token.lexeme);
        ParseTree.ExprIntLit lit = new ParseTree.ExprIntLit(value);
        lit.info.value_type = ("int"); 
        lit.info.value = value;
        return lit;
    }
    Object expr____BOOL_LIT(Object s1) throws Exception
    {
        // 1. create and return node that has int type
        Token token = (Token)s1;
        boolean value = Boolean.parseBoolean(token.lexeme);
        ParseTree.ExprBoolLit lit = new ParseTree.ExprBoolLit(value);
        lit.info.value_type = ("bool"); 
        lit.info.value = value;
        return lit;

    }
}