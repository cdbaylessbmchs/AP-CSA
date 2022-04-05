/*
  Language Interpreter Prototype 6: Function Edition
*/

/* Imports */
import java.lang.*;
import java.util.*;
/* Had to add all of these to get I/O working */
import java.nio.file.*;
import java.io.*;

public class protov6 {

	/* Constants */
	/* 
	   Max size of list of operands - needs to be based on max number
	   a function needs to be passed
	 */
	public static final int MAX_SIZE = 50;
	/* Initialize variable list */
        public static List<mvariable> vars = new ArrayList<mvariable>();
        /* Initialize function list */
        public static List<mfunc> funcs = new ArrayList<mfunc>();
    /* Dummy function for appeasing the JVM gods */
    public static String[] tmp = new String[1];
    public static mfunc err = new mfunc(tmp, 0, "Notfunc", tmp);
    
	/* Objects and subroutines */
	private static String eval(String[] cmds) {
	    /* Evaluate statement */
	    /* I wish I could have multiple different types in an array... */
	    String[] stack = new String[MAX_SIZE];
	    /* Keep track of where we are */
	    int i = 0;
	    // tmp - for ternary op development
	    int intern = 0;
	    List<String> terncmds = new ArrayList<String>();
	    for (String s : cmds) {
		//System.out.println("Current in stack: " + s);
		if (s == null) break;
		/* Handling for if-statements */
		/* 
		   Keep skipping over the command until the if-statement's done
		 */
		if (intern > 0 && !(s.equals(";"))) {
		    terncmds.add(s);
		    continue;
		}
		if (intern > 0 && s.equals(";")) {
		    terncmds.add(s);
		    stack[i] = ifeval(terncmds);
		    intern = 0;
		}
		
		/* Handle user-defined functions */
		if (!(funcsearch(s).name().equals("Notfunc"))) {
		    //System.out.println("Func called");
		    /* Nab function object */
		    mfunc tmp = funcsearch(s);
		    /* Create new array to pass to tmp.call() */
		    String[] params = new String[tmp.params()];
		    /* Move i back */
		    i -= tmp.params();
		    //x = i - tmp.params();
		    /* Add params to array */
		    for (int x = 0; x < tmp.params(); x++) {
			//System.out.println("Param: " + stack[i + x]);
			params[x] = stack[i + x];
		    }
		    /* Evaluate and add to stack */
		    //TODO: this isn't actually adding to stack...
		    stack[i] = eval(tmp.call(params));
		    // System.out.println("Eval'd func: " + stack[i]);
		    // System.out.println("i is: " + i );
		    // int o = 0;
		    //  for (String x : stack) {
		    //  	System.out.println("In stack: " + x + " at " + o);
		    //  	o++;
		    //  }
		     /* Forgetting this caused me so much trouble */
		     break;
		}
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
		    if (!(search(stack[i]).equals("Notvar"))) {
			updatevar(stack[i], stack[i + 1]);
		    } else {
			mvariable tmp = new mvariable(stack[i], stack[i + 1]);
			stack[i] = tmp.name();
			vars.add(tmp);
		    }
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
		    if (!(search(stack[i]).equals("Notvar"))) {
			stack[i] = search(stack[i]);
		    } else if (!(search(stack[i + 1]).equals("Notvar"))) {
			stack[i + 1] = search(stack[i + 1]);	    
		    }
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
		    /* Allow replacing inside vars */
		    if (!(search(stack[i]).equals("Notvar"))) {
			stack[i] = search(stack[i]);	    
		    }
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
		    //stack[i] = null;
		    break;
		case "read":
		    i -= 2;
		    mvariable file = new mvariable(stack[i], slurp(stack[i + 1]));
		    vars.add(file);
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
		case ">":
		    i -= 2;
		    stack[i] = numcompare(stack[i], stack[i + 1], "greater");
		    break;
		case "<":
		    i -= 2;
		    stack[i] = numcompare(stack[i], stack[i + 1], "lesser");
		    break;
		case ">=":
		    i -= 2;
		    stack[i] = numcompare(stack[i], stack[i + 1], "greatereq");
		    break;
		case "<=":
		    i -= 2;
		    stack[i] = numcompare(stack[i], stack[i + 1], "lessereq");
		    break;
		case "==":
		    i -= 2;
		    if (!(stack[i].equals(stack[i + 1]))) {
			stack[i] = "f";
		    } else {
			stack[i] = "t";
		    }
		    break;
		case "!=":
		    i -= 2;
		    if (stack[i].equals(stack[i + 1])) {
			stack[i] = "f";
		    } else {
			stack[i] = "t";
		    }
		    break;
	        /* 
		   This just sets the flag, a lot of actual handling
		   happens up top
		*/
		case "?":
		    /* Set flag */
		    intern = 1;
		    //System.out.println("Tern called: " + intern);
		    i -= 1;
		    /* Set t/f flag */
		    terncmds.add(stack[i]);
		    break;
		case "for":
		    i -= 2;
		    /* Eval each element of array separately */
		    if (!(search(stack[i]).equals("Notvar"))) {
			stack[i] = search(stack[i]);
		    }
		    /* Foreval doesn't need to return anything */
		    foreval(stack[i], stack[i + 1]);
		    i--;
		    break;
		case "defun":
		    i -= 3;
		    /* 
		       This also doesn't need to return, unless I add
		       lambdas...
		       In that case, it'd just be stack[i] = funcdef();
		    */
		    //stack[i] = funcdef(stack[i], stack[i + 1], stack[i + 2]).name();
		    mfunc placeholder = funcdef(stack[i], stack[i + 1], stack[i + 2]);
		    /* Get func name off of stack */
		    i--;
		    // System.out.println("Function defined");
		    // //System.out.println("Stack[i]: " + stack[i]);
		    // System.out.println("Func replaced: " + stack[0]);
		    // System.out.println("At funcdef, i is now: " + i);
		    break;
		case "quit":
		    System.exit(1);
		default:
		    /* Append to stack */
		    stack[i] = s;
		    //System.out.println("Val added to stack: " + s);
		    
		}
		i++;
	    }
	    //System.out.println("Stack[0] " + stack[0]);
	    // int z = 0;
	    // for (String g : stack) {
	    // 	System.out.println("At end: " + g + " at " + z);
	    // 	z++;
	    // }
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

    private static String[] strcheck(String[] input) {
	String[] ret = new String[input.length];
	int i = 0;
	int strs = 0;
	String tmp = "";
	for (String str : input) {
	    /* Newline finagling */
	    str = str.replace("\\n", "\n");
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
	} catch (IOException e) {
	    e.printStackTrace();
	    ret = "An error occurred";
	}
	return ret;

    }

    private static String numcompare(String num1, String num2, String method) {
	float first = Float.parseFloat(num1);
	float sec = Float.parseFloat(num2);
	switch (method) {
	case "greater":
	    if (first > sec) {
		return "t";
	    } else {
		return "f";
	    }
	case "lesser":
	    if (first < sec) {
		return "t";
	    } else {
		return "f";
	    }
	case "greatereq":
	    if (first >= sec) {
		return "t";
	    } else {
		return "f";
	    }
	case "lessereq":
	    if (first <= sec) {
		return "t";
	    } else {
		return "f";
	    }
	default:
	    return "error";
	}

    }

    private static void updatevar(String varname, String newval) {
	int arrflag = 0;
	/* Check if it's an array access */
	if (varname.contains("[")) {
	    //System.out.println("array access!");
	    String[] tmp0 = varname.split("]");
	    varname = tmp0[0];
	    //System.out.println("Varname is: " + varname);
	    arrflag = 1;
	}
	//System.out.println("Arrflag is: " + arrflag);
	for (mvariable v : vars) {
	    /* Check if we're updating an array */
	    //System.out.println("V: " + v.name() + " with flag " + arrflag);
	    if (varname.contains(v.name()) && (arrflag > 0)) {
		/* Yes, this is overloaded. Check the class */
		/* Unnecessary step, but I like the code to be readable */
		String arrindex = varname.split("\\[")[1];
		//System.out.println("Updatevar on array called");
	      	//System.out.println("New val:" + newval + " at " + arrindex);
		v.update(newval, Integer.parseInt(arrindex));
	    } else if (v.name().equals(varname)) {
		//System.out.println("Wrong update called");
		v.update(newval);
	    }
	}
    }

    private static String ifeval(List<String> statements) {
	List<String> tmplist = new ArrayList<String>();
	/* 
	   If you want to know why I didn't use an enum:
	   It's easier to do it this way and skip the t/f flag
	*/
	if (statements.get(0).equals("t")) {
	    for (int i = 1; i < statements.size(); i++) {
		if (statements.get(i).equals(":")) {
		    break;
		} else if (statements.get(i).equals("?")) {
		    /* Do nothing */
		} else {
		    tmplist.add(statements.get(i));
		}
	    }
	} else {
	    /* So we know if we're adding to the eval stack */
		int flag = 0;
		for (int i = 1; i < statements.size(); i++) {
		    if (flag == 1) {
			//System.out.println("Nabbed: " + statements.get(i));
			tmplist.add(statements.get(i));
		    }
		    /* Switch flag */
		    if (statements.get(i).equals(":")) {
			    flag = 1;
			    //System.out.println("Flag switched");
		    }
		}
	}
	String[] strippedlist = new String[tmplist.size()];
	/* Turn arraylist into regular array */
	for (int i = 0; i < tmplist.size(); i++) {
	    //System.out.println("Added to strippedlist: " + tmplist.get(i));
	    strippedlist[i] = tmplist.get(i);
	}
	//for (String cmd : tmplist) {
	String ret = eval(strippedlist);
	//	System.out.println("Ret: " + ret);
	return ret;
	
    }

    private static void foreval(String arr, String cmds) {
	String[] arraccess = arr.split(",");
	for (String str : arraccess) {
	    /* Yes, this is stupid and wastes memory, but it works */
	    String[] split = cmds.split(" ");
	    String[] revsplit = strcheck(split);
	    String[] finalarr = new String[revsplit.length + 1];
	    finalarr[0] = str;
	    for (int i = 0; i < revsplit.length; i++) {
		if (revsplit[i].equals("loc")) {		    
		    revsplit[i] = str;
		}
		finalarr[i + 1] = revsplit[i];
	    }
	    /* 
	       We don't need to send anything back because
	       the variable list is global and anything new
	       will remain in the runtime
	    */
	    System.out.println(eval(finalarr));
	}
    }

    private static mfunc funcdef(String body, String params, String funcname) {
	//int nparams = params.split(",").length;
	String[] paramarr = params.split(",");
	int nparams = paramarr.length;
	String[] newcmds = body.split(" ");
	String[] revnewcmds = strcheck(newcmds);
	mfunc newfunc = new mfunc(revnewcmds, nparams, funcname, paramarr);
	/* Add to list */
	funcs.add(newfunc);
	return newfunc;
    }

    private static mfunc funcsearch(String name) {
	/* Search for user-defined functions */
	for (mfunc func : funcs) {
	    if (func.name().equals(name)) {
		return func;
	    }
	}
	return err;
    }
    
    // private static mfunc funcnab(String name) {
    // 	/* Search functions again */
    // 	for (mfunc func : funcs) {
    // 	    if (func.name().equals(name)) {
    // 		return func;
    // 	    }
    // 	}
    // 	/* Global func */
    // 	return err;
    // }
    
	public static void main(String[] args) {
		/* Indicate status */
		System.out.print(">> ");
		/* Read */
		/* The scanner auto-delimits on spaces, not newlines */
		Scanner scan = new Scanner(System.in).useDelimiter("\n");
		/* REPL */
		while (true) {
		    String input = scan.next();
		    /* Split on spaces */
		    String[] cmds = input.split(" ");
		    String[] revcmds = strcheck(cmds);
		    /* Eval */
		    System.out.println("\n=> " + eval(revcmds));
		    System.out.print(">> ");
		}
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

	public void update(String val) {
	    value = val;
	}

	/* Overloading? In MY interpreter? More likely than you'd think */
	public void update(String val, int index) {
	    //System.out.println("Overloaded update called");
	    String[] arr = value.split(",");
	    arr[index] = val;
	    value = "";
	    for (String s : arr) {
		//System.out.println("S added to value");
		value += s + ",";
	    }
	    
	}
    }

    private static class mfunc {
	/*
	  Class for own-rolled functions/subroutines/whatever
	*/
	private String[] body;
	private String name;
	private String[] paramlist;
	private int params;
	public mfunc(String[] nbody, int nparams, String nname, String[] list) {
	    body = nbody;
	    name = nname;
	    params = nparams;
	    paramlist = list;
	}
	
	/* Accessors */
	public String name() {
	    return name;
	}
	
	public String[] body() {
	    return body;
	}
	
	public int params() {
	    return params;
	}

	public String[] call(String[] passedparams) {
	    /* 
	       Return an array of strings with the parameter vars replaced
	       with the real parameters passed
	    */
	    for (int i = 0; i < paramlist.length; i++) {
		for (int k = 0; k < body.length; k++) {
		    if (body[k].equals(paramlist[i])) {
			//System.out.println("Param subbed: " + body[k] + " for " + passedparams[i]);
			body[k] = passedparams[i];
		    }
		}
	    }
	    return body;
	}

    }
}

