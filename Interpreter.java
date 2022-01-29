import Parser.*;
import Nodes.*;
import Util.Environment;
import Exceptions.InterpreterError;

public class Interpreter {
    
    public static void main(String args[]) {

        new Parser(System.in);
    
        while(true)
          try {
            System.out.print(">");
            ASTNode ast = Parser.Start();
            ast.eval(new Environment<>());
          } catch (ParseException e) {
            System.out.println(e.getMessage());
            Parser.ReInit(System.in);
          } catch (InterpreterError e){
            System.out.println(e.getMessage());
            Parser.ReInit(System.in);

          }
      }
}
