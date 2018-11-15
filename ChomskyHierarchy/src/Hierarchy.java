import java.util.ArrayList;

public class Hierarchy {
    private ArrayList<String>VnList;
    private ArrayList<String>VtList;
    private ArrayList<String>PList;
    private Character begin;
    private int type;
    private ArrayList<String> left;
    private ArrayList<String> right;

    public void setBegin(Character b) {
        this.begin = b;
    }

    public void setPList(ArrayList<String> PList) {
        this.PList = PList;
    }

    public void setVtList(ArrayList<String> vtList) {
        this.VtList = vtList;
    }

    public void setVnList(ArrayList<String> vnList) {
        this.VnList = vnList;
    }

    public void setLeft(ArrayList<String> left) {
        this.left = left;
    }

    public void setRight(ArrayList<String> right) {
        this.right = right;
    }

    public ArrayList<String> getLeft() {
        return left;
    }

    public ArrayList<String> getRight() {
        return right;
    }

    public Character getBegin() {
        return this.begin;
    }

    public ArrayList<String> getPList() {
        return this.PList;
    }

    public ArrayList<String> getVnList() {
        return this.VnList;
    }

    public ArrayList<String> getVtList() {
        return this.VtList;
    }

    public void getType(){
        int k = -1;
        boolean isFirst = false;
        boolean isExtended = false;
        boolean isLeft = true;

        for(String l : left){
            boolean isAllVt = true;
            for(int i=0;i<l.length();i++){
                if(VnList.contains(l.substring(i,i+1))){
                    isAllVt = false;
                }
            }
            if(isAllVt){
                System.out.println("产生式左部不能全是终结符。");
                System.exit(0);
                return;
            }

            if(l.length()!=1 || !VnList.contains(l.substring(0,1))){
                isFirst = true;
                break;
            }
        }

        if(isFirst){
            for(String r : PList){
                String[] lr = r.split("::=");
                String[] rightStr = lr[1].split("\\|");
                for(String sr:rightStr){
                    if(sr.contains("ε")){
                        if(lr[0].length() > sr.length()-1){
                            k = 0;
                            break;
                        }
                    }else{
                        if(lr[0].length() > sr.length()){
                            k = 0;
                            break;
                        }
                    }
                }
                if(k == 0){
                    break;
                }
                k = 1;
            }
        } else {
            k = 2;
            int m = 0;
            for (String sl : this.left) {
                if (sl.length() != 1) {
                    break;
                }
                if (!VnList.contains(sl.substring(0, 1))) {
                    break;
                }
                for (String sr : this.right) {
                    m++;
                    if (sr.length() == 1 && !(VnList.contains(sr.substring(0, 1)) || sr.equals("ε"))) {
                        k = 3;
                    }

                    if (sr.length() == 2) {
                        if ((VnList.contains(sr.substring(0, 1)) && VnList.contains(sr.substring(0, 1)))) {
                            if (m == 1 || isLeft)
                                isLeft = true;
                            else {
                                break;
                            }

                        } else if ((VnList.contains(sr.substring(1, 2)) && VnList.contains(sr.substring(1, 2)))) {
                            if (m == 1 || !isLeft)
                                isLeft = false;
                            else {
                                break;
                            }
                        }
                    }
                }

                for (String r : right) {
                    if (r.contains("ε")) {
                        isExtended = true;
                        break;
                    }
                }
            }
        }

        switch (k){
            case 0:
                this.type = 0;
                break;
            case 1:
                if(isExtended){
                    this.type = 0;
                    break;
                }else{
                    this.type = 1;
                    break;
                }
            case 2:
                if(isExtended){
                    this.type = 4;
                    break;
                }else{this.type = 2;
                    break;
                }
            case 3:
                if(isExtended){
                    this.type = 5;
                    break;
                }else{this.type = 3;
                    break;
                }
            default:
                this.type = -1;
                break;

        }
    }



    //print the grammer of the hierarchy
    public void print1(){
        String vn = "{";
        Character b = this.getBegin();
        String vt = "{";

        ArrayList<String>vnlist = getVnList();
        for(int i=0;i<vnlist.size()-1;i++){
            vn = vn + vnlist.get(i);
            vn = vn+",";
        }
        vn = vn + vnlist.get(vnlist.size()-1) + "}";

        ArrayList<String>vtlist = getVtList();
        for(int i=0;i<vtlist.size()-1;i++){
            vt = vt + vtlist.get(i);
            vt = vt +",";
        }
        vt = vt + vtlist.get(vtlist.size()-1) + "}";

        System.out.println("文法 " + "G[" + b + "]=(" + vn + "," + vt + ",P," + b + "}");

        String r1 = this.getPList().get(0);
        System.out.println("P:  " + r1);
        for (int i = 1 ; i < this.getPList().size(); i++){
            System.out.println("\t" + this.getPList().get(i));
        }
    }

    public void print2(){
        getType();
        switch (this.type){
            case 0:
                System.out.println("该文法是Chomsky0型文法。");
                return;
            case 1:
                System.out.println("该文法是Chomsky1型文法(即上下文有关文法)。");
                return;
            case 2:
                System.out.println("该文法是Chomsky2型文法(即上下文无关文法)。");
                return;
            case 3:
                System.out.println("该文法是Chomsky3型文法(即正规文法)。");
                return;
            case 4:
                System.out.println("该文法是Chomsky2型扩展文法。");
                return;
            case 5:
                System.out.println("该文法是Chomsky3型扩展文法。");
                return;
            default:
                System.out.println("该输入不是合法的Chomsky文法。");
        }
    }

    public void print(){
        this.print1();
        this.print2();
    }

}
