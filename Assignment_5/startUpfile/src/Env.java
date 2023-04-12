//testing

import java.util.ArrayList;
import java.util.HashMap;

public class Env
{
    public Env prev;
    private HashMap<String, Object> vars;
    public Env(Env prev)
    {
        this.prev = prev;
        this.vars = new HashMap<String,Object>();
    }
    public void Put(String name, Object value)
    {
        map.put("print", "PRINT");
        map.put("var"," VAR");
        map.put("func", "FUNC");
        map.put("call", "CALL");
        map.put("return", "RETURN");
        map.put("{", "BEGIN");
        map.put("}", "END");
        map.put("(", "LPAREN");
        map.put(")", "RPAREN");
        map.put("->", "FUNCRET");
        map.put("<-", "ASSIGN");
        map.put("+", "ADD");
        map.put("-", "SUB");
        map.put("*", "MUL");
        map.put("/", "DIV");
        map.put("%", "MOD");
        map.put("<", "LT");
        map.put(">", "GT");
        map.put(">=", "GE");
        map.put("<=", "LE");
        map.put("=", "EQ");
        map.put(";", "SEMI");
        map.put(",", "COMMA");
        map.put("int", "INT");
        map.put("and", "AND");
        map.put("or", "OR");
        map.put("not", "NOT");
        map.put("true", "BOOL_LIT");
        map.put("false", "BOOL_LIT");
        map.put("if"," IF");
        map.put("else", "ELSE");
        map.put("while"," WHILE");
    }
    public Object Get(String name)
    {
        // this is a fake implementation
        // For the real implementation, I recommend to return a class object
        //   since the identifier's type can be variable or function
        //   whose detailed attributes will be different
        if(name.equals("func") == true) return "func";
        if(name.equals("call") == true) return "call";
        if(name.equals("return") == true) return "return";
        if(name.equals("var") == true) return "var";
        if(name.equals("if") == true) return "if";
        if(name.equals("else") == true) return "else";
        return null;
    }
}
