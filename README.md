# CLANG
## Authors
- Daniel João Nunes Castanho, 55078
- Eric Solcan, 55562

## Compiling and Running
The project contains two classes with main methods, the `Interpreter.java` and `Compiler.java`. Both of which can be executed on an IDE like normal projects or compiled with the `javac` command, followed by the name of the java class to compile. Keep in mind this will also create `.class` files for all dependencies.

To use the interpreter and compiler, simply run that respective program. The interpreter takes no arguments, and will expect you to insert the code to be interpreted on the command line. Parsing/execution errors won't stop the program, `Ctrl + C` is recommended to close it. 

Unlike the interpreter, the compiler requires at least one argument, the path of the file to compile. **Said file can either not have an extension or have the `.icll` extension.** There are three aditional options for the compiler that can be introduced in the command arguments. By default, the compiler will generate the necessary `.j` and `.class` files. To execute the compiled program, you can simply use the command `java <source file name>`. 

| Option   | Effect                                                                                                                                                                        |
| ------ | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `-v`   | Verbose: The compiler will print out some information about the resulting code                                                                                                |
| `-jar` | Jar: The compiler will create a `.jar` from the `.class` files. To run it, use the following command: `java -cp <name>.jar <name>` |
| `-d`   | Delete: Will delete all `.j` and `.class` files.                                                                                                                                                                              |

> **WARNINGS:**
>   - Use `/` for path names, only the actual file name will be used.
>  - Keep the `jasmin.jar` file in the same folder as the one you run the compiler in, as it is necessary to turn the `.j` files into `.class` files.
>  - Keep the `Lib.class` file in the same folder as your final compiled program, as some functionalities require this classes functions.

## Features 
Our language implements an interpreter and compiler for Level 3 of the handout. Since this level wasn't very talked about in class, we shall explain what most of our new features entail.

### Strings and Records
Level 3 introduces two new types to our language: Strings and Records. Strings act like java strings, except for the fact that the type is declared with a lower case s. Records are similar to their OCaml cousin, they represent a group of variables, which can be accessed given their identifier. The small example ahead shows a small use of both these types.

```
def
	a : string = "Strawberry"
	fruit : rec[ name : string ] = [ name = "Pineapple" ]
in 
	println fruit.name
end ;;
```

The type String is declared by the word `string` as mentioned above and values are represented by java String literals (sequences of characters between two `"`). Records are defined with `rec[ field name : field type]` as shown in the example.  Additional fields are separated by commas. Attempting to create a record with two or more fields with the same name will give typecheking error .  

### Overloading
With the introduction of strings, we added overloading of several operators. The addition operator, as well as all the relational operators. 

#### Addition
With overloaded addition, we can add variables of different types, but these must be `string`, `int` or `boolean` and only in the specified combinations (which are commutative). Other combinations will return a typecheking error:

| Type One | Type Two | Return Type |
| -------- | -------- | ----------- |
| String   | String   | String      |
| String   | Int      | String      |
| String   | Boolean  | String      |
| Int      | Int      | Int         |

#### Relational Operators
Our relational operators are able to compare not just ints, but also strings and booleans. To compare two values they have to be of the same type, yet we use the same operator (hence, overloading). Ints are compared as normal, while strings are compared with Java's `compareTo` for strings. We established for booleans that `true` is larger than `false`. 

### Comments
Comments are pretty self explanatory, but since they are not part of the original language we decided to add this little section. Comments won't be compiled or interpreted, in fact they are completely ignored by the parser and don't make it onto the AST. Comments are blocks of text that start and end with a `$`. 

