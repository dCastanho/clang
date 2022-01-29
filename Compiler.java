import Parser.*;
import Types.IType;
import Util.CodeBlock;
import Util.Coordinates;
import Util.Environment;
import Util.Generator;
import Exceptions.InterpreterError;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import Nodes.*;

public class Compiler {

  /**
   *
   */
  private static final String EXTENSION = ".icll";
  private static final String JAR_TAG = "-jar";
  private static final String DELETE_TAG = "-d";
  private static final String VERBOSE_TAG = "-v";

  public static void main(String args[]) throws FileNotFoundException, InterruptedException {
    if (args.length < 1) {
      System.out.println("use: Compiler <source>");
      return;
    }


    boolean tags[] = tagInit(args);
    boolean jar = tags[0];
    boolean deleteIntermediate = tags[1];
    boolean verbose = tags[2];
    

    try {
      new Parser(new FileReader(args[0]));
      String mainclass = args[0].replace(EXTENSION, "");
      String[] path = mainclass.split("/");
      mainclass = path[path.length - 1];
      ASTNode ast = Parser.Start();
      ast.typecheck(new Environment<>());
      CodeBlock codeBlock = new CodeBlock();
      ast.compile(codeBlock, new Environment<Coordinates>());
      PrintStream ps = new PrintStream(mainclass + ".j");
      codeBlock.dump(ps, mainclass);

      int frames = codeBlock.getFrames();
      int closures = codeBlock.getClosures();
      List<IType> classedTypes = codeBlock.classedTypes();
      if(verbose){
        System.out.println("Frames: " + frames);
        System.out.println("Closures: " + closures);
      }

      String[] jasminargs = new String[frames + closures + 4 + classedTypes.size()];
      jasminargs[0] = "java";
      jasminargs[1] = "-jar";
      jasminargs[2] = "jasmin.jar";
      jasminargs[3] = mainclass+".j";
      for(int i = 0 ; i < frames; i++)
        jasminargs[i+4] = String.format( Generator.FRAME_NAME_FORMAT + ".j", i);

        for(int i = 0 ; i < closures; i++)
        jasminargs[i+4+frames] = String.format( Generator.CLOSURE_NAME_FORMAT + ".j", i);

      int i = 0;
      for( IType type : classedTypes)
        jasminargs[closures+frames + 4 + i++] = type.getJVMName() + ".j";

      if(verbose){
        for (int j = 3; j < jasminargs.length; j++)
          System.out.println( "Created " + jasminargs[j]);
      }

      ProcessBuilder jasminProcess = new ProcessBuilder(jasminargs);
      Process p = jasminProcess.start();
      p.waitFor();

      if(verbose)
        System.out.println("Generated .classes");

      if(jar){
        String[] n = {"jar", "cf", mainclass+ ".jar", "*.class"} ;
        ProcessBuilder jarProcess = new ProcessBuilder( n );
        Process pJ = jarProcess.start();
        pJ.waitFor();

        if(verbose)
          System.out.println("Generated " + mainclass + ".jar ");
      }

      if(deleteIntermediate){

        deleteInstances(mainclass);

        for(int j = 0 ; j < frames; j++)
          deleteInstances( String.format( Generator.FRAME_NAME_FORMAT, j) );

        for(int j = 0 ; j < closures; j++)
          deleteInstances( String.format( Generator.CLOSURE_NAME_FORMAT, j)  );

        for( IType type : classedTypes)
          deleteInstances( type.getJVMName() );  

          if(verbose)
            System.out.println("Deleted created files");
       
      }

    } catch (ParseException e) {
      System.out.println(e.getMessage());
    } catch (InterpreterError e) {
      System.out.println(e.getMessage());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Passes over the arguments array, returning an array 
   * @param args
   */
  private static boolean[] tagInit(String[] args){
    boolean[] tags = { false, false, false };
    for( String s : args )
      switch(s){
        case JAR_TAG:
          tags[0] = true;
          break;
        case DELETE_TAG:
          tags[1] = true;
          break;
        case VERBOSE_TAG:
          tags[2] = true;
          break;
        
      }
      return tags;
  }

  private static void deleteInstances(String name){
    (new File(name +".class")).delete();
    (new File(name +".j")).delete();
  }

}
