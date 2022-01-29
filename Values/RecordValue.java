package Values;

import Structures.InsertionMap;

public class RecordValue implements IValue{

    private InsertionMap<String, IValue> fields;

    public RecordValue(InsertionMap<String, IValue> fields){
        this.fields = fields;
    }

    @Override
    public String show() {
        return fields.toString();
    }

    public InsertionMap<String, IValue> getFields(){
        return fields;
    }

    @Override
    public boolean sameType(IValue v) {
        return (v instanceof RecordValue);
    }


    
    
}
