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
 * Class representing an if node, with a condition and if and else branches.
 * Ifs with no else, return false (similarly, to how the whiles work).
 * @author Daniel Castanho, 55078
 * @author Eric Solcan, 55562
 */
public class ASTIf implements ASTNode{

    /**
     *
     */
    private static final String DIFFERENT_RETURNS = "Different return types on if branches";

    private static final String INVALID_CONDITION = "Condition in if is not a boolean value";

    private ASTNode condition;
    private ASTNode ifBranch;
    private ASTNode elseBranch;

    private boolean hasElse;

    public ASTIf(ASTNode c, ASTNode iB, ASTNode eB) {
        condition = c;
        ifBranch = iB;
        elseBranch = eB;
        hasElse = elseBranch != null;
    }

    @Override
    public IValue eval(Environment<IValue> e) {
        IValue ifVal = condition.eval(e);
        if(ifVal instanceof BoolValue){
            boolean value = ((BoolValue)ifVal).getValue();
            if(value)
                return ifBranch.eval(e);
            else if( hasElse )
                return elseBranch.eval(e);
        }

        throw new InterpreterError(INVALID_CONDITION);
    }


    @Override
    public void compile(CodeBlock c, Environment<Coordinates> e) {
        ASTShortCircuitNode cSC = (ASTShortCircuitNode) condition;
        String trueLabel = Generator.genLabelName();
        String exitLabel =  Generator.genLabelName();
        String falseLabel =  Generator.genLabelName();

        if(hasElse){       

            cSC.compileSC(c, e, trueLabel, falseLabel);
            c.emit(trueLabel + ":");
            ifBranch.compile(c, e);
            c.emit(String.format(JVMCodes.GOTO, exitLabel));
            c.emit(falseLabel + ":");
            elseBranch.compile(c, e);
            c.emit(exitLabel + ":");

        } else {

            cSC.compileSC(c, e, trueLabel, falseLabel);
            c.emit(trueLabel + ":");
            ifBranch.compile(c, e);
            c.emit( JVMCodes.POP );
            c.emit( JVMCodes.PUSH_FALSE );
            c.emit(String.format(JVMCodes.GOTO, exitLabel));
            c.emit(falseLabel + ":");
            c.emit( JVMCodes.PUSH_FALSE );
            c.emit(exitLabel + ":");

        }
    }

    @Override
    public IType typecheck(Environment<IType> e) {
        IType condition_type = condition.typecheck(e);
        if(condition_type instanceof TypeBool){
            IType if_type = ifBranch.typecheck(e);

            if( !hasElse )
                return TypeBool.BOOL_TYPE;

            IType else_type = elseBranch.typecheck(e);
            if( if_type.sameType(else_type) )
                return else_type;

            throw new TypeCheckerError(DIFFERENT_RETURNS);
        }

        throw new TypeCheckerError( INVALID_CONDITION );
    }
    
}
