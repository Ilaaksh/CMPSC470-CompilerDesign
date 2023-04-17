//testing

import java.util.ArrayList;
import java.util.HashMap;

public class Env
{
    public Env prev;
    public HashMap<String, Object> vars;
    public Env(Env prev)
    {
        this.prev = prev;
        this.vars = new HashMap<String,Object>();
    }
    public void Put(String name, Object value)
    {

        vars.put(name, value);

    }
    public Object Get(String name)
    {
        HashMap<String, Object> t1 = vars;
        Env prevEnv = this.prev;
        while(!t1.containsKey(name)) {
            if (prevEnv==null) {
                return null;
                // return the value
            }
            else {
                t1=prevEnv.vars;
                prevEnv = prevEnv.prev;
            }
        }

        return t1.get(name);
    }
}
