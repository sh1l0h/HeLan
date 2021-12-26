package ge.helan.main;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Usage: java -jar HeLan.jar <hlfile>");
            return;
        }

        HeLan h = null;
        try {
            if (args[0].toLowerCase().endsWith(".hl"))
                h = new HeLan(new String(Files.readAllBytes(Paths.get(args[0]))));
            else {
                System.out.println(".hl file expected.");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        h.execute();
    }

}
