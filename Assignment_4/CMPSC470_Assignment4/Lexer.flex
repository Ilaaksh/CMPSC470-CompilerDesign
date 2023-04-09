/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2000 Gerwin Klein <lsf@jflex.de>                          *
 * All rights reserved.                                                    *
 *                                                                         *
 * Thanks to Larry Bell and Bob Jamison for suggestions and comments.      *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
import java.util.*;

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

float       = [0-9]+("."[0-9]+)?
identifier  = [a-zA-Z][a-zA-Z0-9_]*
newline     = \n
whitespace  = [ \t\r]+
linecomment = "##".*
blockcomment= "#{"[^]*"}#"

%%
","                              { parser.yylval = new ParserVal((Object)yytext()); lineno = yyline; column=yycolumn; return Parser.COMMA   ; }
"."                              { parser.yylval = new ParserVal((Object)yytext());  lineno = yyline; column=yycolumn; return Parser.DOT   ; }
"["                              { parser.yylval = new ParserVal((Object)yytext()); lineno = yyline; column=yycolumn; return Parser.LBRACKET   ; }
"]"                              { parser.yylval = new ParserVal((Object)yytext()); lineno = yyline; column=yycolumn; return Parser.RBRACKET   ; }
"if"                             { parser.yylval = new ParserVal((Object)yytext()); lineno = yyline; column=yycolumn; return Parser.IF   ; }
"sizeof"                          { parser.yylval = new ParserVal((Object)yytext());lineno = yyline; column=yycolumn; return Parser.SIZEOF  ; }
"elemof"                          { parser.yylval = new ParserVal((Object)yytext()); lineno = yyline; column=yycolumn;return Parser.ELEMOF ; }
"else"                             { parser.yylval = new ParserVal((Object)yytext()); lineno = yyline; column=yycolumn; return Parser.ELSE   ; }
"print"                             { parser.yylval = new ParserVal((Object)yytext()); lineno = yyline; column=yycolumn; return Parser.PRINT   ; }
"func"                              { parser.yylval = new ParserVal((Object)yytext()); lineno = yyline; column=yycolumn; return Parser.FUNC    ; }
"var"                               { parser.yylval = new ParserVal((Object)yytext()); lineno = yyline; column=yycolumn; return Parser.VAR    ; }
"bool"                              { parser.yylval = new ParserVal((Object)yytext()); lineno = yyline; column=yycolumn;return Parser.BOOL    ; }
"@"                                 { parser.yylval = new ParserVal((Object)yytext()); lineno = yyline; column=yycolumn; return Parser.PTR    ; }
"size"                              { parser.yylval = new ParserVal((Object)yytext()); lineno = yyline; column=yycolumn;return Parser.SIZEOF    ; }
"new"                               { parser.yylval = new ParserVal((Object)yytext()); lineno = yyline; column=yycolumn; return Parser.NEW    ; }
"int"                               { parser.yylval = new ParserVal((Object)yytext()); lineno = yyline; column=yycolumn; return Parser.INT     ; }
"while"                             { parser.yylval = new ParserVal((Object)yytext()); lineno = yyline; column=yycolumn; return Parser.WHILE    ; }
"call"                              { parser.yylval = new ParserVal((Object)yytext()); lineno = yyline; column=yycolumn; return Parser.CALL   ; }
"return"                            { parser.yylval = new ParserVal((Object)yytext()); lineno = yyline; column=yycolumn; return Parser.RETURN    ; }
"{"                                 { parser.yylval = new ParserVal((Object)yytext());lineno = yyline; column=yycolumn; return Parser.BEGIN   ; }
"}"                                 { parser.yylval = new ParserVal((Object)yytext());lineno = yyline; column=yycolumn; return Parser.END     ; }
"("                                 { parser.yylval = new ParserVal((Object)yytext());lineno = yyline; column=yycolumn; return Parser.LPAREN  ; }
")"                                 { parser.yylval = new ParserVal((Object)yytext());lineno = yyline; column=yycolumn; return Parser.RPAREN  ; }
";"                                 { parser.yylval = new ParserVal((Object)yytext());lineno = yyline; column=yycolumn; return Parser.SEMI    ; }
"<-"                                { parser.yylval = new ParserVal((Object)yytext());lineno = yyline; column=yycolumn; return Parser.ASSIGN  ; }
"->"                                { parser.yylval = new ParserVal((Object)yytext());lineno = yyline; column=yycolumn; return Parser.FUNCRET ; }
"+"                                 { parser.yylval = new ParserVal((Object)yytext());lineno = yyline; column=yycolumn; return Parser.EXPROP      ; }
"*"                                 { parser.yylval = new ParserVal((Object)yytext());lineno = yyline; column=yycolumn; return Parser.TERMOP      ; }
"-"                                 { parser.yylval = new ParserVal((Object)yytext());lineno = yyline; column=yycolumn; return Parser.EXPROP      ; }
"/"                                 { parser.yylval = new ParserVal((Object)yytext());lineno = yyline; column=yycolumn; return Parser.TERMOP      ; }
"and"                                 { parser.yylval = new ParserVal((Object)yytext());lineno = yyline; column=yycolumn; return Parser.TERMOP      ; }
"or"                                 { parser.yylval = new ParserVal((Object)yytext());lineno = yyline; column=yycolumn; return Parser.EXPROP      ; }
"<"                                 { parser.yylval = new ParserVal((Object)yytext()); lineno = yyline; column=yycolumn;return Parser.RELOP   ; }
">"                                 { parser.yylval = new ParserVal((Object)yytext()); lineno = yyline; column=yycolumn;return Parser.RELOP   ; }
"<="                                 { parser.yylval = new ParserVal((Object)yytext());lineno = yyline; column=yycolumn; return Parser.RELOP   ; }
">="                                 { parser.yylval = new ParserVal((Object)yytext());lineno = yyline; column=yycolumn; return Parser.RELOP   ; }
"!="                                 { parser.yylval = new ParserVal((Object)yytext());lineno = yyline; column=yycolumn; return Parser.RELOP   ; }
"="                                 { parser.yylval = new ParserVal((Object)yytext()); lineno = yyline; column=yycolumn;return Parser.RELOP   ; }
"true"                              { parser.yylval = new ParserVal((Object)yytext()); lineno = yyline; column=yycolumn;return Parser.BOOL_LIT   ; }
"false"                             { parser.yylval = new ParserVal((Object)yytext()); lineno = yyline; column=yycolumn;return Parser.BOOL_LIT   ; }
{int}                               { parser.yylval = new ParserVal((Object)yytext()); lineno = yyline; column=yycolumn;return Parser.INT_LIT ; }

{identifier}                        {parser.yylval = new ParserVal((Object)yytext()); lineno = yyline; column=yycolumn; return Parser.IDENT ; }
{linecomment}                       {     }
{newline}                           {lineno +=1; column = 1;/* newline skip */}
{whitespace}                        {
                                        if(yytext().equals("    ")) {
                                            column +=4;

                                        }else {
                                            column +=1;
                                        }
                                     }
{blockcomment}                      { }

\b     { System.err.println("Sorry, backspace doesn't work"); }

/* error fallback */
[^]    { System.err.println("Error: unexpected character '"+yytext()+"'"); lineno = yyline; column=yycolumn;return -1; }
