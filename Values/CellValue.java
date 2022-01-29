package Values;

/**
 * Class representing a references.
 * @author Daniel Castanho
 * @author Eric Solcan
 */
public class CellValue implements IValue{

    private IValue val;
    
    public CellValue(IValue v) {
        val = v;
    }

    @Override
    public String show() {
        return val.hashCode() + "";
    }

    public IValue get() {
        return val;
    }

    public void setVal(IValue v) {
        val = v;
    }

    @Override
    public boolean sameType(IValue v) {
        return (v instanceof CellValue);
    }

    
    
}
