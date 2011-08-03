package de.seideman.dams.test;

import android.test.suitebuilder.TestSuiteBuilder;
import junit.framework.Test;

public class TestSuite extends junit.framework.TestSuite {
	public static Test suite() {
        return new TestSuiteBuilder(TestSuite.class).includeAllPackagesUnderHere().build();
    }
}
