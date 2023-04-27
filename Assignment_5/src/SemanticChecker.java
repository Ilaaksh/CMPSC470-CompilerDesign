public class SemanticChecker {
    public static void main(String[] args) throws Exception
    {
          if(args.length == 0)
          args = new String[]
          {
                  "/home/ijm5304/Downloads/project_5/testCase/minc/test_10_scope1_succ.minc"
          };

        if(args.length <= 0)
            return;
        String minicpath = args[0];
        java.io.Reader r = new java.io.FileReader(minicpath);

        Compiler compiler = new Compiler(r);

        compiler.Parse();
    }
}
