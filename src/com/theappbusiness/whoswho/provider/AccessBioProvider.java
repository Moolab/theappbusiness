package com.theappbusiness.whoswho.provider;

import java.util.ArrayList;

import android.content.ContentValues;

public class AccessBioProvider {

	private String html;

	/**
	 * Parse Website to extract biographies.
	 * 
	 * @return {@link ArrayList} of {@link ContentValues}. Each ContentValues is a Biography.
	 */
	public ArrayList<ContentValues> parseBio() {
		
		/*
		 * If HTML is not valid, then return null.
		 */
		if ( ! checkHtml()) {
			return null;
		}
		
		return null;
	}

	/**
	 * Check if html is valid
	 * @return boolean TRUE if valid
	 */
	protected boolean checkHtml() {
		return html != null;
	}

	/**
	 * Set the WebSite
	 * 
	 * @param html HTML code
	 */
	public void setHtml(String html) {
		this.html = html;
	}
}
