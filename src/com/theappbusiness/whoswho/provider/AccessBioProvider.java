package com.theappbusiness.whoswho.provider;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.ContentValues;

import com.theappbusiness.whoswho.WhosWhoContract;

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
		
		Document doc = Jsoup.parse(html);
		Elements employees = doc.select("#users .wrapper .row .col");
		
		/*
		 * If have not found employee, then return null. 
		 */
		if (employees == null || employees.size() == 0) {
			return null;
		}
			
		ArrayList<ContentValues> values = new ArrayList<ContentValues>();
		for (int i = 0; i < employees.size(); i++) {
			Element employee = employees.get(i);			
			ContentValues employeeValue = elementToContentValue(employee);
			employeeValue.put(WhosWhoContract.Biographies.COLUMN_NAME_NUM, i);
			values.add(employeeValue);
		}
		
		return values;
	}

	protected ContentValues elementToContentValue(Element employee) {
		ContentValues employeeValue = new ContentValues();
		
		/*
		 * Employee bio.
		 */
		Elements description = employee.getElementsByClass("user-description");
		if (description != null && description.size() > 0) {
			employeeValue.put(WhosWhoContract.Biographies.COLUMN_NAME_BIO, description.first().text());
		}
		
		/*
		 * Employee name 
		 */
		Elements name = employee.getElementsByTag("h3");
		if (name != null && name.size() > 0) {
			employeeValue.put(WhosWhoContract.Biographies.COLUMN_NAME_NAME, name.first().text());
		}
		
		/*
		 * Employee title 
		 */
		Elements title = employee.getElementsByTag("p");
		if (title != null && title.size() > 0) {
			employeeValue.put(WhosWhoContract.Biographies.COLUMN_NAME_TITLE, title.first().text());
		}
		
		/*
		 * Employee photo.
		 */
		Elements photo = employee.getElementsByClass("photo");
		if (photo != null && photo.size() > 0) {
			employeeValue.put(WhosWhoContract.Biographies.COLUMN_NAME_PHOTO, photo.first().attr("src"));
		}
		
		return employeeValue;
	}

	/**
	 * Check if html is valid
	 * @return boolean TRUE if valid
	 */
	public boolean checkHtml() {
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
