package com.theappbusiness.whoswho.provider;

import junit.framework.TestCase;

public class AccessBioProviderTest extends TestCase {

	private AccessBioProvider instance;

	protected void setUp() throws Exception {
		super.setUp();
		instance = new AccessBioProvider();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		instance = null;
	}
	
	/*
	 * Test the parse process to extract biographies from website.
	 */
	public void parseBio() {
		
	}
}
