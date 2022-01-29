package Util;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import Types.IType;

/**
 * Class that represents a sequence of instructions
 * @author Daniel Castanho, 55078
 * @author Eric Solcan, 55562
 */
public class CodeBlock {

    private static final String START = 
        ".class public %s\n"+
        ".super java/lang/Object\n"+
        ".method public <init>()V\n"+
        "aload_0\n"+
        "invokenonvirtual java/lang/Object/<init>()V\n"+
        "return\n"+
        ".end method\n"+
        "\n"+
        ".method public static main([Ljava/lang/String;)V\n"+
        ".limit locals 10\n" +
        ".limit stack 256\n" + 
        "aconst_null\n"+
        JVMCodes.STORE_FRAME +" \n";


    private static final String END = 
        "return\n"+
        ".end method";


    private final static int SIZE = 800;

    private String[] ops;
    private int counter;
    private int frames;
    private int closures;
    private List<IType> classedTypes;

    public CodeBlock() {
        ops = new String[SIZE];
        counter = 0;
        frames = 0;
        closures = 0;
        classedTypes = new LinkedList<>();
    }

    public int getFrames() {
        return frames;
    }

    public void addFrame() {
        addFrames(1);
    }

    public void addFrames(int i){
        frames += i;
    }

    

    public void addClosures(int i){
        closures += i;
    }

    public int getClosures() {
        return closures;
    }

    public void addClosure() {
        addClosures(1);
    }

    public void emit(String operation) {
        ops[counter] = operation;
        counter++;
    }

    public void dump(PrintStream pStream, String filename){
        pStream.print( String.format(START, filename) );
        for(int i = 0; i < counter; i++)
            pStream.println(this.ops[i]);
        pStream.print(END);
    }

    public boolean classedTypeExists(IType t){
        return classedTypes.contains(t);
    }

    public void addClassedType(IType t){
        classedTypes.add(t);
    }

    public List<IType> classedTypes(){
        return classedTypes;
    }

    public void addClassedTypes(List<IType> l){
        classedTypes.addAll(l);
    }


    public String get() {
        String ret = "";
        for(int i = 0; i < counter - 1; i++)
            ret += this.ops[i] + "\n";
        ret += ops[counter - 1];
        return ret;
    }

}
