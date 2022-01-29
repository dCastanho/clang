package Values;

import Exceptions.NotSameType;

public interface VComparable {
    
    public int compareTo(VComparable o) throws NotSameType;


}
