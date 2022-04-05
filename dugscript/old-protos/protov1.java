/*
 * Language Interpreter Prototype 0
 * Read single statement from stdin, evaluate, print to stdout
 * Basically just an RPN calculator
 */

/* Imports */
import java.lang.*;
import java.util.*;

public class protov1 {
	
	/* Constants */
	/* Max size of list of operands - arbitrary */
	public static final int MAX_SIZE = 50;

	/* Objects and subroutines */
	private static float matheval(String[] statement){
		float evaluated;
		/* Only need an array because only operate on two 
		 * nums at a time
		 */
		float[] varlist = new float[MAX_SIZE];
		int i = 0;
		for (String s : statement) {
			/* Strip parentheses */
			String strip1 = s.replace("(", "");
			String strip2 = strip1.replace(")", "");
			switch (strip2) {
				case "+":
					i -= 2;
					evaluated = varlist[i] + varlist[i + 1];
					varlist[i] = evaluated;
					break;
				case "-":
					i -= 2;
					evaluated = varlist[i] - varlist[i + 1];
					varlist[i] = evaluated;
					break;
				case "*":
					i -= 2;
					evaluated = varlist[i] * varlist[i + 1];
					varlist[i] = evaluated;
					break;
				case "/":
					i -= 2;
					evaluated = varlist[i] / varlist[i + 1];
					varlist[i] = evaluated;
					break;
				case "%":
					i -= 2;
					evaluated = varlist[i] % varlist[i + 1];
					varlist[i] = evaluated;
					break;
				default:
					/* Append to varlist */
					varlist[i] = Float.parseFloat(strip2);
			}
			i++;
		}
		/* This will always be the final evaluated number */
		return varlist[0];
	}

	public static void main(String[] args) {
		/* Indicate status */
		System.out.print(">> ");
		/* Read */
		/* The scanner auto-delimits on spaces, not newlines */
		Scanner scan = new Scanner(System.in).useDelimiter("\n");
		String input = scan.next();
		/* Split on spaces */
		String[] cmds = input.split(" ");
		/* Eval */
		System.out.println(matheval(cmds));
		/* Loop */
		main();
	}
}
