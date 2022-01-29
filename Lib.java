

/**
 * Static library to be called by the compiler
 * @author Daniel Castanho, 55078
 * @author Eric Solcan, 55562
 */
public class Lib {

    public static void printInt(int val){
        System.out.print(val);
    }

    public static void printString(String val){
        System.out.print(val);
    }

    public static void printBoolean(boolean val){
        System.out.print(val);
    }

    public static void printIntLn(int val){
        System.out.println(val);
    }

    public static void printStringLn(String val){
        System.out.println(val);
    }

    public static void printBooleanLn(boolean val){
        System.out.println(val);
    }


    public static String stringAdd(String s1, String s2){
        return s1 + s2;
    }

    public static String intStringAdd(int i, String s){
        return i + s;
    }

    public static String stringIntAdd(String s, int i){
        return s + i;
    }

    public static String booleanStringAdd(boolean b, String s){
        return b + s ;
    }

    public static String stringBooleanAdd(String s, boolean b){
        return s + b ;
    }

    public static int compareString(String s1, String s2){
        return s1.compareTo(s2);
    }
    
}
