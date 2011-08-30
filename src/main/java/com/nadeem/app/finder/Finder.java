package com.nadeem.app.finder;

import com.nadeem.app.finder.engine.SearchEngine;
import com.nadeem.app.finder.util.DefaultOutputLogger;

public class Finder {

	public static void main(String[] args) {
		
		if (goCommandLine(args)) {
			SearchEngine engine =  new SearchEngine(new DefaultOutputLogger());
			
			engine.searchForClass(args[0], args[1]);
		}

	}

	private static boolean goCommandLine(String[] args) {
		return args.length > 0;
	}	
}
