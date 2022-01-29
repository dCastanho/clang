package Nodes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import Types.IType;
import Types.TypeRef;
import Util.CodeBlock;
import Util.Coordinates;
import Util.Environment;
import Util.JVMCodes;
import Values.CellValue;
import Values.IValue;

/**
 * Class that represents the ASTNode
 * with a reference in it.
 * @author Daniel Castanho, 55078
 * @author Eric Solcan, 55562
 */
public class ASTRef implements ASTNode {

    private ASTNode child;
    private TypeRef type;

    public ASTRef(ASTNode c) {
        child = c;
    }

    @Override
    public IValue eval(Environment<IValue> e) {
        IValue cVal = child.eval(e);
        return new CellValue(cVal);
    }

    @Override
    public void compile(CodeBlock c, Environment<Coordinates> e) {
        // TODO: Clean code

        // Create Reference JVM class
        String ref_name = type.getJVMName();
        String refed_type = type.getReference().getJVMType();

        if (!c.classedTypeExists(type)) {
            c.addClassedType(type);
            File f = new File("./" + ref_name + ".j");
            try (PrintStream pStream = new PrintStream(f)) {
                String class_reference = ".class %s\n" +
                        ".super java/lang/Object\n" +
                        ".field public v %s\n" +
                        ".method public <init>()V\n" +
                        "aload_0\n" +
                        "invokenonvirtual java/lang/Object/<init>()V\n" +
                        "return\n" +
                        ".end method";
                class_reference = String.format(class_reference, ref_name, refed_type);
                pStream.print(class_reference);

            } catch (FileNotFoundException e1) {
            }
        }

        // Instantiate it in the stack
        c.emit(String.format(JVMCodes.NEW, ref_name));
        c.emit(JVMCodes.DUP);
        c.emit(String.format("invokespecial %s/<init>()V ", ref_name));
        c.emit(JVMCodes.DUP);
        child.compile(c, e);
        c.emit(String.format(JVMCodes.PUTVALUE, ref_name, TypeRef.FIELD_NAME, refed_type));
    }

    @Override
    public IType typecheck(Environment<IType> e) {
        IType t = child.typecheck(e);
        type = new TypeRef(t);
        return type;
    }
}
