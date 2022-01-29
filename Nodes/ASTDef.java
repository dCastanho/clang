package Nodes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import Structures.Entry;
import Structures.InsertionMap;
import Types.IType;
import Util.CodeBlock;
import Util.Coordinates;
import Util.Environment;
import Util.Frame;
import Util.Generator;
import Util.JVMCodes;
import Values.IValue;
import Exceptions.TypeCheckerError;

/**
 * Class representing the ASTNode of definitions
 * Holds any number of variable definitions, and a body
 * @author Daniel Castanho, 55078
 * @author Eric Solcan, 55562
 */
public class ASTDef implements ASTNode{

    /**
     *
     */
    private static final String TYPE_DIF_DECLARED = "Actual type different from declared type";
    private InsertionMap<String, ASTNode> vars;
    private InsertionMap<String, IType> types;
    private ASTNode body;

    public ASTDef(InsertionMap<String, ASTNode> vars, ASTNode body, InsertionMap<String, IType> types) {
        this.vars = vars;
        this.body = body;
        this.types = types;
    }

    @Override
    public IValue eval(Environment<IValue> e) {
        //def x = 1 y = x in 
        e = e.beginScope();   
        for (Entry<String, ASTNode> n : vars) {

            IValue i = n.getValue().eval(e);
            e.assoc(n.getKey(), i);
        }
        
        IValue val = body.eval(e);
        e.endScope();
        return val;
        
    }

    @Override
    public void compile(CodeBlock c,  Environment<Coordinates> e) {
        int depth = e.getDepth();
        c.addFrame();
        String frameName = Generator.genFrameName();
        Frame frame = new Frame(types, frameName, e.getName());
        
        //Frame class definition
        String frame_def = frame.getClassDefinition();

        //Write frame file
        try (PrintStream p =  new PrintStream(new File(frameName + ".j"))){
            p.print(frame_def);
        } catch (FileNotFoundException e1) { }

        //initialize frame in code block
        frame.init(c);

        //Insert values into frame
        e = e.beginScope(frameName);
        int slot = 0;
        for(Entry<String, ASTNode> n : vars){
            Coordinates coord = new Coordinates(depth, "v"+slot);
            String id = n.getKey();
            IType type = types.get(id);
            slot++;
            e.assoc(n.getKey(), coord);
            c.emit( JVMCodes.LOAD_FRAME );
            n.getValue().compile(c, e);
            frame.insertField(c, coord.getSlot(), type);
        }

        //Compile the body
        body.compile(c, e);        
        
        //End frame/scope
        e = e.endScope();
        frame.popFrame(c);
        
    }

    @Override
    public IType typecheck(Environment<IType> e) {
       // System.out.println(vars);
        e = e.beginScope();   

        //Compare each value to the declared type and store to use in compilation
        for (Entry<String, ASTNode> n : vars) {
            String id = n.getKey();
            IType declared = types.get(id);
            e.assoc(id, declared);
            IType type = n.getValue().typecheck(e);
            if( !declared.sameType(type))
                throw new TypeCheckerError(TYPE_DIF_DECLARED);
        }
         
        IType type = body.typecheck(e);
        e.endScope();
        return type;
    }

    
    
}
