package Types;
import Util.CodeBlock;
import Util.JVMCodes;

public class TypeInt implements IType, Printable, IComparable{

    public static TypeInt INT_TYPE = new TypeInt();

    public TypeInt(){}

    @Override
    public String show() {
        return "Int";
    }

    @Override
    public String getJVMType() {
        return "I";
    }

    @Override
    public String getJVMName() {
        return getJVMType();
    }

    @Override
    public boolean sameType(IType t2) {
        return t2 instanceof TypeInt;
    }

    @Override
    public String toString() {
        return "int";
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
        c.emit(  JVMCodes.PRINT_INT );        
    }

    @Override
    public void printLn(CodeBlock c) {
        c.emit( JVMCodes.PRINT_INTLN );        
    }

    @Override
    public void comparison(CodeBlock c) {
        c.emit( JVMCodes.SUB );
    }

 
}
