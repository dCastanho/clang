package Types;

import Util.JVMCodes;

public class TypeRef implements IType{

    public static final String FIELD_NAME = "v";

    private IType referenced_type;

    public TypeRef(IType referenced){
        referenced_type = referenced;
    }

    public String show(){
        return "reference_of_" + referenced_type.show();
    }

    public IType getReference() {
        return referenced_type;
    }

    @Override
    public String getJVMType() {
        return "Lref_of_" + referenced_type.show() + ";";
    }

    @Override
    public String getJVMName() {
        return "ref_of_" + referenced_type.show();
    }

    @Override
    public boolean sameType(IType t2) {
        if(!(t2 instanceof TypeRef))
            return false;
        TypeRef r2 = (TypeRef)t2;
        return this.referenced_type.sameType(r2.getReference());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        else if (!(obj instanceof TypeRef))
            return false;
        else 
            return sameType((TypeRef)obj);
    }

    @Override
    public String toString() {
        return "{ref of " + referenced_type.toString() + "}";
    }

    @Override
    public String returnCode() {
        return JVMCodes.ARETURN;
    }

    @Override
    public String loadCode() {
        return JVMCodes.ALOAD;
    }
    
}
