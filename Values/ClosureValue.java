package Values;


import Nodes.ASTNode;
import Structures.InsertionMap;
import Types.IType;
import Util.Environment;

public class ClosureValue implements IValue {

    private InsertionMap<String, IType> arguments;
    private ASTNode body;
    private Environment<IValue> env;

    public ClosureValue(InsertionMap<String, IType> arguments, ASTNode body, Environment<IValue> env) {
        this.arguments = arguments;
        this.body = body;
        this.env = env;
    }

    public InsertionMap<String, IType> getArguments() {
        return arguments;
    }

    public ASTNode getBody() {
        return body;
    }

    public Environment<IValue> getEnv() {
        return env;
    }

    @Override
    public String show() {
        return 
            arguments.toString() + body.toString() + env.toString();
    }

    @Override
    public boolean sameType(IValue v) {
        return (v instanceof ClosureValue);
    }


}
