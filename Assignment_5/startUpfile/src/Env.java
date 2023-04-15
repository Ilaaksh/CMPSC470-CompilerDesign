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

        vars.put(name, value);

    }
    public Object Get(String name)
    {
        Env prevEnv = this.prev;
        while(prevEnv != null) {
            if (prevEnv.vars.containsKey(name)) {
                return prevEnv.vars.get(name); // return the value
            }
            prevEnv = prevEnv.prev;
        }

        return null;
    }
}
