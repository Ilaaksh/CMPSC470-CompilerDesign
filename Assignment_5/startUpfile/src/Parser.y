

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


program         : decl_list                              {Debug("program -> decl_list"                           ); $$ = program_decllist($1);}
                ;

decl_list       : decl_list decl                         {Debug("decl_list -> decl_list decl"                    );$$ = decllist_decllist_decl($1,$2);}
                |                                        {Debug("decl_list -> eps"                               );$$ = decllist_eps          (     );}
                ;

decl            : fun_decl                               {Debug("decl -> fun_decl"                               );$$ = decl_fundecl($1); }
                ;
fun_decl        : FUNC IDENT LPAREN params RPAREN FUNCRET prim_type BEGIN local_decls{ Debug("fun_decl -> FUNC ID(params)->prim_type BEGIN local_decls"); $<obj>$ = fundecl____FUNC_IDENT_LPAREN_params_RPAREN_FUNCRET_primtype_BEGIN_localdecls_10X_stmtlist_END($2, $4, $7, $9          ); }
                                                                        stmt_list END{ Debug("                                           stmt_list END"); $$ =      fundecl____FUNC_IDENT_LPAREN_params_RPAREN_FUNCRET_primtype_BEGIN_localdecls_X10_stmtlist_END($2, $4, $7, $9, $11, $12); }
                ;

params          :                                               { Debug("params -> eps"                         ); $$ = params____eps(); }
                ;

param_list      : param_list  COMMA  param {}
                | param {}
                ;

param           : VAR type_spec IDENT {}
                ;

type_spec       : prim_type {}
                ;

prim_type       : INT {}
                | BOOL {}
                ;

local_decls     : local_decls  local_decl                       { Debug("local_decls -> local_decls local_decl" ); $$ = localdecls____localdecls_localdecl($1, $2); }
                |                                               { Debug("local_decls -> eps"                    ); $$ = localdecls____eps(); }
                ;

local_decl      : VAR  type_spec  IDENT  SEMI                   { Debug("local_decl -> VAR type_spec IDENT SEMI"); $$ = localdecl____VAR_typespec_IDENT_SEMI($2, $3); }
                ;

stmt_list       : stmt_list stmt                                { Debug("stmt_list -> stmt_list stmt"           ); $$ = stmtlist____stmtlist_stmt($1, $2); }
                |                                               { Debug("stmt_list -> eps"                      ); $$ = stmtlist____eps          (      ); }
                ;

stmt            : assign_stmt                                   {Debug("stmt -> assign_stmt"                   ); $$ = stmt____assignstmt  ($1); }
                | return_stmt                                   {Debug("stmt -> return_stmt"                   ); $$ = stmt____returnstmt  ($1); }
                | print_stmt                                   {Debug("stmt -> print_stmt"                   ); $$ = stmt____printstmt  ($1); }
                | if_stmt                                      {Debug("stmt -> if_stmt"                      ); $$ = stmt____ifstmt  ($1); }
                | while_stmt                                   {Debug("stmt -> while_stmt"                   ); $$ = stmt____whilestmt  ($1); }
                | compound_stmt                                {Debug("stmt -> compound_stmt"                   ); $$ = stmt____compoundstmt  ($1); }
                ;

assign_stmt     : IDENT ASSIGN expr SEMI                        { Debug("assign_stmt -> IDENT <- expr ;"        ); $$ = assignstmt____IDENT_ASSIGN_expr_SEMI($1,$2,$3); }
                ;

print_stmt      : PRINT  expr  SEMI {}
                ;

return_stmt     : RETURN expr SEMI                              { Debug("return_stmt -> RETURN expr ;"          ); $$ = returnstmt____RETURN_expr_SEMI($2); }
                ;

if_stmt         :IF  LPAREN  expr  RPAREN  stmt  ELSE  stmt {}
                ;

while_stmt      :WHILE  LPAREN  expr  RPAREN  stmt {}
                ;

compound_stmt   :BEGIN  local_decls  stmt_list  END {}
                ;

args            :arg_list                        {Debug("args -> arg_list"                      ); $$ = args____eps();}
                |                                {Debug("args -> eps"                           ); $$ = args____eps(); }
                ;

arg_list       :arg_list  COMMA  expr  {}
                | expr {}

expr            :expr  ADD  expr                            {$$ = expr__expr_ADD_expr                ($1,$2,$3);}
                | expr  SUB  expr                           {$$ = expr__expr_SUB_expr                 ($1,$2,$3);}
                | expr  MUL  expr                           {$$ = expr__expr_MUL_expr                 ($1,$2,$3);}
                | expr  DIV  expr                           {$$ = expr__expr_DIV_expr                 ($1,$2,$3);}
                | expr  MOD  expr                           {$$ = expr__expr_MOD_expr                 ($1,$2,$3);}
                |  expr  EQ   expr                          {$$ = expr__expr_EQ_expr                 ($1,$2,$3);}
                | expr  NE   expr                           {$$ = expr__expr_NE_expr                 ($1,$2,$3);}
                | expr  LE   expr                           {$$ = expr__expr_LE_expr                 ($1,$2,$3);}
                | expr  LT   expr                           {$$ = expr__expr_LT_expr                 ($1,$2,$3);}
                | expr  GE   expr                           {$$ = expr__expr_GE_expr                 ($1,$2,$3);}
                | expr  GT  expr                            {$$ = expr__expr_GT_expr                 ($1,$2,$3);}
                |  expr  AND  expr                          {$$ = expr__expr_AND_expr                 ($1,$2,$3);}
                | expr  OR   expr                           {$$ = expr__expr_OR_expr                 ($1,$2,$3);}
                | NOT  expr                                 {$$ = expr__NOT_expr                      ($1);}
                |  LPAREN  expr  RPAREN                     {$$ = expr__LPAREN_expr_RPAREN            ($1,$2,$3);}
                |  IDENT                                    {$$ = expr__IDENT                         ($1)      ;}
                |  INT_LIT                                  {$$ = expr__INT_LIT                       ($1)      ;}
                |  BOOL_LIT                                 {$$ = expr__BOOL_LIT                       ($1);}
                |  CALL  IDENT  LPAREN  args  RPAREN        {$$ = expr__CALL IDENT LPAREN args RPAREN  ($1,$2,$3);}
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
            System.out.println ("Error message by Parser.yyerror() at near "  + lexer.getLine()+":"+lexer.getCol() +" by Parser.yyerror(): " + error);
            int last_token_lineno = lexer.getLine();
            int last_token_column = lexer.getCol();
            //System.out.println ("Error message by Parser.yyerror() at near " + last_token_lineno+":"+last_token_column + ": " + error);
    }


    public Parser(Reader r, boolean yydebug) {
        this.lexer   = new Lexer(r, this);
        this.yydebug = yydebug;
    }
