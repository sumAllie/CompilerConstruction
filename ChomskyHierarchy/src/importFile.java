import java.io.*;
import java.lang.String;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.ListIterator;

public class importFile {

    public static File getFile(String filename){
        try {
            File file = new File(filename);
            return file;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Grammer readFile(String filename) throws IOException {
        Grammer grammer = new Grammer();
        File file = getFile(filename);
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

        String line = null;
        int i = 0;
        ArrayList<String>r = new ArrayList<String>();;
        while ((line = br.readLine()) != null) {
           if(i == 0){
               grammer.setStart(line);
           }
           else if(i == 1){
               grammer.setVn(line);
           }
           else{
               r.add(line);
           }
           i++;
        }
        if(i>=3){
            grammer.setRules(r);
        }
        else{
            System.out.println("You miss some of the hierarchy");
            return null;
        }


        br.close();
        return grammer;

    }
}
