/*
  Language Interpreter Prototype 4: The I/O Edition
*/

/* Imports */
import java.lang.*;
import java.util.*;
/* Had to add all of these to get I/O working */
import java.nio.file.*;
import java.io.*;

public class protov4 {

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
		if (!(search(stack[i]).equals("Notvar"))) {
		    stack[i] = search(stack[i]);
		} else if (!(search(stack[i + 1]).equals("Notvar"))) {
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
		    i -= 2; 
		    /* 
		       If there are no errors, replace; otherwise, proceed as 
		       normal
		    */
		    if (!(search(stack[i]).equals("Notvar"))) {
			stack[i] = search(stack[i]);
		    } else if (!(search(stack[i + 1]).equals("Notvar"))) {
			stack[i + 1] = search(stack[i + 1]);	    
		    }
		    stack[i] = stack[i] + stack[i + 1];
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
			if (x < splittmp.length - 1) {
			    stack[i] += t + ",";
			} else {
			    stack[i] += t;
			}
			x++;
		    }
		    break;
		case "replace":
		    i -= 3;
		    String ret = stack[i].replace(stack[i + 1], stack[i + 2]);
		    stack[i] = ret;
		    break;
		case "print":
		    /* 
		       Kind of redundant in the REPL, but useful once it's 
		       in a script file
		    */
		    i -= 1;
		    if (!(search(stack[i]).equals("Notvar"))) {
			System.out.print(search(stack[i]));
		    } else {
			System.out.print(stack[i]);
		    }
		    break;
		case "read":
		    i -= 2;
		    mvariable file = new mvariable(stack[i], slurp(stack[i + 1]));
		    vars.add(file);
		    //Temporary, for troubleshooting 
		    //stack[i] = file.name();
		    /* 
		       stack[i] is already the variable name, so we don't
		       need to do anything with it
		    */
		    break;
		case "write":
		    i -= 2;
		    /* Nab variable contents */
		    if (!(search(stack[i]).equals("Notvar"))) {
			stack[i] = search(stack[i]);
		    } else if (!(search(stack[i + 1]).equals("Notvar"))) {
			stack[i + 1] = search(stack[i + 1]);	    
		    }
		    writeout(stack[i + 1], stack[i]);
		    break;
		case "sys":
		    i -= 2;
		    mvariable result = new mvariable(stack[i], syscall(stack[i + 1]));
		    vars.add(result);
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
		    /* 
		       I think this technically copies it, instead of
		       returning a pointer...
		    */
		    // This doesn't really need to be here, floats can just
		    // die with an error
		    // if (v.value().contains("'")) {
		    // 	return "Str";
		    // } else {
		    // 	return v.value();
		    // }
		    return v.value();
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

    //This needs to be refined and allow escaped spaces n stuff
    //could do like a if startswith("'") { iterate until endswith("'") }
    private static String[] strcheck(String[] input) {
	String[] ret = new String[input.length];
	int i = 0;
	int strs = 0;
	String tmp = "";
	for (String str : input) {

	    /* Newline finagling */
	    str = str.replace("\\n", "\n");
	    //if (str.contains("\\n")) {
		//System.out.println("Called");
		
		//}
	    
	    if (str.startsWith("'") && (strs % 2 == 0)) {
		strs++;
		/* Remove first quote */
		tmp = str.replaceFirst("'", "");
		if (str.replaceFirst("'", "").endsWith("'")) {	    
		    strs++;
		    /* Chop off last quote */
		    ret[i] = str.split("'")[1];
		    i++;
		}
	    } else if (str.contains("'") && (strs % 2 != 0)) {
		strs++;
		tmp += " " + str.split("'")[0];
		ret[i] = tmp;
		i++;
	    } else if (strs % 2 != 0) {
		tmp += " " + str;

	    //} else if (str.contains("n")) {
		//System.out.println("Called");
		//	str.replace("\\n", "\n");
	    } else {
		ret[i] = str;
		i++;
	    }
	}
	return ret;

    }

    private static String slurp(String file) {
	String ret = "";
	System.out.println("File name: " + file);
	if (file.equals("stdin")) {
	    /* Needs to be pipe-compatible */
	    file = "System.in";
	}
	/* This is stupid, but apparently idiomatic */
	/* I guess there's a reason why Java isn't popular for text mangling */
	/* This defaults to UTF-8 */
	Path path = Paths.get(file);
	/* This is stupid */
	try {
	    BufferedReader s = Files.newBufferedReader(path);
	    String line;
	    while ((line = s.readLine()) != null) {;
		ret += line+"\n";
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
	
	
	//could also use s.readAllLines(path)
	//     System.out.println("Ret: " + ret);
	//     System.out.println("Next line: " + s.nextLine());
	//     ret += s.nextLine();
	// }
	return ret;
    }

    private static void writeout(String file, String content) {
	/* Second verse, same as the first */
	Path path = Paths.get(file);
	int len = content.length();
	/* The options have to be done this way... */
	OpenOption[] opts = {StandardOpenOption.WRITE, StandardOpenOption.APPEND, StandardOpenOption.CREATE};
	/* Supposedly I could just do this with path.write(), but w/e */
	try {
	    /* Also defaults to UTF-8 */
	    BufferedWriter w = Files.newBufferedWriter(path, opts);
	    w.write(content, 0, len);
	    w.flush();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private static String syscall(String call) {
	//Process proc = new Process();
	String ret = "";
	try {
	    Process proc = Runtime.getRuntime().exec(call);
	    /* Nab stdout */
	    InputStream stdout = proc.getInputStream();
	    BufferedReader read = new BufferedReader(new InputStreamReader(stdout));
	    String line;
	    while ((line = read.readLine()) != null ) {
		ret += line + "\n";
	    }
	    read.close();
	    //Runtime.proc.exec(call);
	} catch (IOException e) {
	    e.printStackTrace();
	    ret = "An error occurred";
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
		/* Split on spaces */
		String[] cmds = input.split(" ");
		String[] revcmds = strcheck(cmds);
		/* Eval */
		System.out.println(eval(revcmds));
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

