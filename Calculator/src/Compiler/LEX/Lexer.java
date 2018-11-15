package Compiler.LEX;

import java.util.ArrayList;
import java.util.List;
import java.io.*;


public class Lexer {
    Symbol sym;
    List<String> strings = new ArrayList<>();//enter the expressions into the program
    List<Character> tmpNumToken = new ArrayList<>(); // 处理过程中暂时存储一个常数
    List<Number> numberList = new ArrayList<>(); // 常数表
    List<Token> tokenList = new ArrayList<>();
    public static List<List<Token>> lexerList = new ArrayList<>();
    int index = 0;
    String current = null;
    public static String LOutput = "";
    Boolean isErr = false;

    private void inputFile(BufferedReader br){
        String tempString = null;
        try {
            while ((tempString = br.readLine()) != null) {
                strings.add(tempString);
            }

        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (br != null){
                try {
                    br.close();
                }catch (IOException e1){

                }
            }
        }
    }

    private char getChar(){
        char c;
        if (index == current.length() ) {
            index++;
            return '\0';
        }
        c = current.charAt(index);
        index++;
        return c;
    }

    /**
     * 将tmpNumToken转换为String，同时将数字字符串转换位Number放入numberList中
     * 由于可能出现以多个0开头的数字，必须去掉前面无效的0
     * 00000视作0
     * @return num string
     */

    String listToString(Symbol sym){
        String s = "";
        boolean zeroHead = true;
        for (int i = 0; i < tmpNumToken.size(); i++){
            if (i < tmpNumToken.size()-1){
                if (zeroHead) {
                    if (tmpNumToken.get(i) != '0') {
                        zeroHead = false;
                        s += tmpNumToken.get(i).toString();
                    }

                }
                else {
                    s += tmpNumToken.get(i).toString();
                }
            }
            else {
                s += tmpNumToken.get(i).toString();
            }

        }

        // 添加常数到常数表中
        if (sym == Symbol.INTEGER){
            int num = Integer.valueOf(s);
            LOutput = LOutput + num + "\n";
            numberList.add(num);
        }
        else {
            double num = Double.valueOf(s);
            LOutput = LOutput + num + "\n";
            numberList.add(num);
        }

        return s;
    }

    private void scanner(){
        index = 0;
        char c;
        while (index<current.length()){
            boolean isReal = false;
            c = getChar();
            boolean isError = false;
            char tmp = '\0';

            // 忽略空格和制表符和换行符
            if ((int)c == 0x20 || c == '\t' || c =='\n') continue;

            switch (c){
                case '+':
                    tokenList.add(new Token(Symbol.PLUS.ordinal(),c+""));
                    break;
                case '-':
                    tokenList.add(new Token(Symbol.MINUS.ordinal(),c+""));
                    break;
                case '*':
                    tokenList.add(new Token(Symbol.TIMES.ordinal(),c+""));
                    break;
                case '/':
                    tokenList.add(new Token(Symbol.DIVIDE.ordinal(),c+""));
                    break;
                case '(':
                    tokenList.add(new Token(Symbol.LEFT_BRA.ordinal(),c+""));
                    break;
                case ')':
                    tokenList.add(new Token(Symbol.RIGHT_BRA.ordinal(),c+""));
                    break;
                default:
                    if (c >= '0' && c <='9'){ // number
                        isReal = false;
                        isError = false;
                        while ((c >= '0' && c <='9')|| c == '.'){ // 出现不为数字或'.'结束分析
                            if (c == '.'){
                                if (isReal){ // 出现第二个'.'时报错，并结束分析
                                    //isError = true;
                                    //tmp = c;
                                    isErr = true;
                                    break;
                                }
                                else {
                                    isReal = true;
                                }
                            }
                            tmpNumToken.add(c);
                            c = getChar(); // 读取下一个字符
                        }

                        // 如果最后一个字符是'.'而不是数字，如，20.+，则应该认为是整数，并对'.'报错
                        if (tmpNumToken.get(tmpNumToken.size()-1) == '.'){
                            isReal = false;
                            tmpNumToken.remove(tmpNumToken.size()-1);
                            isError = true;
                            tmp = '.';
                        }

                        if (isReal){
                            tokenList.add(new Token(Symbol.REAL.ordinal(),listToString(Symbol.REAL)));
                        }
                        else {
                            tokenList.add(new Token(Symbol.INTEGER.ordinal(),listToString(Symbol.INTEGER)));
                        }
                        tmpNumToken.clear(); // 清空存储的数字

                        // 延迟加入错误单词，避免打印顺序出错
                        if (isError) tokenList.add(new Token(Symbol.ERROR.ordinal(),tmp+""));

                        // 退回一格防止跳过了一个字符
                        // 如果已经读到末尾后两位(表示最后一位是数字)则不退回
                        if (index <= current.length()) index--;
                    }
                    else {
                        isErr = true;
                        sym = Symbol.ERROR;
                        tokenList.add(new Token(Symbol.ERROR.ordinal(),c+""));
                    }

            }

        }

    }

    private void printTokenInfo(){
        for(Token token : tokenList){
            if (token.symIndex == Symbol.ERROR.ordinal()){
                LOutput = LOutput +"含有不合法字符：\'" + token.token + "\'" + "\n";
            }
            else {
                LOutput = LOutput + token.toString() + "\n";
            }

        }
        if (isErr){
            tokenList = new ArrayList<>();
            tokenList.add(new Token(Symbol.ERROR.ordinal(),null));
        }

        lexerList.add(tokenList);
    }


    public static List<List<Token>> LStart(BufferedReader br){
        Lexer lexer = new Lexer();
        lexer.inputFile(br);
        LOutput = "";
        LOutput = LOutput + "----词法分析----\n";
        LOutput = LOutput + "打印格式：(种别码, Token)\n";

        for (int i=0; i<lexer.strings.size(); i++){
            LOutput = LOutput + "第" + (i+1) + "个字符串分析结果：\n";
            lexer.current = lexer.strings.get(i);
            lexer.scanner();
            lexer.printTokenInfo();
            lexer.tokenList = new ArrayList<>();  // 清空tokenList中的Token
            LOutput = LOutput +"";
        }
        return lexerList;
    }





}