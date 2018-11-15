package Compiler.LEX;

public class Token{
    int symIndex;
    String token;

    public Token(int symIndex, String token) {
        this.symIndex = symIndex;
        this.token = token;
    }

    public int getSymIndex(){
        return this.symIndex;
    }

    public String getToken(){
        return this.token;
    }

    public void setToken(String s){
        this.token = s;
    }
    @Override
    public String toString() {
        return "(" + symIndex + ", " + token + ")";
    }
}