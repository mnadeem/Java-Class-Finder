package com.nadeem.app.finder;

import java.util.Scanner;

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
		if (isHelpRequired(params)) {
			helpUser(params);
		} else {		
			engine.searchForClass(params[0], params[1]);
		}

		Scanner scanner = new Scanner(System.in);
		String location;
		String className;
		
		while(true) {
			System.out.println("Do you want to Contiune ? Enter N for No, Y for Yes");
			if ("N".equalsIgnoreCase(scanner.nextLine())) {
				System.out.println("Bye, bye");
				break;
			}
			System.out.print("Enter Search Location : ");
			location = scanner.nextLine();
			System.out.print("Enter Class name : ");
			className= scanner.nextLine();
			engine.searchForClass(location, className);
		}		
			
	}
	
	private static Boolean isHelpRequired(String[] params) {
		return params.length != 2;
	}
	
	private static void helpUser(String[] params) {
		System.out.println("Format : java -jar finder.jar [location] [ClassName]");		
	}

	private static boolean shouldGoCommandLine(String[] args) {
		return args.length > 0;
	}
	
	private static void goGUI() {
		// TODO Auto-generated method stub		
	}
}
