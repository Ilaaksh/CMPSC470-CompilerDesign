

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class ParseTreeInfo
{
    // Use this classes to store information into parse tree node (subclasses of ParseTree.Node)
    // You should not modify ParseTree.java
    public static class TypeSpecInfo
    {
    }
    public static class ProgramInfo
    {
    }
    public static class FuncDeclInfo
    {
        public String id;
        public String ret_type;
        public ArrayList<ParseTree.Param> params;
    }
    public static class ParamInfo
    {
    }
    public static class LocalDeclInfo
    {
        public String value_type;
        public int lineno;
        public int column;
    }
    public static class StmtStmtInfo
    {
        public String value_type;
        public String value;
        public boolean returened = false;   // for ifstmt, whilestmt, compounstmt
    }
    public static class ArgInfo
    {
    }
    public static class ExprInfo
    {
        public String value_type;
        public int lineno;
        public int column;
    }
}
