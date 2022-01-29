package Util;

import java.util.HashMap;
import java.util.Map;

import Exceptions.NotFoundException;

/**
 * Environment for associating ids to <i>something</i>. 
 * Each environment objects in reality represents a scope,
 * where we link the scope to its ancestor
 * 
 * @param E the type of the values to associate to ids.
 * @author Daniel Castanho, 55078
 * @author Eric Solcan, 55562
 */
public class Environment<E> {
    
    private Environment<E> ancestor;
    private Map<String, E> val;
    private String scopeName;

    public Environment(){
        this(null, "java/lang/Object");
    }

    public Environment(Environment<E> ancestor, String name){
        this.ancestor = ancestor;
        scopeName = name;
        val = new HashMap<>();
    }

    public Environment<E> beginScope(){
        //if(ancestor == null && val.isEmpty())
        //    return this;

        Environment<E> e = new Environment<>(this, null);
        return e;
    }

    public Environment<E> beginScope(String name){
        //if(ancestor == null && val.isEmpty())
        //    return this;

        Environment<E> e = new Environment<>(this, name);
        return e;
    }

    public int getDepth() {
        if(ancestor == null)
            return 0;
        else 
            return 1 + ancestor.getDepth();
    }

    public Environment<E> endScope(){
        return ancestor;
    }

    public void assoc(String id, E value) {
        val.put(id, value);
    }

    public String getName() {
        return scopeName;
    }

    public E find(String id){
        E v = val.get(id);
        if(v == null)
            if(ancestor != null)
            v  = ancestor.find(id);
            else
                throw new NotFoundException(id);
        
        return v;
    }

    public String getNameUpstairs(int depth){
        Environment<E> curr = this;
        for(int i = 0; i < depth ; i++)
            curr = curr.ancestor;
        return curr.scopeName;
    }

    @Override
    public String toString() {
        return val.toString() + "\n" + ancestor == null ? ancestor.toString() : "";

    }

}
