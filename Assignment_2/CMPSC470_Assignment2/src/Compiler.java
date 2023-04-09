public class Compiler
{
    Parser parser;

    public Compiler(java.io.Reader r) throws Exception
    {
        parser = new Parser(r, this);
    }
    public void Compile() throws Exception {
        parser.yyparse();

    }}