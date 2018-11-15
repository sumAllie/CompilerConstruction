package Compiler.PAR;
import Compiler.LEX.Lexer;
import Compiler.LEX.Symbol;
import Compiler.LEX.Token;
import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;

import javax.swing.*;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public class Parser {

    private Token currToken = null;
    public static String POutput = "";
    static int j = -1;//

    public static List<List<Token>> lexerList = new ArrayList<>();

    public static List<Token> tokenList = new ArrayList<>();

    boolean isOpr = false;
    static int right_braket = 0;
    static int left_braket = 0;

    private Node parse(List<Token> tList) {
        Node node = new Node();
        tokenList = tList;

        if (j < tokenList.size() - 1 && tokenList.get(++j).getSymIndex() != -1) {
            currToken = tokenList.get(j);
            node = exp();
        }
        if (currToken == null) {
            throw new RuntimeException("syntax error at " + currToken.toString());
        }
        return node;
    }

    private Node factor() {
        Node node = new Node();

        if (j < tokenList.size() - 1 && (currToken.getSymIndex() == 0 || currToken.getSymIndex() == 1)) {
            try{
                node.setNtIndex(NodeType.FACTOR.ordinal());
                node.setToken(currToken);
                currToken = tokenList.get(++j);
                isOpr = false;
                if( ((left_braket - right_braket)==0) && currToken.getSymIndex() == 7){
                    POutput += "Syntax Error : position ("+ (j+1) +")  右括号冗余 \n";
                    if(j < tokenList.size() - 1){
                        currToken = tokenList.get(++j);
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        } else if (j == tokenList.size() - 1 && (currToken.getSymIndex() == 0|| currToken.getSymIndex() == 1)) {
            node.setNtIndex(NodeType.FACTOR.ordinal());
            node.setToken(currToken);
            isOpr = false;
        } else if (j < tokenList.size() - 1 && currToken.getSymIndex() == 6) {
            currToken = tokenList.get(++j);
            left_braket++;
            isOpr = false;
            node = exp();
            if (j == tokenList.size() - 1 && currToken.getSymIndex() != 7) {

            } else if(j < tokenList.size() - 1 && currToken.getSymIndex() == 7) {
                currToken = tokenList.get(++j);
                right_braket++;
            } else if (j == tokenList.size() - 1 && currToken.getSymIndex() == 7) {
                right_braket++;
            }
        } else if(j == tokenList.size() - 1 && currToken.getSymIndex() == 7){
            right_braket++;
        }

       /* if ((right_braket == left_braket) && j < tokenList.size() - 1 &&currToken.getSymIndex() == 7){
            currToken = tokenList.get(++j);
            POutput += "Something wrong.\n";
        }else if ((right_braket == left_braket) &&j == tokenList.size() - 1 && currToken.getSymIndex() == 7){
            POutput += "Something wrong.\n";
        }*/

        return node;
    }

    private Node term() {
        Node lnode = new Node();
        Node rnode;
        Node node = new Node();

        if(node !=null && node.getNtIndex() == 3){
            node = new Node(null, NodeType.ERROR.ordinal());
            return node;
        }

        if (((currToken.getSymIndex() == 4) || (currToken.getSymIndex() == 5)) && j == 0){
            POutput += "Syntax error: 运算符前无被运算数"+"\n";
            node = new Node(null, NodeType.ERROR.ordinal());
            return node;
        }else{
            node = factor();
        }

        while (((currToken.getSymIndex() == 4) || (currToken.getSymIndex() == 5)) && j <= tokenList.size() - 1) {

            if(isOpr){
                POutput += "Syntax error2: 符号重叠"+"\n";
                node = new Node(null, NodeType.ERROR.ordinal());
                return node;
            }

            if(j == tokenList.size() - 1){
                POutput += "Syntax error: 运算符后无运算数"+"\n";
                node = new Node(null, NodeType.ERROR.ordinal());
                break;
            }

            else {
                lnode.childNode1 = node.childNode1;
                lnode.childNode2 = node.childNode2;
                lnode.ntIndex = node.ntIndex;
                lnode.token = node.token;

                node.setChildNode1(lnode);

                lnode = new Node();

                node.setNtIndex(NodeType.TERM.ordinal());
                node.setToken(currToken);
                currToken = tokenList.get(++j);

                if (node.getToken().getSymIndex() == 5 &&
                        (currToken.getSymIndex() == 0 || currToken.getSymIndex() == 1 ) &&
                        ((Double.valueOf(currToken.getToken())+2.0) == 2.0)){
                    POutput += "Syntax error: \"0\"不可作为除数"+"\n";
                    node = new Node(null, NodeType.ERROR.ordinal());
                    return node;
                }

                if(currToken.getSymIndex() == 7){
                    POutput += "括号不匹配.\n";
                    currToken = tokenList.get(++j);
                }

                isOpr = true;
                rnode = term();


                if(rnode !=null && rnode.getNtIndex() == 3){
                    rnode = new Node(null, NodeType.ERROR.ordinal());
                    return rnode;
                }
                node.setChildNode2(rnode);
            }
        }
        return node;
    }

    private Node exp() {
        Node lnode = new Node();
        Node rnode;
        Node node = new Node();


        if(node !=null && node.getNtIndex() == 3){
            node = new Node(null, NodeType.ERROR.ordinal());
            return node;
        }

        if (((currToken.getSymIndex() == 2) || (currToken.getSymIndex() == 3))
                && (j == 0 || node.getToken().getSymIndex() == 6)){
            Token token = new Token(0,"0");
            node = new Node(token,2);
            isOpr = true;
        }
        else{
            node = term();
        }

        if (((currToken.getSymIndex() == 2) || (currToken.getSymIndex() == 3)) && j == tokenList.size() - 1){
            node = new Node(null, NodeType.ERROR.ordinal());
            return node;
        }


        while (((currToken.getSymIndex() == 2) || (currToken.getSymIndex() == 3)) && j <= tokenList.size() - 1) {
            if(j == tokenList.size() - 1){
                POutput += "Syntax error: 运算符后无运算数"+"\n";
                node = new Node(null, NodeType.ERROR.ordinal());
                return node;
            }

            if(isOpr){
                POutput += "Syntax error: 符号重叠"+"\n";
                node = new Node(null,NodeType.ERROR.ordinal());
                return node;
            }
            else{
                isOpr = true;
                lnode.childNode1 = node.childNode1;
                lnode.childNode2 = node.childNode2;
                lnode.ntIndex = node.ntIndex;
                lnode.token = node.token;

                node.setChildNode1(lnode);

                lnode = new Node();

                node.setNtIndex(NodeType.EXP.ordinal());
                node.setToken(currToken);
                currToken = tokenList.get(++j);

                if(tokenList.get(j).getSymIndex() == 7){
                    POutput += "括号不匹配1.\n";
                    currToken = tokenList.get(++j);
                }

                rnode = term();


                if(rnode !=null && rnode.getNtIndex() == 3){
                    rnode = new Node(null, NodeType.ERROR.ordinal());
                    return rnode;
                }
                node.setChildNode2(rnode);
            }

        }
        return node;
    }

    public static void printTree(Node root) {
        printInOrder(root, 0, 8);
        POutput += "\n";
    }

    public static void printInOrder(Node node, int height,int len) {
        if (node == null) {
            return;
        }

        printInOrder(node.childNode1, height + 1,  len);
        String val = node.getToken().getToken();
        POutput += getSpace(height * len) + val + "\n";
        printInOrder(node.childNode2, height + 1,  len);
    }

    public static String getSpace(int num) {
        String space = " ";
        StringBuffer buf = new StringBuffer("");
        for (int i = 0; i < num; i++) {
            buf.append(space);
        }
        return buf.toString();
    }



    public static List<Node> PStart(BufferedReader br){
        Parser parser = new Parser();
        lexerList = Lexer.LStart(br);
        Node node;
        List<Node> nodeList = new ArrayList<>();

        POutput = "";

        int i = 0;
        POutput = POutput + "----语法分析----\n";

        if (lexerList.size() == 0){
            POutput += "Lexical Error, the lexerlist is empty!\n";
        }
        for (List<Token> tlist: lexerList) {
            POutput =  POutput +  "第" + (++i) + "行算数式分析结果：" +  "\n";
            if (tlist.get(0).getSymIndex() == Symbol.ERROR.ordinal()){
                POutput =  POutput + "Lexical Error!" +  "\n";
                node = null;
                nodeList.add(node);
            }
            else if (tlist.size() == 0){
                POutput =  POutput + "Lexical Error!" +  "\n";
                node = null;
                nodeList.add(node);
            }
            else {
                node = parser.parse(tlist);
                if(node.getNtIndex() == 3){
                    nodeList.add(node);
                }
                else{
                    nodeList.add(node);
                    if (j == tokenList.size() - 1 && (right_braket != left_braket)){
                        POutput += "Syntax Error3 : 括号不匹配\n";
                    }
                    parser.printTree(node);
                }
            }
            tlist.clear();
            POutput = POutput + "";
            j = -1;
            left_braket = 0;
            right_braket = 0;
        }

        lexerList.clear();
        return nodeList;
    }

}