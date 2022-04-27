/*
  Swing File Browser v0.1
*/

/* Imports */
import java.lang.*;
import java.io.*;
/* File paths */
import java.nio.file.*;
/* GUI */
//import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Container;
import java.awt.BorderLayout;

public class fbrowsev1 implements ActionListener {

    JFrame frame;
    JTextField field;
    JTextArea flist;
    /* frame's main content pane */
    Container mainpane;

    public fbrowsev1() {
	//TODO: make the names swappoutable
	frame = new JFrame("fbrowse");
	field = new JTextField(80);
	flist = new JTextArea(dirnab(), 50, 80);
	mainpane = frame.getContentPane();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//it's probably faster to do Container c = frame.getContentPane();
	//TODO: need to set these so they don't murk each other
	//frame.getContentPane().add(flist);
	//frame.getContentPane().add(field);
	//frame.getContentPane().doLayout();
	//frame.add(flist);
	//frame.add(field);
	//frame.add(flist);
	mainpane.add(field, BorderLayout.PAGE_START);
	mainpane.add(flist, BorderLayout.CENTER);
	frame.pack();
	frame.setVisible(true);
	field.addActionListener(this);
	flist.setEditable(false);
	//field.addDocumentListener(this);
    }
    
    public void actionPerformed (ActionEvent e) {
	//System.out.println("foo");
	System.out.println(field.getText());
    }

    /* Has to be static to get it from main() */
    private static String dirnab () {
	/* Get the directory we're in */
	Path dir = Paths.get(System.getProperty("user.dir"));
	/* Initialized to appease the compiler */
	String dirlist = "";
	/* Get the directory */
	try {
	    DirectoryStream<Path> stream = Files.newDirectoryStream(dir);
	    //String dirlist;
	    for (Path entry : stream) {
		/* Mark directories */
		if (entry.toFile().isDirectory()) {
		    dirlist += entry.getFileName() + "/" + "\n";
		} else {
		    dirlist += entry.getFileName() + "\n";
		}
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return dirlist;
    }
    
    public static void main (String[] args) {
	/* Create frame */
	//JFrame frame = new JFrame("fbrowse");
	//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	/* This works, but is tiny and also editable */
	/* Get directory */
	//JTextArea flist = new JTextArea(dirnab(), 50, 80);
	//JTextField term = new JTextField(80);
	//chaining may not be the way to go, but I just want to get it working
	//frame.getContentPane().add(flist);
	//need to do flist.setEditable(false);
	//flist.setEditable(false);
	//also need flist.setBackgroud() et al, but that comes later
	//doesn't work
	//frame.getContentPane().add(term);
	//frame.pack();
	//frame.setVisible(true);

	//termtest tmp = new termtest();
	//String txt = term.getText();
	//flist.append(txt + "\n");

	//mainbrowse test = new mainbrowse();
	fbrowsev1 test = new fbrowsev1();
       
    }

}

// class mainbrowse implements ActionListener {
//     JFrame frame;
//     JTextField field;
//     JTextArea flist;

//     public mainbrowse() {
// 	//TODO: make the names swappoutable
// 	frame = new JFrame("fbrowse");
// 	field = new JTextField(80);
// 	flist = new JTextArea(dirnab(), 50, 80);
// 	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
// 	//it's probably faster to do Container c = frame.getContentPane();
// 	frame.getContentPane().add(field);
// 	frame.getContentPane().add(flist);
// 	frame.pack();
// 	frame.setVisible(true);
// 	field.addActionListener(this);
// 	//field.addDocumentListener(this);
//     }
    
//     public void actionPerformed (ActionEvent e) {
// 	//System.out.println("foo");
// 	System.out.println(field.getText());
//     }
// }

/* Class for terminal */
// class termtest implements ActionListener {
//     JFrame frame;
//     JTextField field;

//     public termtest() {
// 	frame = new JFrame("test");
// 	field = new JTextField(80);
// 	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
// 	frame.getContentPane().add(field);
// 	frame.pack();
// 	frame.setVisible(true);
// 	field.addActionListener(this);
// 	//field.addDocumentListener(this);
//     }
    
//     public void actionPerformed (ActionEvent e) {
// 	//System.out.println("foo");
// 	System.out.println(field.getText());
//     }
// }
