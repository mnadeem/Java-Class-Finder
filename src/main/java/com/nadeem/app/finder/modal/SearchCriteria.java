package com.nadeem.app.finder.modal;

import java.util.HashSet;
import java.util.Set;

public class SearchCriteria {

	private String fileName;
	private Set<String> paths 	= new HashSet<String>();
	private Boolean deepSearch 	= Boolean.FALSE;

	public SearchCriteria() {

	}

	public SearchCriteria(String path, String fileName) {
		paths.add(path);
		this.fileName = fileName;
	}

	public SearchCriteria(Set<String> paths, String fileName) {
		this.paths 		= paths;
		this.fileName 	= fileName;
	}

	public SearchCriteria(String path, String fileName, Boolean deepSearch) {
		paths.add(path);
		this.fileName 	= fileName;
		this.deepSearch = deepSearch;
	}

	public Set<String> getPaths() {
		return paths;
	}

	public void setPaths(Set<String> paths) {
		this.paths = paths;
	}
	
	public void addPath(String path) {
		paths.add(path);
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Boolean deepSearch() {
		return deepSearch;
	}

	public void setDeepSearch(Boolean recursiveArchiveSearch) {
		this.deepSearch = recursiveArchiveSearch;
	}

	@Override
	public String toString() {
		return "fileName : " + fileName 
				+ ", recursiveArchiveSearch : " + deepSearch
				+ ", paths : " + paths;
	}

}
