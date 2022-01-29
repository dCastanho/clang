package Util;
import Nodes.ASTNode;
import Structures.InsertionMap;
import Types.IType;
import Types.TypeFunction;

/**
 * Represents a closure, useful for factoring code
 * @author Daniel Castanho, 55078
 * @author Eric Solcan, 55562
 */
public class Closure extends Frame {

    private TypeFunction function;
    private ASTNode body;
    private String frameName;
    private Environment<Coordinates> env;

    public Closure(InsertionMap<String, IType> arguments, ASTNode body, String frameName, String name, String father_name, TypeFunction f, Environment<Coordinates> e) {
        super(arguments, name, father_name);
        function = f;
        this.body = body;
        this.frameName = frameName;
        env = e;
    }

    //".class public %s\n.super java/lang/Object\n%s.method public <init>()V\naload_0\ninvokenonvirtual java/lang/Object/<init>()V\nreturn\n.end method"

    public CodeBlock classDefinition() {
        CodeBlock c = new CodeBlock();

        c.emit( String.format( ".class public %s", getName() ) );
        c.emit( ".super java/lang/Object" );
        c.emit( String.format( JVMCodes.IMPLEMENTS, function.getJVMName() ) );
        c.emit( static_link );
        c.emit( ".method public <init>()V" );
        c.emit( "aload_0" );
        c.emit( "invokenonvirtual java/lang/Object/<init>()V" );
        c.emit( "return" );
        c.emit( ".end method" );
        apply(c);
        return c;
    }
    

    public void apply(CodeBlock c){
        //[args, body, env]

        
        int nVars = Math.max(4, function.getArgs().size() + 3);

        c.emit( String.format( JVMCodes.PUBLIC_METHOD_DECL, function.getApplySignature() ) );
        c.emit( String.format( JVMCodes.LOCAL_VARIABLE_LIMIT, nVars ) );
        c.emit( JVMCodes.LOCAL_STACK_LIMIT );
        c.emit( String.format(JVMCodes.NEW, frameName) );
        c.emit( JVMCodes.DUP );
        c.emit( String.format(JVMCodes.CONSTRUCTOR_CALL, frameName) );
        c.emit( JVMCodes.DUP );
        c.emit( JVMCodes.LOAD_SELF );
        c.emit( String.format( JVMCodes.GETFIELD, name, STATIC_LINK_NAME, father_type) );
        c.emit( String.format( JVMCodes.PUTVALUE, frameName, STATIC_LINK_NAME, father_type) );    

        //local variable assignments
        int i = 1;
        for(IType type : function.getArgs()){
            c.emit( JVMCodes.DUP);
            c.emit( String.format( type.loadCode(), i ) );
            c.emit( String.format(JVMCodes.PUTVALUE, frameName, "v" + (i - 1), type.getJVMType()));
            i++;
        }
        c.emit(JVMCodes.STORE_FRAME);
        body.compile(c, env);
        c.emit( function.getReturnType().returnCode() );
        c.emit( JVMCodes.END_METHOD );
    }

}
