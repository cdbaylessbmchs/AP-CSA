/*
 * Language Interpreter Prototype 3
 */

/* Imports */
import java.lang.*;
import java.util.*;

public class protov3 {

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
		//System.out.println("Current in stack: " + s);
		if (s == null) break;
		switch (s) {
		    /* All the math can fall through */
		case "+":
		case "-":
		case "*":
		case "/":
		case "%":
		    i -= 2;
		    /* Check types */
		if (!(search(stack[i]).equals("Notvar") || (search(stack[i]).equals("Str")))) {
		    stack[i] = search(stack[i]);
		} else if (!(search(stack[i + 1]).equals("Notvar") || (search(stack[i + 1]).equals("Str")))) {
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
		case "concat":
		    i -= 2; //This is going negative for some reason, idk why
		    /* 
		       If there are no errors, replace; otherwise, proceed as 
		       normal
		    */
		    //System.out.println("Concat called");
		    if (!(search(stack[i]).equals("Notvar"))) {
			stack[i] = search(stack[i]);
		    } else if (!(search(stack[i + 1]).equals("Notvar"))) {
			stack[i + 1] = search(stack[i + 1]);	    
		    }
		    //System.out.println("Old i: " + stack[i]);
		    //System.out.println("Old i + 1: " + stack[i + 1]);
		    stack[i] = stack[i] + stack[i + 1];
		    //System.out.println("New: " + stack[i]);
		    break;
		case "split":
		    i -= 2;
		    if (stack[i + 1].equals("s")) {
			stack[i + 1] = " ";
		    }
		    String[] splittmp = stack[i].split(stack[i+1]);
		    stack[i] = "";
		    int x = 0;
		    for (String t : splittmp) {
			/* Keep the extraneous comma from appearing */
			//System.out.println(t);
			if (x < splittmp.length - 1) {
			    stack[i] += t + ",";
			} else {
			    stack[i] += t;
			}
			x++;
		    }
		    //stack[i] = 
		    break;
		case "replace":
		    i -= 3;
		    String ret = stack[i].replace(stack[i + 1], stack[i + 2]);
		    stack[i] = ret;
		    break;
		default:
		    /* Append to stack */
		    stack[i] = s;
		    //System.out.println("Val added to stack: " + s);
		    
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
		    if (v.value().contains("'")) {
			return "Str";
		    } else {
			return v.value();
		    }
		} else if (varname.contains(v.name())) {
		    String[] tmp = varname.split("\\[");
		    String tmp2 = access(v.value(), Integer.parseInt(tmp[1]));
		    return tmp2;
		}
	    }
	    return "Notvar";
	}

    private static String access(String val, int index) {
	/* Access part of array */
	String[] arr = val.split(",");
	return arr[index];
    }

    private static String[] strcheck(String[] input) {
	String[] ret = new String[input.length];
	int i = 0;
	int strs = 0;
	String tmp = "";
	//TODO: strings w/o spaces break this
	//The good news is, strings beginning with spaces don't!
	for (String str : input) {
	    if (str.contains("'") && (strs % 2 == 0)) {
		strs++;
		tmp = str;
		//System.out.println("Tmp: " + tmp);
		if (str.endsWith("'")) {
		    //System.out.println("Made to endswith");
		    strs++;
		    ret[i] = str;
		    i++;
		}
	    } else if (str.contains("'") && (strs % 2 != 0)) {
		strs++;
		tmp += " " + str;
		ret[i] = tmp;
		//System.out.println("Ret[i]: " + ret[i]);
		i++;
	    } else if (strs % 2 != 0) {
		tmp += " " + str;
		//System.out.println("Tmp2: " + tmp);
	    } else {
		ret[i] = str;
		i++;
	    }
		

	}
	return ret;

    }

    
	public static void main(String[] args) {
		/* 
		   String time! Prolly gonna send it through a string analysis
		   and have strings only take up one slot in the array
		 */
		/* Indicate status */
		System.out.print(">> ");
		/* Read */
		/* The scanner auto-delimits on spaces, not newlines */
		Scanner scan = new Scanner(System.in).useDelimiter("\n");
		String input = scan.next();
		//check for strings (tmp)
		//String[] cmds = strcheck(input);
		
		/* Split on spaces */
		//tmp: split on quote
		// String[] precmds = input.split("'");
		// for (String x : precmds) {
		//     System.out.println("cmd: " + x);
		// }
		String[] cmds = input.split(" ");
		// for (String s: cmds) {
		//  //String[] revcmds = strcheck(cmds);
		//  //for (String s : revcmds) {
		//     System.out.println("command: " + s);

		// }
		String[] revcmds = strcheck(cmds);
		// for (String x : revcmds) {
		//     System.out.println("Revcommand: " + x);
		// }
		/* Eval */
		System.out.println(eval(revcmds));
		//System.out.println(eval(revcmds));
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
