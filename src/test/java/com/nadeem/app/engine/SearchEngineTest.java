package com.nadeem.app.engine;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import com.nadeem.app.finder.engine.SearchEngine;
import com.nadeem.app.finder.util.OutputLogger;

public class SearchEngineTest {
	
	private static final String SOME_PATH 	= "somePath";
	private static final String SOME_CLASS  = "SOMECLASS";

	@Mock
	private OutputLogger mockedLogger;
	@Mock
	private File mockedFile;

	private Set<String> paths;
	
	private SearchEngine targetBeingTested;
	
	@Before
	public void doBeforeEachTestCase() {
		initMocks(this);
		targetBeingTested =  new SearchEngine(mockedLogger) {
			@Override
			protected File searchPath(String path) {
				return mockedFile;
			}
		};
		paths = new HashSet<String>();

	}
	
	@Test
	public void shouldLogToLogger() {
		paths.add(SOME_PATH);
		targetBeingTested.searchForClass(paths, SOME_CLASS);
		verify(mockedLogger, times(1)).logResult(anyString());
	}
	
	@Test
	public void shouldLogAbortedMessage() throws Exception {
		ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

		targetBeingTested.abortSearch();
		targetBeingTested.searchForClass(SOME_PATH, SOME_CLASS);
		
		verify(mockedLogger).logResult(argumentCaptor.capture());
		
		assertEquals("Search Aborted !!!", argumentCaptor.getValue());
	}
	
	@Test
	public void shouldLogInvalidFilePathMessage() throws Exception {
		when(mockedFile.exists()).thenReturn(Boolean.FALSE);
		ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
		targetBeingTested.searchForClass(SOME_PATH, SOME_CLASS);
		
		verify(mockedLogger).logResult(argumentCaptor.capture());
		
		assertEquals("Invalid Path : " + SOME_PATH, argumentCaptor.getValue());
	}
}
