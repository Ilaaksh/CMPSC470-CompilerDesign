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
        map.put("int", "INT");
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
        if(name.equals("a") == true) return "int";
        if(name.equals("b") == true) return "bool";
        if(name.equals("testfunc") == true) return "func()->int";
        return null;
    }
}
