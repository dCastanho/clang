package Values;

import Exceptions.NotSameType;

/**
 * Class to represent int values
 * @author Daniel Castanho
 * @author Eric Solcan
 */
public class IntValue implements IValue, VComparable{

    private int val;

    public IntValue(int i) {
        val = i;
    }

    public String show() {
        return String.valueOf(val);
    }

    public int getValue() {
        return val;
    }

    @Override
    public int compareTo(VComparable o) throws NotSameType {
        if(!(o instanceof IntValue))
            throw new NotSameType();
        IntValue other = (IntValue)o;
        int i = other.getValue();
        if( i == val)
            return 0;
        else if (val > i)
            return 1;
        else 
            return -1;
    }

    @Override
    public boolean sameType(IValue v) {
        return (v instanceof IntValue);
    }
    
}
