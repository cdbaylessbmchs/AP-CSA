/*
 * Language Interpreter Prototype 2
 */

/* Imports */
import java.lang.*;
import java.util.*;

public class protov2 {

	/* Constants */
	/* 
	   Max size of list of operands - needs to be based on max number
	   a function needs to be passed
	 */
	public static final int MAX_SIZE = 50;
	/* Initialize variable list */
        public static List<mvariable> vars = new ArrayList<mvariable>();
    
	/* Objects and subroutines */
	private static String eval(String[] cmds) {
	    /* Evaluate statement */
	    /* I wish I could have multiple different types in an array... */
	    String[] stack = new String[MAX_SIZE];
	    /* Keep track of where we are */
	    int i = 0;
	    for (String s : cmds) {
		switch (s) {
		    /* All the math can fall through */
		case "+":
		case "-":
		case "*":
		case "/":
		case "%":
		    i -= 2;
		    /* Check types */
		if (!(search(stack[i]).equals("Error"))) {
		    stack[i] = search(stack[i]);
		} else if (!(search(stack[i + 1]).equals("Error"))) {
		    stack[i + 1] = search(stack[i + 1]);	    
		}
		    stack[i] = Float.toString(matheval(stack[i], stack[i + 1], s));		    
		    break;
		case "set":
		    i -= 2;
		    mvariable tmp = new mvariable(stack[i], stack[i + 1]);
		    stack[i] = tmp.name();
		    vars.add(tmp); 
		    break;
		default:
		    /* Append to stack */
		    stack[i] = s;
		}
		i++;
	    }
	    return stack[0];
	}

    private static Float matheval(String num1, String num2, String op) {
	/* I could do this shorter, but I'd like the code to be readable */
	Float num1_2 = Float.parseFloat(num1);
	Float num2_2 = Float.parseFloat(num2);
	switch (op) {
	case "+":
	    return (num1_2 + num2_2);
	case "-":
	    return (num1_2 - num2_2);
	case "*":
	    return (num1_2 * num2_2);
	case "/":
	    return (num1_2 / num2_2);
	case "%":
	    return (num1_2 % num2_2);
	}
	/* Bad juju here */
	return Float.parseFloat("-1");
    }
    
	private static String search(String varname) {
	    /* Search for variable */
	    /* Check if it's an array access */
	    if (varname.contains("[")) {
		String[] tmp0 = varname.split("]");
		varname = tmp0[0];
     	    }
	    //This is searching twice because of variable checking
	    for (mvariable v : vars) {
		if (v.name().equals(varname)) {
		    /* I think this technically copies it, instead of
		       returning a pointer...
		    */
		    return v.value();
		} else if (varname.contains(v.name())) {
		    String[] tmp = varname.split("\\[");
		    String tmp2 = access(v.value(), Integer.parseInt(tmp[1]));
		    return tmp2;
		}
	    }
	    return "Error";
	}

    private static String access(String val, int index) {
	/* Access part of array */
	String[] arr = val.split(",");
	return arr[index];
    }

    
	public static void main(String[] args) {
		/* Now that we're doing variables, the structure will need
		 * to be slightly different
		 * The skeleton will be the same as matheval(), but
		 * will make different function calls depending on the 
		 * operator
		 *
		 * For variables: will need an associative array
		 * with the variable's name as the key and what it represents
		 * as the value
		 *
		 * Since hashmaps are hard, I'll just have an array of 
		 * variable objects and a function to search through it
		 * by number until we hit a name
		 */
	
		/* Indicate status */
		System.out.print(">> ");
		/* Read */
		/* The scanner auto-delimits on spaces, not newlines */
		Scanner scan = new Scanner(System.in).useDelimiter("\n");
		String input = scan.next();
		/* Split on spaces */
		String[] cmds = input.split(" ");
		/* Eval */
		System.out.println(eval(cmds));
		/* Loop */
		//main();



	}

    private static class mvariable {
	
	/* Class to hold variables, because rolling my own associative array
	 * is a good use of my time
	 I *could* make this just have two lists, one with keys and one with 
	 values...
	 What's better: an object with two lists, or a list with a bunch of
	 objects?
	 */
	
	private String name;
	private String value;

	public mvariable(String initname, String initval) {
		name = initname;
		value = initval;
	}

	public String name() {
		return name;
	}

	public String value() {
		return value;
	}
    }
}


