java -jar jflex-1.6.1.jar Lexer.flex
yacc.exe -Jthrows="Exception" -Jextends=ParserImpl -Jclass=Parser -Jnorun -J Parser.y

javac *.java

java TestEnv

java  SemanticChecker    ..\minc\syntaxerr_01_main.minc          > ..\myoutput\output_syntaxerr_01_main.txt
java  SemanticChecker    ..\minc\syntaxerr_02_expr1.minc         > ..\myoutput\output_syntaxerr_02_expr1.txt
java  SemanticChecker    ..\minc\syntaxerr_03_expr2.minc         > ..\myoutput\output_syntaxerr_03_expr2.txt
java  SemanticChecker    ..\minc\syntaxerr_04_stmt.minc          > ..\myoutput\output_syntaxerr_04_stmt.txt
java  SemanticChecker    ..\minc\syntaxerr_05_func1.minc         > ..\myoutput\output_syntaxerr_05_func1.txt

java  SemanticChecker    ..\minc\test_01_main_fail1.minc         > ..\myoutput\output_test_01_main_fail1.txt
java  SemanticChecker    ..\minc\test_01_main_fail2.minc         > ..\myoutput\output_test_01_main_fail2.txt
java  SemanticChecker    ..\minc\test_01_main_succ.minc          > ..\myoutput\output_test_01_main_succ.txt
java  SemanticChecker    ..\minc\test_02_expr1_fail1.minc        > ..\myoutput\output_test_02_expr1_fail1.txt
java  SemanticChecker    ..\minc\test_02_expr1_fail2.minc        > ..\myoutput\output_test_02_expr1_fail2.txt
java  SemanticChecker    ..\minc\test_02_expr1_fail3.minc        > ..\myoutput\output_test_02_expr1_fail3.txt
java  SemanticChecker    ..\minc\test_02_expr1_fail4.minc        > ..\myoutput\output_test_02_expr1_fail4.txt
java  SemanticChecker    ..\minc\test_02_expr1_fail5.minc        > ..\myoutput\output_test_02_expr1_fail5.txt
java  SemanticChecker    ..\minc\test_02_expr1_fail6.minc        > ..\myoutput\output_test_02_expr1_fail6.txt
java  SemanticChecker    ..\minc\test_02_expr1_succ.minc         > ..\myoutput\output_test_02_expr1_succ.txt
java  SemanticChecker    ..\minc\test_03_expr2_fail1.minc        > ..\myoutput\output_test_03_expr2_fail1.txt
java  SemanticChecker    ..\minc\test_03_expr2_fail2.minc        > ..\myoutput\output_test_03_expr2_fail2.txt
java  SemanticChecker    ..\minc\test_03_expr2_fail3.minc        > ..\myoutput\output_test_03_expr2_fail3.txt
java  SemanticChecker    ..\minc\test_03_expr2_fail4.minc        > ..\myoutput\output_test_03_expr2_fail4.txt
java  SemanticChecker    ..\minc\test_03_expr2_succ.minc         > ..\myoutput\output_test_03_expr2_succ.txt
java  SemanticChecker    ..\minc\test_04_stmt_fail1.minc         > ..\myoutput\output_test_04_stmt_fail1.txt
java  SemanticChecker    ..\minc\test_04_stmt_fail2.minc         > ..\myoutput\output_test_04_stmt_fail2.txt
java  SemanticChecker    ..\minc\test_04_stmt_fail3.minc         > ..\myoutput\output_test_04_stmt_fail3.txt
java  SemanticChecker    ..\minc\test_04_stmt_fail4.minc         > ..\myoutput\output_test_04_stmt_fail4.txt
java  SemanticChecker    ..\minc\test_04_stmt_succ.minc          > ..\myoutput\output_test_04_stmt_succ.txt
java  SemanticChecker    ..\minc\test_05_func1_fail1.minc        > ..\myoutput\output_test_05_func1_fail1.txt
java  SemanticChecker    ..\minc\test_05_func1_fail2.minc        > ..\myoutput\output_test_05_func1_fail2.txt
java  SemanticChecker    ..\minc\test_05_func1_succ.minc         > ..\myoutput\output_test_05_func1_succ.txt
java  SemanticChecker    ..\minc\test_06_func2_fail1.minc        > ..\myoutput\output_test_06_func2_fail1.txt
java  SemanticChecker    ..\minc\test_06_func2_fail2.minc        > ..\myoutput\output_test_06_func2_fail2.txt
java  SemanticChecker    ..\minc\test_06_func2_fail3.minc        > ..\myoutput\output_test_06_func2_fail3.txt
java  SemanticChecker    ..\minc\test_06_func2_fail4.minc        > ..\myoutput\output_test_06_func2_fail4.txt
java  SemanticChecker    ..\minc\test_06_func2_fail5.minc        > ..\myoutput\output_test_06_func2_fail5.txt
java  SemanticChecker    ..\minc\test_06_func2_succ.minc         > ..\myoutput\output_test_06_func2_succ.txt
java  SemanticChecker    ..\minc\test_07_func3_fail1.minc        > ..\myoutput\output_test_07_func3_fail1.txt
java  SemanticChecker    ..\minc\test_07_func3_succ.minc         > ..\myoutput\output_test_07_func3_succ.txt
java  SemanticChecker    ..\minc\test_08_advanced1_succ.minc     > ..\myoutput\output_test_08_advanced1_succ.txt
java  SemanticChecker    ..\minc\test_08_advanced2_succ.minc     > ..\myoutput\output_test_08_advanced2_succ.txt
java  SemanticChecker    ..\minc\test_09_advanced3_fail1.minc    > ..\myoutput\output_test_09_advanced3_fail1.txt
java  SemanticChecker    ..\minc\test_09_advanced3_succ.minc     > ..\myoutput\output_test_09_advanced3_succ.txt
java  SemanticChecker    ..\minc\test_10_scope1_fail1.minc       > ..\myoutput\output_test_10_scope1_fail1.txt
java  SemanticChecker    ..\minc\test_10_scope1_fail2.minc       > ..\myoutput\output_test_10_scope1_fail2.txt
java  SemanticChecker    ..\minc\test_10_scope1_fail3.minc       > ..\myoutput\output_test_10_scope1_fail3.txt
java  SemanticChecker    ..\minc\test_10_scope1_succ.minc        > ..\myoutput\output_test_10_scope1_succ.txt
