import java.util.ArrayList;
import java.lang.String;

public class Grammer {
    private String start;
    private String Vn;
    private ArrayList<String> rules;

    public void setRules(ArrayList<String> rules) {
        this.rules = rules;
    }

    public void setStart(String s){
        this.start = s;
    }

    public void setVn(String vn){
        this.Vn = vn;
    }

    public ArrayList<String>  getRules() {
        return this.rules;
    }

    public String getStart(){
        return this.start;
    }

    public String getVn(){
        return this.Vn;
    }


    public Hierarchy setHierarchy() {
        Hierarchy h = new Hierarchy();

        String s1 = this.getStart();
        int i1 = s1.indexOf("[");
        if(s1.substring(i1,i1+1).equals("]")){
            System.out.println("请检查起始符号是否正确输入！");
            System.exit(0);
        }
        h.setBegin(s1.charAt(i1 + 1));

        ArrayList<String> ch = new ArrayList<>();
        String s2 = this.getVn();
        String[] sArray = s2.split(",");
        for (String s:sArray) {
            ch.add(s);
        }
        h.setVnList(ch);


        ArrayList<String> pl = new ArrayList<>();
        ArrayList<String> gr = this.getRules();
        for (String a: gr) {
            pl.add(a);
        }
        h.setPList(pl);


        ArrayList<String> tlist = new ArrayList<>();
        for (int m = 0; m < gr.size(); m++) {
            String r0 = gr.get(m);
            int i3 = r0.indexOf("::=");
            String r1 = r0.substring(i3 + 3);
            String[] rArray = r1.split("\\|");
            for (String rs : rArray) {
                for (int n = 0; n < rs.length(); n++) {
                    String rc = rs.substring(n,n+1);
                    if (tlist.contains(rc) || ch.contains(rc) || rc.equals(" ")) {
                    } else {
                        tlist.add(rc);
                    }
                }
            }
        }
        h.setVtList(tlist);


        ArrayList<String>l = new ArrayList<>();
        ArrayList<String>r = new ArrayList<>();
        ArrayList<Character> all = new ArrayList<>();

        for(String s : gr){
            String[] lr = s.split("::=");
            l.add(lr[0]);
            Character a;

            //left
            for(int i = 0; i<lr[0].length();i++){
                a = lr[0].charAt(i);
                if(!all.contains(a)){
                    all.add(a);
                }
            }

            //right
            Character b;
            String[] rightStr = lr[1].split("\\|");
            for(String sr : rightStr){
                r.add(sr);
            }
        }
        h.setLeft(l);
        h.setRight(r);



        return h;
    }
}
