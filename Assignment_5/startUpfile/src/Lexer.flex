/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2000 Gerwin Klein <lsf@jflex.de>                          *
 * All rights reserved.                                                    *
 *                                                                         *
 * Thanks to Larry Bell and Bob Jamison for suggestions and comments.      *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

%%

%class Lexer
%byaccj

%{

  public Parser   parser;
  public int      lineno;
  public int      column;

  public Lexer(java.io.Reader r, Parser parser) {
    this(r);
    this.parser = parser;
    this.lineno = 1;
    this.column = 1;
  }

%}

int         = [0-9]+
identifier  = [a-zA-Z][a-zA-Z0-9_]*
newline     = \n
whitespace  = [ \t\r]+
linecomment = "##".*
blkcomment  = "#{"[^]*"}#"

%%

"func"                              { parser.yylval = new ParserVal(new Token(yytext())); return Parser.FUNC       ; }
"call"                              { parser.yylval = new ParserVal(new Token(yytext())); return Parser.CALL       ; }
"return"                            { parser.yylval = new ParserVal(new Token(yytext())); return Parser.RETURN     ; }
"var"                               { parser.yylval = new ParserVal(new Token(yytext())); return Parser.VAR        ; }
"{"                                 { parser.yylval = new ParserVal(new Token(yytext())); return Parser.BEGIN      ; }
"}"                                 { parser.yylval = new ParserVal(new Token(yytext())); return Parser.END        ; }
"("                                 { parser.yylval = new ParserVal(new Token(yytext())); return Parser.LPAREN     ; }
")"                                 { parser.yylval = new ParserVal(new Token(yytext())); return Parser.RPAREN     ; }
"int"                               { parser.yylval = new ParserVal(new Token(yytext())); return Parser.INT        ; }
"print"                             { parser.yylval = new ParserVal(new Token(yytext())); return Parser.PRINT      ; }
"<-"                                { parser.yylval = new ParserVal(new Token(yytext())); return Parser.ASSIGN     ; }
"->"                                { parser.yylval = new ParserVal(new Token(yytext())); return Parser.FUNCRET    ; }
"+"                                 { parser.yylval = new ParserVal(new Token(yytext())); return Parser.ADD        ; }
"="                                 { parser.yylval = new ParserVal(new Token(yytext())); return Parser.EQ         ; }
";"                                 { parser.yylval = new ParserVal(new Token(yytext())); return Parser.SEMI       ; }
{int}                               { parser.yylval = new ParserVal(new Token(yytext())); return Parser.INT_LIT    ; }
{identifier}                        { parser.yylval = new ParserVal(new Token(yytext())); return Parser.IDENT      ; }
{linecomment}                       { /* skip */ }
{newline}                           { /* skip */ }
{whitespace}                        { /* skip */ }
{blkcomment}                        { /* skip */ }


\b     { System.err.println("Sorry, backspace doesn't work"); }

/* error fallback */
[^]    { System.err.println("Error: unexpected character '"+yytext()+"'"); return -1; }
