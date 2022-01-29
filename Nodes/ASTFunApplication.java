package Nodes;

import java.util.Iterator;
import java.util.List;

import Structures.Entry;
import Structures.InsertionMap;
import Types.IType;
import Types.TypeFunction;
import Util.CodeBlock;
import Util.Coordinates;
import Util.Environment;
import Util.JVMCodes;
import Values.ClosureValue;
import Values.IValue;
import Exceptions.InterpreterError;
import Exceptions.TypeCheckerError;

/**
 * Class that represents the application of a function,
 * essentially, a function call
 * @author Daniel Castanho, 55078
 * @author Eric Solcan, 55562
 */
public class ASTFunApplication implements ASTShortCircuitNode{
    


    private static final String INVALID_TYPE_ARGUMENT = "Invalid type argument expected %s, got %s";
    private static final String NON_FUNCTION = "Cannot apply non-function values";
    private static final String INCORRECT_NUMBER_OF_ARGUMENTS = "Incorrect number of arguments";


    private ASTNode function;
    private List<ASTNode> arguments;
    private TypeFunction type;

    public ASTFunApplication(ASTNode function, List<ASTNode> arguments) {
        //System.out.println(arguments);
        this.function = function;
        this.arguments = arguments;
    }

    @Override
    public IValue eval(Environment<IValue> e) {
        IValue fun_val = function.eval(e);
        if( fun_val instanceof ClosureValue) {
            ClosureValue closure = (ClosureValue) fun_val;
            InsertionMap<String, IType> arg_types = closure.getArguments();

            //Check if the number of arguments is correct
            if(arg_types.size() != arguments.size())
                throw new InterpreterError(INCORRECT_NUMBER_OF_ARGUMENTS);

            Environment<IValue> env = closure.getEnv().beginScope();

            Iterator<Entry<String, IType>> arg_type_it = arg_types.iterator();
            Iterator<ASTNode> arguments_it = arguments.iterator();
            
            while(arguments_it.hasNext()){
                
                Entry<String, IType> cArg = arg_type_it.next();
                ASTNode cNode = arguments_it.next();
                env.assoc(cArg.getKey(), cNode.eval(e));
            }

            IValue res = closure.getBody().eval(env);
            env.endScope();
            return res;
        }

        throw new InterpreterError( NON_FUNCTION );
    }
    @Override
    public void compile(CodeBlock c, Environment<Coordinates> e) {
        function.compile(c, e);
        c.emit( String.format( JVMCodes.CHECKCAST, type.getJVMName()));

        for( ASTNode arg : this.arguments){
            arg.compile(c, e);
        }
        c.emit( String.format( JVMCodes.INVOKEINTERFACE, type.getJVMName(), type.getApplySignature(), type.size()));           
        
    }
    @Override
    public IType typecheck(Environment<IType> e) {
        IType funType = this.function.typecheck(e);
        if(funType instanceof TypeFunction){
            TypeFunction functionType = (TypeFunction) funType;
            List<IType> supposedTypes = functionType.getArgs();
            
            if(supposedTypes.size() != arguments.size())
                throw new TypeCheckerError(INCORRECT_NUMBER_OF_ARGUMENTS);

            Iterator<IType> supposedTypesIterator = supposedTypes.iterator();
            Iterator<ASTNode> argsIt = arguments.iterator();

            while(argsIt.hasNext()){
                IType sup = supposedTypesIterator.next();
                IType actual = argsIt.next().typecheck(e);
                if( !actual.sameType(sup) )
                    throw new TypeCheckerError( String.format(INVALID_TYPE_ARGUMENT, actual.show(), sup.show()));
            }

            this.type = functionType;
            return functionType.getReturnType();
        }

        throw new TypeCheckerError(NON_FUNCTION);
    }

    @Override
    public void compileSC(CodeBlock c, Environment<Coordinates> e, String trueL, String falseL) {
        compile(c, e);
        c.emit( String.format( JVMCodes.IFEQ, falseL ) );
        c.emit( String.format( JVMCodes.GOTO, trueL) );
        
    }

    

}
