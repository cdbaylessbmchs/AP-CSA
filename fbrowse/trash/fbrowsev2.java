/*
  Swing File Browser v0.2
*/

/* Imports */
import java.lang.*;
import java.io.*;
/* File paths */
import java.nio.file.*;
/* GUI */
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Container;
import java.awt.BorderLayout;

public class fbrowsev2 implements ActionListener {

    JFrame frame;
    JTextField field;
    JTextArea flist;
    /* frame's main content pane */
    Container mainpane;
    /* TODO: this wastes memory, but I need it to make the refresh work */
    String currfiles;
    /* TODO: this is insecure and bad and etc */
    static String currdir;

    public fbrowsev2() {
	/* Initialize variables */
	currdir = System.getProperty("user.dir");
	currfiles = dirnab();
	frame = new JFrame("fbrowse");
	field = new JTextField(currdir, 80);
	flist = new JTextArea(currfiles, 50, 80);
	mainpane = frame.getContentPane();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	/* Put the terminal field on the top, and the list on bottom */
	mainpane.add(field, BorderLayout.PAGE_START);
	mainpane.add(flist, BorderLayout.CENTER);
	frame.pack();
	frame.setVisible(true);
	/* Get ready for commands thru the textfield */
	field.addActionListener(this);
	/* This ain't plan9 */
	flist.setEditable(false);
    }

    /* Do something when something's typed into terminal */
    public void actionPerformed (ActionEvent e) {
	//System.out.println(field.getText());
	/* Nab text */
	//String call = field.getText();
	String dir = field.getText();
	/* Execute */
	//REMOVED FOR DEBUGGING PURPOSES, MIGHT GO BACK IN IF I FEEL LIKE IT
	//try {
	//TODO: this is crap, use https://docs.oracle.com/javase/7/docs/api/java/lang/ProcessBuilder.html#directory(java.io.File)
	/* 
	   Fun fact: they don't have a basic chdir because it wouldn't work well
	   with multithreaded stuff. So I have to do this instead of one call
	*/
	//if (call.startsWith("cd")) {
	    /* 
	       This is bad, but will keep me from having to reuse strcheck()
	       for now 
	    */
	    //String dir = call.replace("cd ", "");
	    //System.out.println("dir is " + dir);
	    /* Stop it from freaking out if it's a bad file */
	    //if (dir.equals("") || dir.equals(System.getProperty("user.dir")) || dir.equals("cd")) {
		//System.out.println("bad juju");
	//	return;
	//  }
	    /* Create new path from the call */
	    currdir = dir;
	    /* Grab new filelist */
	    String newfiles = dirnab();
	    flist.replaceRange(newfiles, 0, currfiles.length());
	    currfiles = newfiles;
	    //}
	    
	    //also, it does not recover gracefully from errors
	    //Process proc = Runtime.getRuntime().exec(call);
	    // if (call.startsWith("cd")) {
	    // 	/* Truncate so we only have the directory */
	    // 	String dir = call.replace("cd ", "");
	    // 	System.out.println("directory is now: " + dir);
	    // 	/* Change directory */
	    // 	//System.setProperties(something something)
	    // 	currdir = dir;
	    // 	/* Get new directory contents */
	    //     String newfiles = dirnab();
	    // 	/* Overwrite old filelist contents */
	    // 	flist.replaceRange(newfiles, 0, currfiles.length());
	    // 	/* Replace currfiles */
	    // 	currfiles = newfiles;
	    // }
	    //TODO: some kind of responsiveness for shell cmds
	    //} catch (IOException ex) {
	    //TODO: this doesn't exit the browser, but you still can't do
	    //anything
	    //ex.printStackTrace();
	    //}   
    }

    /* Has to be static to get it from main() */
    private static String dirnab () {
	/* Get the directory we're in */
	//changed for debugging purposes
	Path dir = Paths.get(currdir);
	//Path dir = Paths.get(System.getProperty("user.dir"));
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
	    //TODO: this does *not* recover gracefully
	    //e.printStackTrace();
	    dirlist = "Error: no such directory";
	}
	return dirlist;
    }
    
    public static void main (String[] args) {
	/* I *hate* OO */
	fbrowsev2 test = new fbrowsev2();
    }
}
