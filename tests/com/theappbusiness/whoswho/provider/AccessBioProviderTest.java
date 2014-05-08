package com.theappbusiness.whoswho.provider;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.junit.Test;

import android.content.ContentValues;

public class AccessBioProviderTest extends TestCase {

	private static final String HTML = "<section id=\"users\"><div class=\"wrapper\"><div class=\"title\"> <header class=\"with-text\"><h1>Meet the team</h1><p>Based in central London, we blend in-house app strategists and interaction designers with specialist software developers to create mobile products that look and work beautifully.</p> </header></div></div></section>";
	private AccessBioProvider instance;

	protected void setUp() throws Exception {
		super.setUp();
		instance = new AccessBioProvider();
		instance.setHtml(HTML);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		instance = null;
	}
	
	/*
	 * Test the parse process to extract biographies from website.
	 */
	@Test
	public void testParseBio() {
		
		instance.setHtml(HTML);
		ArrayList<ContentValues> bios = instance.parseBio();
	
		/*
		 * Assert parse return a object instance
		 */
		assertNotNull(bios);
		
		/*
		 * Quantity of biographies
		 */
		assertEquals(1, bios.size());
		
		bios.get(0);
	}
	
	/*
	 * Test the check HTML method
	 */
	public void testCheckHtml() {
		instance.setHtml(HTML);
		assertTrue(instance.checkHtml());
	}
	
	/*
	 * Test the check HTML method fail
	 */
	public void testCheckHtmlFail() {
		instance.setHtml(null);
		assertFalse(instance.checkHtml());
	}
}
