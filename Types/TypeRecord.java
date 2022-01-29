package Types;

import java.util.Iterator;

import Structures.Entry;
import Structures.InsertionMap;
import Util.JVMCodes;

public class TypeRecord implements IType{
    
    private InsertionMap<String, IType> fields;

    public TypeRecord(InsertionMap<String, IType> f) {
        fields = f;
    }

    @Override
    public String show() {
        return name();

    }

    @Override
    public String getJVMType() {
        return "L" + getJVMName() + ";";
    }

    @Override
    public String getJVMName() {
        return name();
    }

    private String name(){
        String starter = "record_";
        for( Entry<String, IType> e : fields)
            starter += e.getKey() + "_" + e.getValue().show();
        return starter;
    }

    @Override
    public String returnCode() {
        return JVMCodes.ARETURN;
    }

    @Override
    public String loadCode() {
        return JVMCodes.ALOAD;
    }

    @Override
    public boolean sameType(IType t2) {
        if(!(t2 instanceof TypeRecord))
            return false;
        
        TypeRecord r2 = (TypeRecord)t2;

        InsertionMap<String, IType> r2_fields = r2.fields;

        if(r2_fields.size() != fields.size())
            return false;
        
        boolean same = false;
        Iterator<Entry<String,IType>> this_It = fields.iterator();
        Iterator<Entry<String,IType>> r2_It = r2_fields.iterator();

        while(!same && this_It.hasNext()){
            IType type1 = this_It.next().getValue();
            IType type2 = r2_It.next().getValue();
            if(type1.sameType(type2))
                same = true;
        }

        return same;

    }

    public InsertionMap<String, IType> getFields() {
        return fields;
    }

    @Override
    public String toString() {
        return "[" + fields.toString() + "]";
    }

    
}
