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
    }
    public static class ParamInfo
    {
    }
    public static class LocalDeclInfo
    {
    }
    public static class StmtStmtInfo
    {
        String stmt;
    }
    public static class ArgInfo
    {
    }
    public static class ExprInfo
    {
        public String value_type;
        public Object value;
    }
}
