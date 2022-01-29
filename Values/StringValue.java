package Values;

import Exceptions.NotSameType;

public class StringValue implements IValue, VComparable{

    private String val;

    public StringValue(String value) {
        val = value;
    }

    @Override
    public String show() {
        return val;
    }

    public String getValue(){
        return val;
    }

    @Override
    public int compareTo(VComparable o) throws NotSameType{
        if(!(o instanceof StringValue))
            throw new NotSameType();
        StringValue other = (StringValue)o;
        return val.compareTo(other.val);
    } 

    @Override
    public boolean sameType(IValue v) {
        return (v instanceof StringValue);
    }
    
}
