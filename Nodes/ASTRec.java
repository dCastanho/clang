package Nodes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import Exceptions.InterpreterError;
import Structures.AccompaniedHashMap;
import Structures.Entry;
import Structures.InsertionMap;
import Types.IType;
import Types.TypeRecord;
import Util.CodeBlock;
import Util.Coordinates;
import Util.Environment;
import Util.JVMCodes;
import Values.IValue;
import Values.RecordValue;

/**
 * Class that represents a record
 * @author Daniel Castanho, 55078
 * @author Eric Solcan, 55562
 */
public class ASTRec implements ASTNode{

    /**
     *
     */
    private static final String REPEATED_FIELDS = "Repeated field names in record.";

    private InsertionMap<String, ASTNode> children;
    private TypeRecord type;
    private int nFields;

    public ASTRec(InsertionMap<String, ASTNode> c, int n) {
        children = c;
        nFields = n;
    }

    @Override
    public IValue eval(Environment<IValue> e) {
        if(nFields != children.size())
            throw new InterpreterError(REPEATED_FIELDS);

        InsertionMap<String, IValue> values = new AccompaniedHashMap<>(children.size());
        e = e.beginScope();
        for(Entry<String, ASTNode> entry : children){
            IValue val = entry.getValue().eval(e);
            String name = entry.getKey();
            values.put(name, val);
            e.assoc(name, val);
        }
        e.endScope();
        return new RecordValue(values);
    }

    @Override
    public void compile(CodeBlock c, Environment<Coordinates> e) {
        String recordName = type.getJVMName();
        int recVar = 5;

        if( !c.classedTypeExists(type)){
            c.addClassedType(type);

            CodeBlock defBlock = new CodeBlock();

            defBlock.emit( String.format( ".class public %s", recordName) );
            defBlock.emit( ".super java/lang/Object" );
           

            InsertionMap<String, IType> fields = type.getFields();

            //Setup fields
            for(Entry<String, IType> entry : fields){
                String fieldName = entry.getKey();
                IType currType = entry.getValue();
                String fieldSignature = String.format(JVMCodes.CREATE_FIELD, fieldName, currType.getJVMType());
                defBlock.emit( fieldSignature );
            }

            defBlock.emit( ".method public <init>()V" );
            defBlock.emit( "aload_0" );
            defBlock.emit( "invokenonvirtual java/lang/Object/<init>()V" );
            defBlock.emit( "return" );
            defBlock.emit( JVMCodes.END_METHOD );

            try (PrintStream p =  new PrintStream(new File(recordName + ".j"))){
                p.print(defBlock.get());
            } catch (FileNotFoundException e1) { System.out.println(e1); }

        }

        InsertionMap<String, IType> types = type.getFields();

        c.emit( String.format(JVMCodes.NEW, recordName) );
        c.emit( JVMCodes.DUP );
        c.emit( String.format( JVMCodes.CONSTRUCTOR_CALL, recordName) );
        c.emit( String.format( JVMCodes.STORE, recVar) );

        for( Entry<String, ASTNode> child : children){
            String fname = child.getKey();
            ASTNode node = child.getValue();
            IType t = types.get(fname);

            c.emit( String.format(JVMCodes.ALOAD, recVar) );
            node.compile(c, e);
            c.emit( String.format(JVMCodes.PUTVALUE, recordName, fname, t.getJVMType()) );
        }

        c.emit( String.format(JVMCodes.ALOAD, recVar) );
        
    }

    @Override
    public IType typecheck(Environment<IType> e) {
        if(nFields != children.size())
            throw new InterpreterError(REPEATED_FIELDS);

        InsertionMap<String, IType> types = new AccompaniedHashMap<>(children.size());

        for(Entry<String, ASTNode> entry : children){
            String name = entry.getKey();
            IType t = entry.getValue().typecheck(e);
            types.put(name, t);
        }
        type = new TypeRecord(types);
        return type;
    }
    
}
