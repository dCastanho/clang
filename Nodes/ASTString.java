package Nodes;

import Types.IType;
import Types.TypeString;
import Util.CodeBlock;
import Util.Coordinates;
import Util.Environment;
import Values.IValue;
import Values.StringValue;

/**
 * Class that represents the ASTNode with a string value
 * @author Daniel Castanho, 55078
 * @author Eric Solcan, 55562
 */
public class ASTString implements ASTNode{

    private String value;

    public ASTString(String v) {
        value = v;
    }

    @Override
    public IValue eval(Environment<IValue> e) {
        return new StringValue(value);
    }

    @Override
    public void compile(CodeBlock c, Environment<Coordinates> e) {
        c.emit("ldc \"" + value + "\"");    
    }

    @Override
    public IType typecheck(Environment<IType> e) {
        return TypeString.STRING_TYPE;
    }
    
}
