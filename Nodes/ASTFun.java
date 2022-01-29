package Nodes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import Structures.Entry;
import Structures.InsertionMap;
import Types.IType;
import Types.TypeFunction;
import Util.Closure;
import Util.CodeBlock;
import Util.Coordinates;
import Util.Environment;
import Util.Frame;
import Util.Generator;
import Util.JVMCodes;
import Values.ClosureValue;
import Values.IValue;

/**
 * Class that represents the definition of a function
 * @author Daniel Castanho, 55078
 * @author Eric Solcan, 55562
 */
public class ASTFun implements ASTNode {

    private InsertionMap<String, IType> arguments;
    private ASTNode body;
    private IType returnType;
    private TypeFunction thisType;

    public ASTFun( InsertionMap<String, IType> arguments, ASTNode body) {
        this.arguments = arguments;
        this.body = body;
    }

    @Override
    public IValue eval(Environment<IValue> e) {
        return new ClosureValue(arguments, body, e);
    }

    @Override
    public void compile(CodeBlock c, Environment<Coordinates> e) {

        if(!c.classedTypeExists(thisType)){
            generateInterface();
            c.addClassedType(thisType);
        }
    
        String closeName = Generator.genClosureName();
        String frameName = Generator.genFrameName();
        String fatherName = e.getName();
        String fatherClass = "L"+fatherName+";";
        int depth = e.getDepth();
        c.addClosure();
        c.addFrame();

        Frame frame = new Frame(arguments, frameName, e.getName());

        e = e.beginScope(frameName);

        int slot = 0;
        for(Entry<String, IType> n : arguments){
            Coordinates coord = new Coordinates(depth, "v"+slot);
            String id = n.getKey();
            slot++;
            e.assoc( id , coord);
        }      
        

        Closure closure = new Closure(arguments, body, frameName, closeName, e.endScope().getName(), thisType, e);

        //Frame definition
        String frame_def = frame.getClassDefinition();

        //Write frame file
        try (PrintStream p =  new PrintStream(new File(frameName + ".j"))){
            p.print(frame_def);
        } catch (FileNotFoundException e1) { }

        //Closure class definition
        CodeBlock closure_def = closure.classDefinition();

        c.addClosures( closure_def.getClosures() );
        c.addFrames( closure_def.getFrames() );
        c.addClassedTypes( closure_def.classedTypes() );

        //Write frame file
        try (PrintStream p =  new PrintStream(new File(closeName + ".j"))){
            p.print(closure_def.get());
        } catch (FileNotFoundException e1) { }

        c.emit( String.format(JVMCodes.NEW, closeName) );
        c.emit( JVMCodes.DUP );
        c.emit( String.format(JVMCodes.CONSTRUCTOR_CALL, closeName) );
        c.emit( JVMCodes.DUP );
        c.emit( JVMCodes.LOAD_FRAME );
        c.emit( String.format( JVMCodes.PUTVALUE, closeName, Frame.STATIC_LINK_NAME, fatherClass));

        e = e.endScope();
    }

    /**
     * Prints the JVM interface class into a file
     */
    private void generateInterface(){
        String def = thisType.getInterfaceDefinition();
        String name = thisType.getJVMName();

        try(PrintStream p = new PrintStream(new File(name + ".j"))){
            p.print(def);
        } catch (FileNotFoundException e){}
        
    }

    @Override
    public IType typecheck(Environment<IType> e) {
        e = e.beginScope();

        List<IType> types = new LinkedList<>();
        for(Entry<String, IType> argument : this.arguments){
            IType t = argument.getValue();
            e.assoc( argument.getKey() , t );
            types.add( t );
        }

        returnType = body.typecheck(e);

        thisType= new TypeFunction(types, returnType);
        return thisType;
    }
    
}
