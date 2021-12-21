package ge.helan.main;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {

        HeLan h = null;
        try {
            h = new HeLan(new String ( Files.readAllBytes( Paths.get(args[0]) ) ));
        } catch (IOException e) {
            e.printStackTrace();
        }
        h.execute();
    }

}
