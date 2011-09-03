package com.nadeem.app.finder;

import com.nadeem.app.finder.engine.SearchEngine;
import com.nadeem.app.finder.listener.LogListener;
import com.nadeem.app.finder.listener.MatchCountChangeListener;
import com.nadeem.app.finder.modal.SearchCriteria;

public class SearchThread extends Thread {
	
	private volatile SearchCriteria criteria;
	private volatile SearchEngine engine;
	
	public SearchThread(SearchCriteria criteria, LogListener logListener) {
		this.criteria 	= criteria;
		this.engine 	= new SearchEngine(logListener) {
			@Override
			protected void onFinish() {
				onSearchFinish();
			}

			
		};
	}
	
	@Override
	public void run() {
		engine.searchForClass(criteria);
	}
	public void abortSearch() {
		engine.abortSearch();
	}
	
	public void clearAbortStatus() {
		engine.clearAbortStatus();
	}
	
	public void addCountChageListener (MatchCountChangeListener countListener) {
		engine.setCountListener(countListener);
	}
	
	protected void onSearchFinish() {
		
	}
}
