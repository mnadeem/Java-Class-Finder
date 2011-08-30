package com.nadeem.app.finder;

import com.nadeem.app.finder.engine.SearchEngine;
import com.nadeem.app.finder.util.DefaultConsoleLogger;

public class Finder {

	public static void main(String[] args) {
		
		if (shouldGoCommandLine(args)) {
			goCommandLine(args);
		} else {
			goGUI();
		}
	}	

	private static void goCommandLine(String[] params) {
		SearchEngine engine =  new SearchEngine(new DefaultConsoleLogger());			
		engine.searchForClass(params[0], params[1]);
		
		//TODO : Add logic so that user can search until he/she wishes
	}

	private static boolean shouldGoCommandLine(String[] args) {
		return args.length > 0;
	}
	
	private static void goGUI() {
		// TODO Auto-generated method stub		
	}
}
