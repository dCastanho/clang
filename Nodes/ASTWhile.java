package Nodes;

import Types.IType;
import Types.TypeBool;
import Util.CodeBlock;
import Util.Coordinates;
import Util.Environment;
import Util.Generator;
import Util.JVMCodes;
import Values.BoolValue;
import Values.IValue;
import Exceptions.InterpreterError;
import Exceptions.TypeCheckerError;

/**
 * Class that represents the While ASTNode,
 * with a condition and a body.
 * @author Daniel Castanho, 55078
 * @author Eric Solcan, 55562
 */
public class ASTWhile implements ASTNode{

    private static final String NOT_BOOL = "While condition is not a boolean value";

    private ASTNode condition;
    private ASTNode body;


    public ASTWhile(ASTNode c, ASTNode b) {
        condition = c;
        body = b;
    }

    @Override
    public IValue eval(Environment<IValue> e) {
        IValue v = condition.eval(e);
        if(v instanceof BoolValue){
            boolean cond = ((BoolValue)v).getValue();
            if(cond){
                body.eval(e);
                eval(e);
            } 
            return BoolValue.FALSE;
        }

        throw new InterpreterError(NOT_BOOL);
    }

    @Override
    public void compile(CodeBlock c, Environment<Coordinates> e) {
        String startLabel =  Generator.genLabelName();
        String trueLabel = Generator.genLabelName();
        String falseLabel = Generator.genLabelName(); 
        ASTShortCircuitNode condSC = (ASTShortCircuitNode) condition;

        c.emit(startLabel + ":");
        condSC.compileSC(c, e, trueLabel, falseLabel);
        c.emit(trueLabel + ":");
        body.compile(c, e);
        c.emit( JVMCodes.POP );
        c.emit( String.format(JVMCodes.GOTO, startLabel) );
        c.emit(falseLabel + ":");        
        c.emit( JVMCodes.PUSH_FALSE );
    }

    @Override
    public IType typecheck(Environment<IType> e) {
        IType condition_type = condition.typecheck(e);
        if(condition_type instanceof TypeBool){
            body.typecheck(e); //check for exception
            return TypeBool.BOOL_TYPE;            
        }
        
        throw new TypeCheckerError( NOT_BOOL );
    }
    
}
