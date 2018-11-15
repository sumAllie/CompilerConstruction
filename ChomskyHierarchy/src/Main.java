import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String filename = "testing/chomsky3.txt";
        importFile file = new importFile();

        Grammer g = file.readFile(filename);
        Hierarchy h = g.setHierarchy();
        h.print();
    }
}
