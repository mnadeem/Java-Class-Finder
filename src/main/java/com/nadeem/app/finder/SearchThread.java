package com.nadeem.app.finder;

import com.nadeem.app.finder.engine.SearchEngine;
import com.nadeem.app.finder.modal.SearchCriteria;
import com.nadeem.app.finder.util.LogListener;

public class SearchThread extends Thread {
	
	private volatile SearchCriteria criteria;
	private volatile SearchEngine engine;
	
	public SearchThread(SearchCriteria criteria, LogListener logListener) {
		this.criteria 	= criteria;
		this.engine 	= new SearchEngine(logListener);
	}
	
	@Override
	public void run() {
		engine.searchForClass(criteria);
	}
	public void abortSearch() {
		engine.abortSearch();
	}
}
