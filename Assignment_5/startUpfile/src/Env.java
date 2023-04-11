//testing

import java.util.ArrayList;
import java.util.HashMap;

public class Env
{
    public Env prev;
    public Env(Env prev)
    {
        hello
    }
    public void Put(String name, Object value)
    {
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
