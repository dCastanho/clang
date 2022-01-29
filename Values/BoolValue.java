package Values;

import Exceptions.NotSameType;

/**
 * Class representing a boolean values. Because boolean values must
 * be one of two values, these are easily represented through constants
 * @author Daniel Castanho
 * @author Eric Solcan
 */
public class BoolValue implements IValue, VComparable{

    /**
     * Constant to represent the true value
     */
    public static BoolValue TRUE = new BoolValue(true);

    /**
     * Constant to represent the false value
     */
    public static BoolValue FALSE = new BoolValue(false);


    private boolean val;

    public BoolValue(boolean b) {
        val = b;
    }

    public String show() {
        return String.valueOf(val);
    }

    public boolean getValue(){
        return val;
    }

    public static BoolValue createValue(boolean b){
        return b ? TRUE : FALSE;
    }

    @Override
    public int compareTo(VComparable o) throws NotSameType{
        if(!(o instanceof BoolValue))
            throw new NotSameType();
        BoolValue other = (BoolValue)o;
        boolean b = other.val;
        if (b == val)
            return 0;
        else if (val && !b)
            return 1;
        else 
            return -1;
    }
    
    @Override
    public boolean sameType(IValue v) {
        return (v instanceof BoolValue);
    }

    
}
