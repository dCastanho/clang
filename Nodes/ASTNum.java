package Nodes;

import Types.IType;
import Types.TypeInt;
import Util.CodeBlock;
import Util.Coordinates;
import Util.Environment;
import Util.JVMCodes;
import Values.IValue;
import Values.IntValue;

/**
 * Class that represents the ASTNode
 * that represents a number
 * @author Daniel Castanho, 55078
 * @author Eric Solcan, 55562
 */
public class ASTNum implements ASTNode {

        private int val;

        public IValue eval(Environment<IValue> e) {
                return new IntValue(val);
        }

        public ASTNum(int n) {
                val = n;
        }

        public void compile(CodeBlock c, Environment<Coordinates> e) {
                c.emit(String.format( JVMCodes.SIPUSH , val));
         }

        @Override
        public IType typecheck(Environment<IType> e) {
                return TypeInt.INT_TYPE;
        }
}
