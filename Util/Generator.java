package Util;

/**
 * Class that holds several important name generators
 * @author Daniel Castanho, 55078
 * @author Eric Solcan, 55562
 */
public class Generator {

    private static int frames = 0;
    private static int closures = 0;
    private static int labels = 0;

    public static final String FRAME_NAME_FORMAT = "frame_%d";
    public static final String CLOSURE_NAME_FORMAT = "closure_%d";
    public static final String LABEL_NAME_FORMAT = "lab_%d";

    public static String genFrameName(){
        String symbol = String.format(FRAME_NAME_FORMAT, frames);
        frames++;
        return symbol;
    }

    public static String genClosureName(){
        String symbol = String.format(CLOSURE_NAME_FORMAT, closures);
        closures++;
        return symbol;
    }

    public static String genLabelName(){
        String symbol = String.format(LABEL_NAME_FORMAT, labels);
        labels++;
        return symbol;
    }


    
}
