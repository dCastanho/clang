package Nodes;

import Types.IType;
import Util.CodeBlock;
import Util.Coordinates;
import Util.Environment;
import Util.JVMCodes;
import Values.IValue;

/**
 * Class that represents the ASTNode 
 * that the use of an id
 * @author Daniel Castanho, 55078
 * @author Eric Solcan, 55562
 */
public class ASTId implements ASTShortCircuitNode{

    private String id;
    private IType type;

    public ASTId(String id) {
        this.id = id;
        type = null;
    }

    @Override
    public IValue eval(Environment<IValue> e) {
        return e.find(id);
    }

    @Override
    public void compile(CodeBlock c,  Environment<Coordinates> e) {
        //TODO: Clean code
        c.emit( JVMCodes.LOAD_FRAME );
        Coordinates coord = e.find(id);
        int eDepth = e.getDepth();
        int d = eDepth - coord.getDepth() ;
        Environment<Coordinates> curr = e;
        for(int i = 1; i < d; i++){
            String n = curr.getName();
            curr = curr.endScope();
            String n1 = curr.getName();
            c.emit(String.format(  JVMCodes.GETFIELD, n, "sl", "L" + n1 + ";"));   
        }  
        c.emit(String.format( JVMCodes.GETFIELD, curr.getName(), coord.getSlot(), type.getJVMType()));
    }

    @Override
    public IType typecheck(Environment<IType> e) {
        type = e.find(id);
        return type;
    }

    @Override
    public void compileSC(CodeBlock c, Environment<Coordinates> e, String trueL, String falseL) {
        compile(c, e);
        c.emit( String.format( JVMCodes.IFEQ, falseL ) );
        c.emit( String.format( JVMCodes.GOTO, trueL) );
    }
    
}
