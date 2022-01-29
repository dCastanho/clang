package Types;

import Util.CodeBlock;
import Util.JVMCodes;



public class TypeString implements IType, Printable, IComparable{

    public static TypeString STRING_TYPE = new TypeString();

    @Override
    public String show() {
        return "String";
    }

    @Override
    public String getJVMType() {
        return "Ljava/lang/String;";
    }

    @Override
    public String getJVMName() {
        return "java/lang/String";
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
        return t2 instanceof TypeString;
    }

    @Override
    public void print(CodeBlock c) {
        c.emit( JVMCodes.PRINT_STRING );              
    }

    @Override
    public void printLn(CodeBlock c) {
        c.emit( JVMCodes.PRINT_STRINGLN);      
        
    }

    @Override
    public void comparison(CodeBlock c) {
        c.emit( "invokestatic Lib/compareString(Ljava/lang/String;Ljava/lang/String;)I" );        
    }
    
}
