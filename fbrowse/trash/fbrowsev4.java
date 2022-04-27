/*
  Swing File Browser v0.4
*/

/* Imports */
import java.lang.*;
import java.io.*;
//import java.util.*;
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
//import javax.swing.JScrollBar;

//blagh
public class fbrowsev4 implements ActionListener {
    
    JFrame frame;
    JTextField field;
    JTextArea flist;
    /* frame's main content pane */
    Container mainpane;
    /* TODO: this wastes memory, but I need it to make the refresh work */
    String currfiles;
    /* TODO: this is insecure and bad and etc */
    static String currdir;
    /* Give us the ability to scroll the textarea */
    JScrollPane mainlist;

    /* Set up all the class stuff */
    public fbrowsev4() {
	/* Initialize variables */
	currdir = System.getProperty("user.dir");
	currfiles = dirnab();
	frame = new JFrame("fbrowse");
	field = new JTextField(currdir, 80);
	/* Data, width, height */
	flist = new JTextArea(currfiles, 50, 80);
	/* Add scrolling ability and focus on the file text area */
	//TODO: this starts at the bottom instead of the top
	mainlist = new JScrollPane(flist);
	//neither of these work
	//tested doing it backwards (i.e, with the maximum) as well, nothing
	//mainlist.getVerticalScrollBar().setValue(0);
	//mainlist.getHorizontalScrollBar().setValue(0);
	//mainlist.getVerticalScrollBar().setValue(mainlist.getVerticalScrollBar().getMinimum());
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
	    /* Create new path from the call */
	    currdir = dir;
	    /* Grab new filelist */
	    String newfiles = dirnab();
	    /* Change out the text in the browser */
	    flist.replaceRange(newfiles, 0, currfiles.length());
	    //this is closer, but still doesn't work
	    //System.out.println("view movement called");
	    //mainlist.getViewport().setViewPosition(new Point(0,0));
	    //mainlist.getVerticalScrollBar().setValue(0);
	    /* This seems hacky and dumb, but works when the logical way doesn't */
	    flist.setSelectionStart(0);
	    flist.setSelectionEnd(0);
	    //TODO: remove this when refactoring for more functional-type stuff
	    currfiles = newfiles; 
    }

    /* Has to be static to get it from main() */
    private static String dirnab() {
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
		Process proc = Runtime.getRuntime().exec("emacs " + currdir);
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
    
    public static void main (String[] args) {
	/* I *hate* OO */
	fbrowsev4 test = new fbrowsev4();
    }
}
