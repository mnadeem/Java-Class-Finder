package com.nadeem.app.finder;

import java.util.Scanner;

import com.nadeem.app.finder.engine.SearchEngine;
import com.nadeem.app.finder.util.DefaultConsoleLogger;

public class Finder {

	public static void main(String[] args) {

		if (shouldGoCommandLine(args)) {
			System.out.println("\n**** You are using the Command Line version of Finder !!! ****\n");
			goCommandLine(args);
		} else {
			System.out.println("\n**** You are using the GUI version of Finder !!! ****\n");
			goGUI();
		}
	}

	private static boolean shouldGoCommandLine(String[] args) {
		return args.length > 0;
	}

	private static void goCommandLine(String[] params) {
		SearchEngine engine =  new SearchEngine(new DefaultConsoleLogger());
		if (isHelpRequired(params)) {
			helpUser(params);
		} else {
			engine.searchForClass(params[0], params[1]);
		}
		goFurther(engine);
	}

	private static Boolean isHelpRequired(String[] params) {
		return params.length != 2;
	}

	private static void helpUser(String[] params) {
		System.out.println("It seems you are using the CL Version incorrectly");
		System.out.println("Correct Format : java -jar finder.jar [location] [ClassName]");
		System.out.println("Never mind I can simplify It, just follow the instructions ");
	}

	private static void goFurther(final SearchEngine engine) {
		final Scanner scanner = new Scanner(System.in);
		String location;
		String className;
		String userDecision;

		while (true) {
			userDecision = getString("\nDo you want to Contiune ? Enter N for No, Y for Yes", scanner);
			if ("N".equalsIgnoreCase(userDecision)) {
				System.out.println("\nBye, bye...");
				break;
			}
			location  = getString("Enter Search Location", scanner);
			className = getString("Enter Class name", scanner);
			engine.searchForClass(location, className);
		}
	}

	private static String getString(String message, Scanner scanner) {
		System.out.print(message + " : ");
		return scanner.nextLine();
	}

	private static void goGUI() {
		System.out.println("**** Not yet Implemented ****");
		System.out.println("Bye, bye...");
	}
}
