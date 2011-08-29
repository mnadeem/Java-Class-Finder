package com.nadeem.app.finder.engine;

import java.io.File;
import java.util.Collections;
import java.util.Set;

import com.nadeem.app.util.OutputLogger;

public class SearchEngine {

	private OutputLogger outputLogger;
	private Boolean abortSearch = Boolean.FALSE;
	
	public SearchEngine(OutputLogger outputLogger) {
		this.outputLogger = outputLogger;
	}
	
	public void searchForClass(String path, String className) {
		this.searchForClass(Collections.singleton(path), className);
	}
	
	public void searchForClass(Set<String> paths, String className) {
		for (String path : paths) {
			
			if (abortSearch) {
				outputLogger.logResult("Search Aborted !!!");
				return; 
			}
			searchClassInPath(path, className);			
			
		}	
	}

	private void searchClassInPath(String path, String className) {		
		File searchPath = searchPath(path);
		if (searchPath.exists()) {
			doSearchForClass(path, className);
		} else {
			outputLogger.logResult("Invalid Path : " + path);
		}
	}

	private void doSearchForClass(String path, String className) {		
		
	}

	protected File searchPath(String path) {
		return new File(path);
	}

	public void abortSearch() {
		this.abortSearch = Boolean.TRUE;
	}	

}
