package Types;

import Util.CodeBlock;
import Util.JVMCodes;



public class TypeBool implements IType, Printable, IComparable{

    public static TypeBool BOOL_TYPE = new TypeBool();

    public TypeBool(){}

    @Override
    public String show() {
        return "Boolean";
    }

    public String getJVMType() {
        return "Z";
    }

    @Override
    public String getJVMName() {
        return getJVMType();
    }

    @Override
    public boolean sameType(IType t2) {
        return t2 instanceof TypeBool;
    }

    @Override
    public String toString() {
        return "bool";
    }

    @Override
    public String returnCode() {
        return JVMCodes.IRETURN;
    }

    @Override
    public String loadCode() {
        return JVMCodes.ILOAD;
    }

    @Override
    public void print(CodeBlock c) {
        c.emit(  JVMCodes.PRINT_BOOL );        
    }

    @Override
    public void printLn(CodeBlock c) {
        c.emit( JVMCodes.PRINT_BOOLLN );        
    }

    @Override
    public void comparison(CodeBlock c) {
        c.emit( JVMCodes.SUB );
    }


}
