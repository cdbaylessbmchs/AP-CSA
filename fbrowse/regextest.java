import java.util.*;
import java.lang.*;
import java.util.regex.*;

class regextest {
    /* Regex testing */

    public static void main (String[] args) {
	//pictures
	String[] ex = {"foo.mp3", "bar.ogg", "baz.opus", "qu.flac", "qux.m4a", "quu.m4b", "cat.opus", "dog.wav", "rabbit.wma"};

	// Pattern test = Pattern.compile(".+\\.(o(gg|pus)|w(av|ma)|m(p|4)(3|[a-b])|flac)");
	// //this doesn't work either
	// Pattern test2 = Pattern.compile("");
	// for (String str : ex) {
	//     Matcher go = test.matcher(str);
	//     if (go.matches()) {
	for (String str : ex) {
	    if (Pattern.compile(".+\\.(o(gg|pus)|w(av|ma)|m(p|4)(3|[a-b])|flac)").matcher(str).matches()) {
		System.out.println(str + " matches properly");
	    } else {
		System.out.println(str + " doesn't match");
	    }

	}

    }
    
}
