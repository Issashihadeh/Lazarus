package addOns;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MapScanner {

    public static String WALL = "WAL";

    public static String SPACE = "000";

    public static String LAZARUS = "LAZ";

    public static String STOP = "STP";

    public static String CARDBOARD_BOX = "CBX";

    public static String WOOD_BOX = "WBX";

    public static String STONE_BOX = "SBX";

    public static String Rock = "RBX";

    public static final Set<String> ALL_BOX_SET = new HashSet<String>(Arrays.asList(MapScanner.CARDBOARD_BOX,
            MapScanner.STONE_BOX, MapScanner.WOOD_BOX, MapScanner.Rock));;

    public static String[][] readMap(int level) throws Exception {
        String mapFileName = Api.getMapFileName(level);
        String[][] arr = new String[GameComponents.MAXIMUM_NUMBER_OF_BLOCKS][GameComponents.MAXIMUM_NUMBER_OF_BLOCKS];
        Api.validateFileExists(mapFileName);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(mapFileName));
        String line;
        int row = 0, column = 0;
        while ((line = bufferedReader.readLine()) != null) {
            column = 0;
            String[] tokens = line.split(",");
            for (String value : tokens) {
                arr[row][column] = value;
                column += 1;
            }
            row += 1;
        }
        bufferedReader.close();
        return arr;
    }

}
