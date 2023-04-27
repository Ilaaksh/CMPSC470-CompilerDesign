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

//#line 21 "Parser.java"




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
    0,    1,    1,    2,   22,    3,    8,    8,    9,    9,
   10,    6,    7,    7,    4,    4,    5,   13,   13,   14,
   14,   14,   14,   14,   14,   15,   16,   17,   18,   19,
   23,   20,   11,   11,   12,   12,   21,   21,   21,   21,
   21,   21,   21,   21,   21,   21,   21,   21,   21,   21,
   21,   21,   21,   21,   21,
};
final static short yylen[] = {                            2,
    1,    2,    0,    1,    0,   12,    1,    0,    3,    1,
    3,    1,    1,    1,    2,    0,    4,    2,    0,    1,
    1,    1,    1,    1,    1,    4,    3,    3,    7,    5,
    0,    5,    1,    0,    3,    1,    3,    3,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    2,
    3,    1,    1,    1,    5,
};
final static short yydefred[] = {                         3,
    0,    0,    0,    2,    4,    0,    0,    0,    0,    0,
   10,   14,   13,    0,   12,    0,    0,   11,    0,    9,
    0,   16,    0,    0,   15,   19,    0,    0,    0,    0,
    0,    0,    0,    0,   16,    6,   18,   20,   21,   22,
   23,   24,   25,   17,    0,    0,    0,    0,   52,   53,
   54,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   27,   28,   19,   26,    0,
    0,    0,   51,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   39,   40,   41,    0,    0,   30,    0,
    0,    0,   32,    0,   55,    0,   29,    0,
};
final static short yydgoto[] = {                          1,
    2,    4,    5,   23,   25,   14,   15,    9,   10,   11,
  100,  101,   28,   37,   38,   39,   40,   41,   42,   43,
   54,   26,   78,
};
final static short yysindex[] = {                         0,
    0, -231, -219,    0,    0, -223, -212, -260, -210, -209,
    0,    0,    0, -193,    0, -205, -212,    0, -260,    0,
 -200,    0, -201, -260,    0,    0, -174,    5, -183, -141,
 -160, -155,    8,    8,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    8,    8,    8,    8,    0,    0,
    0, -147,    8,  -79,  -65, -201,  -49,  -35,  -19,   98,
 -151,   -4,    8,    8,    8,    8,    8,    8,    8,    8,
    8,    8,    8,    8,    8,    0,    0,    0,    0, -192,
 -192,    8,    0,   87,   98,  107,  107, -261, -261, -261,
 -261, -268, -268,    0,    0,    0,   21, -139,    0, -140,
 -142,   74,    0, -192,    0,    8,    0,   74,
};
final static short yyrindex[] = {                         0,
    0,  150,    0,    0,    0,    0, -137,    0,    0, -136,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   33,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   45,    0,    0,    0, -215,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -134,    0, -113, -135, -128, -119, -196, -162, -153,
 -144, -241, -207,    0,    0,    0,    0,    0,    0,    0,
 -131, -246,    0,    0,    0,    0,    0, -217,
};
final static short yygindex[] = {                         0,
    0,    0,    0,  123,    0,  135,  142,    0,    0,  147,
    0,    0,   88,  -76,    0,    0,    0,    0,    0,    0,
  -34,    0,    0,
};
final static int YYTABLESIZE=378;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         55,
   73,   74,   75,   98,   99,   71,   72,   73,   74,   75,
   57,   58,   59,   60,   12,   13,   37,   37,   62,   37,
   37,   37,   37,   37,   37,   37,   37,  107,   84,   85,
   86,   87,   88,   89,   90,   91,   92,   93,   94,   95,
   96,   36,   50,   50,   36,    3,   37,  102,   37,   37,
   38,   38,    6,   38,   38,   38,   38,   38,   38,   38,
   38,   44,   44,    7,   44,   44,   44,   44,   44,   44,
   35,  108,   50,   35,   50,   50,    8,   16,   18,   30,
   38,   17,   38,   38,   22,   31,   19,   24,   32,   33,
   34,   44,   35,   44,   44,   45,   45,   29,   45,   45,
   45,   45,   45,   45,   46,   46,   44,   46,   46,   46,
   46,   46,   46,   47,   47,   45,   47,   47,   47,   47,
   47,   47,   48,   48,   61,   45,   46,   45,   45,   42,
   42,   47,   42,   42,   46,   82,   46,   46,   43,   43,
  104,   43,   43,   47,   49,   47,   47,  105,  106,    1,
    8,    7,   48,   34,   48,   48,   33,   56,   27,   42,
   21,   42,   42,   20,    0,   97,    0,    0,   43,    0,
   43,   43,    0,    0,   49,    0,   49,   49,   63,   64,
    0,   65,   66,   67,   68,   69,   70,   71,   72,   73,
   74,   75,   63,   64,    0,   65,   66,   67,   68,   69,
   70,   71,   72,   73,   74,   75,    0,    0,   63,   64,
   76,   65,   66,   67,   68,   69,   70,   71,   72,   73,
   74,   75,   63,   64,   77,   65,   66,   67,   68,   69,
   70,   71,   72,   73,   74,   75,    0,    0,   63,   64,
   79,   65,   66,   67,   68,   69,   70,   71,   72,   73,
   74,   75,   80,   63,   64,    0,   65,   66,   67,   68,
   69,   70,   71,   72,   73,   74,   75,   48,   81,    0,
    0,    0,    0,    0,    0,    0,   30,    0,    0,   49,
   50,   51,   31,   83,    0,   32,   33,   34,    0,   35,
   36,   52,   30,    0,   53,    0,    0,    0,   31,    0,
    0,   32,   33,   34,    5,   35,  103,    0,    0,    0,
    5,    0,    0,    5,    5,    5,   31,    5,    5,    0,
    0,    0,   31,    0,    0,   31,   31,   31,    0,   31,
   31,   63,   64,    0,   65,   66,   67,   68,   69,   70,
   71,   72,   73,   74,   75,   64,    0,   65,   66,   67,
   68,   69,   70,   71,   72,   73,   74,   75,   65,   66,
   67,   68,   69,   70,   71,   72,   73,   74,   75,   67,
   68,   69,   70,   71,   72,   73,   74,   75,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         34,
  269,  270,  271,   80,   81,  267,  268,  269,  270,  271,
   45,   46,   47,   48,  275,  276,  258,  259,   53,  261,
  262,  263,  264,  265,  266,  267,  268,  104,   63,   64,
   65,   66,   67,   68,   69,   70,   71,   72,   73,   74,
   75,  288,  258,  259,  291,  277,  288,   82,  290,  291,
  258,  259,  272,  261,  262,  263,  264,  265,  266,  267,
  268,  258,  259,  287,  261,  262,  263,  264,  265,  266,
  288,  106,  288,  291,  290,  291,  289,  288,  272,  272,
  288,  291,  290,  291,  285,  278,  292,  289,  281,  282,
  283,  288,  285,  290,  291,  258,  259,  272,  261,  262,
  263,  264,  265,  266,  258,  259,  290,  261,  262,  263,
  264,  265,  266,  258,  259,  257,  261,  262,  263,  264,
  265,  266,  258,  259,  272,  288,  287,  290,  291,  258,
  259,  287,  261,  262,  288,  287,  290,  291,  258,  259,
  280,  261,  262,  288,  258,  290,  291,  288,  291,    0,
  288,  288,  288,  288,  290,  291,  288,   35,   24,  288,
   19,  290,  291,   17,   -1,   78,   -1,   -1,  288,   -1,
  290,  291,   -1,   -1,  288,   -1,  290,  291,  258,  259,
   -1,  261,  262,  263,  264,  265,  266,  267,  268,  269,
  270,  271,  258,  259,   -1,  261,  262,  263,  264,  265,
  266,  267,  268,  269,  270,  271,   -1,   -1,  258,  259,
  290,  261,  262,  263,  264,  265,  266,  267,  268,  269,
  270,  271,  258,  259,  290,  261,  262,  263,  264,  265,
  266,  267,  268,  269,  270,  271,   -1,   -1,  258,  259,
  290,  261,  262,  263,  264,  265,  266,  267,  268,  269,
  270,  271,  288,  258,  259,   -1,  261,  262,  263,  264,
  265,  266,  267,  268,  269,  270,  271,  260,  288,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  272,   -1,   -1,  272,
  273,  274,  278,  288,   -1,  281,  282,  283,   -1,  285,
  286,  284,  272,   -1,  287,   -1,   -1,   -1,  278,   -1,
   -1,  281,  282,  283,  272,  285,  286,   -1,   -1,   -1,
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
"$$1 :",
"fun_decl : FUNC IDENT LPAREN params RPAREN FUNCRET prim_type BEGIN local_decls $$1 stmt_list END",
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
"$$2 :",
"compound_stmt : BEGIN local_decls $$2 stmt_list END",
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

//#line 359 "Parser.y"

    private Lexer lexer;
    private Token last_token;

    private int yylex () {
        int yyl_return = -1;
        try {
            yylval = new ParserVal(0);
            yyl_return = lexer.yylex();
            last_token = (Token)yylval.obj;
            String my_text = lexer.yytext();
            //System.out.println("my_text: " + my_text);
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
        System.out.println ("Error message by Parser.yyerror() at near " + lexer.lineno + ":" + lexer.column + ": " + error);
    }


    public Parser(Reader r, boolean yydebug) {
        this.lexer   = new Lexer(r, this);
        this.yydebug = yydebug;
    }
//#line 397 "Parser.java"
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
//#line 48 "Parser.y"
{Debug("program -> decl_list");yyval.obj = program____decllist(val_peek(0).obj);}
break;
case 2:
//#line 51 "Parser.y"
{Debug("decl_list -> decl_list decl");yyval.obj = decl_list_____decl_decl(val_peek(1).obj,val_peek(0).obj);}
break;
case 3:
//#line 53 "Parser.y"
{
                        Debug("decl_list -> eps");
                        yyval.obj = decllist____eps();
                    }
break;
case 4:
//#line 60 "Parser.y"
{
                        Debug("decl -> fun_decl");
                        yyval.obj = decl(val_peek(0).obj);
                    }
break;
case 5:
//#line 67 "Parser.y"
{
                        Debug("fun_decl -> FUNC ID(params)->prim_type BEGIN local_decls");
                        yyval.obj = fundecl____FUNC_IDENT_LPAREN_params_RPAREN_FUNCRET_primtype_BEGIN_localdecls_10X_stmtlist_END(val_peek(8).obj, val_peek(7).obj, val_peek(5).obj, val_peek(2).obj, val_peek(0).obj);
                    }
break;
case 6:
//#line 72 "Parser.y"
{
                        Debug("stmt_list END");
                        yyval.obj = fundecl____FUNC_IDENT_LPAREN_params_RPAREN_FUNCRET_primtype_BEGIN_localdecls_X10_stmtlist_END(val_peek(10).obj, val_peek(8).obj, val_peek(5).obj, val_peek(3).obj, val_peek(1).obj, val_peek(0).obj);
                    }
break;
case 7:
//#line 79 "Parser.y"
{
                        Debug("params -> param_list");
                        yyval.obj = params____paramlist(val_peek(0).obj);
                    }
break;
case 8:
//#line 84 "Parser.y"
{
                        Debug("params -> eps");
                        yyval.obj = params____eps();
                    }
break;
case 9:
//#line 91 "Parser.y"
{
                        Debug("param_list -> param_list COMMA param");
                        yyval.obj = paramlist____param_list_COMMA_param(val_peek(2).obj, val_peek(0).obj);
                    }
break;
case 10:
//#line 96 "Parser.y"
{
                        Debug("param_list -> param");
                        yyval.obj = paramlist____param(val_peek(0).obj);
                    }
break;
case 11:
//#line 103 "Parser.y"
{
                        Debug("param -> VAR type_spec IDENT");
                        yyval.obj = param____VAR_type_spec_IDENT(val_peek(1).obj, val_peek(0).obj);
                    }
break;
case 12:
//#line 110 "Parser.y"
{
                        Debug("type_spec -> prim_type");
                        yyval.obj = type_spec____prim_type(val_peek(0).obj);
                    }
break;
case 13:
//#line 117 "Parser.y"
{
                        Debug("prim_type -> INT" );
                        yyval.obj = primtypeInt____INT();
                    }
break;
case 14:
//#line 122 "Parser.y"
{
                        Debug("prim_type -> BOOL" );
                        yyval.obj = prim_type____BOOL();
                    }
break;
case 15:
//#line 129 "Parser.y"
{
                        Debug("local_decls -> local_decls local_decl");
                        yyval.obj = local_decls____local_decls_local_decl(val_peek(1).obj, val_peek(0).obj);
                    }
break;
case 16:
//#line 134 "Parser.y"
{
                        Debug("local_decls -> eps");
                        yyval.obj = localdecls____eps();
                    }
break;
case 17:
//#line 141 "Parser.y"
{
                        Debug("local_decl -> VAR type_spec IDENT SEMI");
                        yyval.obj = local_decl____VAR_type_spec_IDENT_SEMI(val_peek(3).obj, val_peek(2).obj, val_peek(1).obj);
                    }
break;
case 18:
//#line 148 "Parser.y"
{
                        Debug("stmt_list -> stmt_list stmt");
                        yyval.obj = stmt_list____stmt_list_stmt(val_peek(1).obj, val_peek(0).obj);
                    }
break;
case 19:
//#line 153 "Parser.y"
{
                        Debug("stmt_list -> eps");
                        yyval.obj = stmtlist____eps();
                    }
break;
case 20:
//#line 160 "Parser.y"
{
                        Debug("stmt -> assign_stmt");
                        yyval.obj = stmt____assign_stmt(val_peek(0).obj);
                    }
break;
case 21:
//#line 165 "Parser.y"
{
                        Debug("stmt -> print_stmt");
                        yyval.obj = stmt____print_stmt(val_peek(0).obj);
                    }
break;
case 22:
//#line 170 "Parser.y"
{
                        Debug("stmt -> return_stmt");
                        yyval.obj = stmt____return_stmt(val_peek(0).obj);
                    }
break;
case 23:
//#line 175 "Parser.y"
{
                        Debug("stmt -> if_stmt");
                        yyval.obj = stmt____if_stmt(val_peek(0).obj);
                    }
break;
case 24:
//#line 180 "Parser.y"
{
                        Debug("stmt -> while_stmt");
                        yyval.obj = stmt____while_stmt(val_peek(0).obj);
                    }
break;
case 25:
//#line 185 "Parser.y"
{
                        Debug("stmt -> compound_stmt");
                        yyval.obj = stmt____compound_stmt(val_peek(0).obj);
                    }
break;
case 26:
//#line 192 "Parser.y"
{
                        Debug("assign_stmt -> IDENT ASSIGN expr SEMI");
                        yyval.obj = assign_stmt____IDENT_ASSIGN_expr_SEMI(val_peek(3).obj, val_peek(2).obj, val_peek(1).obj);
                   }
break;
case 27:
//#line 199 "Parser.y"
{
                        Debug("print_stmt -> PRINT expr SEMI");
                        yyval.obj = print_stmt_____PRINT_expr_SEMI(val_peek(1).obj);
                   }
break;
case 28:
//#line 206 "Parser.y"
{
                        Debug("return_stmt -> RETURN expr SEMI");
                        yyval.obj = return_stmt____RETURN_expr_SEMI(val_peek(1).obj);
                    }
break;
case 29:
//#line 213 "Parser.y"
{
                        Debug("if_stmt -> IF LPAREN expr RPAREN stmt ELSE stmt");
                        yyval.obj = if_stmt____IF_LPAREN_expr_RPAREN_stmt_ELSE_stmt(val_peek(4).obj, val_peek(2).obj, val_peek(0).obj);
                    }
break;
case 30:
//#line 220 "Parser.y"
{
                        Debug("while_stmt -> WHILE LPAREN expr RPAREN stmt");
                        yyval.obj = while_stmt____WHILE_LPAREN_expr_RPAREN_stmt(val_peek(2).obj, val_peek(0).obj);
                    }
break;
case 31:
//#line 227 "Parser.y"
{
                        Debug("compound_stmt -> BEGIN local_decls");
                        yyval.obj = compound_stmt____BEGIN_local_decls(val_peek(0).obj);
                    }
break;
case 32:
//#line 232 "Parser.y"
{
                        Debug("compound_stmt                stmt_list END");
                        yyval.obj = compoundstmt____stmtlist_END(val_peek(3).obj, val_peek(1).obj);
                    }
break;
case 33:
//#line 239 "Parser.y"
{
                        Debug("args -> arg_list");
                        yyval.obj = args____arg_list(val_peek(0).obj);
                    }
break;
case 34:
//#line 244 "Parser.y"
{
                        Debug("args -> eps");
                        yyval.obj = args____eps();
                    }
break;
case 35:
//#line 251 "Parser.y"
{
                        Debug("arg_list -> arg_list COMMA expr");
                        yyval.obj = arg_list____arg_list_COMMA_expr(val_peek(2).obj, val_peek(0).obj);
                    }
break;
case 36:
//#line 256 "Parser.y"
{
                        Debug("arg_list -> expr");
                        yyval.obj = arg_list____expr(val_peek(0).obj);
                    }
break;
case 37:
//#line 263 "Parser.y"
{
                        Debug("expr -> expr ADD expr");
                        yyval.obj = expr____expr_ADD_expr(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
                    }
break;
case 38:
//#line 268 "Parser.y"
{
                        Debug("expr -> expr SUB expr");
                        yyval.obj = expr____expr_SUB_expr(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
                    }
break;
case 39:
//#line 273 "Parser.y"
{
                        Debug("expr -> expr MUL expr");
                        yyval.obj = expr____expr_MUL_expr(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
                    }
break;
case 40:
//#line 278 "Parser.y"
{
                        Debug("expr -> expr DIV expr");
                        yyval.obj = expr____expr_DIV_expr(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
                    }
break;
case 41:
//#line 283 "Parser.y"
{
                        Debug("expr -> expr MOD expr");
                        yyval.obj = expr____expr_MOD_expr(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
                    }
break;
case 42:
//#line 288 "Parser.y"
{
                        Debug("expr -> expr EQ expr");
                        yyval.obj = expr____expr_EQ_expr(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
                    }
break;
case 43:
//#line 293 "Parser.y"
{
                         Debug("expr -> expr NE expr");
                         yyval.obj = expr____expr_NE_expr(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
                    }
break;
case 44:
//#line 298 "Parser.y"
{
                        Debug("expr -> expr LE expr");
                        yyval.obj = expr____expr_LE_expr(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
                    }
break;
case 45:
//#line 303 "Parser.y"
{
                        Debug("expr -> expr LT expr");
                        yyval.obj = expr____expr_LT_expr(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
                    }
break;
case 46:
//#line 308 "Parser.y"
{
                        Debug("expr -> expr GE expr");
                        yyval.obj = expr____expr_GE_expr(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
                    }
break;
case 47:
//#line 313 "Parser.y"
{
                        Debug("expr -> expr GT expr");
                        yyval.obj = expr____expr_GT_expr(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
                    }
break;
case 48:
//#line 318 "Parser.y"
{
                        Debug("expr -> expr AND expr");
                        yyval.obj = expr____expr_AND_expr(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
                    }
break;
case 49:
//#line 323 "Parser.y"
{
                        Debug("expr -> expr OR expr");
                        yyval.obj = expr____expr_OR_expr(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
                    }
break;
case 50:
//#line 328 "Parser.y"
{
                        Debug("expr -> NOT expr");
                        yyval.obj = expr____NOT_expr(val_peek(1).obj, val_peek(0).obj);
                    }
break;
case 51:
//#line 333 "Parser.y"
{
                        Debug("expr -> LPAREN expr RPAREN");
                        yyval.obj = expr____LPAREN_expr_RPAREN(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
                    }
break;
case 52:
//#line 338 "Parser.y"
{
                        Debug("expr -> IDENT");
                        yyval.obj = expr____ident(val_peek(0).obj);
                    }
break;
case 53:
//#line 343 "Parser.y"
{
                        Debug("expr -> INT_LIT");
                        yyval.obj = expr_______int_lit(val_peek(0).obj);
                    }
break;
case 54:
//#line 348 "Parser.y"
{
                        Debug("expr -> BOOL_LIT");
                        yyval.obj = expr_______bool_lit(val_peek(0).obj);
                    }
break;
case 55:
//#line 353 "Parser.y"
{
                        Debug("expr -> CALL IDENT LPAREN args RPAREN");
                        yyval.obj = expr____CALL_IDENT_LPAREN_args_RPAREN(val_peek(4).obj, val_peek(3).obj, val_peek(1).obj);
                    }
break;
//#line 925 "Parser.java"
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
