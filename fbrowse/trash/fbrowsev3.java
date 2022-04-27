/*
  Swing File Browser v0.3
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
//import javax.swing.JScrollBar;

//blagh
//public class fbrowsev3 implements ActionListener, AdjustmentListener {
public class fbrowsev3 implements ActionListener {
    
    JFrame frame;
    JTextField field;
    JTextArea flist;
    /* frame's main content pane */
    Container mainpane;
    /* TODO: this wastes memory, but I need it to make the refresh work */
    String currfiles;
    /* TODO: this is insecure and bad and etc */
    static String currdir;
    //scrollbar time
    //TODO: this doesn't scroll - probably need an adjustmentlistener
    //JScrollBar scroll;
    JScrollPane mainlist;
    //might need to make a jscrollpane instead of a textarea
    /* TODO: remove and replace with something better */
    //int scrollval;

    /* Set up all the class stuff */
    public fbrowsev3() {
	/* Initialize variables */
	currdir = System.getProperty("user.dir");
	currfiles = dirnab();
	frame = new JFrame("fbrowse");
	field = new JTextField(currdir, 80);
	/* Data, width, height */
	flist = new JTextArea(currfiles, 50, 80);
	//IN PROGRESS, see https://docs.oracle.com/javase/7/docs/api/javax/swing/JScrollBar.html
	/* 
	   Alignment, current position, how much is shown, minimum value and 
	   maximum value
	*/
	//scroll = new JScrollBar();
	//scroll.addAdjustmentListener(this);
	//scrollval = 1;
	/* Add scrolling ability */
	//TODO: this starts at the bottom instead of the top
	mainlist = new JScrollPane(flist);
	//mainlist.add(flist);
	//STABLE
	mainpane = frame.getContentPane();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	/* Put the terminal field on the top, and the list on bottom */
	mainpane.add(field, BorderLayout.PAGE_START);
	//mainpane.add(flist, BorderLayout.CENTER);
	mainpane.add(mainlist, BorderLayout.CENTER);
	/* Add scrollbar to the right-hand side */
	//mainpane.add(scroll, BorderLayout.EAST);
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
	    //TODO: remove this when refactoring for more functional-type stuff
	    currfiles = newfiles; 
    }

    //SCROLLBAR STUFF
    // public void adjustmentValueChanged(AdjustmentEvent e) {
    // 	//temporary;
    // 	//this works, but only scrolls down and crashes if it goes too far
    // 	//Easy!
    // 	//this needs to check if we're too close to the bottom
    // 	//flist.replaceRange("", scroll.getValue(), 20);
    // 			   //scroll.getMaximum());
    // 	//so, this doesn't replace a line at a time
    // 	//also, it crashes if you're at maximum
    // 	//also, it doesn't go up
    // 	//System.out.println("Adj type: " + e.getAdjustmentType());
    // 	//the adjustment type is 5 (tracking)
    // 	//SCROLL MAXVAL IS 90, NOT 100
    // 	System.out.println("Scroll position: " + scroll.getValue());
    // 	/* If you're going up */
    // 	//this doesn't work

    // 	System.out.println("Scrollval: " + scrollval);

    // 	if (scroll.getValue() < scrollval) {
    // 	    System.out.println("Smaller!");
    // 	    scrollval = scroll.getValue();
    // 	    //scrollval++;
    // 	    flist.insert(currfiles, 0);
    // 	} else {
    // 	    /* If you're going down */
    // 	    if (scroll.getValue() < 90) {
    // 		 flist.replaceRange("", 0, scroll.getValue());
    // 	    }
    // 	}
    // 	//if (e.getAdjustmentType() == 2 || e.getAdjustmentType() == 3) {
    // 	    //decrement
    // 	    //hacky, but might work
    // 	//     flist.insert(currfiles, 0);
    // 	// } else {
    // 	//     if (scroll.getValue() < 90) {
    // 	// 	flist.replaceRange("", 0, scroll.getValue());
    // 	//     }
    // 	// }

    // }

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
	    /* Recover gracefully if the directory doesn't exist */
	    dirlist = "Error: no such directory";
	}
	return dirlist;
    }
    
    public static void main (String[] args) {
	/* I *hate* OO */
	fbrowsev3 test = new fbrowsev3();
    }
}
