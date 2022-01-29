package Nodes;

import Types.IType;
import Types.TypeBool;
import Util.CodeBlock;
import Util.Coordinates;
import Util.Environment;
import Util.JVMCodes;
import Values.BoolValue;
import Values.IValue;

/**
 * ASTNode for boolean values (<code>true</code> or <code>false</code>)
 * @author Daniel Castanho, 55078
 * @author Eric Solcan, 55562
 */
public class ASTBool implements ASTShortCircuitNode{

    private boolean val;

    public ASTBool(boolean b) {
        val = b;
    }

    @Override
    public IValue eval(Environment<IValue> e) {
        return val ? BoolValue.TRUE : BoolValue.FALSE;
    }

    @Override
    public void compile(CodeBlock c, Environment<Coordinates> e) {    
        int jvmBool = this.val ? 1 : 0; // true = 1 | false = 0
        c.emit( String.format(JVMCodes.SIPUSH, jvmBool) );    
    }

    @Override
    public IType typecheck(Environment<IType> e) {
        return TypeBool.BOOL_TYPE;
    }


    @Override
    public void compileSC(CodeBlock c, Environment<Coordinates> e, String trueL, String falseL) {
        String label = this.val ? trueL : falseL;
        c.emit( String.format(JVMCodes.GOTO, label) );
    }

    
    
}
