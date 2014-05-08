package com.theappbusiness.whoswho.provider;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.junit.Test;

import android.content.ContentValues;

public class AccessBioProviderTest extends TestCase {

	public static final String HTML = "<section id=\"users\"><div class=\"wrapper\"><div class=\"row\"><div class=\"col col2\"><div class=\"title\"><img src=\"http://www.theappbusiness.com/wp-content/uploads/2013/06/dan-e1372264031694.jpg\" width=\"187\" height=\"187\" alt=\"Daniel Joseph\" class=\"wp-user-avatar wp-user-avatar-336 avatar avatar avatar-336 photo\"></div><h3>Daniel Joseph</h3><p>Founder, Strategy Director</p><p class=\"user-description\">Daniel was previously European Planning Director for Apple at Media Arts Lab, launching the iPhone, App Store and hundreds of apps. Prior to this, Daniel spent his career planning advertising and communications for Sony Computer Entertainment Europe and Mars. He has a BA in Geography from Keble College, Oxford.</p></div><div class=\"col col2\"><div class=\"title\"><img src=\"http://www.theappbusiness.com/wp-content/uploads/2013/06/rob-e1372264067931.jpg\" width=\"187\" height=\"187\" alt=\"Rob Evans\" class=\"wp-user-avatar wp-user-avatar-336 avatar avatar avatar-336 photo\"></div><h3>Rob Evans</h3><p>Founder, Strategy Director</p><p class=\"user-description\">Rob led the European Audience Planning team at Media Arts Lab for Apple, specialising in app audience behaviours across Europe. He has also planned media and communications for Pepsi, Nike and PlayStation at Mindshare Worldwide. He graduated in Economics from Bristol University.</p></div><div class=\"col col2\"><div class=\"title\"><img src=\"http://www.theappbusiness.com/wp-content/uploads/2013/06/steve1-e1372252596777.jpg\" width=\"187\" height=\"187\" alt=\"Stephen Wilson\" class=\"wp-user-avatar wp-user-avatar-336 avatar avatar avatar-336 photo\"></div><h3>Stephen Wilson</h3><p>Head of Engineering</p><p class=\"user-description\">Head of Engineering at The App Business, Steve directs our teams of Android, iOS and web developers. He previously worked as a software engineer at Siemens. His specialism is telecoms and network engineering. Steve holds a BSc in Computer Science from Nottingham Trent University.</p></div></div></div></section>";
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
