package Compiler.PAR;

import Compiler.LEX.Token;

public class Node {
    Token token;
    int ntIndex;
    Node childNode1 = null;
    Node childNode2 = null;

    public Node(){}

    public Node(Token token,int ntIndex) {
        this.token = token;
        this.ntIndex = ntIndex;
    }

    public void setToken(Token token){
        this.token = token;
    }

    public void setNtIndex(int ntIndex){
        this.ntIndex = ntIndex;
    }

    public void setChildNode1(Node node){
        this.childNode1 = node;
    }

    public void setChildNode2(Node node){
        this.childNode2 = node;
    }

    public Token getToken(){
        return this.token;
    }

    public int getNtIndex(){return this.ntIndex;}

    public Node getChildNode1(){
        return this.childNode1;
    }

    public Node getChildNode2(){
        return this.childNode2;
    }


    @Override
    public String toString() {
        return "(" +token.toString() + "," + ntIndex + ")";
    }
}
