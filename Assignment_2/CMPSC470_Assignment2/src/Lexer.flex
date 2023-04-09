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

%{

  public Parser   parser;
  public int      lineno;
  public int      column;
  public int      counter=0;

  public Lexer(java.io.Reader r, Parser parser) {
    this(r);
    this.parser = parser;
    this.lineno = 1;
    this.column = 1;

  }
  HashMap<String, Integer> symbolTable = new HashMap<>();


%}

int         = [0-9]+

float       = [0-9]+("."[0-9]+)?
identifier  = [a-zA-Z][a-zA-Z0-9_]*
newline     = \n
whitespace  = [ \t\r]+
linecomment = "##".*
blockcomment= "#{"(.|\n)*"}#"

%%
"break"                              { parser.yylval = new ParserVal((Object)yytext()); return Parser.BREAK   ;

                                                }
","                              { parser.yylval = new ParserVal((Object)yytext()); return Parser.COMMA   ; }
""                              { parser.yylval = new ParserVal((Object)yytext()); return Parser.COMMA   ; }
"."                              { parser.yylval = new ParserVal((Object)yytext()); return Parser.DOT   ; }
"&"                              { parser.yylval = new ParserVal((Object)yytext()); return Parser.ADDR   ; }
"["                              { parser.yylval = new ParserVal((Object)yytext()); return Parser.LBRACKET   ; }
"]"                              { parser.yylval = new ParserVal((Object)yytext()); return Parser.RBRACKET   ; }
"if"                             { parser.yylval = new ParserVal((Object)yytext()); return Parser.IF   ; }
"else"                             { parser.yylval = new ParserVal((Object)yytext()); return Parser.ELSE   ; }
"print"                             { parser.yylval = new ParserVal((Object)yytext()); return Parser.PRINT   ; }
"func"                              { parser.yylval = new ParserVal((Object)yytext()); return Parser.FUNC    ; }
"var"                               { parser.yylval = new ParserVal((Object)yytext()); return Parser.VAR    ; }
"void"                              { parser.yylval = new ParserVal((Object)yytext()); return Parser.VOID    ; }
"bool"                              { parser.yylval = new ParserVal((Object)yytext()); return Parser.BOOL    ; }
"float"                             { parser.yylval = new ParserVal((Object)yytext()); return Parser.FLOAT    ; }
"struct"                            { parser.yylval = new ParserVal((Object)yytext()); return Parser.STRUCT    ; }
"size"                              { parser.yylval = new ParserVal((Object)yytext()); return Parser.SIZE    ; }
"new"                               { parser.yylval = new ParserVal((Object)yytext()); return Parser.NEW    ; }
"int"                               { parser.yylval = new ParserVal((Object)yytext()); return Parser.INT     ; }
"while"                             { parser.yylval = new ParserVal((Object)yytext()); return Parser.WHILE    ; }
"size"                              { parser.yylval = new ParserVal((Object)yytext()); return Parser.SIZE    ; }
"return"                            { parser.yylval = new ParserVal((Object)yytext()); return Parser.RETURN    ; }
"size"                              { parser.yylval = new ParserVal((Object)yytext()); return Parser.SIZE    ; }
"continue"                          { parser.yylval = new ParserVal((Object)yytext()); return Parser.CONTINUE    ; }
"{"                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.BEGIN   ; }
"}"                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.END     ; }
"("                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.LPAREN  ; }
")"                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.RPAREN  ; }
";"                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.SEMI    ; }
"<-"                                { parser.yylval = new ParserVal((Object)yytext()); return Parser.ASSIGN  ; }
"->"                                { parser.yylval = new ParserVal((Object)yytext()); return Parser.FUNCRET ; }
"+"                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.OP      ; }
"*"                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.OP      ; }
"-"                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.OP      ; }
"/"                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.OP      ; }
"and"                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.OP      ; }
"or"                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.OP      ; }
"not"                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.OP      ; }
"<"                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.RELOP   ; }
">"                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.RELOP   ; }
"<="                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.RELOP   ; }
">="                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.RELOP   ; }
"!="                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.RELOP   ; }
"="                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.RELOP   ; }
"true"                              { parser.yylval = new ParserVal((Object)yytext()); return Parser.BOOL_LIT   ; }
"false"                             { parser.yylval = new ParserVal((Object)yytext()); return Parser.BOOL_LIT   ; }
{int}                               { parser.yylval = new ParserVal((Object)yytext()); return Parser.INT_LIT ; }
{float}                             {parser.yylval = new ParserVal((Object)yytext()); return Parser.FLOAT_LIT   ;}
{identifier}                        { parser.yylval = new ParserVal((Object)yytext());
                                      if(!symbolTable.containsKey(yytext())){
                                          symbolTable.put(yytext(),counter);
                                          System.out.print(String.format("<<new symbol table entity [%d, %s]>>", counter, yytext()));
                                          counter++;
                                      }
                                        return Parser.IDENT   ;}
{linecomment}                       { String lexeme=yytext();
          System.out.print(yytext()); /* linwecomm skip */ }
{newline}                           { String lexeme=yytext();
          System.out.print(yytext());
          lineno++;
          column=1;/* line skip */ }
{whitespace}                        {

          if (yytext().equals("    ")){
              column+=4;
          }
          else if (yytext().equals("\t")){
                        column+=4;
                    }
          else{
              for(int i=0; i<yytext().length();i++){
                  if (yytext().charAt(i)==' '){
                      column++;
                  }
              }
          }
          System.out.print(yytext());
          /* white skip */ }
{blockcomment}                      {String lexeme=yytext();
          System.out.print(yytext());/* blk skip */ }

\b     { System.err.println("Sorry, backspace doesn't work"); }

/* error fallback */
[^]    { System.err.println("Error: unexpected character '"+yytext()+"'"); return -1; }
