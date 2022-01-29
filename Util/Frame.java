package Util;

import Structures.Entry;
import Structures.InsertionMap;
import Types.IType;

/**
 * Class that represents JVM Frame, useful for factoring code.
 * @author Daniel Castanho, 55078
 * @author Eric Solcan, 55562
 */
public class Frame {

    protected static final String FRAME_DEFINITION = ".class public %s\n" +
            ".super java/lang/Object\n" +
            "%s" +
            ".method public <init>()V\n" +
            "aload_0\n" +
            "invokenonvirtual java/lang/Object/<init>()V\n" +
            "return\n" +
            ".end method";

    protected static final String CLASS_NAME_FORMAT = "L%s;";
    public static final String STATIC_LINK_NAME = "sl";

    protected InsertionMap<String, IType> types;
    protected String name;
    protected String static_link;
    protected String father_type;

    public Frame(InsertionMap<String, IType> types, String name, String father_name) {
        this.types = types;
        this.name = name;
        father_type = String.format(CLASS_NAME_FORMAT, father_name);
        static_link = String.format(JVMCodes.CREATE_FIELD, STATIC_LINK_NAME, father_type);
    }

    /**
     * Gets the name of the frame
     * 
     * @return the name of the frame
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the class name of the frame
     * 
     * @return class the name
     */
    public String getClassName() {
        return String.format(CLASS_NAME_FORMAT, getName());
    }

    public String getClassDefinition() {
        String fields = static_link + "\n"; // Could be more efficent with String builder
        int i = 0;
        for (Entry<String, IType> entry : types) {
            String field_name = "v" + i;
            String field_type = entry.getValue().getJVMType();
            fields += String.format(JVMCodes.CREATE_FIELD + "\n", field_name, field_type);
            i++;
        }
        return String.format(FRAME_DEFINITION, getName(), fields);
    }

    public void init(CodeBlock c) {
        c.emit(String.format(JVMCodes.NEW, getName()));
        c.emit(JVMCodes.DUP);
        c.emit(String.format(JVMCodes.CONSTRUCTOR_CALL, getName()));
        c.emit(JVMCodes.DUP);
        c.emit(JVMCodes.LOAD_FRAME);
        c.emit(String.format(JVMCodes.PUTVALUE, getName(), STATIC_LINK_NAME, father_type));
        c.emit(JVMCodes.STORE_FRAME);
    }

    public String initString(){
        String initialization = 
            String.format(JVMCodes.NEW, getName()) + "\n" + 
            JVMCodes.DUP + "\n" + 
            String.format( JVMCodes.CONSTRUCTOR_CALL , getName()) + "\n" + 
            JVMCodes.DUP + "\n" + 
            JVMCodes.LOAD_FRAME + "\n" + 
            JVMCodes.PUTVALUE + "\n" + 
            JVMCodes.STORE_FRAME;
        return initialization;
    }

    public void insertField(CodeBlock c, String slot, IType type) {

        c.emit(String.format(JVMCodes.PUTVALUE, getName(), slot, type.getJVMType()));

    }

    public void popFrame(CodeBlock c) {
        c.emit(JVMCodes.LOAD_FRAME);
        c.emit(String.format(JVMCodes.GETFIELD, getName(), "sl", father_type));
        c.emit(JVMCodes.STORE_FRAME);
    }

}
