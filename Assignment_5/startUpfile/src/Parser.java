//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 14 "Parser.y"

import java.io.*;
//#line 20 "Parser.java"




public class Parser
             extends ParserImpl
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short ASSIGN=257;
public final static short OR=258;
public final static short AND=259;
public final static short NOT=260;
public final static short EQ=261;
public final static short NE=262;
public final static short LE=263;
public final static short LT=264;
public final static short GE=265;
public final static short GT=266;
public final static short ADD=267;
public final static short SUB=268;
public final static short MUL=269;
public final static short DIV=270;
public final static short MOD=271;
public final static short IDENT=272;
public final static short INT_LIT=273;
public final static short BOOL_LIT=274;
public final static short BOOL=275;
public final static short INT=276;
public final static short FUNC=277;
public final static short IF=278;
public final static short THEN=279;
public final static short ELSE=280;
public final static short WHILE=281;
public final static short PRINT=282;
public final static short RETURN=283;
public final static short CALL=284;
public final static short BEGIN=285;
public final static short END=286;
public final static short LPAREN=287;
public final static short RPAREN=288;
public final static short VAR=289;
public final static short SEMI=290;
public final static short COMMA=291;
public final static short FUNCRET=292;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    2,    3,    8,    8,    9,    9,   10,
    6,    7,    7,    4,    4,    5,   13,   13,   14,   14,
   14,   14,   14,   14,   15,   16,   17,   18,   19,   20,
   11,   11,   12,   12,   21,   21,   21,   21,   21,   21,
   21,   21,   21,   21,   21,   21,   21,   21,   21,   21,
   21,   21,   21,
};
final static short yylen[] = {                            2,
    1,    2,    0,    1,   11,    1,    0,    3,    1,    3,
    1,    1,    1,    2,    0,    4,    2,    0,    1,    1,
    1,    1,    1,    1,    4,    3,    3,    7,    5,    4,
    1,    0,    3,    1,    3,    3,    3,    3,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    2,    3,    1,
    1,    1,    5,
};
final static short yydefred[] = {                         3,
    0,    0,    0,    2,    4,    0,    0,    0,    0,    0,
    9,   13,   12,    0,   11,    0,    0,   10,    0,    8,
    0,   15,    0,    0,   14,    0,    0,    0,    0,    0,
    0,    0,   15,    5,   17,   19,   20,   21,   22,   23,
   24,    0,    0,    0,    0,    0,   50,   51,   52,    0,
    0,    0,    0,    0,   16,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   26,   27,    0,   25,    0,    0,
    0,   49,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   37,   38,   39,   30,    0,   29,    0,    0,
    0,    0,   53,    0,   28,    0,
};
final static short yydgoto[] = {                          1,
    2,    4,    5,   23,   25,   14,   15,    9,   10,   11,
   99,  100,   26,   35,   36,   37,   38,   39,   40,   41,
   52,
};
final static short yysindex[] = {                         0,
    0, -254, -251,    0,    0, -259, -229, -230, -219, -221,
    0,    0,    0, -201,    0, -218, -229,    0, -230,    0,
 -207,    0, -208, -230,    0,   17, -190, -174, -203, -202,
   20,   20,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -204,   20,   20,   20,   20,    0,    0,    0, -185,
   20,  -67,  -53, -208,    0,  -37,  -23,   -7,   98, -198,
    8,   20,   20,   20,   20,   20,   20,   20,   20,   20,
   20,   20,   20,   20,    0,    0,   33,    0, -256, -256,
   20,    0,   87,   98,  107,  107, -264, -264, -264, -264,
 -261, -261,    0,    0,    0,    0, -186,    0, -176, -188,
   74, -256,    0,   20,    0,   74,
};
final static short yyrindex[] = {                         0,
    0,  119,    0,    0,    0,    0, -168,    0,    0, -167,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   45,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   45,    0,    0,    0,    0, -215,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
 -165,    0, -101, -105, -132, -123, -166, -157, -148, -114,
 -211, -200,    0,    0,    0,    0,    0,    0,    0, -160,
 -273,    0,    0,    0,    0, -271,
};
final static short yygindex[] = {                         0,
    0,    0,    0,   99,    0,  113,  122,    0,    0,  129,
    0,    0,  101,  -78,    0,    0,    0,    0,    0,    0,
  -32,
};
final static int YYTABLESIZE=378;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         53,
   97,   98,   70,   71,   72,   73,   74,   72,   73,   74,
   56,   57,   58,   59,   34,   28,   33,   34,   61,   33,
    6,   29,    3,  105,   30,   31,   32,    7,   33,   83,
   84,   85,   86,   87,   88,   89,   90,   91,   92,   93,
   94,   95,   48,   48,   12,   13,   35,   35,  101,   35,
   35,   35,   35,   35,   35,   35,   35,   36,   36,    8,
   36,   36,   36,   36,   36,   36,   36,   36,   16,   17,
   18,  106,   48,   19,   48,   48,   35,   22,   35,   35,
   24,   42,   43,   44,   45,   55,   60,   36,   81,   36,
   36,   42,   42,  102,   42,   42,   42,   42,   42,   42,
   43,   43,  104,   43,   43,   43,   43,   43,   43,   44,
   44,  103,   44,   44,   44,   44,   44,   44,    1,    7,
    6,   42,   32,   42,   42,   40,   40,   31,   40,   40,
   43,   54,   43,   43,   41,   41,   27,   41,   41,   44,
   21,   44,   44,   45,   45,   20,   45,   45,   45,   45,
   45,   45,   46,   46,   77,   40,   47,   40,   40,    0,
    0,    0,    0,    0,   41,    0,   41,   41,    0,    0,
    0,    0,    0,   45,    0,   45,   45,    0,    0,    0,
    0,    0,   46,    0,   46,   46,   47,    0,   47,   47,
   62,   63,    0,   64,   65,   66,   67,   68,   69,   70,
   71,   72,   73,   74,   62,   63,    0,   64,   65,   66,
   67,   68,   69,   70,   71,   72,   73,   74,    0,    0,
   62,   63,   75,   64,   65,   66,   67,   68,   69,   70,
   71,   72,   73,   74,   62,   63,   76,   64,   65,   66,
   67,   68,   69,   70,   71,   72,   73,   74,    0,    0,
   62,   63,   78,   64,   65,   66,   67,   68,   69,   70,
   71,   72,   73,   74,   79,   62,   63,    0,   64,   65,
   66,   67,   68,   69,   70,   71,   72,   73,   74,   46,
   80,    0,    0,    0,    0,    0,    0,    0,   28,    0,
    0,   47,   48,   49,   29,   82,    0,   30,   31,   32,
    0,   33,   34,   50,   28,    0,   51,    0,    0,    0,
   29,    0,    0,   30,   31,   32,   18,   33,   96,    0,
    0,    0,   18,    0,    0,   18,   18,   18,    0,   18,
   18,   62,   63,    0,   64,   65,   66,   67,   68,   69,
   70,   71,   72,   73,   74,   63,    0,   64,   65,   66,
   67,   68,   69,   70,   71,   72,   73,   74,   64,   65,
   66,   67,   68,   69,   70,   71,   72,   73,   74,   66,
   67,   68,   69,   70,   71,   72,   73,   74,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         32,
   79,   80,  267,  268,  269,  270,  271,  269,  270,  271,
   43,   44,   45,   46,  288,  272,  288,  291,   51,  291,
  272,  278,  277,  102,  281,  282,  283,  287,  285,   62,
   63,   64,   65,   66,   67,   68,   69,   70,   71,   72,
   73,   74,  258,  259,  275,  276,  258,  259,   81,  261,
  262,  263,  264,  265,  266,  267,  268,  258,  259,  289,
  261,  262,  263,  264,  265,  266,  267,  268,  288,  291,
  272,  104,  288,  292,  290,  291,  288,  285,  290,  291,
  289,  272,  257,  287,  287,  290,  272,  288,  287,  290,
  291,  258,  259,  280,  261,  262,  263,  264,  265,  266,
  258,  259,  291,  261,  262,  263,  264,  265,  266,  258,
  259,  288,  261,  262,  263,  264,  265,  266,    0,  288,
  288,  288,  288,  290,  291,  258,  259,  288,  261,  262,
  288,   33,  290,  291,  258,  259,   24,  261,  262,  288,
   19,  290,  291,  258,  259,   17,  261,  262,  263,  264,
  265,  266,  258,  259,   54,  288,  258,  290,  291,   -1,
   -1,   -1,   -1,   -1,  288,   -1,  290,  291,   -1,   -1,
   -1,   -1,   -1,  288,   -1,  290,  291,   -1,   -1,   -1,
   -1,   -1,  288,   -1,  290,  291,  288,   -1,  290,  291,
  258,  259,   -1,  261,  262,  263,  264,  265,  266,  267,
  268,  269,  270,  271,  258,  259,   -1,  261,  262,  263,
  264,  265,  266,  267,  268,  269,  270,  271,   -1,   -1,
  258,  259,  290,  261,  262,  263,  264,  265,  266,  267,
  268,  269,  270,  271,  258,  259,  290,  261,  262,  263,
  264,  265,  266,  267,  268,  269,  270,  271,   -1,   -1,
  258,  259,  290,  261,  262,  263,  264,  265,  266,  267,
  268,  269,  270,  271,  288,  258,  259,   -1,  261,  262,
  263,  264,  265,  266,  267,  268,  269,  270,  271,  260,
  288,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  272,   -1,
   -1,  272,  273,  274,  278,  288,   -1,  281,  282,  283,
   -1,  285,  286,  284,  272,   -1,  287,   -1,   -1,   -1,
  278,   -1,   -1,  281,  282,  283,  272,  285,  286,   -1,
   -1,   -1,  278,   -1,   -1,  281,  282,  283,   -1,  285,
  286,  258,  259,   -1,  261,  262,  263,  264,  265,  266,
  267,  268,  269,  270,  271,  259,   -1,  261,  262,  263,
  264,  265,  266,  267,  268,  269,  270,  271,  261,  262,
  263,  264,  265,  266,  267,  268,  269,  270,  271,  263,
  264,  265,  266,  267,  268,  269,  270,  271,
};
}
final static short YYFINAL=1;
final static short YYMAXTOKEN=292;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"ASSIGN","OR","AND","NOT","EQ","NE","LE","LT","GE","GT","ADD",
"SUB","MUL","DIV","MOD","IDENT","INT_LIT","BOOL_LIT","BOOL","INT","FUNC","IF",
"THEN","ELSE","WHILE","PRINT","RETURN","CALL","BEGIN","END","LPAREN","RPAREN",
"VAR","SEMI","COMMA","FUNCRET",
};
final static String yyrule[] = {
"$accept : program",
"program : decl_list",
"decl_list : decl_list decl",
"decl_list :",
"decl : fun_decl",
"fun_decl : FUNC IDENT LPAREN params RPAREN FUNCRET prim_type BEGIN local_decls stmt_list END",
"params : param_list",
"params :",
"param_list : param_list COMMA param",
"param_list : param",
"param : VAR type_spec IDENT",
"type_spec : prim_type",
"prim_type : INT",
"prim_type : BOOL",
"local_decls : local_decls local_decl",
"local_decls :",
"local_decl : VAR type_spec IDENT SEMI",
"stmt_list : stmt_list stmt",
"stmt_list :",
"stmt : assign_stmt",
"stmt : print_stmt",
"stmt : return_stmt",
"stmt : if_stmt",
"stmt : while_stmt",
"stmt : compound_stmt",
"assign_stmt : IDENT ASSIGN expr SEMI",
"print_stmt : PRINT expr SEMI",
"return_stmt : RETURN expr SEMI",
"if_stmt : IF LPAREN expr RPAREN stmt ELSE stmt",
"while_stmt : WHILE LPAREN expr RPAREN stmt",
"compound_stmt : BEGIN local_decls stmt_list END",
"args : arg_list",
"args :",
"arg_list : arg_list COMMA expr",
"arg_list : expr",
"expr : expr ADD expr",
"expr : expr SUB expr",
"expr : expr MUL expr",
"expr : expr DIV expr",
"expr : expr MOD expr",
"expr : expr EQ expr",
"expr : expr NE expr",
"expr : expr LE expr",
"expr : expr LT expr",
"expr : expr GE expr",
"expr : expr GT expr",
"expr : expr AND expr",
"expr : expr OR expr",
"expr : NOT expr",
"expr : LPAREN expr RPAREN",
"expr : IDENT",
"expr : INT_LIT",
"expr : BOOL_LIT",
"expr : CALL IDENT LPAREN args RPAREN",
};

//#line 142 "Parser.y"

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
//#line 392 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
throws Exception
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 47 "Parser.y"
{}
break;
case 2:
//#line 50 "Parser.y"
{}
break;
case 3:
//#line 51 "Parser.y"
{}
break;
case 4:
//#line 54 "Parser.y"
{}
break;
case 5:
//#line 57 "Parser.y"
{}
break;
case 6:
//#line 60 "Parser.y"
{}
break;
case 7:
//#line 61 "Parser.y"
{}
break;
case 8:
//#line 64 "Parser.y"
{}
break;
case 9:
//#line 65 "Parser.y"
{}
break;
case 10:
//#line 68 "Parser.y"
{}
break;
case 11:
//#line 71 "Parser.y"
{}
break;
case 12:
//#line 74 "Parser.y"
{}
break;
case 13:
//#line 75 "Parser.y"
{}
break;
case 14:
//#line 78 "Parser.y"
{}
break;
case 15:
//#line 79 "Parser.y"
{}
break;
case 16:
//#line 82 "Parser.y"
{}
break;
case 17:
//#line 85 "Parser.y"
{}
break;
case 18:
//#line 86 "Parser.y"
{}
break;
case 19:
//#line 89 "Parser.y"
{}
break;
case 20:
//#line 90 "Parser.y"
{}
break;
case 21:
//#line 91 "Parser.y"
{}
break;
case 22:
//#line 92 "Parser.y"
{}
break;
case 23:
//#line 93 "Parser.y"
{}
break;
case 24:
//#line 94 "Parser.y"
{}
break;
case 25:
//#line 97 "Parser.y"
{}
break;
case 26:
//#line 100 "Parser.y"
{}
break;
case 27:
//#line 103 "Parser.y"
{}
break;
case 28:
//#line 106 "Parser.y"
{}
break;
case 29:
//#line 109 "Parser.y"
{}
break;
case 30:
//#line 112 "Parser.y"
{}
break;
case 31:
//#line 115 "Parser.y"
{}
break;
case 32:
//#line 116 "Parser.y"
{}
break;
case 33:
//#line 118 "Parser.y"
{}
break;
case 34:
//#line 119 "Parser.y"
{}
break;
case 35:
//#line 121 "Parser.y"
{}
break;
case 36:
//#line 122 "Parser.y"
{}
break;
case 37:
//#line 123 "Parser.y"
{}
break;
case 38:
//#line 124 "Parser.y"
{}
break;
case 39:
//#line 125 "Parser.y"
{}
break;
case 40:
//#line 126 "Parser.y"
{}
break;
case 41:
//#line 127 "Parser.y"
{}
break;
case 42:
//#line 128 "Parser.y"
{}
break;
case 43:
//#line 129 "Parser.y"
{}
break;
case 44:
//#line 130 "Parser.y"
{}
break;
case 45:
//#line 131 "Parser.y"
{}
break;
case 46:
//#line 132 "Parser.y"
{}
break;
case 47:
//#line 133 "Parser.y"
{}
break;
case 48:
//#line 134 "Parser.y"
{}
break;
case 49:
//#line 135 "Parser.y"
{}
break;
case 50:
//#line 136 "Parser.y"
{}
break;
case 51:
//#line 137 "Parser.y"
{}
break;
case 52:
//#line 138 "Parser.y"
{}
break;
case 53:
//#line 139 "Parser.y"
{}
break;
//#line 753 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
//## The -Jnorun option was used ##
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
