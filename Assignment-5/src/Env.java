

import java.util.ArrayList;
import java.util.HashMap;

public class Env {
    public Env prev;
    public HashMap<String, Object> symbols;

    public Env(Env prev) {
        this.prev = prev;
        this.symbols = new HashMap<String, Object>();
    }

    public void Put(String name, Object value) {
        this.symbols.put(name, value);
    }

    public Object Get(String name) {
        Env env = this;
        while (env != null) {
            if (env.symbols.containsKey(name)) {
                return env.symbols.get(name);
            }
            env = env.prev;
        }
        return null;
    }

    public Object GetCurrentScope(String name) {
        Env env = this;
        return env.symbols.get(name);
    }
}