/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2001 Gerwin Klein <lsf@jflex.de>                          *
 * All rights reserved.                                                    *
 *                                                                         *
 * This is a modified version of the example from                          *
 *   http://www.lincom-asg.com/~rjamison/byacc/                            *
 *                                                                         *
 * Thanks to Larry Bell and Bob Jamison for suggestions and comments.      *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

%{
import java.io.*;
%}

%right  ASSIGN
%left   OR
%left   AND
%right  NOT
%left   EQ      NE
%left   LE      LT      GE      GT
%left   ADD     SUB
%left   MUL     DIV     MOD

%token <obj>    EQ   NE   LE   LT   GE   GT
%token <obj>    ADD  SUB  MUL  DIV  MOD
%token <obj>    OR   AND  NOT

%token <obj>    IDENT     INT_LIT   BOOL_LIT

%token <obj> BOOL  INT
%token <obj> FUNC  IF  THEN  ELSE  WHILE  PRINT  RETURN  CALL
%token <obj> BEGIN  END  LPAREN  RPAREN
%token <obj> ASSIGN  VAR  SEMI  COMMA  FUNCRET

%type <obj> program   decl_list  decl
%type <obj> fun_decl  local_decls  local_decl  type_spec  prim_type
%type <obj> params  param_list  param  args  arg_list
%type <obj> stmt_list  stmt  assign_stmt  print_stmt  return_stmt  if_stmt  while_stmt  compound_stmt     
%type <obj> expr

%%


program         : decl_list                                     { }
                ;

decl_list       : decl_list decl                                { }
                |                                               {    }
                ;

decl            : fun_decl                                      {  }
                ;

prim_type       : INT                                           {  }
                ;

type_spec       : prim_type                                     {  }
                ;

fun_decl        : FUNC IDENT LPAREN params RPAREN FUNCRET prim_type BEGIN local_decls{   }
                                                                        stmt_list END{  }
                ;

params          :                                               { }
                ;

stmt_list       : stmt_list stmt                                {  }
                |                                               {  }
                ;

stmt            : assign_stmt                                   {  }
                | return_stmt                                   {  }
                ;

assign_stmt     : IDENT ASSIGN expr SEMI                        { SIGN_expr_SEMI($1,$2,$3); }
                ;

return_stmt     : RETURN expr SEMI                              { Debug("return_stmt -> RETURN expr ;"          ); $$ = returnstmt____RETURN_expr_SEMI($2); }
                ;

local_decls     : local_decls  local_decl                       { Debug("local_decls -> local_decls local_decl" ); $$ = localdecls____localdecls_localdecl($1, $2); }
                |                                               { Debug("local_decls -> eps"                    ); $$ = localdecls____eps(); }
                ;

local_decl      : VAR  type_spec  IDENT  SEMI                   { Debug("local_decl -> VAR type_spec IDENT SEMI"); $$ = localdecl____VAR_typespec_IDENT_SEMI($2, $3); }
                ;

args            :                                               { Debug("args -> eps"                           ); $$ = args____eps(); }
                ;

expr            : expr ADD expr                                 { Debug("expr -> expr ADD expr"                 ); $$ = expr____expr_ADD_expr                ($1,$2,$3); }
                | expr EQ  expr                                 { Debug("expr -> expr EQ  expr"                 ); $$ = expr____expr_EQ_expr                 ($1,$2,$3); }
                | LPAREN expr RPAREN                            { Debug("expr -> LPAREN expr RPAREN"            ); $$ = expr____LPAREN_expr_RPAREN           ($1,$2,$3); }
                | IDENT                                         { Debug("expr -> IDENT"                         ); $$ = expr____IDENT                        ($1      ); }
                | INT_LIT                                       { Debug("expr -> INT_LIT"                       ); $$ = expr____INTLIT                       ($1      ); }
                | CALL IDENT LPAREN args RPAREN                 { Debug("expr -> CALL IDENT LPAREN args RPAREN" ); $$ = expr____CALL_IDENT_LPAREN_args_RPAREN($2,$4   ); }
                ;

%%
    private Lexer lexer;
    private Token last_token;

    private int yylex () {
        int yyl_return = -1;
        try {
            yylval = new ParserVal(0);
            yyl_return = lexer.yylex();
            last_token = (Token)yylval.obj;
        }
        catch (IOException e) {
            System.out.println("IO error :"+e);
        }
        return yyl_return;
    }


    public void yyerror (String error) {
        //System.out.println ("Error message for " + lexer.lineno+":"+lexer.column +" by Parser.yyerror(): " + error);
        int last_token_lineno = 0;
        int last_token_column = 0;
        System.out.println ("Error message by Parser.yyerror() at near " + last_token_lineno+":"+last_token_column + ": " + error);
    }


    public Parser(Reader r, boolean yydebug) {
        this.lexer   = new Lexer(r, this);
        this.yydebug = yydebug;
    }
