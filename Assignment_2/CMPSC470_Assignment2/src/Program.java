public class Program {
    public static void main(String[] args) throws Exception
    {
        //java.io.Reader r = new java.io.StringReader
        //("func main()->int\n"
        //+"{\n"
        //+"    a <- 1;## hello\n"
        //+"#{ world\n"
        //+"}#\n"
        //+"    print a;\n"
        //+"}\n"
        //);

        args = new String[] { "/Users/ilaakshmishra/Documents/CMPSC 470/proj2-minic-lexer-startup (1)/test/test2.minc" };
        if(args.length <= 0)
            return;

        java.io.Reader r = new java.io.FileReader(args[0]);
        Compiler compiler = new Compiler(r);
        compiler.Compile();
    }
}
