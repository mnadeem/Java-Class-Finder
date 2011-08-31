package com.nadeem.app.finder;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.nadeem.app.finder.engine.SearchEngineTest;
import com.nadeem.app.finder.util.FileTypeTest;
import com.nadeem.app.finder.util.ResultTypeTest;

@RunWith(Suite.class)
@SuiteClasses({FileTypeTest.class,
	SearchEngineTest.class,
	ResultTypeTest.class})
public class AllTests {

}
