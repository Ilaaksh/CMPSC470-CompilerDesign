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

%line
%column

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

"func"                              { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.FUNC       ; }
"call"                              { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.CALL       ; }
"return"                            { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.RETURN     ; }
"var"                               { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.VAR        ; }
"if"                                { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.IF         ; }
"else"                              { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.ELSE       ; }
"{"                                 { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.BEGIN      ; }
"}"                                 { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.END        ; }
"while"                             { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.WHILE      ; }
"("                                 { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.LPAREN     ; }
")"                                 { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.RPAREN     ; }
"int"                               { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.INT        ; }
"bool"                              { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.BOOL       ; }
"print"                             { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.PRINT      ; }
"<-"                                { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.ASSIGN     ; }
"->"                                { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.FUNCRET    ; }
"+"                                 { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.ADD        ; }
"-"                                 { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.SUB        ; }
"*"                                 { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.MUL        ; }
"/"                                 { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.DIV        ; }
"%"                                 { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.MOD        ; }
"and"                               { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.AND        ; }
"or"                                { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.OR         ; }
"not"                               { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.NOT        ; }
"<"                                 { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.LT         ; }
">"                                 { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.GT         ; }
"<="                                { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.LE         ; }
">="                                { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.GE         ; }
"="                                 { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.EQ         ; }
"!="                                { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.NE         ; }
";"                                 { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.SEMI       ; }
","                                 { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.COMMA      ; }
"true" | "false"                    { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.BOOL_LIT   ; }
{int}                               { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.INT_LIT    ; }
{identifier}                        { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.IDENT      ; }

{newline}                           { /* skip */ }
{whitespace}                        { /* skip */ }
{linecomment}                       { /* skip */ }
{blkcomment}                        { /* skip */ }


\b     { System.err.println("Sorry, backspace doesn't work"); }

/* error fallback */
[^]    { System.err.println("Error: unexpected character '"+yytext()+"'"); return -1; }
