import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class ParseTree
{
    public static enum OptToString
    {
        Default,
        CommentRunEnv
    }

    public static class RunEnv
    {
        public static RunEnv StackFrame;

        // https://en.wikipedia.org/wiki/Call_stack
        // function-running environment
        class StackFrame
        {
            HashMap<Integer, Object> idx_val;
                // the runtime environment of running function
                // idx -i: i-th param
                // idx  0: return value
                // idx  i: i-th local variable
            public StackFrame()
            {
                idx_val = new HashMap<Integer, Object>();
            }
            public boolean HasValue(int reladdr)
            {
                // get value at reladdr (relative address)
                return idx_val.containsKey(reladdr);
            }
            public Object GetValue(int reladdr)
            {
                // get value at reladdr (relative address)
                assert(idx_val.containsKey(reladdr));
                Object val = idx_val.get(reladdr);
                return val;
            }
            public void SetValue(int reladdr, Object val)
            {
                // update value at reladdr (relative address)
                idx_val.put(reladdr, val);
            }
        }
        // RunEnv maintains the program running environment by maintaining
        // * the function call stack (each func-call stack item contains the running environment including variable vlaues)
        // * the map for func_name -> func_body
        public Stack<StackFrame>            stackframes;        // stack frame list (stack of function call)
        public HashMap<String, FuncDecl>    funcname_funcdecl;  // name -> func body/node

        public RunEnv(ArrayList<ParseTree.FuncDecl> funcs)
        {
            // create calling stack environment
            stackframes = new Stack<StackFrame>();
            // create name->function map
            funcname_funcdecl = new HashMap<String, FuncDecl>();
            for(FuncDecl func : funcs)
            {
                funcname_funcdecl.put(func.ident, func);
            }
        }
        public StackFrame GetTopStackFrame()
        {
            StackFrame top = stackframes.peek();
            return top;
        }
        public void PushStackFrame()
        {
            // push a new function-running environment (stackframe)
            // to prepare function call
            stackframes.push(new StackFrame());
        }
        public void PopStackFrame()
        {
            // pop the used function-running environment
            // to cleaning the called-function environment
            stackframes.pop();
        }
    }

    public static abstract class Node
    {
        abstract public String[] ToStringList(OptToString opt) throws Exception; // This is used to print conde with indentation and comments
    }

    public static class TypeSpec extends Node
    {
        public ParseTreeInfo.TypeSpecInfo info = new ParseTreeInfo.TypeSpecInfo();  // store your own data in TypeSpecInfo
        public String typename;
        public TypeSpec(String typename)              { this.typename = typename; }
        public String ToString(OptToString opt)       { return typename; }
        public String[] ToStringList(OptToString opt) { return new String[] { ToString(opt) }; }
    }

    public static class Program extends Node
    {
        public ParseTreeInfo.ProgramInfo info = new ParseTreeInfo.ProgramInfo();    // store your own data in ProgramInfo
        public ArrayList<FuncDecl> funcs;
        public Program(ArrayList<FuncDecl> funcs)
        {
            this.funcs = funcs;
        }
        public Object Exec() throws Exception
        {
            ParseTree.RunEnv runenv = new ParseTree.RunEnv(funcs);  // prepare running environment
            FuncDecl main = runenv.funcname_funcdecl.get("main");   // find main function
            Object ret = main.Exec(runenv, new Object[0]);          // run the main function
            return ret;                                             // return value
        }
        public String[] ToStringList(OptToString opt) throws Exception
        {
            ArrayList<String> strs = new ArrayList<String>();
            for(var func : funcs)
            {
                for(String str : func.ToStringList(opt))
                    strs.add(str);
            }
            return strs.toArray(String[]::new);
        }
    }

    public static class FuncDecl extends Node
    {
        public ParseTreeInfo.FuncDeclInfo info = new ParseTreeInfo.FuncDeclInfo();  // store your own data in FuncDeclInfo
        public String               ident     ;
        public TypeSpec             rettype   ;
        public ArrayList<Param    > params    ;
        public ArrayList<LocalDecl> localdecls;
        public ArrayList<Stmt     > stmtlist  ;
        public FuncDecl(String ident, TypeSpec rettype, ArrayList<Param> params, ArrayList<LocalDecl> localdecls, ArrayList<Stmt> stmtlist)
        {
            this.ident      = ident     ;
            this.rettype    = rettype   ;
            this.params     = params    ;
            this.localdecls = localdecls;
            this.stmtlist   = stmtlist  ;
        }
        public Object Exec(RunEnv runenv, Object[] vals) throws Exception
        {
            // enter function environment
            runenv.PushStackFrame();
            Object ret;
            {
                // pass params' values into the running-function's stackframe
                for(int i=0; i<vals.length; i++)
                {
                    Param param = params.get(i);
                    if(param.reladdr == null)
                        throw new Exception("The "+i+"-th Param.reladdr is not assigned.");
                    runenv.GetTopStackFrame().SetValue(param.reladdr, vals[i]);
                }
            
                // call function's instructions
                for(Stmt stmt : stmtlist)
                {
                    Stmt.ExecStatus stat = stmt.Exec(runenv);
                    if(stat == Stmt.ExecStatus.Return)
                        break;
                }

                // get return value
                ret = runenv.GetTopStackFrame().GetValue(0);
            }
            // leave function environment
            runenv.PopStackFrame();

            return ret;
        }
        public String[] ToStringList(OptToString opt) throws Exception
        {
            String head = "func " + ident + "(";
            if(params.size() != 0) head += " ";
            for(int i=0; i<params.size(); i++)
                if(i == 0) head +=      params.get(i).ToString(opt);
                else       head += ", "+params.get(i).ToString(opt);
            if(params.size() != 0) head += " ";
            head += ") -> " + rettype.ToString(opt);

            ArrayList<String> strs = new ArrayList<String>();
            strs.add(head);
            if(opt == OptToString.CommentRunEnv)
            {   // print comments for running environment
                for(int i=0; i<params.size(); i++)
                {
                    Param param = params.get(i);
                    if(param.reladdr == null)
                        throw new Exception("The "+i+"-th Param.reladdr is not assigned.");
                    strs.add("## relative address of parameter "+param.ident+" from this func call base pointer is "+param.reladdr+"");
                }
            }
            strs.add("{");
            for(var localdecl : localdecls)
                strs.add("    " + localdecl.ToString(opt));
            for(var stmt : stmtlist)
                for(String str : stmt.ToStringList(opt))
                    strs.add("    "+str);
            strs.add("}");

            return strs.toArray(String[]::new);
        }
    }

    public static class Param extends Node
    {
        public ParseTreeInfo.ParamInfo info = new ParseTreeInfo.ParamInfo();    // store your own data in ParamInfo
        public String   ident   ;
        public TypeSpec typespec;
        public Integer  reladdr = null; // assign this value later for running the parse tree
        public Param(String ident, TypeSpec typespec)
        {
            this.ident    = ident   ;
            this.typespec = typespec;
        }
        public String[] ToStringList(OptToString opt) { return new String[] { ToString(opt) }; }
        public String   ToString(OptToString opt)     { return "var " + typespec.ToString(opt) + " " + ident; }
    }

    public static class LocalDecl extends Node
    {
        public ParseTreeInfo.LocalDeclInfo info = new ParseTreeInfo.LocalDeclInfo();    // store your own data in LocalDeclInfo
        public String   ident   ;
        public TypeSpec typespec;
        public Integer  reladdr = null; // assign this value later for running the parse tree
        public LocalDecl(String ident, TypeSpec typespec)
        {
            this.ident    = ident   ;
            this.typespec = typespec;
        }
        public String[] ToStringList(OptToString opt) { return new String[] { ToString(opt) }; }
        public String ToString(OptToString opt)
        {
            String str ="var " + typespec.ToString(opt) + " " + ident + ";";
            if(opt == OptToString.CommentRunEnv)
            {   // print comments for running environment
                str += " ## relative address of local variable "+ident+" from this func call base pointer is "+reladdr+"";
            }
            return str;
        }
    }

    public abstract static class Stmt extends Node
    {
        public static enum ExecStatus
        {
            Normal,     // normal statement
            Return      // return function-call without running other statements
        }

        public ParseTreeInfo.StmtStmtInfo info = new ParseTreeInfo.StmtStmtInfo();  // store your own data in StmtStmtInfo
        abstract public ExecStatus Exec(RunEnv runenv)         throws Exception;
        abstract public String[] ToStringList(OptToString opt) throws Exception;
    }
    public static class AssignStmt extends Stmt
    {
        public String  ident;
        public Integer ident_reladdr = null; // assign this value later for running the parse tree
        public Expr    expr ;
        public AssignStmt(String ident, Expr expr)
        {
            this.ident = ident;
            this.expr  = expr ;
        }
        public ExecStatus Exec(RunEnv runenv) throws Exception
        {
            Object exprval = expr.Exec(runenv);                         // get value of expr
            runenv.GetTopStackFrame().SetValue(ident_reladdr, exprval); // update the value to ident_reladdr
            return ExecStatus.Normal;
        }
        public String[] ToStringList(OptToString opt) throws Exception 
        {
            String str = ident;
            if(opt == OptToString.CommentRunEnv)
            {   // print comments for running environment
                if(ident_reladdr == null)
                    throw new Exception("AssignStmt.ident_reladdr is not assigned yet.");
                str += "["+ident_reladdr+"]";
            }
            str  += " <- " + expr.ToString(opt) + ";";
            return new String[] { str };
        }
    }
    public static class PrintStmt extends Stmt
    {
        public Expr expr;
        public PrintStmt(Expr expr)
        {
            this.expr = expr;
        }
        public ExecStatus Exec(RunEnv runenv) throws Exception
        {
            Object exprval = expr.Exec(runenv); // get value
            System.out.println(exprval);        // print on screen
            return ExecStatus.Normal;
        }
        public String[] ToStringList(OptToString opt) throws Exception
        {
            return new String[]
            {
                "print " + expr.ToString(opt) + ";"
            };
        }
    }
    public static class ReturnStmt extends Stmt
    {
        public Expr expr;
        public ReturnStmt(Expr expr)
        {
            this.expr = expr;
        }
        public ExecStatus Exec(RunEnv runenv) throws Exception
        {
            Object val = expr.Exec(runenv);              // evaluate the return value
            runenv.GetTopStackFrame().SetValue(0, val);  // assign the return value at location 0
            return ExecStatus.Return;                    // exec status is "return"
        }
        public String[] ToStringList(OptToString opt) throws Exception
        {
            return new String[]
            {
                "return " + expr.ToString(opt) + ";"
            };
        }
    }
    public static class IfStmt extends Stmt
    {
        public Expr cond    ;
        public Stmt thenstmt;
        public Stmt elsestmt;
        public IfStmt(Expr cond, Stmt thenstmt, Stmt elsestmt)
        {
            this.cond     = cond    ;
            this.thenstmt = thenstmt;
            this.elsestmt = elsestmt;
        }
        public ExecStatus Exec(RunEnv runenv) throws Exception
        {
            // get the conditional value
            Object condval = cond.Exec(runenv);

            // if the condition is true, exec thenstmts
            // if not                  , exec elsestmts
            if((boolean)condval)
            {
                ExecStatus stat = thenstmt.Exec(runenv);
                if(stat == ExecStatus.Return)
                    return ExecStatus.Return;
            }
            else
            {
                ExecStatus stat = elsestmt.Exec(runenv);
                if(stat == ExecStatus.Return)
                    return ExecStatus.Return;
            }

            return null;
        }
        public String[] ToStringList(OptToString opt) throws Exception
        {
            ArrayList<String> strs = new ArrayList<String>();
            strs.add("if( " + cond.ToString(opt) + " )");
            for(String str : thenstmt.ToStringList(opt))
                strs.add("    "+str);
            strs.add("else");
            for(String str : elsestmt.ToStringList(opt))
                strs.add("    "+str);
            return strs.toArray(String[]::new);
        }
    }
    public static class WhileStmt extends Stmt
    {
        public Expr cond;
        public Stmt stmt;
        public WhileStmt(Expr cond, Stmt stmt)
        {
            this.cond = cond;
            this.stmt = stmt;
        }
        public ExecStatus Exec(RunEnv runenv) throws Exception
        {
            while(true)
            {
                // if condition is false, break
                // if not               , repeat
                Object condval = cond.Exec(runenv);
                if((boolean)condval == false)
                    break;

                // exec stat
                ExecStatus stat = stmt.Exec(runenv);
                if(stat == ExecStatus.Return)
                    return ExecStatus.Return;
            }
            return null;
        }
        public String[] ToStringList(OptToString opt) throws Exception
        {
            ArrayList<String> strs = new ArrayList<String>();
            strs.add("while( " + cond.ToString(opt) + " )");
            for(String str : stmt.ToStringList(opt))
                strs.add(str);
            return strs.toArray(String[]::new);
        }
    }
    public static class CompoundStmt extends Stmt
    {
        public ArrayList<LocalDecl> localdecls;
        public ArrayList<Stmt     > stmtlist  ;
        public CompoundStmt(ArrayList<LocalDecl> localdecls, ArrayList<Stmt> stmtlist)
        {
            this.localdecls = localdecls;
            this.stmtlist   = stmtlist  ;
        }
        public ExecStatus Exec(RunEnv runenv) throws Exception
        {
            for(Stmt stmt : stmtlist)
            {
                // exec each stmt
                ExecStatus stat = stmt.Exec(runenv);

                // if exec_status was return,
                // then steop exec, and return with "return" to prevent executing other statements
                if(stat == ExecStatus.Return)           
                    return ExecStatus.Return;
            }
            return null;
        }
        public String[] ToStringList(OptToString opt) throws Exception
        {
            ArrayList<String> strs = new ArrayList<String>();
            strs.add("{");
            for(LocalDecl localdecl : localdecls)
                strs.add("    " + localdecl.ToString(opt));
            for(Stmt stmt : stmtlist)
                for(String str : stmt.ToStringList(opt))
                    strs.add("    "+str);
            strs.add("}");
            return strs.toArray(String[]::new);
        }
    }

    public static class Arg extends Node
    {
        public ParseTreeInfo.ArgInfo info = new ParseTreeInfo.ArgInfo();    // store your own data in ArgInfo
        public Expr expr;
        public Arg(Expr expr)                                          { this.expr = expr;       }
        public Object Exec(RunEnv runenv)             throws Exception { return expr.Exec(runenv); }
        public String ToString(OptToString opt)       throws Exception { return expr.ToString(opt); }
        public String[] ToStringList(OptToString opt) throws Exception { return new String[] { ToString(opt) }; }
    }

    public static abstract class Expr extends Node
    {
        public ParseTreeInfo.ExprInfo info = new ParseTreeInfo.ExprInfo();  // store your own data in ExprInfo
        abstract public Object Exec(RunEnv runenv)       throws Exception;
        abstract public String ToString(OptToString opt) throws Exception;
        public String[] ToStringList(OptToString opt)    throws Exception { return new String[] { ToString(opt) }; }
    }
    public static class ExprBoolLit extends Expr
    {
        boolean val;
        public ExprBoolLit(boolean val)         { this.val = val; }
        public Object Exec(RunEnv runenv)       { return val; }
        public String ToString(OptToString opt) { return ""+val; }
    }
    public static class ExprIntLit extends Expr
    {
        Integer val;
        public ExprIntLit (Integer val)         { this.val = val; }
        public Object Exec(RunEnv runenv)       { return val; }
        public String ToString(OptToString opt) { return ""+val; }
    }
    public static class ExprIdent extends Expr
    {
        public String   ident;
        public Integer  reladdr = null; // assign this value later for running the parse tree
        public ExprIdent  (String ident)  { this.ident = ident; }
        public Object Exec(RunEnv runenv) throws Exception
        {
            if(reladdr == null)
                throw new Exception("ExprIdent.reladdr is not assigned.");
            if(runenv.GetTopStackFrame().HasValue(reladdr) == false)
                throw new Exception("unassigned variable is used.");
            return runenv.GetTopStackFrame().GetValue(reladdr);
        }
        public String ToString(OptToString opt) throws Exception
        {
            String str = ident;
            if(opt == OptToString.CommentRunEnv)
            {   // print comments for running environment
                if(reladdr == null)
                    throw new Exception("ExprIdent.reladdr is not assigned.");
                str += "["+reladdr+"]";
            }
            return str;
        }
    }
    public static class ExprCall extends Expr
    {
        public String         ident;
        public ArrayList<Arg> args ;

        public ExprCall(String ident, ArrayList<Arg> args)
        {
            this.ident = ident;
            this.args  = args ;
        }
        public Object Exec(RunEnv runenv) throws Exception
        {
            FuncDecl func = runenv.funcname_funcdecl.get(ident);

            // calculate argument values
            assert(args.size() == func.params.size());
            Object[] vals = new Object[args.size()];
            for(int i=0; i<vals.length; i++)
                vals[i] = args.get(i).Exec(runenv);

            // call the function with passing the calculated arguments
            Object   ret  = func.Exec(runenv, vals);
            
            return   ret;
        }
        public String ToString(OptToString opt) throws Exception
        {
            String str = "call " + ident + "(";
            if(args.size() != 0) str += " ";
            for(int i=0; i<args.size(); i++)
                if(i == 0) str +=      args.get(i).ToString(opt);
                else       str += ", "+args.get(i).ToString(opt);
            if(args.size() != 0) str += " ";
            str += ")";
            return str;
        }
    }
    public static abstract class ExprUnary  extends Expr
    {
        public Expr op;
        public ExprUnary (Expr op)
        {
            this.op = op;
        }
        public String ToString(OptToString opt) throws Exception
        {
            String str = "";
            str += OperString();
            str += op.ToString(opt);
            return str;
        }
        public Object Exec(RunEnv runenv) throws Exception
        {
            Object val = op.Exec(runenv);
            Object ret = Exec(val);
            return ret;
        }
        public abstract String OperString();
        public abstract Object Exec(Object val);
    }
    public static abstract class ExprBinary extends Expr
    {
        public Expr op1;
        public Expr op2;
        public ExprBinary(Expr op1, Expr op2)
        {
            this.op1 = op1;
            this.op2 = op2;
        }
        public String ToString(OptToString opt) throws Exception
        {
            String str = "";
            str += op1.ToString(opt);
            str += OperString();
            str += op2.ToString(opt);
            return str;
        }
        public Object Exec(RunEnv runenv) throws Exception
        {
            Object val1 = op1.Exec(runenv);
            Object val2 = op2.Exec(runenv);
            Object ret  = Exec(val1, val2);
            return ret;
        }
        public abstract String OperString();
        public abstract Object Exec(Object val1, Object val2);
    }
    public static class ExprAdd extends ExprBinary { public ExprAdd(Expr op1, Expr op2) { super(op1, op2); } public Object Exec(Object val1, Object val2) { return ( (Integer)val1 +  (Integer)val2); } public String OperString() { return " + " ; } }
    public static class ExprSub extends ExprBinary { public ExprSub(Expr op1, Expr op2) { super(op1, op2); } public Object Exec(Object val1, Object val2) { return ( (Integer)val1 -  (Integer)val2); } public String OperString() { return " - " ; } }
    public static class ExprMul extends ExprBinary { public ExprMul(Expr op1, Expr op2) { super(op1, op2); } public Object Exec(Object val1, Object val2) { return ( (Integer)val1 *  (Integer)val2); } public String OperString() { return " * " ; } }
    public static class ExprDiv extends ExprBinary { public ExprDiv(Expr op1, Expr op2) { super(op1, op2); } public Object Exec(Object val1, Object val2) { return ( (Integer)val1 /  (Integer)val2); } public String OperString() { return " / " ; } }
    public static class ExprMod extends ExprBinary { public ExprMod(Expr op1, Expr op2) { super(op1, op2); } public Object Exec(Object val1, Object val2) { return ( (Integer)val1 /  (Integer)val2); } public String OperString() { return " % " ; } }
    public static class ExprAnd extends ExprBinary { public ExprAnd(Expr op1, Expr op2) { super(op1, op2); } public Object Exec(Object val1, Object val2) { return ( (Boolean)val1 && (Boolean)val2); } public String OperString() { return " and ";} }
    public static class ExprOr  extends ExprBinary { public ExprOr (Expr op1, Expr op2) { super(op1, op2); } public Object Exec(Object val1, Object val2) { return ( (Boolean)val1 || (Boolean)val2); } public String OperString() { return " or "; } }
    public static class ExprNot extends ExprUnary  { public ExprNot(Expr op           ) { super(op      ); } public Object Exec(Object val              ) { return (!(Boolean)val                  ); } public String OperString() { return "not "; } }
    public static class ExprEq  extends ExprBinary { public ExprEq (Expr op1, Expr op2) { super(op1, op2); } public Object Exec(Object val1, Object val2) { return (          val1 .equals(   val2)); } public String OperString() { return " = " ; } }
    public static class ExprNe  extends ExprBinary { public ExprNe (Expr op1, Expr op2) { super(op1, op2); } public Object Exec(Object val1, Object val2) { return (!         val1 .equals(   val2)); } public String OperString() { return " != "; } }
    public static class ExprLe  extends ExprBinary { public ExprLe (Expr op1, Expr op2) { super(op1, op2); } public Object Exec(Object val1, Object val2) { return ( (Integer)val1 <= (Integer)val2); } public String OperString() { return " <= "; } }
    public static class ExprLt  extends ExprBinary { public ExprLt (Expr op1, Expr op2) { super(op1, op2); } public Object Exec(Object val1, Object val2) { return ( (Integer)val1 <  (Integer)val2); } public String OperString() { return " < " ; } }
    public static class ExprGe  extends ExprBinary { public ExprGe (Expr op1, Expr op2) { super(op1, op2); } public Object Exec(Object val1, Object val2) { return ( (Integer)val1 >= (Integer)val2); } public String OperString() { return " >= "; } }
    public static class ExprGt  extends ExprBinary { public ExprGt (Expr op1, Expr op2) { super(op1, op2); } public Object Exec(Object val1, Object val2) { return ( (Integer)val1 >  (Integer)val2); } public String OperString() { return " > " ; } }
    public static class ExprParen  extends Expr
    {
        public Expr op1;
        public ExprParen (Expr op1)
        {
            this.op1 = op1;
        }
        public String ToString(OptToString opt) throws Exception
        {
            String str = op1.ToString(opt);
            str = "(" + str + ")";
            return str;
        }
        public Object Exec(RunEnv runenv) throws Exception
        {
            Object ret = op1.Exec(runenv);
            return ret;
        }
    }
    //public static class ExprOper extends Expr
    //{
    //    public Expr   op1 ;
    //    public Expr   op2 ;
    //    public String oper;
    //    public ExprOper(String oper, Expr op1, Expr op2)
    //    {
    //        this.op1  = op1 ;
    //        this.op2  = op2 ;
    //        this.oper = oper;
    //    }
    //    public Object Exec(RunEnv runenv) throws Exception
    //    {
    //        Object val1 = null; if(op1 != null) val1 = op1.Exec(runenv);
    //        Object val2 = null; if(op2 != null) val2 = op2.Exec(runenv);
    //        switch(oper)
    //        {
    //            case "+"  : return  ((Integer)val1 +  (Integer)val2 );
    //            case "-"  : return  ((Integer)val1 -  (Integer)val2 );
    //            case "*"  : return  ((Integer)val1 *  (Integer)val2 );
    //            case "/"  : return  ((Integer)val1 /  (Integer)val2 );
    //            case "%"  : return  ((Integer)val1 %  (Integer)val2 );
    //            case "and": return  ((Boolean)val1 && (Boolean)val2 );
    //            case "or" : return  ((Boolean)val1 || (Boolean)val2 );
    //            case "not": return !((Boolean)val1                  );
    //            case "="  : return  ((        val1).equals(    val2));
    //            case "!=" : return !((        val1).equals(    val2));
    //            case "<=" : return  ((Integer)val1 <= (Integer)val2 );
    //            case "<"  : return  ((Integer)val1 <  (Integer)val2 );
    //            case ">=" : return  ((Integer)val1 >= (Integer)val2 );
    //            case ">"  : return  ((Integer)val1 >  (Integer)val2 );
    //            case "()" : return  (         val1                  );
    //        }
    //        return null;
    //    }
    //    public String ToString(int opt) throws Exception
    //    {
    //        String str1 = null; if(op1 != null) str1 = op1.ToString(opt);
    //        String str2 = null; if(op2 != null) str2 = op2.ToString(opt);
    //        switch(oper)
    //        {
    //            case "+"  : return (       str1+" + "  +str2);
    //            case "-"  : return (       str1+" - "  +str2);
    //            case "*"  : return (       str1+" * "  +str2);
    //            case "/"  : return (       str1+" / "  +str2);
    //            case "%"  : return (       str1+" % "  +str2);
    //            case "and": return (       str1+" and "+str2);
    //            case "or" : return (       str1+" or " +str2);
    //            case "not": return ("not "+str1             );
    //            case "="  : return (       str1+" = "  +str2);
    //            case "!=" : return (       str1+" != " +str2);
    //            case "<=" : return (       str1+" <= " +str2);
    //            case "<"  : return (       str1+" < "  +str2);
    //            case ">=" : return (       str1+" >= " +str2);
    //            case ">"  : return (       str1+" > "  +str2);
    //            case "()" : return (  "( "+str1+" )"        );
    //        }
    //        return null;
    //    }
    //}
}
