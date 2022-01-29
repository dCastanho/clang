package Types;

import java.util.Iterator;
import java.util.List;

import Util.JVMCodes;


public class TypeFunction implements IType{

    private List<IType> args;
    private IType returnType;

    public TypeFunction(List<IType> args, IType ret) {
        this.args = args;
        returnType = ret;
    }

    @Override
    public String show() {
        return getJVMName();
    }

    public List<IType> getArgs() {
        return args;
    }

    public IType getReturnType() {
        return returnType;
    }

    @Override
    public String getJVMType() {
        return "L" + getJVMName() + ";";
    }

    @Override
    public String getJVMName() {
        return nameGenerator("interface");
    }

    public int size(){
        return args.size() + 1;
    }

    private String nameGenerator(String starter){
        String name = starter;
        for( IType t : args)
            name += "_" + t.show();
        return name + "_r" + returnType.show();
    }

    public String getApplySignature(){
        String typeList = "apply(";
        boolean first = true;
        for( IType t : args)
            if(first){
                first = false;
                typeList += t.getJVMType();
            } else 
                typeList += t.getJVMType();
        typeList += ")";
        return typeList + returnType.getJVMType();
    }

    public String getInterfaceDefinition(){
        String def = ".interface public %s\n"+ 
        ".super java/lang/Object\n"+ 
        ".method public abstract %s\n"+ 
        ".end method";
        def = String.format(def, nameGenerator("interface"), getApplySignature());
        return def;
    }

    @Override
    public boolean sameType(IType t2) {
        if(! (t2 instanceof TypeFunction))
            return false;
        TypeFunction f = (TypeFunction) t2;

        if(!returnType.sameType(f.getReturnType()))
            return false;;

        List<IType> t2_args = f.getArgs();

        if(t2_args.size() != args.size())
            return false;
        
        Iterator<IType> t1_it = args.iterator();
        Iterator<IType> t2_it = f.getArgs().iterator();

        while(t1_it.hasNext())
            if(!t1_it.next().sameType( t2_it.next() ))
                return false;

        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
        return false;
    else if (!(obj instanceof TypeFunction))
        return false;
    else 
        return sameType((TypeFunction)obj);
    }
    

    @Override
    public String toString() {
        return args.toString() + " -> " + returnType.toString();
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
