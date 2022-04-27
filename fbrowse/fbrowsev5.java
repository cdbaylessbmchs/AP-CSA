/*
  Swing File Browser v0.5
*/

/* Imports */
import java.lang.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;
/* File paths */
import java.nio.file.*;
/* GUI */
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.event.AdjustmentListener;
import java.awt.event.AdjustmentEvent;
import java.awt.Point;

public class fbrowsev5 implements ActionListener {
    
    JFrame frame;
    JTextField field;
    JTextArea flist;
    /* frame's main content pane */
    Container mainpane;
    /* TODO: this wastes memory, but I need it to make the refresh work */
    String currfiles;
    /* Give us the ability to scroll the textarea */
    JScrollPane mainlist;
    /* Config holder */
    static HashMap<String, String> config;
    //idk if I'll need this, but it seems faster for associating files
    static HashMap<String, String> fileassoc;

    //NOTE: temporary
    public static final String configloc = "test-config.txt";

    /* Set up all the class stuff */
    public fbrowsev5() {
	/* Initialize variables */
	//currdir = System.getProperty("user.dir");
	fileassoc = config(configloc);
	currfiles = dirnab(System.getProperty("user.dir"));
	frame = new JFrame("fbrowse");
	field = new JTextField(System.getProperty("user.dir"), 80);
	/* Data, width, height */
	flist = new JTextArea(currfiles, 50, 80);
	/* Add scrolling ability and focus on the file text area */
	mainlist = new JScrollPane(flist);
	mainpane = frame.getContentPane();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	/* Put the terminal field on the top, and the list on bottom */
	mainpane.add(field, BorderLayout.PAGE_START);
	mainpane.add(mainlist, BorderLayout.CENTER);
	/* Make it the correct size and show */
	frame.pack();
	frame.setVisible(true);
	/* Get ready for commands through the textfield */
	field.addActionListener(this);
	/* This ain't plan9 */
	flist.setEditable(false);
    }

    /* Change file listing when the terminal changes */
    public void actionPerformed(ActionEvent e) {
	/* Nab text */
	String dir = field.getText(); 
	    /* Grab new filelist */
	    String newfiles = dirnab(dir);
	    /* Change out the text in the browser */
	    flist.replaceRange(newfiles, 0, currfiles.length());
	    /* 
	       Set scrollbar (if we have one) at the top
	       This seems hacky and dumb, but works when 
	       the logical way doesn't
	    */
	    flist.setSelectionStart(0);
	    flist.setSelectionEnd(0);
	    //TODO: remove this when refactoring for more functional-type stuff
	    currfiles = newfiles; 
    }

    /* Has to be static to get it from main() */
    private static String dirnab(String currdir) {
	/* Get the directory we want */
	Path dir = Paths.get(currdir);
	/* Initialized to appease the compiler */
	String dirlist = "";
	/* Get the directory */
	try {
	    DirectoryStream<Path> stream = Files.newDirectoryStream(dir);
	    for (Path entry : stream) {
		/* Mark directories */
		if (entry.toFile().isDirectory()) {
		    dirlist += entry.getFileName() + "/" + "\n";
		} else {
		    dirlist += entry.getFileName() + "\n";
		}
	    }
	} catch (IOException e) {
	    /* Try to open it as a file first */
	    try {
		/* Thank the heavens for garbage collectors */
		FileReader file = new FileReader(currdir);
		file.close();
		//this really defeats the point, since it already has dired,
		//but w/e
		//Process proc = Runtime.getRuntime().exec("emacs " + currdir);
		//System.out.println("Current: " + currdir);
		runprog(currdir);
	    } catch (FileNotFoundException f) {
		/* Recover gracefully if the file/directory doesn't exist */
		dirlist = "Error: no such directory";
	    } catch (IOException a) {
	    	System.out.println("Bruh moment");
	    	a.printStackTrace();
	    }
	}
	return dirlist;
    }

    /* Open a file in a program */
    private static void runprog (String filen) {
	/* 
	   This is horrific, I can see the Rust programmers weeping and gnashing
	   teeth as I write this
	   I wanted to make the regexes more complex, but they do the job for
	   now
	*/
	//TODO: add the ability to set specific programs for file extensions
	System.out.println("Starting process");
	//TODO: breaks if there are spaces in the filename or if you want to
	//launch a curses application
	//possible fix for spaces: split it into array, and then pass that
	//^ yes, do this
	if (Pattern.compile(".+\\.(txt|md|(ya|to|x)ml|ini|json)").matcher(filen).matches()) {
	    //text file
	    //System.out.println("Text file detected, using " + fileassoc.get("TXT"));
	    try {
		/* These are ALWAYS absolute paths */
		String[] tmp = {fileassoc.get("TXT"), filen};
		Process proc = Runtime.getRuntime().exec(tmp);
		//Process proc = Runtime.getRuntime().exec(fileassoc.get("TXT") + ' ' + filen);
	    } catch (IOException b) {
		System.out.println("IO Error");
	    }
	} else if (Pattern.compile(".+\\.(jp(e|)g|png|gif|tiff)").matcher(filen).matches()) {
	    //image file
	    try {
		String[] tmp = {fileassoc.get("PIC"), filen};
		Process proc = Runtime.getRuntime().exec(tmp);

		//Process proc = Runtime.getRuntime().exec(fileassoc.get("PIC") + ' ' + filen);
	    } catch (IOException b) {
		System.out.println("IO Error");
	    }
	    
	} else if (Pattern.compile(".+\\.(o(gg|pus)|w(av|ma)|m(p|4)(3|[a-b])|flac)").matcher(filen).matches()) {
	    //audio file
	    try {
		String[] tmp = {fileassoc.get("MUS"), filen};
		Process proc = Runtime.getRuntime().exec(tmp);
		//Process proc = Runtime.getRuntime().exec(fileassoc.get("MUS") + ' ' + filen);
	    } catch (IOException b) {
		System.out.println("IO Error");
	    }
	} else if (Pattern.compile(".+\\.(o(gg|pus)|w(av|ma)|m(p|4)(3|[a-b])|flac)").matcher(filen).matches()) {
	    //video file
	    try {
		String[] tmp = {fileassoc.get("VID"), filen};
		Process proc = Runtime.getRuntime().exec(tmp);
		//Process proc = Runtime.getRuntime().exec(fileassoc.get("VID") + ' ' + filen);
	    } catch (IOException b) {
		System.out.println("IO Error");
	    }
	} else {
	    //use default
	    //System.out.println("Default called with " + fileassoc.get("DEF"));
	    try {
		String[] tmp = {fileassoc.get("DEF"), filen};
		Process proc = Runtime.getRuntime().exec(tmp);
		//Process proc = Runtime.getRuntime().exec(fileassoc.get("DEF") + ' ' + filen);
	    } catch (IOException b) {
		System.out.println("IO Error");
	    }
	}
    }


    //Working just fine
    private static HashMap<String, String> config (String location) {
	//NOTE: it defaults to a size of 16
	HashMap<String, String> ret = new HashMap<String, String>();
	Path path = Paths.get(location);
	try {
	    BufferedReader buf = Files.newBufferedReader(path);
	    String line;
	    while ((line = buf.readLine()) != null) {
		/* If the line starts with #, it's a comment */
		if (line.startsWith("#")) {
		    continue;
		} else {
		    /* 
		       Since regex can be occasionally unreliable, we're just 
		       gonna split it at the equals sign
		    */
		    //TODO: gonna need to chomp off trailing newline and spaces
		    String[] confline = line.split("=");
		    //this is an attempt, but prolly better to do another way
		    ret.put(confline[0], confline[1].replaceAll("\\n", ""));
		}
	    }
	} catch (IOException e) {
	    System.out.println("Error: could not read configuration file");
	}
	//System.out.println(ret.get("TXT"));
	return ret;
    }
        
    public static void main (String[] args) {
	/* I *hate* OO */
	//run configurator
	//config = config(configloc);
	fbrowsev5 test = new fbrowsev5();
    }
}
