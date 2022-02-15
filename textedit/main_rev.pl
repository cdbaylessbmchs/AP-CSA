#Invoke curseslib
use Curses;
use utf8;
#Setting global filename        
our $filename = $ARGV[0];
#Full array of lines
our @file;
our @oldfile;
#Used for copy/paste
our $clipboard = "";
#Used for tracking last save
our $arrpos = 0;

main();

sub main{
    if ($ARGV[0] eq ""){
	our $filename = ".0xscratch";
    } else {
        reader();
    }
        init();
	insert();
}
#TODO: make this better 
sub hacky{
	open($filein, "<", "$_");
	@file = <$filein>;
	$tmp = join("", @file);
	@file = split("", $tmp);
        close($filein);

}
sub reader{
        open($filein, "<", "$filename") or hacky($filename); 
        @file = <$filein>;
	$tmp = join("", @file);
	@file = split("", $tmp);
        close($filein);
}

sub init{
    initscr();
    start_color();
    #use_default_colors();
    init_pair(1,COLOR_BLACK,COLOR_WHITE);
    #enable keypad and arrows
    $stdscr->keypad();
    #stop C-z from suspending, etc
    raw();
    noecho();
    fullrefresh();
}

sub fullrefresh{
    clear();
    $filestr = join("", @file);
    push(@oldfile, $filestr);
    addstr($filestr);
    $xval = 0;
    $yval = 0;
    $i = 0;
    until ($i == $arrpos){
	if ($file[$i] =~ /\n/){
	    $xval = 0;
	    $yval++;
	} else {
	    $xval++;
	}
	$i++;
    }
    move($yval, $xval);
    refresh();
    insert();
}

sub insert{
    $ch = getchar();
    if ($ch =~ /\n/){
	    goto PISA;
    }
    if ($ch =~ /\x7F/ || $ch == KEY_BACKSPACE){ 
    	if ($arrpos == 0){
	    #I hate myself
	    goto ITALIAN;
	} else {
	    backspace();
	    $arrpos--;
	}
    #in form \c[A-Z], like C-[A-Z] in emacs
    } elsif ($ch =~ /\cC/){
	    #why can't I just use break ;-;
	   if ($arrpos == (scalar @file)){
		   goto ITALIAN;
	   } else {
		copy();
	}
    } elsif ($ch =~ /\cX/){
	   if ($arrpos == (scalar @file)){
		   goto ITALIAN;
	   } else {
		cut();
	}
    } elsif ($ch =~ /\cV/){
	paste();
    } elsif ($ch =~ /\cZ/){
	undo();
    } elsif ($ch =~ /\cQ/){
	#quit
	end();
    } elsif ($ch =~ /\cF/){
	find();
    } elsif ($ch =~ /\cS/){
	#hard-coding this may be a bad idea, but I don't really care
	if ($filename =~ /^\.0xscratch$/){
	    saveas();
	} else {
	    save();
	}
    } elsif ($ch =~ /\cA/){
	saveas();
    } elsif ($ch =~ /\cY/){
	#delete a variable number (prompted) of lines
	delvarline();
    } elsif ($ch =~ /\cL/){
	#delete line
	delline();
    } elsif ($ch =~ /\cK/){
	#to top
	totop();
    } elsif ($ch =~ /\cJ/){
	#to bottom
	addstr("to bot");
	refresh();
	sleep(5);
	tobot();
	#break;
    } elsif ($ch =~ /\cN/){
	newfile();
    #arrowkeys 
    } elsif ($ch == KEY_UP){
	#up
	$i = 0;
	$arrpos--;
	until ($file[$arrpos] =~ /\n/ || $arrpos == 0){
		$arrpos--;
		$i++;
	}
	$arrpos -= $i;
	$arrpos--;
	if ($arrpos < 0){
		$arrpos = 0;
	}
    } elsif ($ch == KEY_DOWN){
	#down
	$i = 0;
	until ($file[$arrpos] =~ /\n/ || $arrpos == scalar @file){
		$arrpos++;
		$i++;
	}
	if ($arrpos < scalar @file){
		$arrpos += $i;
	}
    } elsif ($ch == KEY_LEFT){
	#left
	$arrpos--;
    } elsif ($ch == KEY_RIGHT){
	#right
	if (($arrpos) < (scalar @file)){
		$arrpos++;
	}
	#	} elsif ($ch =~ /(\cB|\cD|\cE|\cG|\cH|\cI|\cL|\cM|\cO|\cP|\cQ|\cR|\cT|\cU|\cW)/){
	#this is bad, but perl isn't letting me use a kewl regex for it ):
	#init_pair(2,COLOR_BLACK,COLOR_WHITE);
	#    $bruhwin = newwin(1,getmaxx(),getmaxy() - 1,0);
	#    $bruhwin->bkgd(COLOR_PAIR(2));
	#    $bruhwin->refresh();
	#    $bruhwin->addstr("That's not a command");
	#    $bruhwin->refresh();
	#    $bruhwin->delwin();
    } else {
	#had to use a goto here, because otherwise it wouldn't work properly	
	PISA:	
	insert_inline();
    	$arrpos++;
    }
    #if ($ch !~ /(\cS|\cA)/){
    #} 
    #for undo handling
    if (($ch =~ /\c (X|V|F)/) || ($ch =~ /[^\x00-\x7e]+/)){
	$tmp = join('', @file);
	push(@oldfile, $tmp);
    }	    
  ITALIAN:
    fullrefresh();
}

sub insert_inline{
    $pos = $arrpos;
	$i = 0;
	@edited = @file;
	while ($i < scalar @file + 1){
	if ($i < $pos){
	    $edited[$i] = $file[$i];
	} elsif ($i == $pos){
		$edited[$i] = $ch;
	} else {
	    $edited[$i] = $file[$i - 1];
	}
	$i++;
	}
    @file = @edited
}

sub backspace{
    $i = 0;
    @tmp;
    while ($i < scalar @file){
	if ($i < $arrpos - 1){
	    $tmp[$i] = $file[$i];
	} else {
	    $tmp[$i] = $file[$i+1];
	}
	$i++;
    }
    @file = @tmp;
}

#Orthogonal function area
#refreshing and going back to initial function will be handled by fullrefresh()
sub totop{
    $arrpos = 0;
}

sub tobot{
    while ($file[$arrpos] != undef){
	    $arrpos++;
    }
    #$arrpos = (scalar @file - 2);
}

sub copy{
    @clipboard = ();
    our $i = $arrpos - 1;
    until ($file[$i] =~ /\n/ || $i == -1){
    	$i--;
    }
    for $k ($i+1..$arrpos - 1){
        push(@clipboard, $file[$k]);
	$k++;
    }
    our $j = $arrpos;
    until ($file[$j] =~ /\n/ || $j == scalar @file){
        push(@clipboard, $file[$j]);
        $j++;
    }
    push(@clipboard,"\n");
    our $clipboard = join('', @clipboard);
}


sub paste{
	@edited = ();
	$tmp = "";
	@tmp = ();
	if ($clipboard =~ //){ 
		return;
	}
	#gonna reuse the insert_inline code for this
    while ($i < scalar @file + 1){
	if ($i < $arrpos){
	    $edited[$i] = $file[$i];
	} elsif ($i == $arrpos){
		$edited[$i] = $clipboard;
	} else {
	    $edited[$i] = $file[$i - 1];
	}
	$i++;
	}
	$tmp = join('', @edited);
	@tmp = split('', $tmp);
    @file = @tmp;
}

sub end{
    save();
	endwin();
	exit();
}

sub undo{
    #oldfile == array of scalars
    @file = split('', @oldfile[scalar @oldfile - 1]);
    #allows for infinite undos within session
    pop(@oldfile);
}

sub cut{
    #reset clipboard 
    @clipboard = ();
    our $i = $arrpos - 1;
    until ($file[$i] =~ /\n/ || $i == -1){
    	$i--;
    }
    for $k ($i + 1..($arrpos - 1)){
        push(@clipboard, $file[$k]);
    }
    $j = $arrpos;
    until ($file[$j] =~ /\n/ || $j == scalar @file){ 
        push(@clipboard, $file[$j]);
        $j++;
    }
    push(@clipboard,"\n");
    @tmp = ();
    #removing the line from the file 
    delline();
    $newarrpos = $i + 1;
    $arrpos = $newarrpos;
    our @file = @tmp;
    our $clipboard = join('', @clipboard);
}


sub find{
    echo();
    init_pair(2,COLOR_BLACK,COLOR_WHITE);
    $win = newwin(2,getmaxx(),getmaxy() - 2,0);
    $win->bkgd(COLOR_PAIR(2));
    $win->refresh();
    $win->addstr("Find: ");
    $win->refresh();
    $win->getstr($statement);
    $win->refresh();
    $regex = qr/$statement/;
    $win->addstr("Replace: ");
    $win->getstr($statement2);
    $win->refresh();
    $regex2 = qr/$statement/;
    $win->delwin();
    $edit = join('', @file);
    $edit =~ s/$statement/$statement2/g;
    @file = split('', $edit);
    noecho();
}

sub save{
    #TODO: needs error handling
    open($fileout, ">", "$filename") or die "Error: could not open file";
    print $fileout @file;
    close($fileout);
}

sub saveas{
    #ncurses thing to prompt
    echo();
    init_pair(2,COLOR_BLACK,COLOR_WHITE);
    $win = newwin(1,getmaxx(),getmaxy() - 1,0);
    $win->bkgd(COLOR_PAIR(2));
    $win->refresh();
    $win->addstr("Save as: ");
    $win->refresh();
    $win->getstr($newname);
    open($fileout, ">", "$newname");# or $win->addstr("I can't do that");
    print $fileout @file;
    close($fileout);
    $win->delwin();
    noecho();
    our $filename = $newname;
}

sub delline{
    $i = $arrpos - 1;
    until ($file[$i] =~ /\n/ || $i == -1){
	$i--;
    }
    $j = $arrpos;
    until ($file[$j] =~ /\n/ || $j == scalar @file){ #$file[$j] == undef){
	$j++;
    }
    #$j++;
    #NOTE: if you're getting dupes, then reset the tmp array lol
    @tmp = ();
    #beware off-by-ones - I think it's okay, but will need checking
    for $l (0..$i - 1){
	push(@tmp, $file[$l]);
    }
    for $z ($j..(scalar @file - 1)){
	push(@tmp, $file[$z]);
    }
    #push(@tmp, "\n");
    our @file = @tmp;
}

sub delvarline{
    echo();
    init_pair(2,COLOR_BLACK,COLOR_WHITE);
    $win = newwin(1,getmaxx(),getmaxy() - 1,0);
    $win->bkgd(COLOR_PAIR(2));
    $win->refresh();
    $win->addstr("# of lines to delete: ");
    $win->refresh();
    $win->getstr($linenum);
    $win->delwin();
    noecho();
    for $i (0..$linenum - 1){
	    #lmao
	    delline();
    }
}

sub newfile{
    echo();
    init_pair(2,COLOR_BLACK,COLOR_WHITE);
    $win = newwin(1,getmaxx(),getmaxy() - 1,0);
    $win->bkgd(COLOR_PAIR(2));
    $win->refresh();
    $win->addstr("New file name: ");
    $win->refresh();
    $win->getstr($newname);
    our $filename = $newname;
    open($fileout,">","$filename");
    $tmp = join('', @file);
    print $fileout $tmp;
    close($fileout);
    $win->delwin();
    noecho();
}
