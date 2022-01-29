package Util;

import Types.IType;
import Types.TypeBool;
import Types.TypeInt;
import Types.TypeString;
import Values.BoolValue;
import Values.IValue;
import Values.IntValue;
import Values.StringValue;

public class Overloader {

    /* Possible combinations for overloading

     String + String -> String 
     String + Int -> String 
     String + Boolean -> String 
     Int + Int -> Int 
     Int + Boolean -> !ERROR!
     Boolean + Boolean -> !ERROR!

     */


    public static IType typecheckArguments(IType t1, IType t2){

        // If either one is invalid, the result is false
        if( !isValid(t1) || !isValid(t2) )
            return null;

        // Both types are either an int, bool or string

        // Strings can be added to any value
        if( isString(t1) || isString(t2))
            return TypeString.STRING_TYPE;

        // If one is int the other has to be int.
        if( isInt(t1) && isInt(t2)) 
            return TypeInt.INT_TYPE;

        // All other possibilities are invalid
        return null;
    } 

    public static IValue evalArguments(IValue v1, IValue v2){

        //If either type is invalid, can't evaluate
        if( !isValid(v1) || !isValid(v2) )
            return null;

        // v1 is an int
        if( isInt(v1) ){
            
            if (isString(v2))
                return intString(v1, v2, true);

            if (isInt(v2))
                return intInt(v1, v2);

            return null;
        }

        //v1 is a string
        if( isString(v1) ){

            if( isString(v2) )
                return stringString(v1, v2);

            if( isInt(v2) )
                return intString(v2, v1, false);

            if( isBoolean(v2) )
                return booleanString(v2, v1, false);
        }

        // v1 is a boolean
        if( isBoolean( v1) ) {

            if( isString(v2) )
                return booleanString(v1, v2, true);

        }

        return null;
    }

    public static void compileArguments(CodeBlock c, IType l, IType r){
        if( isInt(l) ){
            
            if (isString(r))
                intString(c, true);

            else if (isInt(r))
                intInt(c);
                
        }

        //l is a string
        else if( isString(l) ){

            if( isString(r) )
                stringString(c);

            else if( isInt(r) )
                intString(c, false);

            else if( isBoolean(r) )
                booleanString(c, false);
        }

        // l is a boolean
        else if( isBoolean( l) ) {

            if( isString(r) )
                booleanString(c, true);

        }


    }

    //& Private methods for compilation
    
    private static void intInt(CodeBlock c){
        c.emit( JVMCodes.ADD );
    }

    private static void intString(CodeBlock c, boolean order){
        if(order)
            c.emit( "invokestatic Lib/intStringAdd(ILjava/lang/String;)Ljava/lang/String;" );
        else 
            c.emit( "invokestatic Lib/stringIntAdd(Ljava/lang/String;I)Ljava/lang/String;" );
    }

    private static void stringString(CodeBlock c){
        c.emit( "invokestatic Lib/stringAdd(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;" );
    }

    private static void booleanString(CodeBlock c, boolean order){
        if(order)
            c.emit( "invokestatic Lib/booleanStringAdd(ZLjava/lang/String;)Ljava/lang/String;" );
        else 
            c.emit( "invokestatic Lib/stringBooleanAdd(Ljava/lang/String;Z)Ljava/lang/String;" );
    }


    //& Private methods for value additions

    private static IValue intInt(IValue i1, IValue i2){
        int v1 = ((IntValue)i1).getValue();
        int v2 =  ((IntValue)i2).getValue();
        return new IntValue(  v1 + v2);
    }

    private static IValue intString(IValue i, IValue s, boolean order){
        int v1 = ((IntValue)i).getValue();
        String s1 = ((StringValue)s).getValue();
        return new StringValue( order ? v1 + s1 : s1 + v1);
    }

    private static IValue stringString(IValue s1, IValue s2){
        String v1 = ((StringValue)s1).getValue();
        String v2 = ((StringValue)s2).getValue();
        return new StringValue( v1 + v2 );
    }

    private static IValue booleanString(IValue b, IValue s, boolean order){
        boolean v1 = ((BoolValue)b).getValue();
        String v2 = ((StringValue)s).getValue();
        return new StringValue( order ? v1 + v2 : v2 + v1);
    }



    //& Private ITYPE comparisons to make reading easier

    private static boolean isString(IType t){
        return t instanceof TypeString;
    }

    
    private static boolean isInt(IType t){
        return t instanceof TypeInt;
    }

    
    private static boolean isBoolean(IType t){
        return t instanceof TypeBool;
    }

    public static boolean isValid(IType t){
        return isBoolean(t) || isString(t) || isInt(t);
    }
    
    //& Private IValue Comparisons to make reading easier

    private static boolean isString(IValue v){
        return v instanceof StringValue;
    }

    
    private static boolean isInt(IValue v){
        return v instanceof IntValue;
    }

    
    private static boolean isBoolean(IValue v){
        return v instanceof BoolValue;
    }

    public static boolean isValid(IValue v){
        return isBoolean(v) || isString(v) || isInt(v);
    }
}