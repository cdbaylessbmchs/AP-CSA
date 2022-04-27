Java File Browser v0.4

End Goal:
    Lightweight file browser
    Config file for associating programs with file types - nah
    If it's plaintext, show it; if it's a image, show it; if else, don't show it

Windows and Stuff:
	java swing main: https://docs.oracle.com/javase/7/docs/api/javax/swing/package-summary.html
	Use an editor pane to preview textfiles? https://docs.oracle.com/javase/7/docs/api/javax/swing/JEditorPane.html
	
NOTES:
	JFrame is the basis of everything else
	       It contains a JRootPane that will hold everything non-menu

Issues:
	Font is really small (will need to make a Font object for the text field)
	Java's idea of 80 columns is a lot bigger than mine
	
Phases:
1. Basic Swing window with a list of the current directory (incl. dotfiles) *
   1a. Basic Swing window *
       See: https://docs.oracle.com/javase/tutorial/uiswing/start/compile.html
   1b. Getting list of files *
2. Terminal pane - maybe later
   Use this https://docs.oracle.com/javase/7/docs/api/javax/swing/JTextField.html
   2a. Reuse old syscall code from dugscript here *
   2b. Make sure to change directory if cd is called *
       This will change - we're opening directories and looking in, not changing
3. Scrollbar *
   3a. Make it *actually scroll* *
       Might need scrollpane for text
       https://docs.oracle.com/javase/7/docs/api/javax/swing/JScrollPane.html
4. Syscalls *
   	   Plug a filename into the browser it opens it in the associated program
	   Config file where you associate extensions with programs *
5. Associating programs with files and (async) syscalls to open them
   5a. Async work *
6. Make it pretty and colorful
   6a. Remove most of the global vars and make it more functional
   6b. Add alphabetical ordering and/or modification ordering

NOTE: refreshing can be achieved by just pressing enter in the field
      equals signs in the names in config file can break it
