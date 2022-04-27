//testing textfields for terminal panes

import javax.swing.*;
import java.lang.*;

import javax.swing.event.*;

//actionevent
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

//working off of https://docs.oracle.com/javase/tutorial/uiswing/examples/components/TextFieldDemoProject/src/components/TextFieldDemo.java
public class termpane {
//implements DocumentListener {

    public static void main (String[] args) {
    	// JFrame frame = new JFrame("testterm");
    	// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	// JTextField term = new JTextField(80);
    	// frame.getContentPane().add(term);
    	// frame.pack();
    	// frame.setVisible(true);
	// //actionlistener time
	//     //term.addActionListener(this);
	// //this works, but not well
        // while (true) {
	//      //System.out.println(term.getText());
	//     //kills exception, but still doesn't work
	//     if (term.getText() == null) {
	// 	continue;
	//     }
	//     //works with "f", but not with \n
	//     //trying to use \t causes a nullpointerexception
	//     //I can get \n to work like once...wtf?
	//     //if you put in 9 chars, it'll register a newline
	//     //if you type too fase, you'll get a nullpointerexception
	//     if (term.getText().contains("\n")) {
	// 	//null pointer exception
	// 	System.out.println(term.getText());
	// 	break;
	//     }
	// }
	termtest term = new termtest();
    }

	//term.addActionListener(new ActionListener());
    //}

    // term.addActionListener(new ActionListener() {
    // 	    public void actionPerformed (ActionEvent e) {
    // 		System.out.println("Thing did");
    // 	    }
    // 	});
    // public void actionPerformed (DocumentEvent e){  
    // 	System.out.println("foo");
    // }

    //public static void main (String[] args) {
	//termtest term = new termtest();
    //}


}

class termtest implements ActionListener {
    JFrame frame;
    JTextField field;

    public termtest() {
	frame = new JFrame("test");
	field = new JTextField(80);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.getContentPane().add(field);
	frame.pack();
	frame.setVisible(true);
	field.addActionListener(this);
	//field.addDocumentListener(this);
    }
    
    public void actionPerformed (ActionEvent e) {
	//System.out.println("foo");
	System.out.println(field.getText());
    }
}
// class termtest implements ActionListener {
//     JFrame frame;
//     JTextField field;

//     public termtest() {
// 	frame = new JFrame("testterm");
// 	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
// 	JTextField term = new JTextField(80);
// 	frame.getContentPane().add(term);
// 	frame.pack();
// 	frame.setVisible(true);

// 	//experimental
// 	//frame.getRootPane().setDefaultButton(submitButton);
// 	//term.setActionCommand(ENTER);
//     }

//     public void actionPerformed(ActionEvent e) {
// 	System.out.println("Foo!");
//     }

//     public void keyPressed(KeyEvent e) {
// 	//System.out.println("bar!");
// 	if (e.getKeyCode()==KeyEvent.VK_ENTER) {
// 	    System.out.println("bar");
// 	}
//     }
// }
	
