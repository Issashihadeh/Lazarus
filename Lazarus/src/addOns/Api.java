package addOns;

import java.io.File;
import java.io.FileNotFoundException;


public class Api {

    public static void validateFileExists(String fileName) {
        File file = new File(fileName);
    }

    public static String getMapFileName(int level) throws IllegalLevelException {
        if (level == 1) return GameComponents.MAP1;
        if (level == 2) return GameComponents.MAP2;
        throw new IllegalLevelException();
    }

}
class IllegalLevelException extends Exception {};
