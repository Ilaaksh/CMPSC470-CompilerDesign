public class Token
{
    public String lexeme;
    public int lineno;
    public int column;
    public Token(String lexeme, int lineno, int column)
    {
        this.lexeme = lexeme;
        this.lineno = lineno;
        this.column = column;
    }
}
