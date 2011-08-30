package com.nadeem.app.finder;

import com.nadeem.app.finder.engine.SearchEngine;
import com.nadeem.app.finder.util.DefaultOutputLogger;

public class Finder {

	public static void main(String[] args) {
		
		if (shouldGoCommandLine(args)) {
			goCommandLine(args);
		}
	}

	private static void goCommandLine(String[] args) {
		SearchEngine engine =  new SearchEngine(new DefaultOutputLogger());			
		engine.searchForClass(args[0], args[1]);
	}

	private static boolean shouldGoCommandLine(String[] args) {
		return args.length > 0;
	}	
}
