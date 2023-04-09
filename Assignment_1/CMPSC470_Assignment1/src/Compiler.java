public class Compiler
{
    Parser _parser;

    public Compiler(java.io.Reader r) throws Exception
    {
        _parser = new Parser(r, this);
    }
    public void Compile() throws Exception
    {
        _parser.yyparse();
    }
}
